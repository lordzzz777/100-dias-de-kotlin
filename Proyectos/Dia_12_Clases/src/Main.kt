// ------------ Clases ------------

// Las clases en Kotlin se declaran usando la palabra clave class:

class Person { /*...*/ }

// La declaración de clase consiste en el nombre de la clase,
// el encabezado de la clase (especificando sus parámetros de tipo,
// el constructor principal y algunas otras cosas) y el cuerpo de
// la clase rodeado de llaves. Tanto el encabezado como el cuerpo son
// opcionales; si la clase no tiene cuerpo, se pueden omitir las llaves.

class Empty

// ------- Constructors -------

// Una clase en Kotlin tiene un constructor primario y posiblemente uno
// o más constructores secundarios. El constructor primario se declara
// en la cabecera de la clase, y va tras el nombre de la clase y los
// parámetros de tipo opcional.

class Person2 constructor(firstName: String) { /*...*/ }

// Si el constructor primario no tiene anotaciones o modificadores
// de visibilidad, el constructor palabra clave se puede omitir:

class Person3(firstName: String) { /*...*/ }

// El constructor primario inicializa una instancia de clase y sus
// propiedades en la cabecera de clase. La cabecera de clase no puede
// contener ningún código manejable. Si desea ejecutar algún código
// durante la creación de objetos, utilice bloques inicializadores dentro
// del cuerpo de clase. Se declaran bloques de iniciales con la init
// palabra clave seguida de aparatos corridos. Escriba cualquier código
// que desee ejecutar dentro de los aparatos corridos.

//Durante la inicialización de una instancia, los bloques inicializados
// se ejecutan en el mismo orden que aparecen en el cuerpo de clase,
// entrelazados con los inicializadores de la propiedad:

class InitOrderDemo(name: String) {
    val firstProperty = "First property: $name".also(::println)

    init {
        println("First initializer block that prints $name")
    }

    val secondProperty = "Second property: ${name.length}".also(::println)

    init {
        println("Second initializer block that prints ${name.length}")
    }
}


// Los parámetros del constructor primario se pueden utilizar en los bloques
// inicializadores. También se pueden utilizar en los inicializadores de
// propiedades declarados en el cuerpo de clase:


class Customer(name: String) {
    val customerKey = name.uppercase()
}

// Kotlin tiene una sintaxis concisa para declarar propiedades y inicializarlas
// del constructor primario:

class Person4(val firstName: String, val lastName: String, var age: Int)

// Puedes usar una coma de rastro cuando declares propiedades de clase:

class Person5(
    val firstName: String,
    val lastName: String,
    var age: Int, // trailing comma
) { /*...*/ }

// Al igual que las propiedades regulares, las propiedades declaradas
// en el constructor primario pueden ser mutables (var) o solo de lectura (val).

//Si el constructor tiene anotaciones o modificadores de visibilidad, el constructor
// palabra clave es necesaria y los modificadores van antes de ella:

class Customer2 public @Inject constructor(name: String) { /*...*/ }

// Obtenga más información sobre los modificadores de visibilidad.

// ----- Constructores secundarios -----

// na clase también puede declarar constructores secundarios, que están
// prefijo con constructor:

class Person6(val pets: MutableList<Pet> = mutableListOf())

class Pet {
    constructor(owner: Person6) {
        owner.pets.add(this) // adds this pet to the list of its owner's pets
    }
}

// Si la clase tiene un constructor primario, cada constructor secundario
// necesita delegar en el constructor primario, ya sea directa o indirectamente
// a través de otro constructor secundario (s). Delegación en otro constructor
// de la misma clase se hace utilizando el this palabra clave:

class Person7(val name: String) {
    val children: MutableList<Person> = mutableListOf()
    constructor(name: String, parent: Person) : this(name) {
        parent.children.add(this)
    }
}

// Código en bloques inicializados se convierte efectivamente en parte del
// constructor primario. La delegación en el constructor primario se da en
// el momento de acceso a la primera declaración de un constructor secundario,
// por lo que el código en todos los bloques inicializadores y inicializadores
// de propiedad se ejecuta antes del cuerpo del constructor secundario.

//Incluso si la clase no tiene constructor primario, la delegación todavía
// sucede implícitamente, y los bloques inicializadores todavía se ejecutan:

class Constructors2 {
    init {
        println("Init block")
    }

    constructor(i: Int) {
        println("Constructor $i")
    }
}


// Si una clase no abstracta no declara constructores (primarios o secundarios),
// tendrá un constructor primario generado sin argumentos. La visibilidad de la
// constructora será pública.

//Si no quieres que tu clase tengas un constructor público, declara un constructor
// primario vacío con visibilidad no por defecto:

class DontCreateMe private constructor() { /*...*/ }

// ************ Info ************
// En la JVM, si todos los parámetros de constructor primario tienen valores
// predeterminados, el compilador generará un constructor sin parámetro adicional
// que utilizará los valores por defecto. Esto hace que sea más fácil utilizar
// Kotlin con bibliotecas como Jackson o JPA que crean instancias de clase a
// través de constructores sin parámetro.
class Customer3(val customerName: String = "")

// ------------ Creación de instancias de clases ------------

// Para crear una instancia de una clase, llama al constructor como si
// fuera una función regular:

val invoice = Invoice()

val customer = Customer("Joe Smith")

// ***** info *****
// Kotlin no tiene un newPalabra clave.

// El proceso de crear instancias de clases interiores anidadas, internas y anónimas
// se describe en las clases de Nested.

// ------------ Miembros de clase ------------

// Las clases pueden contener:
//
//    Constructores y bloques inicializadores
//
//    Funciones
//
//    Propiedades
//
//    Clases desperdiciadas y interiores
//
//    Declaraciones de objetos

// ------------ Herencia ------------

// Las clases se pueden derivar entre sí y formar jerarquías de herencia.
// Más información sobre la herencia en Kotlin.


// ------------ Clases abstractas ------------

// Una clase puede ser declarada abstract, junto con algunos o todos sus miembros.
// Un miembro abstracto no tiene una implementación en su clase.
// No necesitas anotar clases abstractas o funciones con open.

abstract class Polygon {
    abstract fun draw()
}

class Rectangle : Polygon() {
    override fun draw() {
        // draw the rectangle
    }
}

// Puede anular un no abstracto. openmiembro con uno abstracto.

open class Polygon2 {
    open fun draw() {
        // some default polygon drawing method
    }
}

abstract class WildShape : Polygon() {
    // Classes that inherit WildShape need to provide their own
    // draw method instead of using the default on Polygon
    abstract override fun draw()
}

// ------------ Objetos de acompañamiento ------------

// Si necesitas escribir una función que se pueda llamar sin tener
// una instancia de clase pero que necesita acceso a los internos
// de una clase (como un método de fábrica), puedes escribirlo como
// miembro de una declaración de objeto dentro de esa clase.

//Más específicamente, si declaras un objeto compañero dentro de tu
// clase, puedes acceder a sus miembros usando sólo el nombre de
// clase como calificador.

fun main() {
    InitOrderDemo("hello")
    Constructors2(1)
}

