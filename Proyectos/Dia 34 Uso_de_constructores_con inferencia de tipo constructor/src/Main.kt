// ------------ Uso de constructores con inferencia de tipo constructor ------------

// Kotlin admite la inferencia de tipo de constructor (o inferencia de constructor),
// que puede ser útil cuando se trabaja con constructores genéricos. Ayuda al compilador
// a inferir los argumentos de tipo de una llamada de constructor en función de la
// información de tipo sobre otras llamadas dentro de su argumento lambda.

// Considere este ejemplo de uso de buildMap():

fun addEntryToMap(baseMap: Map<String, Number>, additionalEntry: Pair<String, Int>?) {
    val myMap = buildMap {
        putAll(baseMap)
        if (additionalEntry != null) {
            put(additionalEntry.first, additionalEntry.second)
        }
    }
}

// No hay suficiente información de tipo aquí para inferir argumentos de tipo de manera
// regular, pero la inferencia del constructor puede analizar las llamadas dentro del
// argumento lambda. Basado en la información de tipo sobre las llamadas putAll() y put(),
// el compilador puede inferir automáticamente los argumentos de tipo de la llamada
// buildMap() en Cadena y Número. La inferencia del constructor permite omitir los
// argumentos de tipo mientras se utilizan constructores genéricos.

// ------------ Escribiendo a tus propios constructores ------------

// ------- Requisitos para permitir la inferencia del constructor -------

// ***************************************** info ****************************************
// Antes de Kotlin 1.7.0, se requiere habilitar la inferencia del constructor para una
// función de constructor -Xenable-builder-inference-option compiler. En 1.7.0, la opción
// está habilitada de forma predeterminada.
// ****************************************************************************************

// Para que la inferencia del constructor funcione para su propio constructor, asegúrese de
// que su declaración tenga un parámetro lambda del constructor de un tipo de función con un
// receptor. También hay dos requisitos para el tipo de receptor:

//  1 -. Debería usar los argumentos de tipo que se supone que debe inferir la inferencia del
//       constructor. Por ejemplo:
fun <V> buildList(builder: MutableList<V>.() -> Unit) {  }

// ***************************************** info ****************************************
// Tenga en cuenta que pasar el parámetro de tipo directamente como fun <T> myBuilder
// (builder: T.() -> Unit) aún no es compatible.
// ***************************************************************************************

//  2 -. Debe proporcionar miembros públicos o extensiones que contengan los parámetros de tipo
//       correspondientes en su firma. Por ejemplo:
class ItemHolder<T> {
    private val items = mutableListOf<T>()

    fun addItem(x: T) {
        items.add(x)
    }

    fun getLastItem(): T? = items.lastOrNull()
}

fun <T> ItemHolder<T>.addAllItems(xs: List<T>) {
    xs.forEach { addItem(it) }
}

fun <T> itemHolderBuilder(builder: ItemHolder<T>.() -> Unit): ItemHolder<T> =
    ItemHolder<T>().apply(builder)

fun test(s: String) {
    val itemHolder1 = itemHolderBuilder { // Type of itemHolder1 is ItemHolder<String>
        addItem(s)
    }
    val itemHolder2 = itemHolderBuilder { // Type of itemHolder2 is ItemHolder<String>
        addAllItems(listOf(s))
    }
    val itemHolder3 = itemHolderBuilder { // Type of itemHolder3 is ItemHolder<String?>
        val lastItem: String? = getLastItem()
        // ...
    }
}

// ------- Características compatibles -------

// La inferencia del constructor admite:

// -. Inferir varios argumentos de tipo
// fun <K, V> myBuilder(builder: MutableMap<K, V>.() -> Unit): Map<K, V> { ... }

// -. Inferir los argumentos de tipo de varios lambdas de construcción dentro de una llamada, incluidos
//    los interdependientes
/*
fun <K, V> myBuilder(
    listBuilder: MutableList<V>.() -> Unit,
    mapBuilder: MutableMap<K, V>.() -> Unit
): Pair<List<V>, Map<K, V>> =
    mutableListOf<V>().apply(listBuilder) to mutableMapOf<K, V>().apply(mapBuilder)

fun main() {
    val result = myBuilder(
        { add(1) },
        { put("key", 2) }
    )
    // result has Pair<List<Int>, Map<String, Int>> type
}
 */

// -. Inferring type arguments whose type parameters are lambda's parameter or return types
/*
fun <K, V> myBuilder1(
    mapBuilder: MutableMap<K, V>.() -> K
): Map<K, V> = mutableMapOf<K, V>().apply { mapBuilder() }

fun <K, V> myBuilder2(
    mapBuilder: MutableMap<K, V>.(K) -> Unit
): Map<K, V> = mutableMapOf<K, V>().apply { mapBuilder(2 as K) }

fun main() {
    // result1 has the Map<Long, String> type inferred
    val result1 = myBuilder1 {
        put(1L, "value")
        2
    }
    val result2 = myBuilder2 {
        put(1, "value 1")
        // You can use `it` as "postponed type variable" type
        // See the details in the section below
        put(it, "value 2")
    }
}
 */

// ------------ Cómo funciona la inferencia del constructor ------------

