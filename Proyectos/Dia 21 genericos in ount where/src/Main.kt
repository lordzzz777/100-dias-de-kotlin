// ------------ Genéricos: in, out, where ------------

// Las clases en Kotlin pueden tener parámetros de tipo,
// al igual que en Java:

class Box<T>(t: T) {
    var value = t
}

// Para crear una instancia de dicha clase, simplemente proporcione
// los argumentos de tipo:

val box2: Box<Int> = Box<Int>(1)

// Pero si los parámetros se pueden inferir, por ejemplo, de los argumentos
// del constructor, puede omitir los argumentos de tipo:

val box3 = Box(1) // 1 has type Int, so the compiler figures out that it is Box<Int>

// ------------ Variante ------------

// Uno de los aspectos más complicados del sistema de tipos de Java son los
// tipos comodines (consulte las preguntas frecuentes sobre los genéricos de
// Java). Kotlin no tiene estos. En su lugar, Kotlin tiene variaciones en el
// sitio de declaración y proyecciones de tipo.

// ----- Varianza y comodines en Java -----

// Pensemos en por qué Java necesita estos misteriosos comodines. En primer lugar,
// los tipos genéricos en Java son invariantes, lo que significa que List<String>
// no es un subtipo de List<Object>. Si List no hubiera sido invariante, no habría
// sido mejor que las matrices de Java, ya que el siguiente código se habría
// compilado, pero habría causado una excepción en tiempo de ejecución:

/*
// Java
List<String> strs = new ArrayList<String>();

// Java reports a type mismatch here at compile-time.
List<Object> objs = strs;

// What if it didn't?
// We would be able to put an Integer into a list of Strings.
objs.add(1);

// And then at runtime, Java would throw
// a ClassCastException: Integer cannot be cast to String
String s = strs.get(0);
 */

// Java prohíbe tales cosas para garantizar la seguridad en tiempo de ejecución.
// Pero esto tiene implicaciones. Por ejemplo, considere el método addAll() de
// la interfaz de la colección. ¿Cuál es la firma de este método? Intuitivamente,
// lo escribirías de esta manera:

// Java
interface Collection<E> ... {
    void addAll(Collection<E> items);
}

// Pero entonces, no podrías hacer lo siguiente (lo cual es perfectamente seguro):

/*
// Java

// The following would not compile with the naive declaration of addAll:
// Collection<String> is not a subtype of Collection<Object>
void copyAll(Collection<Object> to, Collection<String> from) {
    to.addAll(from);
}
 */

// Es por eso que la firma real de addAll() es la siguiente:

// ¿El argumento del tipo de comodín? Extends E indica que este método acepta una
// colección de objetos de E o un subtipo de E, no solo E en sí. Esto significa
// que puede leer de forma segura las E de los elementos (los elementos de esta
// colección son instancias de una subclase de E), pero no puede escribir en ella,
// ya que no sabe qué objetos cumplen con ese subtipo desconocido de E. A cambio
// de esta limitación, obtienes el comportamiento deseado: Collection<String> es
// un subtipo de Collection<? Extiende el objeto>. En otras palabras, el comodín
// con un límite de extensión (límine superior) hace que el tipo sea covariante.

//La clave para entender por qué esto funciona es bastante simple: si solo puedes
// tomar elementos de una colección, entonces usar una colección de cuerdas y leer
// objetos de ella está bien. Por el contrario, si solo puedes poner elementos en
// la colección, está bien tomar una colección de objetos y poner cadenas en ella:
// en Java hay List<? Super String>, que acepta cadenas o cualquiera de sus supertipos.

//Este último se llama contravarianza, y solo se pueden llamar a métodos que tomen
// String como argumento en List<? Super String> (por ejemplo, puedes llamar a
// add(String) o set(int, String)). Si llamas a algo que devuelve T en List<T>,
// no obtienes una cadena, sino más bien un objeto.

//Joshua Bloch, en su libro Effective Java, 3rd Edition, explica bien el problema
// (Artículo 31: "Usa comodines acotados para aumentar la flexibilidad de la API").
// Él da el nombre de Productores a los objetos de los que solo lees y Consumidores
// a aquellos a los que solo escribes. Él recomienda:

// "Para una máxima flexibilidad, utilice tipos de comodín en los parámetros de
// entrada que representen a los productores o consumidores".

// Luego propone el siguiente mnemotécnico:
// PECS significa Producer-Extends, Consumer-Super.

// ************************* info *********************************
// Si usas un objeto de productor, digamos, ¿Lista<? Extiende Foo>,
// no se le permite llamar a add() o set() en este objeto, pero esto
// no significa que sea inmutable: por ejemplo, nada le impide llamar
// a clear() para eliminar todos los elementos de la lista, ya que
// clear() no toma ningún parámetro en absoluto.

//Lo único que garantizan los comodines (u otros tipos de variación)
// es la seguridad del tipo. La inmutabilidad es una historia
// completamente diferente.

// Declaración-sitio variance

// Supongamos que hay una interfaz genérica Source<T> que no tiene
// ningún método que tome T como parámetro, solo métodos que devuelvan T:

// Java
interface Source<T> {
    fun nextT(): T
}

// Entonces, sería perfectamente seguro almacenar una referencia a una instancia
// de Source<String> en una variable de tipo Source<Object> - no hay métodos de
// consumo a los que llamar. Pero Java no lo sabe, y todavía lo prohíbe:

// Java
fun demo(strs: Source<String>) {
    val objects: Source<Any> = strs // !!! Not allowed in Java
    // ...
}

// Para solucionar esto, debe declarar objetos de tipo Fuente<? Extiende el objeto>.
// Hacerlo no tiene sentido, porque puedes llamar a todos los mismos métodos en una
// variable como antes, por lo que no hay valor añadido por el tipo más complejo.
// Pero el compilador no lo sabe.

//En Kotlin, hay una manera de explicar este tipo de cosas al compilador.
// Esto se llama varianza del sitio de declaración: puede anotar el parámetro de tipo
// T de Source para asegurarse de que solo se devuelve (produce) de los miembros de
// Source<T>, y nunca se consume. Para ello, utilice el modificador de salida:

interface Source2<out T> {
    fun nextT(): T
}

fun demo2(strs: Source<String>) {
    val objects: Source<Any> = strs // This is OK, since T is an out-parameter
    // ...
}

// La regla general es la siguiente: cuando se declara un parámetro de tipo T de una
// clase C, puede ocurrir solo en la posición exterior en los miembros de C, pero a
// cambio C<Base> puede ser con seguridad un supertipo de C<Derivado>.

//En otras palabras, se puede decir que la clase C es covariante en el parámetro T, o
// que T es un parámetro de tipo covariante. Puedes pensar en C como un productor de T,
// y NO un consumidor de T.

//El modificador de salida se llama anotación de varianza y, dado que se proporciona en
// el sitio de declaración de parámetros de tipo, proporciona la varianza del sitio de
// declaración. Esto contrasta con la variación del sitio de uso de Java, donde los
// comodines en los usos del tipo hacen que los tipos sean covariantes.

//Además de out, Kotlin proporciona una anotación de varianza complementaria: in. Hace
// que un parámetro de tipo sea contravariante, lo que significa que solo se puede
// consumir y nunca se puede producir. Un buen ejemplo de un tipo contravariante es
// Comparable:

interface Comparable<in T> {
    operator fun compareTo(other: T): Int
}

fun demo(x: Comparable<Number>) {
    x.compareTo(1.0) // 1.0 has type Double, which is a subtype of Number
    // Thus, you can assign x to a variable of type Comparable<Double>
    val y: Comparable<Double> = x // OK!
}

// Las palabras de entrada y salida parecen explicarse por sí mismas (ya que ya se han
// utilizado con éxito en C# durante bastante tiempo), por lo que la mnemotecnia mencionada
// anteriormente no es realmente necesaria. De hecho, se puede reformular a un nivel más
// alto de abstracción:

//La transformación existencial: ¡Consumidor dentro, Productor fuera! :-)

// ------------ Tipo de proyecciones ------------

// Variación del sitio de uso: proyecciones de tipo

// Es muy fácil declarar un parámetro de tipo T como fuera y evitar problemas con la
// submecanografía en el sitio de uso, ¡pero algunas clases en realidad no se pueden
// restringir a devolver solo T! Un buen ejemplo de esto es Array:

class Array<T>(val size: Int) {
    operator fun get(index: Int): T {  }
    operator fun set(index: Int, value: T) {  }
}

// Esta clase no puede ser ni co- ni contravariante en T. Y esto impone ciertas
// inflexibilidades. Considere la siguiente función:

fun copy(from: Array<Any>, to: Array<Any>) {
    assert(from.size == to.size)
    for (i in from.indices)
        to[i] = from[i]
}

// Se supone que esta función copia elementos de una matriz a otra. Tratemos de
// aplicarlo en la práctica:

val ints: Array<Int> = arrayOf(1, 2, 3)
val any = Array<Any>(3) { "" }
copy(ints, any)
//   ^ type is Array<Int> but Array<Any> was expected

// Aquí te topas con el mismo problema familiar: Array<T> es invariante en T,
// por lo que ni Array<Int> ni Array<Any> es un subtipo del otro. ¿Por qué no?
// Una vez más, esto se debe a que la copia podría tener un comportamiento
// inesperado, por ejemplo, puede intentar escribir una cadena a from, y si
// realmente pasas una matriz de Int allí, se lanzará una
// ClassCastException más tarde.

// Para prohibir que la función de copia escriba a desde, puede hacer lo siguiente:

fun copy(from: Array<out Any>, to: Array<Any>) {  }

// Este es un tipo de proyección, lo que significa que from no es una matriz simple,
// sino más bien una matriz restringida (proyectada). Solo puede llamar a métodos que
// devuelvan el parámetro de tipo T, lo que en este caso significa que solo puede
// llamar a get(). Este es nuestro enfoque para la varianza del sitio de uso, y
// corresponde a la matriz de Java<? Extiende Object>
// a la vez que es un poco más simple.

// También puedes proyectar un tipo con in:

fun fill(dest: Array<in String>, value: String) {  }

// Array<in String> corresponde a Array< de Java? Súper cuerda>. Esto significa que puede
// pasar una matriz de CharSequence o una matriz de objeto a la función fill().

// Proyecciones de estrellas
//
//A veces quieres decir que no sabes nada sobre el argumento de tipo, pero aún así quieres
// usarlo de una manera segura. La forma segura aquí es definir tal proyección del tipo
// genérico, que cada instanciación concreta de ese tipo genérico será un subtipo de esa
// proyección.

//Kotlin proporciona la llamada sintaxis de proyección de estrellas para esto:

//Para Foo<out T : TUpper>, donde T es un parámetro de tipo covariante con el límite superior
// TUpper, Foo<*> es equivalente a Foo<out TUpper>. Esto significa que cuando se desconoce
// la T, puedes leer de forma segura los valores de TUpper de Foo<*>.

//Para Foo<in T>, donde T es un parámetro de tipo contravariante, Foo<*> es equivalente a
// Foo<in Nothing>. Esto significa que no hay nada que puedas escribir a Foo<*> de una
// manera segura cuando T es desconocida.

//Para Foo<T : TUpper>, donde T es un parámetro de tipo invariante con el límite superior
// TUpper, Foo<*> es equivalente a Foo<out TUpper> para los valores de lectura y a Foo<in
// Nothing> para los valores de escritura.

//Si un tipo genérico tiene varios parámetros de tipo, cada uno de ellos se puede proyectar
// de forma independiente. Por ejemplo, si el tipo se declara como función de interfaz<en T,
// fuera de U>, podría utilizar las siguientes proyecciones de estrellas:

// Función<*, Cadena> significa Función<en Nada, Cadena>.

//  Función<Int, *> significa Función<Int, out Any? >.

// Función<*, *> significa Función<in Nothing, out Any? >.

// ************************* info *********************************
// Las proyecciones de estrellas se parecen mucho a los tipos en
// bruto de Java, pero son seguras.

// ------------ Funciones genéricas ------------

// Las clases no son las únicas declaraciones que pueden tener parámetros de tipo. Las funciones
// también pueden. Los parámetros de tipo se colocan antes del nombre de la función:

fun <T2> singletonList(item: T2): List<T2> {
    // ...
}

fun <T> T.basicToString(): String { // extension function
    // ...
}

// Para llamar a una función genérica, especifique los argumentos de tipo en el sitio de llamada
// después del nombre de la función:

val l2 = singletonList<Int>(1)

// Los argumentos de tipo se pueden omitir si se pueden inferir del contexto, por lo que el
// siguiente ejemplo también funciona:

val l = singletonList(1)

// ------------ Restricciones genéricas ------------

// El conjunto de todos los tipos posibles que se pueden sustituir por un parámetro de tipo
// determinado puede estar restringido por restricciones genéricas.

// ----- Los límites superiores -----

// El tipo más común de restricción es un límite superior, que corresponde a la palabra
// clave extends de Java:

fun <T : Comparable<T>> sort(list: List<T>) {  }

// El tipo especificado después de dos puntos es el límite superior, lo que indica que solo
// un subtipo de Comparable<T> puede ser sustituido por T. Por ejemplo:

