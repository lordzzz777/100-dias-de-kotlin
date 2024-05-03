// ------------ Reflexión ------------

// Reflection es un conjunto de características de lenguaje
// y biblioteca que le permite introducir la estructura de
// su programa en tiempo de ejecución. Las funciones y
// propiedades son ciudadanos de primera clase en Kotlin,
// y la capacidad de introducirlas (por ejemplo, aprender
// el nombre o el tipo de una propiedad o función en tiempo
// de ejecución) es esencial cuando se utiliza un estilo
// funcional o reactivo.

// *************************** Info ******************************
// Kotlin/JS proporciona un soporte limitado para las funciones de
// reflexión. Más información sobre la reflexión en Kotlin/JS.
// ***************************************************************

// ------------ Dependencia de JVM ------------

// En la plataforma JVM, la distribución del compilador Kotlin
// incluye el componente de tiempo de ejecución necesario para
// usar las características de reflexión como un artefacto
// separado, kotlin-reflect.jar. Esto se hace para reducir el
// tamaño requerido de la biblioteca de tiempo de ejecución para
// las aplicaciones que no utilizan funciones de reflexión.

// Para usar la reflexión en un proyecto de Gradle o Maven, añade
// la dependencia de kotlin-reflect:


/*
 -.  En Gradle:
dependencies {
    implementation(kotlin("reflect"))
}
 */

/*
 -. En Maven:

<dependencies>
  <dependency>
      <groupId>org.jetbrains.kotlin</groupId>
      <artifactId>kotlin-reflect</artifactId>
  </dependency>
</dependencies>
 */

// Si no usas Gradle o Maven, asegúrate de tener kotlin-reflect.jar
// en la ruta de clase de tu proyecto. En otros casos compatibles
// (proyectos IntelliJ IDEA que utilizan el compilador de línea de
// comandos o Ant), se añade de forma predeterminada. En el compilador
// de línea de comandos y Ant, puede usar la opción del compilador
// -no-reflect para excluir kotlin-reflect.jar de la ruta de clase.

// ------------ Referencias de clase ------------

// La característica de reflexión más básica es obtener la referencia
// en tiempo de ejecución a una clase de Kotlin. Para obtener la
// referencia a una clase Kotlin estadísticamente conocida, puede usar
// la sintaxis literal de la clase:

// val c = MyClass::class

// La referencia es un valor de tipo KClass.


// *************************** Info ******************************
// En JVM: una referencia de clase Kotlin no es lo mismo que una
// referencia de clase Java. Para obtener una referencia de clase
// de Java, utilice la propiedad .java en una instancia de KClass.
// ****************************************************************

// ------- Referencias de clases encuadernadas -------

// Puede obtener la referencia a la clase de un objeto específico con
// la misma sintaxis ::class utilizando el objeto como receptor:

// ------------ Referencias que se pueden llamar ------------

// Las referencias a funciones, propiedades y constructores también
// se pueden llamar o utilizar como instancias de tipos de funciones.

// El supertipo común para todas las referencias invocables es
// KCallable<out R>, donde R es el tipo de valor de retorno.
// Es el tipo de propiedad para las propiedades y el tipo construido
// para los constructores.

// ------- Referencias de funciones -------

// Cuando tiene una función con nombre declarada de la siguiente
// manera, puede llamarla directamente (isOdd(5)):

// fun isOdd(x: Int) = x % 2 != 0

// Alternativamente, puede usar la función como un valor de tipo
// de función, es decir, pasarla a otra función. Para ello,
// utilice el operador :::

fun isOdd(x: Int) = x % 2 != 0