// ------- Variables de tipo aplazadas -------

// La inferencia del constructor funciona en términos de variables de tipo pospuestas,
// que aparecen dentro del lambda del constructor durante el análisis de inferencia del
// constructor. Una variable de tipo pospuesta es el tipo de un argumento de tipo, que
// está en proceso de inferir. El compilador lo utiliza para recopilar información de
// tipo sobre el argumento de tipo.

// Considere el ejemplo con buildList():
/*
val result = buildList {
    val x = get(0)
}
 */

// Aquí x tiene un tipo de variable de tipo pospuesto: la llamada get() devuelve un valor
// de tipo E, pero E en sí misma aún no está fija. En este momento, se desconoce un tipo
// concreto para E.

// Cuando un valor de una variable de tipo pospuesta se asocia con un tipo concreto, la
// inferencia del constructor recopila esta información para inferir el tipo resultante
// del argumento de tipo correspondiente al final del análisis de inferencia del constructor.
// Por ejemplo:

val result = buildList {
    val x = get(0)
    val y: String = x
} // El resultado tiene el tipo List<String> inferido

// Después de que la variable de tipo pospuesto se asigne a una variable del tipo Cadena,
// la inferencia del constructor obtiene la información de que x es un subtipo de Cadena.
// Esta asignación es la última declaración en el lambda del constructor, por lo que el
// análisis de inferencia del constructor termina con el resultado de inferir el argumento
// de tipo E en String.

//Tenga en cuenta que siempre puede llamar a las funciones equals(), hashCode() y toString()
// con una variable de tipo pospuesto como receptor.

// ------- Contribuyendo a los resultados de la inferencia del constructor -------

// La inferencia del constructor puede recopilar diferentes variedades de información de tipo
// que contribuyen al resultado del análisis. Considera:

// -. Métodos de llamada en el receptor de un lambda que utilizan el tipo del parámetro de tipo

val result2 = buildList {
    // El argumento de tipo se infiere en String basado en el "valor" pasado
    add("value")
} // El resultado tiene el tipo List<String> inferido

// -. Especificar el tipo esperado para las llamadas que devuelven el tipo del parámetro de tipo

val result3 = buildList {
    // El argumento de tipo se infiere en Float en función del tipo esperado
    val x: Float = get(0)
} // El resultado tiene el tipo List<Float>

/*
class Foo<T> {
    val items = mutableListOf<T>()
}

fun <K> myBuilder(builder: Foo<K>.() -> Unit): Foo<K> = Foo<K>().apply(builder)

fun main() {
    val result = myBuilder {
        val x: List<CharSequence> = items
        // ...
    } // result has the Foo<CharSequence> type
}
 */

// -. Pasar los tipos de variables de tipo pospuestas en métodos que esperan tipos concretos

/*
fun takeMyLong(x: Long) { ... }

fun String.isMoreThat3() = length > 3

fun takeListOfStrings(x: List<String>) { ... }

fun main() {
    val result1 = buildList {
        val x = get(0)
        takeMyLong(x)
    } // result1 has the List<Long> type

    val result2 = buildList {
        val x = get(0)
        val isLong = x.isMoreThat3()
    // ...
    } // result2 has the List<String> type

    val result3 = buildList {
        takeListOfStrings(this)
    } // result3 has the List<String> type
}
 */

// -. Tomando una referencia invocable al miembro del receptor lambda

/*
fun main() {
    val result = buildList {
        val x: KFunction1<Int, Float> = ::get
    } // result has the List<Float> type
}
 */

/*
fun takeFunction(x: KFunction1<Int, Float>) { ... }

fun main() {
    val result = buildList {
        takeFunction(::get)
    } // result has the List<Float> type
}
 */

// Al final del análisis, la inferencia del constructor considera toda la información
// de tipo recopilada e intenta fusionarla con el tipo resultante. Vea el ejemplo.

val result4 = buildList { // Inferring postponed type variable E
    // Considering E is Number or a subtype of Number
    val n: Number? = getOrNull(0)
    // Considering E is Int or a supertype of Int
    add(1)
    // E gets inferred into Int
} // result has the List<Int> type

// El tipo resultante es el tipo más específico que corresponde a la información de tipo
// recopilada durante el análisis. Si la información de tipo dada es contradictoria y no
// se puede fusionar, el compilador informa de un error.

// Tenga en cuenta que el compilador de Kotlin utiliza la inferencia del constructor solo
// si la inferencia de tipo regular no puede inferir un argumento de tipo. Esto significa
// que puede contribuir con información de tipo fuera de uns lambda constructor, y luego no
// se requiere el análisis de inferencia del constructor. Considere el ejemplo:

fun someMap() = mutableMapOf<CharSequence, String>()

fun <E> MutableMap<E, String>.f(x: MutableMap<E, String>) {  }

fun main() {
    val x: Map<in String, String> = buildMap {
        put("", "")
       // f(someMap()) // Type mismatch (required String, found CharSequence)
    }
}

// Aquí aparece un desajuste de tipo porque el tipo esperado del mapa se especifica fuera
// del lambda del constructor. El compilador analiza todas las declaraciones en el interior
// con el tipo de receptor fijo Map<in String, String>.
