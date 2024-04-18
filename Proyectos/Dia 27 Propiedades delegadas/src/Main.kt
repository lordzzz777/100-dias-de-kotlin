// ------------ Propiedades delegadas ------------

// Con algunos tipos comunes de propiedades, a pesar de que puedes implementarlas
// manualmente cada vez que las necesites, es más útil implementarlas una vez,
// agregarlas a una biblioteca y reutilizarlas más tarde. Por ejemplo:

// Propiedades perezosas: el valor se calcula solo en el primer acceso.

// Propiedades observables: se notifica a los oyentes sobre los cambios en esta propiedad.

// Almacenar propiedades en un mapa en lugar de un campo separado para cada propiedad.

// Para cubrir estos (y otros) casos, Kotlin admite propiedades delegadas:

class Example {
    var p: String by Delegate()
}

// La sintaxis es: val/var <nombre de la propiedad>: <Tipo> por <expresión>.
// La expresión after by es un delegado, porque el get() (y set()) que corresponde
// a la propiedad se delegará a sus métodos getValue() y setValue().
// Los delegados de propiedades no tienen que implementar una interfaz, pero tienen
// que proporcionar una función getValue() (y setValue() para vars).

//Por ejemplo:

import kotlin.reflect.KProperty

class Delegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "$thisRef, thank you for delegating '${property.name}' to me!"
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("$value has been assigned to '${property.name}' in $thisRef.")
    }
}

// Cuando lees desde p, que delega a una instancia de Delegado, se llama a la función getValue()
// de Delegado. Su primer parámetro es el objeto desde el que lees p, y el segundo parámetro
// contiene una descripción de p en sí (por ejemplo, puedes tomar su nombre).

val e = Example()
println(e.p)

// Esto imprime:

// Ejemplo@33a17727, ¡gracias por delegar "p" en mí!

// Del mismo modo, cuando se asigna a p, se llama a la función setValue().
// Los dos primeros parámetros son los mismos, y el tercero contiene el valor que se está
// asignando:

e.p = "NEW"

// Esto imprime:

// NUEVO ha sido asignado a "p" en Example@33a17727.

// La especificación de los requisitos para el objeto delegado se puede encontrar a continuación.

// Puedes declarar una propiedad delegada dentro de una función o bloque de código;
// no tiene que ser miembro de una clase. A continuación puedes encontrar un ejemplo.

// ------------ Delegados estándar ------------

// La biblioteca estándar de Kotlin proporciona métodos de fábrica para varios tipos útiles
// de delegados.

// Propiedades perezosas

// Lazy() es una función que toma un lambda y devuelve una instancia de Lazy<T>, que puede
// servir como delegado para implementar una propiedad lazy. La primera llamada a get()
// ejecuta el lambda pasado a lazy() y recuerda el resultado. Las llamadas posteriores a
// get() simplemente devuelven el resultado recordado.

/*
val lazyValue: String by lazy {
    println("computed!")
    "Hello"
}

fun main() {
    println(lazyValue)
    println(lazyValue)
}
 */

// De forma predeterminada, la evaluación de las propiedades perezosas se sincroniza:
// el valor se calcula solo en un hilo, pero todos los hilos verán el mismo valor.
// Si no se requiere la sincronización del delegado de inicialización para permitir
// que varios subprocesos lo ejecuten simultáneamente, pase LazyThreadSafetyMode
// .PUBLICATION como parámetro a lazy().

// Si está seguro de que la inicialización siempre ocurrirá en el mismo hilo en el que
// usa la propiedad, puede usar LazyThreadSafetyMode.NONE. No incurre en ninguna garantía
// de seguridad de los hilos y gastos generales relacionados.

// Propiedades observables

// Delegates.observable() toma dos argumentos: el valor inicial y un controlador para
// las modificaciones.

// El controlador se llama cada vez que se asigna a la propiedad (después de que se haya
// realizado la asignación). Tiene tres parámetros: la propiedad a la que se asigna,
// el valor antiguo y el nuevo valor:

/*
import kotlin.properties.Delegates

class User {
    var name: String by Delegates.observable("<no name>") {
        prop, old, new ->
        println("$old -> $new")
    }
}

fun main() {
    val user = User()
    user.name = "first"
    user.name = "second"
}
 */

