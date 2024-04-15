// ------------ Clases de valor en línea ------------

// A veces es útil envolver un valor en una clase para crear un tipo más
// específico del dominio. Sin embargo, introduce sobrecargas en tiempo
// de ejecución debido a asignaciones de montón adicionales. Además, si
// el tipo envuelto es primitivo, el rendimiento es significativo, porque
// los tipos primitivos suelen estar muy optimizados por el tiempo de
// ejecución, mientras que sus envolturas no reciben ningún tratamiento especial.

//Para resolver estos problemas, Kotlin introduce un tipo especial de
// clase llamada clase en línea. Las clases en línea son un subconjunto
// de clases basadas en valores. No tienen identidad
// y solo pueden tener valores.

//Para declarar una clase en línea, use el modificador de valor antes
// del nombre de la clase:

value class Password(private val s: String)

// Para declarar una clase en línea para el backend de JVM, utilice el
// modificador de valor junto con la anotación @JvmInline antes de la
// declaración de clase:

// For JVM backends
@JvmInline
value class Password2(private val s: String)

// Una clase en línea debe tener una sola propiedad inicializada en el
// constructor principal. En tiempo de ejecución, las instancias de la
// clase en línea se representarán utilizando esta única propiedad (
// consulte los detalles sobre la representación en tiempo de ejecución
// a continuación):

// No actual instantiation of class 'Password' happens
// At runtime 'securePassword' contains just 'String'
val securePassword = Password("Don't try this in production")

// Esta es la característica principal de las clases en línea, que inspiraron
// el nombre en línea: los datos de la clase están en línea en sus usos
// (similar a la forma en que el contenido de las funciones en línea está en
// línea para llamar a los sitios).

// ------------ Miembros ------------

// Las clases en línea admiten algunas funcionalidades de las clases regulares.
// En particular, se les permite declarar propiedades y funciones, tener un
// bloque de inicio y constructores secundarios:

@JvmInline
value class Person(private val fullName: String) {
    init {
        require(fullName.isNotEmpty()) {
            "Full name shouldn't be empty"
        }
    }

    constructor(firstName: String, lastName: String) : this("$firstName $lastName") {
        require(lastName.isNotBlank()) {
            "Last name shouldn't be empty"
        }
    }

    val length: Int
        get() = fullName.length

    fun greet() {
        println("Hello, $fullName")
    }
}

fun main() {
    val name1 = Person("Kotlin", "Mascot")
    val name2 = Person("Kodee")
    name1.greet() // the `greet()` function is called as a static method
    println(name2.length) // property getter is called as a static method

// Las propiedades de la clase en línea no pueden tener campos de respaldo. Solo pueden
// tener propiedades computables simples (sin propiedades lateinit/delegadas).

// ------------ Herencia ------------

// Las clases en línea pueden heredar de las interfaces:

/*
interface Printable {
    fun prettyPrint(): String
}

@JvmInline
value class Name(val s: String) : Printable {
    override fun prettyPrint(): String = "Let's $s!"
}

fun main() {
    val name = Name("Kotlin")
    println(name.prettyPrint()) // Still called as a static method
}
 */

// Está prohibido que las clases en línea participen en una jerarquía de clases.
// Esto significa que las clases en línea no pueden extender otras clases y
// siempre son definitivas.

// ------------ Representación ------------

// En el código generado, el compilador Kotlin mantiene una envoltura para cada clase
// en línea. Las instancias de clase en línea se pueden representar en tiempo de
// ejecución, ya sea como envolturas o como el tipo subyacente. Esto es similar a
// la forma en que Int se puede representar como un int primitivo o como
// el envoltorio Entero.

//El compilador de Kotlin preferirá usar tipos subyacentes en lugar de envoltorios
// para producir el código más eficiente y optimizado. Sin embargo, a veces es
// necesario mantener las envolturas alrededor. Como regla general, las clases en
// línea se empaquetan cada vez que se utilizan como otro tipo.
   /*
   interface I

@JvmInline
value class Foo(val i: Int) : I

fun asInline(f: Foo) {}
fun <T> asGeneric(x: T) {}
fun asInterface(i: I) {}
fun asNullable(i: Foo?) {}

fun <T> id(x: T): T = x

fun main() {
    val f = Foo(42)

    asInline(f)    // unboxed: used as Foo itself
    asGeneric(f)   // boxed: used as generic type T
    asInterface(f) // boxed: used as type I
    asNullable(f)  // boxed: used as Foo?, which is different from Foo

    // below, 'f' first is boxed (while being passed to 'id') and then unboxed (when returned from 'id')
    // In the end, 'c' contains unboxed representation (just '42'), as 'f'
    val c = id(f)
}
    */

// Debido a que las clases en línea pueden representarse tanto como el valor subyacente
// como una envoltura, la igualdad referencial no tiene sentido para ellas y, por lo tanto,
// está prohibida.

//Las clases en línea también pueden tener un parámetro de tipo genérico como tipo subyacente.
// En este caso, el compilador lo asigna a ¿Alguno? O, en general, al límite superior del
// parámetro de tipo.


}
@JvmInline
value class UserId<T>(val value: T)

