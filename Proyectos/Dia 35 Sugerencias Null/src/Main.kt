// ------------ Sugerencias Null ------------

// ------------ Tipos nulos y tipos no nulos ------------

// El sistema de tipos de Kotlin tiene como objetivo eliminar el peligro de las
// referencias nulas, también conocidas como El error de un billón de dólares.

// Una de las trampas más comunes en muchos lenguajes de programación, incluido
// Java, es que acceder a un miembro de una referencia nula dará lugar a una
// excepción de referencia nula. En Java, esto sería el equivalente a una
// NullPointerException, o un NPE para abreviar.

// Las únicas causas posibles de un NPE en Kotlin son:

// Una llamada explícita para lanzar NullPointerException().

// ¡¡Uso del !! Operador que se describe a continuación.

// Inconsistencia de los datos con respecto a la inicialización, como cuando:

// Un esto no inicializado disponible en un constructor se pasa y se usa en algún
// lugar (un "fuga de esto").

// Un constructor de superclase llama a un miembro abierto cuya implementación en
// la clase derivada utiliza un estado no inicializado.

// Interoperación de Java:

// Intentos de acceder a un miembro de una referencia nula de un tipo de plataforma;

// Problemas de nulidad con los tipos genéricos que se utilizan para la interoperación
// de Java. Por ejemplo, un trozo de código Java podría agregar nulo a una Kotlin
// MutableList<String>, por lo que requiere una MutableList<String? > por trabajar con él.

// Otros problemas causados por el código Java externo.

// En Kotlin, el sistema de tipos distingue entre las referencias que pueden contener
// nulas (referencias nulas) y las que no pueden (referencias no nulas). Por ejemplo,
// una variable regular de tipo String no puede contener nulo:

/*
fun main() {
    var a: String = "abc" // La inicialización regular significa que no se puede anular por defecto
    a = null // compilation error
}
*/

// Para permitir nulos, puede declarar una variable como una cadena nula escribiendo String?:

fun main() {
    var b: String? = "abc" // can be set to null
    b = null // ok
    print(b)

// Ahora, si llama a un método o accede a una propiedad en un, está garantizado que no causará un NPE,
// por lo que puede decir con seguridad:

// val l = a.length

// Pero si quieres acceder a la misma propiedad en b, eso no sería seguro, y el compilador informa de un error:

// val l = b.length // error: variable 'b' can be null

// Pero todavía necesitas acceder a esa propiedad, ¿verdad? Hay algunas formas de hacerlo.

// ------------ Comprobando si hay nulo en las condiciones ------------

// En primer lugar, puede comprobar explícitamente si b es nulo y manejar las dos opciones por separado:

// val l = if (b != null) b.length else -1

// El compilador realiza un seguimiento de la información sobre la comprobación que realizó y permite que
// la llamada se prolonge dentro del if. También se admiten condiciones más complejas:

    val b2: String? = "Kotlin"

       if (b2 != null && b2.length > 0) {
         print("String of length ${b2.length}")
       } else {
          print("Empty string")
       }

// Tenga en cuenta que esto solo funciona cuando b es inmutable (lo que significa que es una variable local
// que no se modifica entre la comprobación y su uso o que es un valor miembro que tiene un campo de
// respaldo y no es anulable), porque de lo contrario podría ser el caso de que b cambie a nulo después de
// la comprobación.

// ------------ Llamadas seguras ------------

// Su segunda opción para acceder a una propiedad en una variable nula es usar el operador de llamada segura ?.:
    val a = "Kotlin"
    val b3: String? = null
    println(b3?.length)
    println(a?.length) // Unnecessary safe call

// Esto devuelve b.length si b no es nulo, y nulo de lo contrario. El tipo de expresión es Int?.
//
//Las llamadas seguras son útiles en las cadenas. Por ejemplo, Bob es un empleado que puede ser asignado a un
// departamento (o no). Ese departamento puede, a su vez, tener otro empleado como jefe de departamento.
// Para obtener el nombre del jefe de departamento de Bob (si lo hay), escriba lo siguiente

// bob?.department?.head?.name

    val listWithNulls: List<String?> = listOf("Kotlin", null)
    for (item in listWithNulls) {
        item?.let { println(it) } // prints Kotlin and ignores null
    }

// También se puede hacer una llamada segura en el lado izquierdo de una tarea. Entonces, si uno de los
// receptores en la cadena de llamadas seguras es nulo, la asignación se omite y la expresión de la derecha
// no se evalúa en absoluto:

// If either `person` or `person.department` is null, the function is not called:
//person?.department?.head = managersPool.getManager()

// ------------ Receptor nulo ------------

// Las funciones de extensión se pueden definir en un receptor nulo. De esta manera, puede especificar el
// comportamiento de los valores nulos sin la necesidad de usar la lógica de comprobación nula en cada sitio
// de llamada.

// Por ejemplo, la función toString() se define en un receptor anulable. Devuelve la cadena "nulo"
// (a diferencia de un valor nulo). Esto puede ser útil en ciertas situaciones, por ejemplo, el registro:
/*
    val person: Person? = null
    logger.debug(person.toString()) // Logs "null", does not throw an exception
 */

// Si desea que su invocación toString() devuelva una cadena nula, utilice el operador de llamada segura ?.:

/*
var timestamp: Instant? = null
val isoTimestamp = timestamp?.toString() // Returns a String? object which is `null`
if (isoTimestamp == null) {
   // Handle the case where timestamp was `null`
}
 */

// ------------ Operador de Elvis ------------

// Cuando tienes una referencia nula, b, puedes decir "si b no es nula, úsala, de lo contrario usa algún
// valor no nulo":

    val l: Int = if (b != null) b.length else -1

// En lugar de escribir la expresión si completa, ¿también puedes expresar esto con el operador de Elvis?::

    val l2 = b?.length ?: -1

// Si la expresión a la izquierda de ?: no es nula, el operador de Elvis la devuelve, de lo contrario
// devuelve la expresión a la derecha. Tenga en cuenta que la expresión del lado derecho se evalúa solo
// si el lado izquierdo es nulo.

//Dado que el lanzamiento y el retorno son expresiones en Kotlin, también se pueden usar en el lado derecho
// del operador de Elvis. Esto puede ser útil, por ejemplo, al comprobar los argumentos de la función:

    /*
    fun foo(node: Node): String? {
        val parent = node.getParent() ?: return null
        val name = node.getName() ?: throw IllegalArgumentException("name expected")
        // ...
    }
     */

// ------------ ¡¡Él!! Operador ------------

// La tercera opción es para los amantes de NPE: el operador de aserción no nulo (!!) Convierte cualquier
// valor en un tipo no nulo y lanza una excepción si el valor es nulo. Puedes escribir b!!, y esto devolverá
// un valor no nulo de b (por ejemplo, una cadena en nuestro ejemplo) o lanzar un NPE si b es nulo:

  //  val l3 = b!!.length

// Por lo tanto, si quieres un NPE, puedes tenerlo, pero tienes que pedirlo explícitamente y
// no aparecerá de la nada.

// ------------ Yesos seguros ------------

// Los lanzamientos regulares pueden resultar en una ClassCastException si el objeto no es del tipo de destino.
// Otra opción es usar lanzamientos seguros que devuelvan nulo si el intento no tuvo éxito:

    val aInt: Int? = a as? Int

// ------------ Colecciones de un tipo anulable ------------

// Si tiene una colección de elementos de un tipo nulo y desea filtrar elementos no nulos, puede hacerlo utilizando
// filterNotNull:

// val nullableList: List<Int?> = listOf(1, 2, null, 4)
//val intList: List<Int> = nullableList.filterNotNull()

// ------------ ¿Qué sigue? ------------

// Aprende a manejar la nulidad en Java y Kotlin.

// Aprende sobre los tipos genéricos que definitivamente no son nulos.
}



