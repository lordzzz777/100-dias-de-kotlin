// ------------- Herencias -------------

// Todas las clases en Kotlin tienen una superclase común,
// Any, que es la superclase predeterminada para una clase
// sin supertipos declarados:

class Example // Implicitly inherits from Any

// Cualquiera tiene tres métodos: equals(), hashCode() y toString().
// Por lo tanto, estos métodos se definen para todas las clases de Kotlin.

// Por defecto, las clases de Kotlin son definitivas, no se pueden heredar.
// Para que una clase sea heredable, márquela con la palabra clave abierta:

open class Base // Class is open for inheritance

// Para declarar un supertipo explícito, coloque el tipo después de dos puntos
// en el encabezado de la clase:

open class Base2(p: Int)

class Derived(p: Int) : Base2(p)

// Si la clase derivada tiene un constructor primario, la clase base puede (y debe)
// inicializarse en ese constructor primario de acuerdo con sus parámetros.

// Si la clase derivada no tiene un constructor primario, entonces cada constructor
// secundario tiene que inicializar el tipo base usando la súper palabra clave o
// tiene que delegar en otro constructor que lo haga. Tenga en cuenta que en este
// caso diferentes constructores secundarios pueden llamar a diferentes constructores
// del tipo base:

class MyView : View {
    constructor(ctx: Context) : super(ctx)

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)
}


// ------------ Overriding methods ------------

// Kotlin requires explicit modifiers for overridable members and overrides:

open class Shape {
    open fun draw() { /*...*/ }
    fun fill() { /*...*/ }
}

class Circle() : Shape() {
    override fun draw() { /*...*/ }
}

// El modificador de anulación es necesario para Circle.draw(). Si falta, el
// compilador se quejará. Si no hay un modificador abierto en una función,
// como Shape.fill(), no se permite declarar un método con la misma firma en
// una subclase, ya sea con anulación o sin ella. El modificador abierto no
// tiene efecto cuando se añade a los miembros de una clase final, una clase
// sin un modificador abierto.

//Un miembro marcado como anulación está abierto, por lo que puede ser anulado
// en subclases. Si quieres prohibir la anulación, usa final:

open class Rectangle() : Shape() {
    final override fun draw() { /*...*/ }
}

// ------- Overriding properties -------

// El mecanismo de anulación funciona en las propiedades de la misma manera que
// lo hace en los métodos. Las propiedades declaradas en una superclase que luego
// se vuelven a declarar en una clase derivada deben estar precedidas con anulación,
// y deben tener un tipo compatible. Cada propiedad declarada puede ser anulada por
// una propiedad con un inicializador o por una propiedad con un método get:

open class Shape {
    open val vertexCount: Int = 0
}

class Rectangle : Shape() {
    override val vertexCount = 4
}

// También puede anular una propiedad val con una propiedad var, pero no al revés.
// Esto está permitido porque una propiedad val esencialmente declara un método get,
// y sobrescribirlo como un var, además, declara un método set en la clase derivada.

//Tenga en cuenta que puede usar la palabra clave override como parte de la declaración
// de propiedad en un constructor primario:

interface Shape {
    val vertexCount: Int
}

class Rectangle(override val vertexCount: Int = 4) : Shape // Always has 4 vertices

class Polygon : Shape {
    override var vertexCount: Int = 0  // Can be set to any number later
}

// ------- Orden de inicialización de la clase derivada -------

// Durante la construcción de una nueva instancia de una clase derivada,
// la inicialización de la clase base se realiza como el primer paso (precedido
// solo por la evaluación de los argumentos para el constructor de la clase base),
// lo que significa que ocurre antes de que se ejecute la lógica de inicialización
// de la clase derivada.

open class Base(val name: String) {

    init { println("Initializing a base class") }

    open val size: Int =
        name.length.also { println("Initializing size in the base class: $it") }
}

class Derived(
    name: String,
    val lastName: String,
) : Base(name.replaceFirstChar { it.uppercase() }.also { println("Argument for the base class: $it") }) {

    init { println("Initializing a derived class") }

    override val size: Int =
        (super.size + lastName.length).also { println("Initializing size in the derived class: $it") }
}

// Esto significa que cuando se ejecuta el constructor de la clase base, las propiedades
// declaradas o anuladas en la clase derivada aún no se han inicializado.
// El uso de cualquiera de esas propiedades en la lógica de inicialización
// de la clase base (ya sea directa o indirectamente a través de otra implementación
// de miembro abierto anulada) puede conducir a un comportamiento incorrecto o a un
// fallo en tiempo de ejecución. Por lo tanto, al diseñar una clase base,
// debe evitar el uso de miembros abiertos en los constructores, los inicializadores
// de propiedades o los bloques de inicio.

// ---------- Llamando a la implementación de la superclase --------

// El código de una clase derivada puede llamar a sus funciones de superclase
// e implementaciones de accesor de propiedades utilizando la palabra clave super:

open class Rectangle {
    open fun draw() { println("Drawing a rectangle") }
    val borderColor: String get() = "black"
}

class FilledRectangle : Rectangle() {
    override fun draw() {
        super.draw()
        println("Filling the rectangle")
    }

    val fillColor: String get() = super.borderColor
}

// Dentro de una clase interna, el acceso a la superclase de la clase externa se realiza
// utilizando la palabra clave super calificada con el nombre de la clase externa:
// super@Outer:

open class Rectangle {
    open fun draw() { println("Drawing a rectangle") }
    val borderColor: String get() = "black"
}

class FilledRectangle: Rectangle() {
    override fun draw() {
        val filler = Filler()
        filler.drawAndFill()
    }

    inner class Filler {
        fun fill() { println("Filling") }
        fun drawAndFill() {
            super@FilledRectangle.draw() // Calls Rectangle's implementation of draw()
            fill()
            println("Drawn a filled rectangle with color ${super@FilledRectangle.borderColor}") // Uses Rectangle's implementation of borderColor's get()
        }
    }
}

// ------ Reglas que anulan ------

// En Kotlin, la herencia de la implementación está regulada por la siguiente regla:
// si una clase hereda múltiples implementaciones del mismo miembro de sus superclases
// inmediatas, debe anular a este miembro y proporcionar su propia implementación
// (tal vez, usando una de las heredadas).

// Para denotar el supertipo del que se toma la implementación heredada, use super
// calificado por el nombre del supertipo entre corchetes angulares, como super<Base>:

open class Rectangle {
    open fun draw() { /* ... */ }
}

interface Polygon {
    fun draw() { /* ... */ } // interface members are 'open' by default
}

class Square() : Rectangle(), Polygon {
    // The compiler requires draw() to be overridden:
    override fun draw() {
        super<Rectangle>.draw() // call to Rectangle.draw()
        super<Polygon>.draw() // call to Polygon.draw()
    }
}

// Está bien heredar tanto de Rectangle como de Polygon, pero ambos tienen sus
// implementaciones de draw(), por lo que debe anular draw() en Square y proporcionar
// una implementación separada para que elimine la ambigüedad.

fun main() {
    println("Constructing the derived class(\"hello\", \"world\")")
    Derived("hello", "world")

    val fr = FilledRectangle()
    fr.draw()
}