// Si quieres interceptar asignaciones y vetarlas, usa vetoable() en lugar de observable().
// El controlador pasado a vetoable será llamado antes de la asignación de un nuevo valor
// de propiedad.

// ------------ Delegar en otra propiedad ------------

// Una propiedad puede delegar su getter y setter a otra propiedad. Dicha delegación está
// disponible tanto para las propiedades de nivel superior como para las de clase
// (miembro y extensión). La propiedad de delegado puede ser:

// Una propiedad de nivel superior

// Un miembro o una propiedad de extensión de la misma clase

// Un miembro o una propiedad de extensión de otra clase

// Para delegar una propiedad en otra propiedad, use el calificador :: en el nombre del
// delegado, por ejemplo, this::delegate o MyClass::delegate.

var topLevelInt: Int = 0
class ClassWithDelegate(val anotherClassInt: Int)

class MyClass(var memberInt: Int, val anotherClassInstance: ClassWithDelegate) {
    var delegatedToMember: Int by this::memberInt
    var delegatedToTopLevel: Int by ::topLevelInt

    val delegatedToAnotherClass: Int by anotherClassInstance::anotherClassInt
}
var MyClass.extDelegated: Int by ::topLevelInt

// Esto puede ser útil, por ejemplo, cuando desea cambiar el nombre de una propiedad de una
// manera compatible con versiones anteriores: introduzca una nueva propiedad, anote la
// antigua con la anotación @Deprecated y delegue su implementación.

/*
class MyClass {
   var newName: Int = 0
   @Deprecated("Use 'newName' instead", ReplaceWith("newName"))
   var oldName: Int by this::newName
}
fun main() {
   val myClass = MyClass()
   // Notification: 'oldName: Int' is deprecated.
   // Use 'newName' instead
   myClass.oldName = 42
   println(myClass.newName) // 42
}
 */

// ------------ Almacenamiento de propiedades en un mapa ------------

// Un caso de uso común es almacenar los valores de las propiedades en un mapa.
// Esto aparece a menudo en aplicaciones para cosas como analizar JSON o realizar
// otras tareas dinámicas. En este caso, puede utilizar la propia instancia del
// mapa como delegado para una propiedad delegada.

class User(val map: Map<String, Any?>) {
    val name: String by map
    val age: Int     by map
}

// En este ejemplo, el constructor toma un mapa:

val user = User(mapOf(
    "name" to "John Doe",
    "age"  to 25
))

// Las propiedades delegadas toman valores de este mapa a través de claves de cadena,
// que están asociadas con los nombres de las propiedades:

/*
println(user.name) // Prints "John Doe"
println(user.age)  // Prints 25
 */

// Esto también funciona para las propiedades de var si usas un mapa mutable en lugar
// de un mapa de solo lectura:

class MutableUser(val map: MutableMap<String, Any?>) {
    var name: String by map
    var age: Int     by map
}

// ------------ Propiedades delegadas locales ------------

// Puede declarar variables locales como propiedades delegadas. Por ejemplo, puedes hacer
// que una variable local sea perezosa:

fun example(computeFoo: () -> Foo) {
    val memoizedFoo by lazy(computeFoo)

    if (someCondition && memoizedFoo.isValid()) {
        memoizedFoo.doSomething()
    }
}

// La variable memoizedFoo se calculará solo en el primer acceso. Si someCondition falla,
// la variable no se calculará en absoluto.

// Requisitos de delegado de propiedad

// Para una propiedad de solo lectura (val), un delegado debe proporcionar una función de
// operador getValue() con los siguientes parámetros:

//thisRef debe ser del mismo tipo que, o un supertipo de, el propietario de la propiedad
// (para las propiedades de extensión, debe ser el tipo que se está extendiendo).

//La propiedad debe ser de tipo KProperty<*> o su supertipo.

//getValue() debe devolver el mismo tipo que la propiedad (o su subtipo).

class Resource

class Owner {
    val valResource: Resource by ResourceDelegate()
}

class ResourceDelegate {
    operator fun getValue(thisRef: Owner, property: KProperty<*>): Resource {
        return Resource()
    }
}

