// ------------ Funciones ------------

// Las funciones de Kotlin se declaran usando la palabra clave fun:

fun double(x: Int): Int {
    return 2 * x
}

// ------------ Uso de la función ------------

// Las funciones se llaman utilizando el enfoque estándar:

val result = double(2)

// Las funciones de llamada de los miembros utilizan la notación de puntos:

// Stream().read() // Crear una instancia de la clase Stream y llamar a read()

// ------- Parámetros -------

// Los parámetros de la función se definen usando la notación Pascal - nombre:
// tipo. Los parámetros se separan con comas, y cada parámetro debe escribirse
// explícitamente:

// fun powerOf(number: Int, exponent: Int): Int { /*...*/ }

// Puedes usar una coma final cuando declares los parámetros de la función:

fun powerOf(
    number: Int,
    exponent: Int, // trailing comma
) { /*...*/ }

// ------- Argumentos predeterminados -------

// Se establece un valor predeterminado añadiendo = al tipo.

// Los métodos de anulación siempre utilizan los valores de parámetros predeterminados
// del método base. Al anular un método que tiene valores de parámetros predeterminados,
// los valores de parámetros predeterminados deben omitirse de la firma:



fun read(
    b: ByteArray,
    off: Int = 0,
    len: Int = b.size,
) { /*...*/ }

// Se establece un valor predeterminado añadiendo = al tipo.

// Los métodos de anulación siempre utilizan los valores de parámetros predeterminados del
// método base. Al anular un método que tiene valores de parámetros predeterminados,
// los valores de parámetros predeterminados deben omitirse de la firma:

open class A {
    open fun foo(i: Int = 10) { /*...*/ }
}

class B : A() {
    override fun foo(i: Int) { /*...*/ }  // No default value is allowed.
}

// Si un parámetro predeterminado precede a un parámetro sin valor predeterminado, el valor
// predeterminado solo se puede usar llamando a la función con argumentos con nombre:

fun foo(
    bar: Int = 0,
    baz: Int,
) { /*...*/ }

// foo(baz = 1) // El valor predeterminado bar = 0 Se utiliza

// Si el último argumento después de los parámetros predeterminados es un lambda, puede pasarlo
// como un argumento con nombre o fuera de los paréntesis:

fun foo(
    bar: Int = 0,
    baz: Int = 1,
    qux: () -> Unit,
) { /*...*/ }
/*
foo(1) { println("hello") }     // Uses the default value baz = 1
foo(qux = { println("hello") }) // Uses both default values bar = 0 and baz = 1
foo { println("hello") }        // Uses both default values bar = 0 and baz = 1
*/

// ------- Argumentos con nombre -------

// Puedes nombrar uno o más de los argumentos de una función al llamarla. Esto puede ser útil
// cuando una función tiene muchos argumentos y es difícil asociar un valor con un argumento,
// especialmente si es un valor booleano o nulo.

// Cuando utiliza argumentos con nombre en una llamada a la función, puede cambiar libremente
// el orden en el que se enumeran. Si quieres usar sus valores predeterminados, puedes dejar
// estos argumentos fuera por completo.

// Considere la función reformat(), que tiene 4 argumentos con valores predeterminados.

fun reformat(
    str: String,
    normalizeCase: Boolean = true,
    upperCaseFirstLetter: Boolean = true,
    divideByCamelHumps: Boolean = false,
    wordSeparator: Char = ' ',
) { /*...*/ }

// Al llamar a esta función, no tienes que nombrar todos sus argumentos:

/*
reformat(
    "String!",
    false,
    upperCaseFirstLetter = false,
    divideByCamelHumps = true,
    '_'
)
 */

// Puedes omitir todos los que tienen valores predeterminados:

// reformat("This is a long String!")

// También puedes omitir argumentos específicos con valores predeterminados, en lugar de
// omitirlos todos. Sin embargo, después del primer argumento omitido, debe nombrar todos
// los argumentos posteriores:

// reformat("This is a short String!", upperCaseFirstLetter = false, wordSeparator = '_')

fun foo(vararg strings: String) { /*...*/ }

// foo(strings = *arrayOf("a", "b", "c"))

// ******************** Info ********************
// Al llamar a las funciones de Java en la JVM, no se puede usar la sintaxis del argumento
// con nombre porque el código de bytes de Java no siempre conserva los nombres de los
// parámetros de la función.

