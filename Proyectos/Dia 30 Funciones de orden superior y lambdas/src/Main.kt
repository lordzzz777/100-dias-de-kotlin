// ------------ Funciones de orden superior y lambdas ------------

// Las funciones de Kotlin son de primera clase, lo que significa que se pueden
// almacenar en variables y estructuras de datos, y se pueden pasar como argumentos
// y devolver desde otras funciones de orden superior. Puede realizar cualquier
// operación en funciones que sean posibles para otros valores que no son de función.

// Para facilitar esto, Kotlin, como un lenguaje de programación de tipo estático,
// utiliza una familia de tipos de funciones para representar funciones, y proporciona
// un conjunto de construcciones de lenguaje especializadas, como las expresiones lambda.

// ------------ Funciones de orden superior ------------

// Una función de orden superior es una función que toma funciones como parámetros o
// devuelve una función.

// Un buen ejemplo de una función de orden superior es el pliegue del modismo de
// programación funcional para colecciones. Toma un valor de acumulador inicial
// y una función de combinación y construye su valor de retorno combinando
// consecutivamente el valor del acumulador actual con cada elemento de recolección,
// reemplazando el valor del acumulador cada vez:

fun <T, R> Collection<T>.fold(
    initial: R,
    combine: (acc: R, nextElement: T) -> R
): R {
    var accumulator: R = initial
    for (element: T in this) {
        accumulator = combine(accumulator, element)
    }
    return accumulator
}

// En el código anterior, el parámetro combine tiene el tipo de función (R, T) -> R,
// por lo que acepta una función que toma dos argumentos de tipos R y T y devuelve un
// valor de tipo R. Se invoca dentro del bucle for, y el valor de retorno se asigna al
// acumulador.

// Para llamar a fold, debe pasarle una instancia del tipo de función como argumento,
// y las expresiones lambda (descritas con más detalle a continuación) se utilizan
// ampliamente para este propósito en los sitios de llamada a funciones de orden superior:


fun main() {
    val items = listOf(1, 2, 3, 4, 5)

    // Lambdas are code blocks enclosed in curly braces.
    items.fold(0, {
        // When a lambda has parameters, they go first, followed by '->'
            acc: Int, i: Int ->
        print("acc = $acc, i = $i, ")
        val result = acc + i
        println("result = $result")
        // The last expression in a lambda is considered the return value:
        result
    })

    // Parameter types in a lambda are optional if they can be inferred:
    val joinedToString = items.fold("Elements:", { acc, i -> acc + " " + i })

    // Function references can also be used for higher-order function calls:
    val product = items.fold(1, Int::times)

    println("joinedToString = $joinedToString")
    println("product = $product")
}

// ------------ Tipos de funciones ------------

// Kotlin utiliza tipos de funciones, como (Int) -> String, para declaraciones que se ocupan de funciones:
// val onClick: () -> Unit = ....

// Estos tipos tienen una notación especial que corresponde a las firmas de las funciones: sus parámetros
// y valores de retorno:

// Todos los tipos de funciones tienen una lista de tipos de parámetros entre paréntesis y un tipo de retorno:
// (A, B) -> C denota un tipo que representa funciones que toman dos argumentos de los tipos A y B y devuelven
// un valor de tipo C. La lista de tipos de parámetros puede estar vacía, como en () -> A. El tipo de
// devolución de la unidad no se puede omitir.

// Los tipos de funciones pueden tener opcionalmente un tipo de receptor adicional, que se especifica antes
// del punto en la notación: el tipo A.( B) -> C representa funciones que se pueden llamar en un objeto
// receptor A con un parámetro B y devolver un valor C. Los literales de función con receptor se utilizan
// a menudo junto con estos tipos.

// Las funciones de suspensión pertenecen a un tipo especial de tipo de función que tiene un modificador de
// suspensión en su notación, como suspender () -> Unidad o suspender A.( B) -> C.

// La notación del tipo de función puede incluir opcionalmente nombres para los parámetros de la función:
// (x: Int, y: Int) -> Punto. Estos nombres se pueden utilizar para documentar el significado de los parámetros.

// Para especificar que un tipo de función es anulable, use los paréntesis de la siguiente manera:
// ((Int, Int) -> Int)?.

// Los tipos de funciones también se pueden combinar usando paréntesis: (Int) -> ((Int) -> Unidad).


// ****************************************** Info ******************************************
// La notación de la flecha es asociativa a la derecha, (Int) -> (Int) -> Unidad es equivalente
// al ejemplo anterior, pero no a ((Int) -> (Int)) -> Unidad.


// También puede dar a un tipo de función un nombre alternativo usando un alias de tipo:

// typealias ClickHandler = (Button, ClickEvent) -> Unit


// ------- Instanciación de un tipo de función -------

// Hay varias formas de obtener una instancia de un tipo de función:

// Utilice un bloque de código dentro de un literal de función, en una de las siguientes formas:

// Una expresión lambda: { a, b -> a + b },

