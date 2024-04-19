// ------------ Tipos de alias ------------

// Los alias de tipo proporcionan nombres alternativos para los tipos existentes.
// Si el nombre del tipo es demasiado largo, puede introducir un nombre diferente
// más corto y usar el nuevo en su lugar.

// Es útil acortar los tipos genéricos largos. Por ejemplo, a menudo es tentador
// reducir los tipos de recolección:

typealias NodeSet = Set<Network.Node>

typealias FileTable<K> = MutableMap<K, MutableList<File>>

// Puede proporcionar diferentes alias para los tipos de funciones:

typealias MyHandler = (Int, String, Any) -> Unit

typealias Predicate<T> = (T) -> Boolean

// Puedes tener nuevos nombres para las clases internas y anidadas:

class A {
    inner class Inner
}
class B {
    inner class Inner
}

typealias AInner = A.Inner
typealias BInner = B.Inner

// Los alias de tipo no introducen nuevos tipos. Son equivalentes a los tipos
// subyacentes correspondientes. Cuando agregas typealias Predicate<T> y usas
// Predicate<Int> en tu código, el compilador de Kotlin siempre lo expande a
// (Int) -> Boolean. Por lo tanto, puede pasar una variable de su tipo siempre
// que se requiera un tipo de función general y viceversa:


typealias Predicate2<T> = (T) -> Boolean

fun foo(p: Predicate2<Int>) = p(42)

fun main() {
    val f: (Int) -> Boolean = { it > 0 }
    println(foo(f)) // prints "true"

    val p: Predicate2<Int> = { it > 0 }
    println(listOf(1, -2).filter(p)) // prints "[1]"
}