// Para una propiedad mutable (var), un delegado tiene que proporcionar adicionalmente una
// función de operador setValue() con los siguientes parámetros:

// thisRef debe ser del mismo tipo que, o un supertipo de, el propietario de la propiedad
// (para las propiedades de extensión, debe ser el tipo que se está extendiendo).

// La propiedad debe ser de tipo KProperty<*> o su supertipo.

// El valor debe ser del mismo tipo que la propiedad (o su supertipo).

class Resource

class Owner {
    var varResource: Resource by ResourceDelegate()
}

class ResourceDelegate(private var resource: Resource = Resource()) {
    operator fun getValue(thisRef: Owner, property: KProperty<*>): Resource {
        return resource
    }
    operator fun setValue(thisRef: Owner, property: KProperty<*>, value: Any?) {
        if (value is Resource) {
            resource = value
        }
    }
}

// Las funciones getValue() y/o setValue() se pueden proporcionar como funciones de miembro
// de la clase delegada o como funciones de extensión. Esto último es útil cuando necesita
// delegar una propiedad en un objeto que originalmente no proporciona estas funciones.
// Ambas funciones deben estar marcadas con la palabra clave del operador.

//Puede crear delegados como objetos anónimos sin crear nuevas clases, utilizando las interfaces
// ReadOnlyProperty y ReadWriteProperty de la biblioteca estándar de Kotlin. Proporcionan los
// métodos requeridos: getValue() se declara en ReadOnlyProperty; ReadWriteProperty lo extiende
// y añade setValue(). Esto significa que puede pasar una ReadWriteProperty siempre
// que se espere una ReadOnlyProperty.

fun resourceDelegate(resource: Resource = Resource()): ReadWriteProperty<Any?, Resource> =
    object : ReadWriteProperty<Any?, Resource> {
        var curValue = resource
        override fun getValue(thisRef: Any?, property: KProperty<*>): Resource = curValue
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Resource) {
            curValue = value
        }
    }

val readOnlyResource: Resource by resourceDelegate()  // ReadWriteProperty as val
var readWriteResource: Resource by resourceDelegate()


// ------------ Reglas de traducción para propiedades delegadas ------------

// Bajo el capó, el compilador Kotlin genera propiedades auxiliares para algunos tipos de propiedades
// delegadas y luego delega a ellas.

// ********************************* Info ************************************
// Para fines de optimización, el compilador no genera propiedades auxiliares en varios casos.
// Aprenda sobre la optimización con el ejemplo de delegar en otra propiedad.

// Por ejemplo, para el accesorio de la propiedad, genera la propiedad oculta prop$delegate, y el
// código de los accesores simplemente delega a esta propiedad adicional:

class C {
    var prop: Type by MyDelegate()
}

// this code is generated by the compiler instead:
class C {
    private val prop$delegate = MyDelegate()
    var prop: Type
        get() = prop$delegate.getValue(this, this::prop)
    set(value: Type) = prop$delegate.setValue(this, this::prop, value)
}

// El compilador Kotlin proporciona toda la información necesaria sobre prop en los argumentos:
// el primer argumento se refiere a una instancia de la clase externa C, y este::prop es un objeto
// de reflexión del tipo KProperty que describe el prop en sí mismo.

// ------- Casos optimizados para propiedades delegadas -------

// El campo $delegate se omitirá si un delegado es:

//Una propiedad referenciada:

class C<Type> {
    private var impl: Type = ...
    var prop: Type by ::impl
}

// Un objeto con nombre:

object NamedObject {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String = ...
}

val s: String by NamedObject

// Una propiedad val final con un campo de respaldo y un getter predeterminado en el mismo módulo:

val impl: ReadOnlyProperty<Any?, String> = ...

class A {
    val s: String by impl
}

// Una expresión constante, entrada de enumerado, esto, nulo. El ejemplo de esto:

class A {
    operator fun getValue(thisRef: Any?, property: KProperty<*>) ...

    val s by this
}

// -------- Reglas de traducción al delegar en otra propiedad --------

//Al delegar en otra propiedad, el compilador Kotlin genera acceso inmediato a la propiedad
// a la que se hace referencia. Esto significa que el compilador no genera el campo
// prop$delegate.
// Esta optimización ayuda a ahorrar memoria.

