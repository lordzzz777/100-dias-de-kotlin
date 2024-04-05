// ------------ Propiedades ------------

// Las propiedades en las clases de Kotlin se pueden declarar
// como mutables, usando la palabra clave var, o como de solo
// lectura, usando la palabra clave val.

class Address {
    var name: String = "Holmes, Sherlock"
    var street: String = "Baker"
    var city: String = "London"
    var state: String? = null
    var zip: String = "123456"
}

// Para usar una propiedad, simplemente refiérase a ella por su nombre:

fun copyAddress(address: Address): Address {
    val result = Address() // there's no 'new' keyword in Kotlin
    result.name = address.name // accessors are called
    result.street = address.street
    // ...
    return result
}

// ------------ Getters y setters ------------

// La sintaxis completa para declarar una propiedad es la siguiente:

var <propertyName>[: <PropertyType>] [= <property_initializer>]
[<getter>]
[<setter>]

// El inicializador, el getter y el setter son opcionales.
// El tipo de propiedad es opcional si se puede inferir del inicializador
// o del tipo de retorno del getter, como se muestra a continuación:

var initialized = 1 // has type Int, default getter and setter
// var allByDefault // ERROR: explicit initializer required, default getter and setter implied

// La sintaxis completa de una declaración de propiedad de solo lectura difiere de una mutable
// de dos maneras: comienza con val en lugar de var y no permite un setter:

val simple: Int? // has type Int, default getter, must be initialized in constructor
val inferredType = 1 // has type Int and a default getter

// Puede definir accesores personalizados para una propiedad. Si define un getter
// personalizado, se llamará cada vez que acceda a la propiedad (de esta manera
// puede implementar una propiedad calculada). Aquí hay un ejemplo de un getter
// personalizado:

class Rectangle(val width: Int, val height: Int) {
    val area: Int // property type is optional since it can be inferred from the getter's return type
        get() = this.width * this.height
}
fun main() {
    val rectangle = Rectangle(3, 4)
    println("Width=${rectangle.width}, height=${rectangle.height}, area=${rectangle.area}")

// Puede omitir el tipo de propiedad si se puede inferir del getter:

    val area get() = this.width * this.height

// Por convención, el nombre del parámetro setter es value, pero puede elegir un nombre
// diferente si lo prefiere.

// Si necesita anotar un accesor o cambiar su visibilidad, pero no desea cambiar la
// implementación predeterminada, puede definir el accesor sin definir su cuerpo:

    var setterVisibility: String = "abc"
    private set // the setter is private and has the default implementation

    var setterWithAnnotation: Any? = null
    @Inject set // annotate the setter with Inject

// ------- Campos de respaldo -------

// En Kotlin, un campo solo se utiliza como parte de una propiedad para mantener
// su valor en la memoria. Los campos no se pueden declarar directamente.
// Sin embargo, cuando una propiedad necesita un campo de respaldo,
// Kotlin lo proporciona automáticamente. Se puede hacer referencia a este
// campo de respaldo en los accesores utilizando el identificador de field:

    var counter = 0 // the initializer assigns the backing field directly
    set(value) {
        if (value >= 0)
            field = value
        // counter = value // ERROR StackOverflow: Using actual name 'counter' would make setter recursive
    }

// El identificador de campo solo se puede utilizar en los accesores de la propiedad.

// Se generará un campo de respaldo para una propiedad si utiliza la implementación
// predeterminada de al menos uno de los accesores, o si un accesor personalizado
// hace referencia a él a través del identificador de field.

//Por ejemplo, no habría ningún campo de respaldo en el siguiente caso:

    val isEmpty: Boolean
    get() = this.size == 0

// ------- Propiedades de respaldo -------

    private var _table: Map<String, Int>? = null
    public val table: Map<String, Int>
    get() {
        if (_table == null) {
            _table = HashMap() // Type parameters are inferred
        }
        return _table ?: throw AssertionError("Set to null by another thread")
    }

// ------------ Info ------------
// En la JVM: El acceso a las propiedades privadas con getters y setters predeterminados está
// optimizado para evitar la sobrecarga de llamadas a la función.

// ---------- Constantes de tiempo de compilación ------------

// Si el valor de una propiedad de solo lectura se conoce en tiempo de compilación,
// márquelo como una constante de tiempo de compilación utilizando el modificador const.
// Dicha propiedad debe cumplir con los siguientes requisitos:

// Debe ser una propiedad de nivel superior, o un miembro de una declaración de objeto
// o un objeto complementario.

// Debe inicializarse con un valor de tipo String o un tipo primitivo

// No puede ser un getter personalizado

// El compilador hará usos en línea de la constante, reemplazando la referencia a la
// constante por su valor real. Sin embargo, el campo no se eliminará y, por lo tanto,
// se puede interactuar con el uso de la reflexión.
// Tales propiedades también se pueden utilizar en anotaciones:

    const val SUBSYSTEM_DEPRECATED: String = "This subsystem is deprecated"

    @Deprecated(SUBSYSTEM_DEPRECATED) fun foo() { ... }

// ------------ Propiedades y variables inicializadas tardías ------------

// Normalmente, las propiedades declaradas como que tienen un tipo no nulo deben inicializarse
// en el constructor. Sin embargo, a menudo se da cuenta de que hacerlo no es conveniente.
// Por ejemplo, las propiedades se pueden inicializar a través de la inyección de dependencias
// o en el método de configuración de una prueba unitaria. En estos casos, no puede proporcionar
// un inicializador no nulo en el constructor, pero aún así desea evitar las comprobaciones nulas
// al hacer referencia a la propiedad dentro del cuerpo de una clase.

// Para manejar estos casos, puede marcar la propiedad con el modificador lateinit:

    public class MyTest {
        lateinit var subject: TestSubject

        @SetUp fun setup() {
            subject = TestSubject()
        }

        @Test fun test() {
            subject.method()  // dereference directly
        }
    }

// Este modificador se puede utilizar en las propiedades var declaradas dentro del
// cuerpo de una clase (no en el constructor principal, y solo cuando la propiedad
// no tiene un getter o setter personalizado), así como para las propiedades de
// nivel superior y las variables locales. El tipo de la propiedad o variable no
// debe ser nulo, y no debe ser un tipo primitivo.

//Acceder a una propiedad lateinit antes de que se haya inicializado lanza una
// excepción especial que identifica claramente la propiedad a la que se accede
// y el hecho de que no se ha inicializado.

// ------- Comprobando si se inicializa un lateinit var --------

// Para comprobar si ya se ha inicializado un lateinit var, use .isInitialized
// en la referencia a esa propiedad:

    if (foo::bar.isInitialized) {
        println(foo.bar)
    }

// Esta comprobación solo está disponible para las propiedades que son lexicalmente accesibles
// cuando se declaran en el mismo tipo, en uno de los tipos externos o en el nivel superior en
// el mismo archivo.

// -------- Propiedades anuladas ---------

// Ver Propiedades anuladas

// ------- Propiedades delegadas --------

// El tipo más común de propiedad simplemente lee (y tal vez escribe en) un campo de respaldo,
// pero los getters y los setters personalizados le permiten usar propiedades para que se pueda
// implementar cualquier tipo de comportamiento de una propiedad. En algún lugar entre la
// simplicidad del primer tipo y la variedad del segundo, hay patrones comunes de lo que las
// propiedades pueden hacer. Algunos ejemplos: valores perezosos, lectura de un mapa por una
// clave determinada, acceso a una base de datos, notificación a un oyente sobre el acceso.

// Tales comportamientos comunes se pueden implementar como bibliotecas
// utilizando propiedades delegadas.    
}