fun compute(s: UserId<String>) {} // compiler generates fun compute-<hashcode>(s: Any?)

// ------- Mangling -------

//Dado que las clases en línea se compilan a su tipo subyacente, puede conducir a varios
// errores oscuros, por ejemplo, conflictos inesperados de la firma de la plataforma:

@JvmInline
value class UInt(val x: Int)

// Represented as 'public final void compute(int x)' on the JVM
fun compute(x: Int) { }

// Also represented as 'public final void compute(int x)' on the JVM!
fun compute2(x: UInt) { }

// Para mitigar estos problemas, las funciones que utilizan clases en línea se mezclan
// añadiendo un código hash estable al nombre de la función. Por lo tanto,
// fun compute(x: UInt) se representará como public final void compute-<hashcode>(int x),
// lo que resuelve el problema de choque.

// ------- Llamando desde el código Java -------

// Puedes llamar a funciones que acepten clases en línea desde el código Java. Para ello,
// debe desactivar manualmente la manclación: agregue la anotación @JvmName antes de la
// declaración de la función:

@JvmInline
value class UInt3(val x: Int)

fun compute3(x: Int) { }

@JvmName("computeUInt")
fun compute(x: UInt) { }

// ------------ Clases en línea frente a alias de tipo ------------

//A primera vista, las clases en línea parecen muy similares a los alias de tipo. De hecho,
// ambos parecen introducir un nuevo tipo y ambos se representarán como el tipo subyacente
// en tiempo de ejecución.

// Sin embargo, la diferencia crucial es que los alias de tipo son compatibles con la asignación
// con su tipo subyacente (y con otros alias de tipo con el mismo tipo subyacente), mientras
// que las clases en línea no lo son.

// En otras palabras, las clases en línea introducen un tipo verdaderamente nuevo, a diferencia
// de los alias de tipo que solo introducen un nombre alternativo (alias)
// para un tipo existente:

/*
typealias NameTypeAlias = String

@JvmInline
value class NameInlineClass(val s: String)

fun acceptString(s: String) {}
fun acceptNameTypeAlias(n: NameTypeAlias) {}
fun acceptNameInlineClass(p: NameInlineClass) {}

fun main() {
    val nameAlias: NameTypeAlias = ""
    val nameInlineClass: NameInlineClass = NameInlineClass("")
    val string: String = ""

    acceptString(nameAlias) // OK: pass alias instead of underlying type
    acceptString(nameInlineClass) // Not OK: can't pass inline class instead of underlying type

    // And vice versa:
    acceptNameTypeAlias(string) // OK: pass underlying type instead of alias
    acceptNameInlineClass(string) // Not OK: can't pass underlying type instead of inline class
}
*/

// ------------ Clases en línea y delegación ------------

// La implementación por delegación al valor en línea de la clase en línea está permitida
// con interfaces:

/*
interface MyInterface {
    fun bar()
    fun foo() = "foo"
}

@JvmInline
value class MyInterfaceWrapper(val myInterface: MyInterface) : MyInterface by myInterface

fun main() {
    val my = MyInterfaceWrapper(object : MyInterface {
        override fun bar() {
            // body
        }
    })
    println(my.foo()) // prints "foo"
}
 */