//Tomemos el siguiente código, por ejemplo:

class C<Type> {
    private var impl: Type = ...
    var prop: Type by ::impl
}

// Los accesores de propiedades de la variable prop invocan la variable impl directamente,
// omitiendo los operadores getValue y setValue de la propiedad delegada y, por lo tanto,
// no es necesario el objeto de referencia KProperty.

//Para el código anterior, el compilador genera el siguiente código:

class C<Type> {
    private var impl: Type = ...

    var prop: Type
        get() = impl
        set(value) {
            impl = value
        }

    fun getProp$delegate(): Type = impl // This method is needed only for reflection
}

// ------------ Proporcionar un delegado ------------

// Al definir el operador provideDelegate, puede ampliar la lógica para crear el objeto al que
// se delega la implementación de la propiedad. Si el objeto utilizado en el lado derecho de by
// define provideDelegate como una función de miembro o extensión, esa función se llamará para
// crear la instancia de delegado de propiedad.

// Uno de los posibles casos de uso de provideDelegate es comprobar la consistencia de la
// propiedad al inicializarla.

// Por ejemplo, para comprobar el nombre de la propiedad antes de enlazar, puedes escribir algo
// como esto:

class ResourceDelegate<T> : ReadOnlyProperty<MyUI, T> {
    override fun getValue(thisRef: MyUI, property: KProperty<*>): T { ... }
}

class ResourceLoader<T>(id: ResourceID<T>) {
    operator fun provideDelegate(
        thisRef: MyUI,
        prop: KProperty<*>
    ): ReadOnlyProperty<MyUI, T> {
        checkProperty(thisRef, prop.name)
        // create delegate
        return ResourceDelegate()
    }

    private fun checkProperty(thisRef: MyUI, name: String) { ... }
}

class MyUI {
    fun <T> bindResource(id: ResourceID<T>): ResourceLoader<T> { ... }

    val image by bindResource(ResourceID.image_id)
    val text by bindResource(ResourceID.text_id)
}

// Los parámetros de provideDelegate son los mismos que los de getValue:

// thisRef debe ser del mismo tipo que, o un supertipo de, el propietario de la propiedad
// (para las propiedades de extensión, debe ser el tipo que se está extendiendo);

// La propiedad debe ser de tipo KProperty<*> o su supertipo.

// El método provideDelegate se llama para cada propiedad durante la creación de la instancia
// MyUI, y realiza la validación necesaria de inmediato.

// Sin esta capacidad de interceptar el enlace entre la propiedad y su delegado, para lograr
// la misma funcionalidad, tendría que pasar el nombre de la propiedad explícitamente,
// lo cual no es muy conveniente:

// Checking the property name without "provideDelegate" functionality

class MyUI {
    val image by bindResource(ResourceID.image_id, "image")
    val text by bindResource(ResourceID.text_id, "text")
}

fun <T> MyUI.bindResource(
    id: ResourceID<T>,
    propertyName: String
): ReadOnlyProperty<MyUI, T> {
    checkProperty(this, propertyName)
    // create delegate
}

// En el código generado, se llama al método provideDelegate para inicializar la propiedad auxiliar
// prop$delegate. Compare el código generado para la declaración de propiedades val prop:
// Type by MyDelegate() con el código generado anterior (cuando el método provideDelegate no está
// presente):

class C {
    var prop: Type by MyDelegate()
}

// this code is generated by the compiler
// when the 'provideDelegate' function is available:
class C {
    // calling "provideDelegate" to create the additional "delegate" property
    private val prop$delegate = MyDelegate().provideDelegate(this, this::prop)
    var prop: Type
        get() = prop$delegate.getValue(this, this::prop)
    set(value: Type) = prop$delegate.setValue(this, this::prop, value)
}


// Tenga en cuenta que el método provideDelegate solo afecta a la creación de la propiedad auxiliar
// y no afecta al código generado para el getter o el setter.

// Con la interfaz PropertyDelegateProvider de la biblioteca estándar, puede crear proveedores de
// delegados sin crear nuevas clases.

val provider = PropertyDelegateProvider { thisRef: Any?, property ->
    ReadOnlyProperty<Any?, Int> {_, property -> 42 }
}
val delegate: Int by provider

fun main() {
}

