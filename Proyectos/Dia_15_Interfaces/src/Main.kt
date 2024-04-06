// ------------ Interfaces ------------

// Las interfaces en Kotlin pueden contener declaraciones de métodos
// abstractos, así como implementaciones de métodos. Lo que los hace
// diferentes de las clases abstractas es que las interfaces no pueden
// almacenar el estado. Pueden tener propiedades, pero estas deben ser
// abstractas o proporcionar implementaciones de acceso.

// Una interfaz se define utilizando la palabra clave interface:

interface MyInterface2 {
    fun bar()
    fun foo() {
        // optional body
    }
}

// ------------ Implementación de interfaces ------------

// Una clase u objeto puede implementar una o más interfaces:

class Child : MyInterface {
    override fun bar() {
        // body
    }
}

// ------------ Propiedades en las interfaces ------------

// Puedes declarar propiedades en las interfaces. Una propiedad declarada en una
// interfaz puede ser abstracta o proporcionar implementaciones para los accesores.
// Las propiedades declaradas en las interfaces no pueden tener campos de respaldo
// y, por lo tanto, los accesores declarados en las interfaces no pueden hacer
// referencia a ellos:

interface MyInterface {
    val prop: Int // abstract

    val propertyWithImplementation: String
        get() = "foo"

    fun foo() {
        print(prop)
    }
}

class Child3 : MyInterface {
    override val prop: Int = 29
}


// ------------ Herencia de interfaces ------------

// Una interfaz puede derivarse de otras interfaces, lo que significa que puede
// proporcionar implementaciones para sus miembros y declarar nuevas funciones
// y propiedades. Naturalmente, las clases que implementan una interfaz de este
// tipo solo son necesarias para definir las implementaciones que faltan:

interface Named {
    val name: String
}

interface Person : Named {
    val firstName: String
    val lastName: String

    override val name: String get() = "$firstName $lastName"
}

data class Employee(
    // implementing 'name' is not required
    override val firstName: String,
    override val lastName: String,
    val position: Position
) : Person

// ------------ Resolver conflictos imperativos ------------

// Cuando declaras muchos tipos en tu lista de supertipos, puedes heredar más
// de una implementación del mismo método:

interface A {
    fun foo() { print("A") }
    fun bar()
}

interface B {
    fun foo() { print("B") }
    fun bar() { print("bar") }
}

class C : A {
    override fun bar() { print("bar") }
}

class D : A, B {
    override fun foo() {
        super<A>.foo()
        super<B>.foo()
    }

    override fun bar() {
        super<B>.bar()
    }
}

// Las interfaces A y B declaran las funciones foo() y bar(). Ambos implementan
// foo(), pero solo B implementa bar() (bar() no está marcado como abstracto en A,
// porque este es el valor predeterminado para las interfaces si la función no
// tiene cuerpo). Ahora, si derivas una clase concreta C de A, tienes que anular
// bar() y proporcionar una implementación.

//Sin embargo, si deriva D de A y B, debe implementar todos los métodos que ha
// heredado de múltiples interfaces, y debe especificar exactamente cómo D debe
// implementarlos. Esta regla se aplica tanto a los métodos para los que ha
// heredado una sola implementación (bar()) como a aquellos para los que ha
// heredado múltiples implementaciones (foo()).

fun main() {

}