// Una función anónima: fun(s): String): Int { return s.toIntOrNull() ?: 0 }

// Los literales de función con receptor se pueden utilizar como valores de tipos
// de función con receptor.

// Utilice una referencia invocable a una declaración existente:

// Una función de nivel superior, local, miembro o de extensión: ::isOdd, String::toInt,

// Una propiedad de nivel superior, miembro o extensión: List<Int>::size,

// Un constructor: ::Regex

// Estos incluyen referencias invocables enlazadas que apuntan a un miembro de una instancia
// en particular: foo::toString.

// Utilice instancias de una clase personalizada que implemente un tipo de función como interfaz:


class IntTransformer: (Int) -> Int {
    override operator fun invoke(x: Int): Int = TODO()
}

val intFunction: (Int) -> Int = IntTransformer()

// El compilador puede inferir los tipos de funciones para las variables si hay suficiente información:

val a = { i: Int -> i + 1 } // The inferred type is (Int) -> Int

// Los valores no literales de los tipos de función con y sin un receptor son intercambiables, por lo que
// el receptor puede sustituir el primer parámetro, y viceversa. Por ejemplo, se puede pasar o asignar un
// valor de tipo (A, B) -> C cuando un valor de tipo A.( B) -> Se espera C, y al revés:

/*
fun main() {
    val repeatFun: String.(Int) -> String = { times -> this.repeat(times) }
    val twoParameters: (String, Int) -> String = repeatFun // OK

    fun runTransformation(f: (String, Int) -> String): String {
        return f("hello", 3)
    }
    val result = runTransformation(repeatFun) // OK

    println("result = $result")
}
 */

// ****************************************** Info ******************************************
// Un tipo de función sin receptor se infiere de forma predeterminada, incluso si una variable
// se inicializa con una referencia a una función de extensión. Para modificar eso, especifique
// el tipo de variable explícitamente.


// ------- Invocación de una instancia de tipo de función ------

// Se puede invocar un valor de un tipo de función utilizando su operador invoke(...): f.invoke(x)
// o simplemente f(x).

// Si el valor tiene un tipo de receptor, el objeto receptor debe pasarse como el primer argumento.
// Otra forma de invocar un valor de un tipo de función con receptor es anteponerlo con el objeto
// receptor, como si el valor fuera una función de extensión: 1.foo(2).

// Ejemplo:

/*
fun main() {
    val stringPlus: (String, String) -> String = String::plus
    val intPlus: Int.(Int) -> Int = Int::plus

    println(stringPlus.invoke("<-", "->"))
    println(stringPlus("Hello, ", "world!"))

    println(intPlus.invoke(1, 1))
    println(intPlus(1, 2))
    println(2.intPlus(3)) // extension-like call

}
 */


// ------------ Expresiones Lambda y funciones anónimas ------------

// Las expresiones Lambda y las funciones anónimas son literales de función. Los literales de función
// son funciones que no se declaran, pero que se pasan inmediatamente como una expresión. Considere el
// siguiente ejemplo:

// max(strings, { a, b -> a.length < b.length })

// La función max es una función de orden superior, ya que toma un valor de función como su segundo
// argumento. Este segundo argumento es una expresión que es en sí misma una función, llamada función
// literal, que es equivalente a la siguiente función nombrada:

fun compare(a: String, b: String): Boolean = a.length < b.length

// Sintaxis de expresión Lambda

// La forma sintáctica completa de las expresiones lambda es la siguiente:

// val sum: (Int, Int) -> Int = { x: Int, y: Int -> x + y }

// Una expresión lambda siempre está rodeada de llaves.

// Las declaraciones de parámetros en la forma sintáctica completa van dentro de llaves y tienen
// anotaciones de tipo opcionales.

// El cuerpo va tras el ->.

// Si el tipo de retorno inferido de la lambda no es Unidad, la última expresión (o posiblemente única)
// dentro del cuerpo de lambda se trata como el valor de retorno.

// Si dejas fuera todas las anotaciones opcionales, lo que queda se ve así:

// val sum = { x: Int, y: Int -> x + y }

// ------- Pasando lambdas de trailing -------

// De acuerdo con la convención de Kotlin, si el último parámetro de una función es una función, entonces
// una expresión lambda pasada como el argumento correspondiente se puede colocar fuera de los paréntesis:

// val product = items.fold(1) { acc, e -> acc * e }

// Dicha sintaxis también se conoce como lambda final.

// Si el lambda es el único argumento en esa llamada, los paréntesis se pueden omitir por completo:

// run { println("...") }

// It: nombre implícito de un solo parámetro

// Es muy común que una expresión lambda tenga un solo parámetro.

// Si el compilador puede analizar la firma sin ningún parámetro, no es necesario declarar el parámetro
// y -> se puede omitir. El parámetro se declarará implícitamente con el nombre:

// ints.filter { it > 0 } // this literal is of type '(it: Int) -> Boolean'

// Devolver un valor de una expresión lambda

