// ------------ Estas expresiones ------------

// Para indicar el receptor actual, utiliza estas expresiones:

// En un miembro de una clase, esto se refiere al objeto actual de esa clase.

// En una función de extensión o una función literal con receptor, esto denota
// el parámetro receptor que se pasa en el lado izquierdo de un doto.

// Si esto no tiene calificadores, se refiere al ámbito más interno que lo encierra.
// Para hacer referencia a esto en otros ámbitos, se utilizan calificadores de etiqueta:

// ------------ Calificó esto ------------

// Para acceder a esto desde un ámbito externo (una clase, una función de extensión
// o un literal de función etiquetada con receptor), escriba this@label, donde @label
// es una etiqueta en el ámbito del que está destinado a ser:

class A { // implicit label @A
    inner class B { // implicit label @B
        fun Int.foo() { // implicit label @foo
            val a = this@A // A's this
            val b = this@B // B's this

            val c = this // foo()'s receiver, an Int
            val c1 = this@foo // foo()'s receiver, an Int

            val funLit = lambda@ fun String.() {
                val d = this // funLit's receiver, a String
            }

            val funLit2 = { s: String ->
                // foo()'s receiver, since enclosing lambda expression
                // doesn't have any receiver
                val d1 = this
            }
        }
    }
}

// ------------ Implícito esto ------------

// Cuando llamas a una función de miembro en esto, puedes omitir la parte de esto.
// Si tiene una función que no es miembro con el mismo nombre, utilícela con
// precaución porque en algunos casos se puede llamar en su lugar:


fun main() {
    fun printLine() { println("Top-level function") }

    class A {
        fun printLine() { println("Member function") }

        fun invokePrintLine(omitThis: Boolean = false)  {
            if (omitThis) printLine()
            else this.printLine()
        }
    }

    A().invokePrintLine() // Member function
    A().invokePrintLine(omitThis = true) // Top-level function()
}