fun main() {
    val numbers = listOf(1, 2, 3)
    println(numbers.filter(::isOdd))

// Aquí ::isOdd es un valor del tipo de función (Int) -> Booleano.

// Las referencias de funciones pertenecen a uno de los subtipos de
// KFunction<out R>, dependiendo del recuento de parámetros.
// Por ejemplo, KFunction3<T1, T2, T3, R>.

//:: Se puede usar con funciones sobrecargadas cuando el tipo esperado
// se conoce por el contexto. Por ejemplo:

    fun isOdd(x: Int) = x % 2 != 0
    fun isOdd(s: String) = s == "brillig" || s == "slithy" || s == "tove"

    val numbers2 = listOf(1, 2, 3)
    println(numbers.filter(::isOdd)) // refers to isOdd(x: Int)

// Alternativamente, puede proporcionar el contexto necesario almacenando
// la referencia del método en una variable con un tipo especificado
// explícitamente

// val predicate: (String) -> Boolean = ::isOdd
// refers to isOdd(x: String)

// Si necesita usar un miembro de una clase o una función de extensión,
// debe estar calificado: String::toCharArray.

// Incluso si inicializa una variable con una referencia a una función
// de extensión, el tipo de función inferida no tendrá receptor, pero
// tendrá un parámetro adicional que acepta un objeto receptor. Para
// tener un tipo de función con un receptor en su lugar, especifique
// el tipo explícitamente:

//  val isEmptyStringList: List<String>.() -> Boolean = List<String>::isEmpty

// Ejemplo: composición de funciones

// Considere la siguiente función:

    fun <A, B, C> compose(f: (B) -> C, g: (A) -> B): (A) -> C {
        return { x -> f(g(x)) }
    }

// Devuelve una composición de dos funciones que se le pasan:
// compose(f, g) = f(g(*)). Puedes aplicar esta función a las
// referencias invocables:

/*
fun <A, B, C> compose(f: (B) -> C, g: (A) -> B): (A) -> C {
    return { x -> f(g(x)) }
}

fun isOdd(x: Int) = x % 2 != 0

fun main() {
    fun length(s: String) = s.length

    val oddLength = compose(::isOdd, ::length)
    val strings = listOf("a", "ab", "abc")

    println(strings.filter(oddLength))
}
 */

// ------- Referencias de propiedades -------

// Para acceder a las propiedades como objetos de primera clase
// en Kotlin, utilice el operador :::

/*
val x = 1

fun main() {
    println(::x.get())
    println(::x.name)
}
 */

// La expresión ::x se evalúa como un objeto de propiedad de tipo
// KProperty0<Int>. Puede leer su valor usando get() o recuperar
// el nombre de la propiedad usando la propiedad name. Para obtener
// más información, consulte los documentos de la clase KProperty.

// Para una propiedad mutable como var y = 1, ::y devuelve un valor
// con el tipo KMutableProperty0<Int> que tiene un método set():

/*
var y = 1

fun main() {
    ::y.set(2)
    println(y)
}
 */

// Se puede utilizar una referencia de propiedad cuando se espera
// una función con un solo parámetro genérico:

/*
fun main() {
    val strs = listOf("a", "bc", "def")
    println(strs.map(String::length))
}
 */

// Para acceder a una propiedad que es miembro de una clase,
// califíquela de la siguiente manera:

/*
fun main() {
    class A(val p: Int)
    val prop = A::p
    println(prop.get(A(1)))
}
 */

// Para una propiedad de extensión:

/*
val String.lastChar: Char
    get() = this[length - 1]

fun main() {
    println(String::lastChar.get("abc"))
}
 */

// ------- Interoperabilidad con la reflexión de Java -------

// En la plataforma JVM, la biblioteca estándar contiene extensiones
// para clases de reflexión que proporcionan un mapeo hacia y desde
// objetos de reflexión de Java (consulte el paquete kotlin.reflect.jvm).
// Por ejemplo, para encontrar un campo de respaldo o un método Java que
// sirva como getter para una propiedad de Kotlin,
// puede escribir algo como esto:

/*
import kotlin.reflect.jvm.*

class A(val p: Int)

fun main() {
    println(A::p.javaGetter) // prints "public final int A.getP()"
    println(A::p.javaField)  // prints "private final int A.p"
    }
 */

// Para obtener la clase Kotlin que corresponde a una clase de Java,
// utilice la propiedad de extensión .kotlin:

 // fun getKClass(o: Any): KClass<Any> = o.javaClass.kotlin

// ------- Referencias del constructor -------

// Se puede hacer referencia a los constructores al igual que los
// métodos y las propiedades. Puede usarlos dondequiera que el programa
// espere un objeto de tipo de función que tome los mismos parámetros
// que el constructor y devuelva un objeto del tipo apropiado.
// Se hace referencia a los constructores utilizando el
// operador :: y añadiendo el nombre de la clase.
// Considere la siguiente función que espera un parámetro de función
// sin parámetros y devuelve el tipo Foo:
}

class Foo

fun function(factory: () -> Foo) {
    val x: Foo = factory()
}

// Usando ::Foo, el constructor de argumento cero de la clase Foo,
// puedes llamarlo así:

// function(::Foo)

// Las referencias llamables a los constructores se escriben como
// uno de los subtipos de KFunction<out R> dependiendo del
// recuento de parámetros.

// ------- Referencias de funciones y propiedades enlazadas -------

// Puede hacer referencia a un método de instancia de un objeto en particular:


/*
fun main() {
    val numberRegex = "\\d+".toRegex()
    println(numberRegex.matches("29"))

    val isNumber = numberRegex::matches
    println(isNumber("29"))
}
 */

// En lugar de llamar al método coincide directamente, el ejemplo utiliza
// una referencia a él. Tal referencia está vinculada a su receptor.
// Se puede llamar directamente (como en el ejemplo anterior) o usar siempre
// que se espere una expresión de tipo de función:

/*
fun main() {
2
    val numberRegex = "\\d+".toRegex()
3
    val strings = listOf("abc", "124", "a70")
4
    println(strings.filter(numberRegex::matches))
5
}
 */

// Compara los tipos de referencias enlazadas y no enlazadas. La referencia
// invocable encuadernada tiene su receptor "conectado", por lo que el tipo
// de receptor ya no es un parámetro:

val isNumber: (CharSequence) -> Boolean = numberRegex::matches

val matches: (Regex, CharSequence) -> Boolean = Regex::matches

// También se puede enlazar una referencia de propiedad:

/*
fun main() {
    val prop = "abc"::length
    println(prop.get())
}
 */

// No es necesario especificar esto como receptor: this::foo y ::foo son equivalentes.

// ------- Referencias de constructores enlazados -------

// Se puede obtener una referencia llamada enlazada a un constructor de una clase
// interna proporcionando una instancia de la clase externa: