// ------------ Expresiones y declaraciones de objetos ------------

// A veces es necesario crear un objeto que sea una ligera modificación de alguna clase,
// sin declarar explícitamente una nueva subclase para él. Kotlin puede manejar esto con
// expresiones de objetos y declaraciones de objetos.

// ------------ Expresiones de objetos ------------

// Las expresiones de objetos crean objetos de clases anónimas, es decir, clases que no
// se declaran explícitamente con la declaración de clase. Estas clases son útiles para
// un solo uso. Puedes definirlos desde cero, heredar de las clases existentes o
// implementar interfaces. Las instancias de clases anónimas también se llaman objetos
// anónimos porque están definidas por una expresión, no por un nombre.

// ------- Crear objetos anónimos desde cero -------

//Las expresiones de objeto comienzan con la palabra clave de objeto.

//Si solo necesitas un objeto que no tenga ningún supertipo no trivial, escribe sus
// miembros en llaves después del objeto:

/*
fun main() {
    val helloWorld = object {
        val hello = "Hello"
        val world = "World"
        // object expressions extend Any, so `override` is required on `toString()`
        override fun toString() = "$hello $world"
    }

    print(helloWorld)
}
 */

// Heredar objetos anónimos de supertipos
//
// Para crear un objeto de una clase anónima que herede de algún tipo (o tipos),
// especifique este tipo después del objeto y dos puntos (:). Luego implementa o
// anula a los miembros de esta clase como si estuvieras heredando de ella:

/*
window.addMouseListener(object : MouseAdapter() {
    override fun mouseClicked(e: MouseEvent) { /*...*/ }

    override fun mouseEntered(e: MouseEvent) { /*...*/ }
})
 */

// Si un supertipo tiene un constructor, pásale los parámetros de constructor apropiados.
// Se pueden especificar varios supertipos como una lista delimitada por comas después de
// los dos puntos:

open class A(x: Int) {
    public open val y: Int = x
}

interface B { /*...*/ }

val ab: A = object : A(1), B {
    override val y = 15
}

// ------- Uso de objetos anónimos como tipos de retorno y valor -------

// Cuando un objeto anónimo se utiliza como un tipo de declaración local o privada pero no
// en línea (función o propiedad), todos sus miembros son accesibles a través de esta
// función o propiedad:

class C {
    private fun getObject() = object {
        val x: String = "x"
    }

    fun printX() {
        println(getObject().x)
    }
}

// Si esta función o propiedad es pública o privada en línea, su tipo real es:

// bCualquiera si el objeto anónimo no tiene un supertipo declarado

// El supertipo declarado del objeto anónimo, si hay exactamente uno de esos tipos

// El tipo declarado explícitamente si hay más de un supertipo declarado

// En todos estos casos, los miembros añadidos en el objeto anónimo no son accesibles.
// Los miembros anulados son accesibles si se declaran en el tipo
// real de la función o propiedad:

/*
interface A {
    fun funFromA() {}
}
interface B

class C {
    // The return type is Any; x is not accessible
    fun getObject() = object {
        val x: String = "x"
    }

    // The return type is A; x is not accessible
    fun getObjectA() = object: A {
        override fun funFromA() {}
        val x: String = "x"
    }

    // The return type is B; funFromA() and x are not accessible
    fun getObjectB(): B = object: A, B { // explicit return type is required
        override fun funFromA() {}
        val x: String = "x"
    }
}
 */

// ------- Acceso a variables desde objetos anónimos -------

// El código en las expresiones de objetos puede acceder a las variables
// desde el ámbito adjunto:

/*
fun countClicks(window: JComponent) {
    var clickCount = 0
    var enterCount = 0

    window.addMouseListener(object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) {
            clickCount++
        }

        override fun mouseEntered(e: MouseEvent) {
            enterCount++
        }
    })
    // ...
}
 */

// ------------ Declaraciones de objetos ------------

// El patrón Singleton puede ser útil en varios casos, y Kotlin hace que sea fácil declarar
// singletons:

/*
object DataProviderManager {
    fun registerDataProvider(provider: DataProvider) {
        // ...
    }

    val allDataProviders: Collection<DataProvider>
        get() = // ...
}
 */

// Esto se llama declaración de objeto, y siempre tiene un nombre después de la palabra clave
// de objeto. Al igual que una declaración de variable, una declaración de objeto no es una
// expresión y no se puede usar en el lado derecho de una declaración de asignación.

// La inicialización de una declaración de objeto es segura para subprocesos y se realiza en
// el primer acceso.

//Para hacer referencia al objeto, use su nombre directamente:

// DataProviderManager.registerDataProvider(...)

// Tales objetos pueden tener supertipos:

/*
object DefaultListener : MouseAdapter() {
    override fun mouseClicked(e: MouseEvent) { ... }

    override fun mouseEntered(e: MouseEvent) { ... }
}
 */

// ******************** info ********************
// Las declaraciones de objetos no pueden ser locales (es decir, no se pueden anidar directamente
// dentro de una función), pero se pueden anidar en otras declaraciones de objetos o clases no
// internas.

// Objetos de datos

// Al imprimir una declaración de objeto simple en Kotlin, la representación de cadena contiene
// tanto su nombre como el hash del objeto:

/*
object MyObject

fun main() {
    println(MyObject) // MyObject@1f32e575
}
 */

// Al igual que las clases de datos, puedes marcar una declaración de objeto con el modificador
// de datos. Esto indica al compilador que genere una serie de funciones para su objeto:

// toString() devuelve el nombre del objeto de datos

// Par equals()/hashCode()

// ******************** info ********************
// No se puede proporcionar una implementación personalizada de iguales o hashCode para un
// objeto de datos.

// La función toString() de un objeto de datos devuelve el nombre del objeto:

/*
data object MyDataObject {
    val x: Int = 3
}

fun main() {
    println(MyDataObject) // MyDataObject
}
 */

// La función equals() para un objeto de datos garantiza que todos los objetos que tienen el tipo
// de su objeto de datos se consideren iguales. En la mayoría de los casos, solo tendrá una sola
// instancia de su objeto de datos en tiempo de ejecución (después de todo, un objeto de datos
//  declara un singleton). Sin embargo, en el caso de borde en el que se genera otro objeto del
//  mismo tipo en tiempo de ejecución (por ejemplo, mediante el uso de la reflexión de la
//  plataforma con java.lang.reflect o una biblioteca de serialización de JVM que utiliza esta
//  API bajo el capó), esto garantiza que los objetos sean tratados como iguales.

// ******************** Atención ********************
// Asegúrese de comparar solo los objetos de datos estructuralmente (usando el operador ==) y
// nunca por referencia (usando el operador ===). Esto le ayuda a evitar trampas cuando existe
// más de una instancia de un objeto de datos en tiempo de ejecución.

/*
import java.lang.reflect.Constructor

data object MySingleton

fun main() {
    val evilTwin = createInstanceViaReflection()

    println(MySingleton) // MySingleton
    println(evilTwin) // MySingleton

    // Even when a library forcefully creates a second instance of MySingleton, its `equals` method returns true:
    println(MySingleton == evilTwin) // true

    // Do not compare data objects via ===.
    println(MySingleton === evilTwin) // false
}

fun createInstanceViaReflection(): MySingleton {
    // Kotlin reflection does not permit the instantiation of data objects.
    // This creates a new MySingleton instance "by force" (i.e. Java platform reflection)
    // Don't do this yourself!
    return (MySingleton.javaClass.declaredConstructors[0].apply { isAccessible = true } as Constructor<MySingleton>).newInstance()
}
 */

// La función hashCode() generada tiene un comportamiento consistente con la función equals(),
// de modo que todas las instancias de tiempo de ejecución de un objeto de datos tienen el
// mismo código hash.

// Diferencias entre los objetos de datos y las clases de datos

// Si bien las declaraciones de objetos de datos y clases de datos a menudo se utilizan juntas
// y tienen algunas similitudes, hay algunas funciones que no se generan para un objeto de datos:

// No hay función copy(). Debido a que una declaración de objeto de datos está destinada a ser
// utilizada como objetos singleton, no se genera ninguna función copy(). El patrón singleton
// restringe la instanciación de una clase a una sola instancia, lo que se violaría al permitir
// que se creen copias de la instancia.

// Sin función componentN(). A diferencia de una clase de datos, un objeto de datos no tiene
// ninguna propiedad de datos. Dado que intentar desestructurar dicho objeto sin propiedades
// de datos no tendría sentido, no se generan funciones componentN().

// Uso de objetos de datos con jerarquías selladas

// Las declaraciones de objetos de datos son particularmente útiles para jerarquías selladas
// como clases selladas o interfaces selladas, ya que le permiten mantener la simetría con
// cualquier clase de datos que pueda haber definido junto con el objeto. En este ejemplo,
// declarar EndOfFile como un objeto de datos en lugar de un objeto simple significa que
// obtendrá la función toString() sin necesidad de anularla manualmente:

/*
sealed interface ReadResult
data class Number(val number: Int) : ReadResult
data class Text(val text: String) : ReadResult
data object EndOfFile : ReadResult
​
fun main() {
    println(Number(7)) // Number(number=7)
    println(EndOfFile) // EndOfFile
}
 */

// ------- Objetos de acompañamiento -------

// Una declaración de objeto dentro de una clase se puede marcar con la palabra
// clave complementaria:

class MyClass {
    companion object Factory {
        fun create(): MyClass = MyClass()
    }
}

// Los miembros del objeto acompañante se pueden llamar simplemente usando el nombre de la clase
// como calificador:

val instance = MyClass.create()

// Se puede omitir el nombre del objeto acompañante, en cuyo caso se utilizará
// el nombre Acompañante:

class MyClass2 {
    companion object { }
}

val x = MyClass2.Companion

// Los miembros de la clase pueden acceder a los miembros privados del objeto
// acompañante correspondiente.

// El nombre de una clase utilizada por sí misma (no como un calificador de otro nombre) actúa
// como una referencia al objeto acompañante de la clase (ya sea nombrado o no):

class MyClass1 {
    companion object Named { }
}

val x2 = MyClass1

class MyClass3 {
    companion object { }
}

val y = MyClass3

// Tenga en cuenta que a pesar de que los miembros de los objetos acompañantes parecen miembros
// estáticos en otros lenguajes, en tiempo de ejecución siguen siendo miembros
// de instancia de objetos reales y pueden, por ejemplo, implementar interfaces:

/*
interface Factory<T> {
    fun create(): T
}

class MyClass {
    companion object : Factory<MyClass> {
        override fun create(): MyClass = MyClass()
    }
}

val f: Factory<MyClass> = MyClass
 */

//Sin embargo, en la JVM puede tener miembros de objetos complementarios generados como métodos
// y campos estáticos reales si utiliza la anotación @JvmStatic. Consulte la sección de
// interoperabilidad de Java para obtener más detalles.

//Diferencia semántica entre expresiones de objetos y declaraciones

//Hay una diferencia semántica importante entre las expresiones de objetos y las declaraciones
// de objetos:

//Las expresiones de objeto se ejecutan (e inicializan) inmediatamente, donde se utilizan.

//Las declaraciones de objetos se inicializan con reza, cuando se accede por primera vez.

//Un objeto compañero se inicializa cuando se carga (resuelve) la clase correspondiente que
// coincide con la semántica de un inicializador estático de Java.

fun main() {

}