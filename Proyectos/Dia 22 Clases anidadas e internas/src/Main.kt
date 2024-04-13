// ------------ Clases anidadas e internas ------------

// Las clases se pueden anidar en otras clases:

class Outer {
    private val bar: Int = 1
    class Nested {
        fun foo() = 2
    }
}

val demo = Outer.Nested().foo() // == 2

// También puedes usar interfaces con anidación. Todas las combinaciones de clases e
// interfaces son posibles: puedes anidar interfaces en clases, clases en interfaces
// e interfaces en interfaces.

interface OuterInterface {
    class InnerClass
    interface InnerInterface
}

class OuterClass {
    class InnerClass
    interface InnerInterface
}

// ------------ Clases internas ------------

// Una clase anidada marcada como interna puede acceder a los miembros de su clase externa.
// Las clases internas llevan una referencia a un objeto de una clase externa:

class Outer2 {
    private val bar: Int = 1
    inner class Inner {
        fun foo() = bar
    }
}

val demo2 = Outer2().Inner().foo() // == 1

// Consulte Calificado estas expresiones para aprender sobre la desambiguación de esto
// en las clases internas.

// ------------ Clases internas anónimas ------------

// Las instancias anónimas de clase interna se crean utilizando una expresión de objeto:
/*
window.addMouseListener(object : MouseAdapter() {

    override fun mouseClicked(e: MouseEvent) { ... }

    override fun mouseEntered(e: MouseEvent) { ... }
})
*/

// ************ info ************
// En la JVM, si el objeto es una instancia de una interfaz Java funcional (es decir, una
// interfaz Java con un solo método abstracto), puede crearlo usando una expresión lambda
// con el prefijo del tipo de interfaz:
//     val listener = ActionListener { println("clicked") }


fun main() {

}