// Puede devolver explícitamente un valor del lambda utilizando la sintaxis de retorno calificada.
// De lo contrario, el valor de la última expresión se devuelve implícitamente.

// Por lo tanto, los dos fragmentos siguientes son equivalentes:

/*
ints.filter {
    val shouldFilter = it > 0
    shouldFilter
}

ints.filter {
    val shouldFilter = it > 0
    return@filter shouldFilter
}
 */

// Esta convención, junto con pasar una expresión lambda fuera de los paréntesis,
// permite el código de estilo LINQ:

// strings.filter { it.length == 5 }.sortedBy { it }.map { it.uppercase() }

// ------- Subrayado para variables no utilizadas -------

// Si el parámetro lambda no se usa, puede colocar un guión bajo en lugar de su nombre:
// map.forEach { (_, value) -> println("$value!") }

// ------- Destrucción en lambdas -------

// La destrucción en lambdas se describe como parte de las declaraciones de desestructuración.

// ------- Funciones anónimas -------

// A la sintaxis de la expresión lambda anterior le falta una cosa: la capacidad de especificar
// el tipo de retorno de la función. En la mayoría de los casos, esto es innecesario porque el
// tipo de devolución se puede inferir automáticamente. Sin embargo, si necesita especificarlo
// explícitamente, puede usar una sintaxis alternativa: una función anónima.

// fun(x: Int, y: Int): Int = x + y

// Una función anónima se parece mucho a una declaración de función regular, excepto que se omite
// su nombre. Su cuerpo puede ser una expresión (como se muestra arriba) o un bloque:

/*
fun(x: Int, y: Int): Int {
    return x + y
}
*/

// Los parámetros y el tipo de retorno se especifican de la misma manera que para las funciones
// regulares, excepto que los tipos de parámetros se pueden omitir si se pueden inferir del contexto:

// ints.filter(fun(item) = item > 0)

// La inferencia de tipo de retorno para las funciones anónimas funciona igual que para las funciones
// normales: el tipo de retorno se infiere automáticamente para las funciones anónimas con un cuerpo
// de expresión, pero tiene que especificarse explícitamente (o se supone que es Unidad) para las
// funciones anónimas con un cuerpo de bloque.


// ****************************************** Info *******************************************
// Al pasar funciones anónimas como parámetros, colóquelas dentro de los paréntesis.
// La sintaxis abreviada que le permite dejar la función fuera de los paréntesis solo funciona
// para expresiones lambda.


// Otra diferencia entre las expresiones lambda y las funciones anónimas es el comportamiento de
// los retornos no locales. Una declaración de retorno sin etiqueta siempre regresa de la función
// declarada con la palabra clave fun. Esto significa que un retorno dentro de una expresión lambda
// volverá de la función adjunta, mientras que un retorno dentro de una función anónima volverá de
// la propia función anónima.

// ------- Cierres -------

// Una expresión lambda o función anónima (así como una función local y una expresión de objeto)
// pueden acceder a su cierre, que incluye las variables declaradas en el ámbito exterior.
// Las variables capturadas en el cierre se pueden modificar en el lambda:

/*
var sum = 0
ints.filter { it > 0 }.forEach {
    sum += it
}
print(sum)
 */

// ------- Literales de función con receptor -------

// Tipos de funciones con receptor, como A.( B) -> C, se puede instanciar con una forma especial
// de literales de función: literales de función con receptor.

// Como se mencionó anteriormente, Kotlin proporciona la capacidad de llamar a una instancia de
// un tipo de función con receptor mientras proporciona el objeto receptor.

// Dentro del cuerpo del literal de la función, el objeto receptor pasado a una llamada se convierte
// en un esto implícito, de modo que puede acceder a los miembros de ese objeto receptor sin ningún
// calificador adicional, o acceder al objeto receptor usando una expresión this.

// Este comportamiento es similar al de las funciones de extensión, que también le permiten acceder
// a los miembros del objeto receptor dentro del cuerpo de la función.

// Aquí hay un ejemplo de una función literal con receptor junto con su tipo, donde se llama más en
// el objeto receptor:

// val sum: Int.(Int) -> Int = { other -> plus(other) }

// La sintaxis de la función anónima le permite especificar directamente el tipo de receptor de una
// función literal. Esto puede ser útil si necesita declarar una variable de un tipo de función con
// el receptor, y luego usarla más tarde.

// val sum = fun Int.(other: Int): Int = this + other

// Las expresiones Lambda se pueden usar como literales de función con receptor cuando el tipo de
// receptor se puede inferir del contexto. Uno de los ejemplos más importantes de su uso son los
// constructores seguros para tipos

/*
class HTML {
    fun body() { ... }
}

fun html(init: HTML.() -> Unit): HTML {
    val html = HTML()  // create the receiver object
    html.init()        // pass the receiver object to the lambda
    return html
}

html {       // lambda with receiver begins here
    body()   // calling a method on the receiver object
}
 */