/*
sort(listOf(1, 2, 3)) // OK. Int is a subtype of Comparable<Int>
sort(listOf(HashMap<Int, String>())) // Error: HashMap<Int, String> is not a subtype of Comparable<HashMap<Int, String>>
 */

// El límite superior predeterminado (si no se especificó ninguno) es ¿Any?. Solo se puede
// especificar un límite superior dentro de los corchetes angulares. Si el mismo parámetro
// de tipo necesita más de un límite superior, necesita una cláusula de dónde separada

fun <T> copyWhenGreater(list: List<T>, threshold: T): List<String>
        where T : CharSequence,
              T : Comparable<T> {
    return list.filter { it > threshold }.map { it.toString() }
}

// El tipo aprobado debe satisfacer todas las condiciones de la cláusula where simultáneamente.
// En el ejemplo anterior, el tipo T debe implementar tanto CharSequence como Comparable.

// ------------ Definitivamente tipos no nulos ------------

// Para facilitar la interoperabilidad con clases e interfaces Java genéricas, Kotlin admite
// la declaración de un parámetro de tipo genérico como definitivamente no nulo.

// Para declarar un tipo genérico T como definitivamente no nulo, declare el tipo con & Any.
// Por ejemplo: T & Any.

//Un tipo definitivamente no nulo debe tener un límite superior nulo.

// El caso de uso más común para declarar tipos definitivamente no nulos es cuando se desea
// anular un método Java que contiene @NotNull como argumento. Por ejemplo, considere el
// método load():

/*
import org.jetbrains.annotations.*;

public interface Game<T> {
    public T save(T x) {}
    @NotNull
    public T load(@NotNull T x) {}
}
 */

// Para anular el método load() en Kotlin con éxito, necesita que T1 se declare
// como definitivamente no nulo:

/*
interface ArcadeGame<T1> : Game<T1> {
    override fun save(x: T1): T1
    // T1 is definitely non-nullable
    override fun load(x: T1 & Any): T1 & Any
}
 */

// Cuando trabajas solo con Kotlin, es poco probable que tengas que declarar explícitamente
// tipos definitivamente no anulables porque la inferencia de tipos de Kotlin se encarga de
// esto por ti.

// ------------ Tipo de borrado ------------

// Las comprobaciones de seguridad de tipo que Kotlin realiza para usos de declaraciones
// genéricas se realizan en tiempo de compilación. En tiempo de ejecución, las instancias
// de tipos genéricos no contienen ninguna información sobre sus argumentos de tipo reales.
// Se dice que la información del tipo se ha borrado. Por ejemplo, ¿las instancias de Foo<Bar>
// y Foo<Baz? > se borran solo a Foo<*>.

// ----- Comprobaciones y lanzamientos de tipo genérico -----

// Debido al borrado del tipo, no hay una forma general de verificar si se creó una instancia
// de un tipo genérico con ciertos argumentos de tipo en tiempo de ejecución, y el compilador
// prohíbe tales comprobaciones como ints es List<Int> o list es T (parámetro de tipo).
// Sin embargo, puedes comparar una instancia con un tipo proyectado por una estrella:

/*
 if (something is List<*>) {
    something.forEach { println(it) } // The items are typed as `Any?`
}
 */

// Del mismo modo, cuando ya tiene los argumentos de tipo de una instancia comprobados de
// forma estática (en tiempo de compilación), puede hacer un is-check o un cast que involucre
// la parte no genérica del tipo. Tenga en cuenta que los corchetes angulares se omiten en
// este caso:

fun handleStrings(list: MutableList<String>) {
    if (list is ArrayList) {
        // `list` is smart-cast to `ArrayList<String>`
    }
}

// La misma sintaxis, pero con los argumentos de tipo omitidos, se puede utilizar para
// castings que no tienen en cuenta los argumentos de tipo: lista como ArrayList.

//Los argumentos de tipo de las llamadas a funciones genéricas también solo se comprueban
// en tiempo de compilación. Dentro de los cuerpos de función, los parámetros de tipo no
// se pueden utilizar para las comprobaciones de tipo, y los fundidos de tipo a los
// parámetros de tipo (foo como T) no se marcan. La única exclusión son las funciones en
// línea con parámetros de tipo reificados, que tienen sus argumentos de tipo reales en
// línea en cada sitio de llamada. Esto permite comprobaciones y lanzamientos de tipo para
// los parámetros de tipo. Sin embargo, las restricciones descritas anteriormente todavía
// se aplican a los casos de tipos genéricos utilizados dentro de los controles o moldes.
// Por ejemplo, en la comprobación de tipo arg es T, si arg es una instancia de un tipo
// genérico en sí, sus argumentos de tipo aún se borran.


