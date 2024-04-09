// ------------ Extensiones ------------

// Kotlin provides the ability to extend a class or an interface
// with new functionality without having to inherit from the class
// or use design patterns such as Decorator. This is done via
// special declarations called extensions.

//For example, you can write new functions for a class or an interface
// from a third-party library that you can't modify. Such functions can
// be called in the usual way, as if they were methods of the original class.
// This mechanism is called an extension function. There are also extension
// properties that let you define new properties for existing classes.

// ------------ Funciones de extensión ------------

// Para declarar una función de extensión, prefija su nombre con un tipo de
// receptor, que se refiere al tipo que se está extendiendo. Lo siguiente
// añade una función de intercambio a MutableList<Int>:

fun MutableList<Int>.swap(index1: Int, index2: Int) {
    val tmp = this[index1] // 'this' corresponds to the list
    this[index1] = this[index2]
    this[index2] = tmp
}

// La palabra clave está dentro de una función de extensión corresponde al objeto
// receptor (el que se pasa antes del punto). Ahora, puedes llamar a una función
// de este tipo en cualquier MutableList<Int>:

val list = mutableListOf(1, 2, 3)
list.swap(0, 2) // 'this' inside 'swap()' will hold the value of 'list'

// Esta función tiene sentido para cualquier MutableList<T>, y puedes hacerla genérica:

fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    val tmp = this[index1] // 'this' corresponds to the list
    this[index1] = this[index2]
    this[index2] = tmp
}

// Debe declarar el parámetro de tipo genérico antes del nombre de la función para que
// esté disponible en la expresión de tipo receptor. Para obtener más información sobre
// los genéricos, consulte las funciones genéricas.

// ------------ Las extensiones se resuelven de forma estática ------------

// Las extensiones en realidad no modifican las clases que extienden. Al definir una
// extensión, no se están insertando nuevos miembros en una clase, solo haciendo que
// se puedan llamar nuevas funciones con la notación de dos en variables de este tipo.

// Las funciones de extensión se envían de forma estática. Entonces, qué función de
// extensión se llama ya se conoce en tiempo de compilación en función del tipo de
// receptor. Por ejemplo:


fun main() {
    open class Shape
    class Rectangle: Shape()

    fun Shape.getName() = "Shape"
    fun Rectangle.getName() = "Rectangle"

    fun printClassName(s: Shape) {
        println(s.getName())
    }

    printClassName(Rectangle())


// This example prints Shape, because the extension function called depends only on
// the declared type of the parameter s, which is the Shape class.

// If a class has a member function, and an extension function is defined which has
// the same receiver type, the same name, and is applicable to given arguments,
// the member always wins. For example:

// Este código imprime el método de clase.

    class Example2 {
        fun printFunctionType() { println("Class method") }
    }

    fun Example2.printFunctionType() { println("Extension function") }

    Example2().printFunctionType()

// Sin embargo, está perfectamente bien que las funciones de extensión sobrecarguen
// las funciones de los miembros que tienen el mismo nombre pero una firma diferente:

    class Example3 {
        fun printFunctionType() { println("Class method") }
    }

    fun Example3.printFunctionType(i: Int) { println("Extension function #$i") }

    Example3().printFunctionType(1)

// ------------ Receptor nulo ------------

// Tenga en cuenta que las extensiones se pueden definir con un tipo de receptor nulo.
// Estas extensiones se pueden llamar en una variable de objeto incluso si su valor es nulo.
// Si el receptor es nulo, entonces esto también es nulo. Por lo tanto, al definir una
// extensión con un tipo de receptor nulo, recomendamos realizar una verificación
// esta == nula dentro del cuerpo de la función para evitar errores del compilador.

//Puedes llamar a toString() en Kotlin sin comprobar si es nulo, ya que la comprobación
// ya ocurre dentro de la función de extensión:

    fun Any?.toString(): String {
        if (this == null) return "null"
        // After the null check, 'this' is autocast to a non-nullable type, so the toString() below
        // resolves to the member function of the Any class
        return toString()
    }

// ------------ Propiedades de extensión ------------

// Kotlin admite propiedades de extensión al igual que admite funciones:

    val <T> List<T>.lastIndex: Int
    get() = size - 1

// ************ info ************
// Dado que las extensiones en realidad no insertan miembros en las clases, no hay una forma
// eficiente de que una propiedad de extensión tenga un campo de respaldo. Esta es la razón
// por la que los inicializadores no están permitidos para las propiedades de extensión.
// Su comportamiento solo se puede definir proporcionando explícitamente getters/setters.

// Ejemplo:

    val House.number = 1 // error: initializers are not allowed for extension properties

// ------------ Extensiones de objetos complementarios ------------

// Si una clase tiene un objeto acompañante definido, también puede definir funciones y
// propiedades de extensión para el objeto acompañante. Al igual que los miembros
// habituales del objeto acompañante, se les puede llamar usando solo el nombre de la
// clase como calificador:

    class MyClass {
        companion object { }  // will be called "Companion"
    }

    fun MyClass.Companion.printCompanion() { println("companion") }

    fun main() {
        MyClass.printCompanion()
    }

// ------------ Scope of extensions ------------

// En la mayoría de los casos, usted define extensiones en el nivel superior,
// directamente debajo de los paquetes:

    package org.example.declarations

    fun List<String>.getLongestString() { /*...*/}

// Para usar una extensión fuera de su paquete de declaración, impórtela en el
// sitio de llamada:

    package org.example.usage

    import org.example.declarations.getLongestString

    fun main() {
        val list = listOf("red", "green", "blue")
        list.getLongestString()
    }

// Consulte Importaciones para obtener más información.

// ------------ Declarar extensiones como miembros ------------

// Puedes declarar extensiones para una clase dentro de otra clase. Dentro de dicha
// extensión, hay múltiples receptores implícitos, objetos a cuyos miembros se puede
// acceder sin un calificador. Una instancia de una clase en la que se declara
// la extensión se llama receptor de envío, y una instancia del tipo de receptor del
// método de extensión se llama receptor de extensión.

    class Host(val hostname: String) {
        fun printHostname() { print(hostname) }
    }

    class Connection(val host: Host, val port: Int) {
        fun printPort() { print(port) }

        fun Host.printConnectionString() {
            printHostname()   // calls Host.printHostname()
            print(":")
            printPort()   // calls Connection.printPort()
        }

        fun connect() {
            /*...*/
            host.printConnectionString()   // calls the extension function
        }
    }

    /*
    fun main() {
    Connection(Host("kotl.in"), 443).connect()
    //Host("kotl.in").printConnectionString()
    // error, the extension function is unavailable outside Connection
}
     */

// En caso de un conflicto de nombre entre los miembros de un receptor de despacho
// y un receptor de extensión, el receptor de extensión tiene prioridad.
// Para referirse al miembro del receptor de envío, puede usar esta sintaxis calificada.

    class Connection {
        fun Host.getConnectionString() {
            toString()         // calls Host.toString()
            this@Connection.toString()  // calls Connection.toString()
        }
    }

// Las extensiones declaradas como miembros se pueden declarar como abiertas y anuladas
// en subclases. Esto significa que el envío de dichas funciones es virtual con respecto
// al tipo de receptor de despacho, pero estático con respecto al
// tipo de receptor de extensión.
open class Base { }

    class Derived : Base() { }

    open class BaseCaller {
        open fun Base.printFunctionInfo() {
            println("Base extension function in BaseCaller")
        }

        open fun Derived.printFunctionInfo() {
            println("Derived extension function in BaseCaller")
        }

        fun call(b: Base) {
            b.printFunctionInfo()   // call the extension function
        }
    }

    class DerivedCaller: BaseCaller() {
        override fun Base.printFunctionInfo() {
            println("Base extension function in DerivedCaller")
        }

        override fun Derived.printFunctionInfo() {
            println("Derived extension function in DerivedCaller")
        }
    }
/*
fun main() {
    BaseCaller().call(Base())   // "Base extension function in BaseCaller"
    DerivedCaller().call(Base())  // "Base extension function in DerivedCaller" - dispatch receiver is resolved virtually
    DerivedCaller().call(Derived())  // "Base extension function in DerivedCaller" - extension receiver is resolved statically
}
 */

// ------------ Nota sobre la visibilidad ------------

// Las extensiones utilizan los mismos modificadores de visibilidad que las funciones
// regulares declaradas en el mismo alcance. Por ejemplo:

//Una extensión declarada en el nivel superior de un archivo tiene acceso a las otras
// declaraciones privadas de nivel superior en el mismo archivo.

//Si una extensión se declara fuera de su tipo de receptor, no puede acceder a los
// miembros privados o protegidos del receptor.



}