// ------------ Funciones de retorno de unidades ------------

// Si una función no devuelve un valor útil, su tipo de retorno es Unidad. Unidad es un tipo
// con un solo valor: Unidad. Este valor no tiene que ser devuelto explícitamente:

fun printHello(name: String?): Unit {
    if (name != null)
        println("Hello $name")
    else
        println("Hi there!")
    // `return Unit` or `return` is optional
}

// La declaración del tipo de devolución de la unidad también es opcional. El código anterior
// es equivalente a:

// fun printHello(name: String?) { ... }

// ------------ Funciones de expresión única ------------

// Cuando el cuerpo de la función consiste en una sola expresión, se pueden omitir las llaves y
// el cuerpo se puede especificar después de un símbolo =:

// fun double(x: Int): Int = x * 2

// Declarar explícitamente que el tipo de retorno es opcional cuando el compilador puede
// inferir esto:

// fun double(x: Int) = x * 2

// ------- Tipos de devolución explícitos -------

// Las funciones con el cuerpo del bloque siempre deben especificar los tipos de retorno
// explícitamente, a menos que esté destinado a que devuelvan la unidad, en cuyo caso
// especificar el tipo de retorno es opcional.

// Kotlin no infiere tipos de retorno para funciones con cuerpos de bloque porque tales
// funciones pueden tener un flujo de control complejo en el cuerpo, y el tipo de retorno
// no será obvio para el lector (y a veces incluso para el compilador).

// ------- Número variable de argumentos (varargs) -------

// Puedes marcar un parámetro de una función (generalmente el último) con el modificador vararg:

fun <T> asList(vararg ts: T): List<T> {
    val result = ArrayList<T>()
    for (t in ts) // ts is an Array
        result.add(t)
    return result
}

// En este caso, puedes pasar un número variable de argumentos a la función:

val list = asList(1, 2, 3)

// Dentro de una función, un parámetro vararg de tipo T es visible como una matriz de T,
// como en el ejemplo anterior, donde la variable ts tiene el tipo Array<out T>.

// Solo se puede marcar un parámetro como vararg. Si un parámetro vararg no es el último
// de la lista, los valores de los parámetros posteriores se pueden pasar utilizando la
// sintaxis del argumento con nombre o, si el parámetro tiene un tipo de función, pasando
// un lambda fuera de los paréntesis.

// Cuando llamas a una función vararg, puedes pasar argumentos individualmente, por ejemplo,
// asList(1, 2, 3). Si ya tiene una matriz y desea pasar su contenido a la función, utilice
// el operador de propagación (prefija la matriz con *):

val a = arrayOf(1, 2, 3)
val list = asList(-1, 0, *a, 4)

// Si desea pasar una matriz de tipo primitivo en vararg, debe convertirla en una matriz normal
// (tipada) utilizando la función toTypedArray():

val a = intArrayOf(1, 2, 3) // IntArray is a primitive type array
val list = asList(-1, 0, *a.toTypedArray(), 4)

// ------- Notación de infijo -------

// Las funciones marcadas con la palabra clave infix también se pueden llamar usando la notación
// infix (omitiendo el punto y los paréntesis para la llamada). Las funciones de infijo deben
// cumplir con los siguientes requisitos:

// Deben ser funciones de miembro o de extensión.

// Deben tener un solo parámetro.

// El parámetro no debe aceptar el número variable de argumentos y no debe tener un valor
// predeterminado.

/*
infix fun Int.shl(x: Int): Int { ... }

// calling the function using the infix notation
1 shl 2

// is the same as
1.shl(2)

*/

// ******************** Info ********************
// Las llamadas a funciones infix tienen una precedencia más baja que los operadores aritméticos,
// los moldes de tipo y el operador rangeTo. Las siguientes expresiones son equivalentes:

// 1 shl 2 + 3 es equivalente a 1 shl (2 + 3)

// 0 hasta n * 2 es equivalente a 0 hasta (n * 2)

// Xs union ys como Set<*> es equivalente a xs union (ys como Set<*>)

// Por otro lado, la precedencia de una llamada a una función de infix es mayor que la de los
// operadores booleanos && y ||, is- y in-checks, y algunos otros operadores. Estas expresiones
// también son equivalentes:

// A && b xor c es equivalente a a && (b xor c)

// A xor b en c es equivalente a (a xor b) en c
// **********************************************

// Tenga en cuenta que las funciones de infijo siempre requieren que se especifique tanto el
// receptor como el parámetro. Cuando llames a un método en el receptor actual usando la notación
// de infijo, úsalo explícitamente. Esto es necesario para garantizar un análisis inequívoco.

class MyStringCollection {
    infix fun add(s: String) { /*...*/ }

    fun build() {
        this add "abc"   // Correct
        add("abc")       // Correct
        //add "abc"        // Incorrect: the receiver must be specified
    }
}

// ------------ Alcance de la función ------------

// Las funciones de Kotlin se pueden declarar en el nivel superior en un archivo, lo que
// significa que no es necesario crear una clase para mantener una función, lo que debe
// hacer en lenguajes como Java, C# y Scala (la definición de nivel superior está
// disponible desde Scala 3). Además de las funciones de nivel superior, las funciones de
// Kotlin también se pueden declarar localmente como funciones miembros y
// funciones de extensión.

// ------- Funciones locales -------

// Kotlin admite funciones locales, que son funciones dentro de otras funciones:

fun dfs(graph: Graph) {
    fun dfs(current: Vertex, visited: MutableSet<Vertex>) {
        if (!visited.add(current)) return
        for (v in current.neighbors)
            dfs(v, visited)
    }

    dfs(graph.vertices[0], HashSet())
}

// Una función local puede acceder a las variables locales de las funciones externas (el cierre).
// En el caso anterior, visitado puede ser una variable local:

fun dfs(graph: Graph) {
    val visited = HashSet<Vertex>()
    fun dfs(current: Vertex) {
        if (!visited.add(current)) return
        for (v in current.neighbors)
            dfs(v)
    }

    dfs(graph.vertices[0])
}

// Funciones de los miembros

//Una función miembro es una función que se define dentro de una clase u objeto:

class Sample {
    fun foo() { print("Foo") }
}

// Las funciones de los miembros se llaman con notación de puntos:

// Sample().foo() // Reates la instancia de la muestra de la clase y llama a foo

// Para obtener más información sobre las clases y los miembros andentados,
// consulte Clases y herencia.

// ------------ Funciones genéricas ------------

// Las funciones pueden tener parámetros genéricos, que se especifican usando
// corchetes de ángulo antes del nombre de la función:

fun <T> singletonList(item: T): List<T> { /*...*/ }

// Para obtener más información sobre las funciones genéricas, consulte Genéricos.

// ------------ Funciones recursivas de cola ------------

// Kotlin admite un estilo de programación funcional conocido como recursividad de cola.
// Para algunos algoritmos que normalmente usarían bucles, puede usar una función
// recursiva en su lugar sin el riesgo de desbordamiento de la pila. Cuando una función
// está marcada con el modificador tailrec y cumple con las condiciones formales requeridas,
// el compilador optimiza la recursión, dejando atrás una versión rápida y
// eficiente basada en bucles:

val eps = 1E-10 // "good enough", could be 10^-15

tailrec fun findFixPoint(x: Double = 1.0): Double =
    if (Math.abs(x - Math.cos(x)) < eps) x else findFixPoint(Math.cos(x))

// Este código calcula el punto fijo del coseno, que es una constante matemática. Simplemente
// llama a Math.cos repetidamente comenzando en 1.0 hasta que el resultado ya no cambia,
// lo que produce un resultado de 0,7390851332151611 para la precisión eps especificada.
// El código resultante es equivalente a este estilo más tradicional:

val eps = 1E-10 // "good enough", could be 10^-15

private fun findFixPoint(): Double {
    var x = 1.0
    while (true) {
        val y = Math.cos(x)
        if (Math.abs(x - y) < eps) return x
        x = Math.cos(x)
    }
}

// Para ser elegible para el modificador tailrec, una función debe llamarse a sí misma como la
// última operación que realiza. No puede usar la recursión de cola cuando hay más código después
// de la llamada recursiva, dentro de los bloques try/catch/finally, o en funciones abiertas.
// Actualmente, la recursión de la cola es compatible con Kotlin para la JVM y Kotlin/Native.

// Ver también:

// Funciones en línea

// Funciones de extensión

// Funciones de orden superior y lambdas

fun main() {

}