inline fun <reified A, reified B> Pair<*, *>.asPairOf(): Pair<A, B>? {
    if (first !is A || second !is B) return null
    return first as A to second as B
}

val somePair: Pair<Any?, Any?> = "items" to listOf(1, 2, 3)


val stringToSomething = somePair.asPairOf<String, Any>()
val stringToInt = somePair.asPairOf<String, Int>()
val stringToList = somePair.asPairOf<String, List<*>>()
val stringToStringList = somePair.asPairOf<String, List<String>>() // Compiles but breaks type safety!
// Expand the sample for more details


fun main() {
    println("stringToSomething = " + stringToSomething)
    println("stringToInt = " + stringToInt)
    println("stringToList = " + stringToList)
    println("stringToStringList = " + stringToStringList)
    //println(stringToStringList?.second?.forEach() {it.length}) // This will throw ClassCastException as list items are not String
}

// Yesos sin comprobar

// Los moldes de tipo a tipos genéricos con argumentos de tipo concretos como foo como
// List<String> no se pueden comprobar en tiempo de ejecución.

// Estos lanzamientos no comprobados se pueden utilizar cuando la seguridad del tipo está
// implícita en la lógica del programa de alto nivel, pero no pueden ser inferidas
// directamente por el compilador. Vea el siguiente ejemplo.

fun readDictionary(file: File): Map<String, *> = file.inputStream().use {
    TODO("Read a mapping of strings to arbitrary elements.")
}

// We saved a map with `Int`s into this file
val intsFile = File("ints.dictionary")

// Warning: Unchecked cast: `Map<String, *>` to `Map<String, Int>`
val intsDictionary: Map<String, Int> = readDictionary(intsFile) as Map<String, Int>

// Aparece una advertencia para el elenco en la última línea. El compilador no puede
// comprobarlo completamente en tiempo de ejecución y no garantiza que los valores del
// mapa sean Int.

// Para evitar lanzamientos sin control, puede rediseñar la estructura del programa.
// En el ejemplo anterior, podrías usar las interfaces DictionaryReader<T> y
// DictionaryWriter<T> con implementaciones seguras para diferentes tipos.
// Puede introducir abstracciones razonables para mover los lanzamientos no comprobados
// desde el sitio de la llamada a los detalles de la implementación. El uso adecuado de
// la varianza genérica también puede ayudar.

// Para las funciones genéricas, el uso de parámetros de tipo reificado hace que los
// lanzamientos como arg como T estén comprobados, a menos que el tipo de arg tenga sus
// propios argumentos de tipo que se borren.

// Una advertencia de reparto sin marcha se puede suprimir anotando la declaración o la
// declaración donde se produce con @Suppress("UNCHECKED_CAST"):

inline fun <reified T> List<*>.asListOfType(): List<T>? =
    if (all { it is T })
        @Suppress("UNCHECKED_CAST")
        this as List<T> else
        null

// ************************* info *********************************
// En la JVM: los tipos de matriz (Array<Foo>) conservan información
// sobre el tipo borrado de sus elementos, y las disticiones de tipo
// a un tipo de matriz se comprueban parcialmente: los argumentos de
// nulidad y tipo real del tipo de elemento todavía se borran.
// Por ejemplo, ¿el elenco foo como Array<List<String>? > tendrá éxito
// si foo es una matriz que contiene cualquier lista<*>,
// ya sea anulable o no.

// ------------ Operador de subrayado para argumentos de tipo ------------

// El operador de guión bajo _ se puede utilizar para los argumentos de tipo. Úsalo para
// inferir automáticamente un tipo del argumento cuando se especifiquen explícitamente
// otros tipos:

/*
abstract class SomeClass<T> {
    abstract fun execute() : T
}

class SomeImplementation : SomeClass<String>() {
    override fun execute(): String = "Test"
}

class OtherImplementation : SomeClass<Int>() {
    override fun execute(): Int = 42
}

object Runner {
    inline fun <reified S: SomeClass<T>, T> run() : T {
        return S::class.java.getDeclaredConstructor().newInstance().execute()
    }
}

fun main() {
    // T is inferred as String because SomeImplementation derives from SomeClass<String>
    val s = Runner.run<SomeImplementation, _>()
    assert(s == "Test")

    // T is inferred as Int because OtherImplementation derives from SomeClass<Int>
    val n = Runner.run<OtherImplementation, _>()
    assert(n == 42)
}
 */

