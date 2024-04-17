// ------------ Delegación ------------

// El patrón de delegación ha demostrado ser una buena alternativa a la herencia de
// la implementación, y Kotlin lo apoya de forma nativa que requiere cero código repetitivo.

// Una clase derivada puede implementar una base de interfaz delegando a todos sus miembros
// públicos en un objeto específico:

/*
interface Base {
    fun print()
}

class BaseImpl(val x: Int) : Base {
    override fun print() { print(x) }
}

class Derived(b: Base) : Base by b

fun main() {
    val b = BaseImpl(10)
    Derived(b).print()
}
 */

// La cláusula de by-type en la lista de supertipos para Derivado indica que b se almacenará
// internamente en objetos de Derivado y el compilador generará todos los métodos de Base que
// se reenvíen a b.

// ------------Anular a un miembro de una interfaz implementada por delegación ------------

// Anula el trabajo como se espera: el compilador utilizará sus implementaciones de anulación
// en lugar de las del objeto delegado. Si desea agregar override fun printMessage()
// { print("abc") } a Derived, el programa imprimiría abc en lugar de 10 cuando se llama a
// printMessage:

/*
interface Base {
    fun printMessage()
    fun printMessageLine()
}

class BaseImpl(val x: Int) : Base {
    override fun printMessage() { print(x) }
    override fun printMessageLine() { println(x) }
}

class Derived(b: Base) : Base by b {
    override fun printMessage() { print("abc") }
}

fun main() {
    val b = BaseImpl(10)
    Derived(b).printMessage()
    Derived(b).printMessageLine()
}
 */

// Tenga en cuenta, sin embargo, que los miembros anulados de esta manera no son llamados
// por los miembros del objeto delegado, que solo pueden acceder a sus propias implementaciones
// de los miembros de la interfaz:

interface Base {
    val message: String
    fun print()
}

class BaseImpl(val x: Int) : Base {
    override val message = "BaseImpl: x = $x"
    override fun print() { println(message) }
}

class Derived(b: Base) : Base by b {
    // This property is not accessed from b's implementation of `print`
    override val message = "Message of Derived"
}

fun main() {
    val b = BaseImpl(10)
    val derived = Derived(b)
    derived.print()
    println(derived.message)
}