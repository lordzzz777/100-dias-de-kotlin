// ------------ Anotaciones ------------

// Las anotaciones son medios para adjuntar metadatos al código.
// Para declarar una anotación, ponga el modificador de anotación
// delante de una clase:

// annotation class Fancy

// Los atributos adicionales de la anotación se pueden especificar
// anotando la clase de anotación con metanotaciones:

// @Target especifica los posibles tipos de elementos que se pueden
// anotar con la anotación (como clases, funciones, propiedades
// y expresiones);

// @Retention especifica si la anotación se almacena en los archivos
// de clase compilados y si es visible a través de la reflexión en
// tiempo de ejecución (por defecto, ambos son verdaderos);

// @Repeatable permite usar la misma anotación en un solo elemento
// varias veces;

// @MustBeDocumented especifica que la anotación es parte de la API
// pública y debe incluirse en la firma de clase o método que se
// muestra en la documentación de la API generada.
/*
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION,
    AnnotationTarget.TYPE_PARAMETER, AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.EXPRESSION)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class Fancy2
*/

// ------------ Uso -----------

/*
@Fancy class Foo {
    @Fancy fun baz(@Fancy foo: Int): Int {
        return (@Fancy 1)
    }
}
*/


// Si necesita anotar el constructor principal de una clase, debe
// agregar la palabra clave del constructor a la declaración del
// constructor y agregar las anotaciones antes de ella:

// class Foo @Inject constructor(dependency: MyDependency) { ... }

// También puedes anotar los accesores de propiedades:

/*
class Foo {
    var x: MyDependency? = null
        @Inject set
}
 */

// ------------ Constructores ------------

// Las anotaciones pueden tener constructores que toman parámetros.
/*
annotation class Special(val why: String)

@Special("example") class Foo {}
*/
// Los tipos de parámetros permitidos son:

// Tipos que corresponden a los tipos primitivos de Java (Int, Long, etc.)

// Cadena

// Clases (Foo::clase)

// Enums

// Otras anotaciones

// Matrices de los tipos enumerados anteriormente

// Los parámetros de anotación no pueden tener tipos anulables, porque la JVM
// no admite el almacenamiento de null como un valor de un atributo de anotación.

// Si una anotación se utiliza como parámetro de otra anotación, su nombre no
// tiene el prefijo del carácter @:

/*
annotation class ReplaceWith(val expression: String)

annotation class Deprecated(
        val message: String,
        val replaceWith: ReplaceWith = ReplaceWith(""))

@Deprecated("This function is deprecated, use === instead",
 ReplaceWith("this === other"))
 */

// Si necesita especificar una clase como argumento de una anotación, utilice
// una clase Kotlin (KClass). El compilador Kotlin lo convertirá automáticamente
// en una clase Java, para que el código Java pueda acceder a las anotaciones
// y argumentos normalmente.

import kotlin.reflect.KClass

annotation class Ann(val arg1: KClass<*>, val arg2: KClass<out Any>)

@Ann(String::class, Int::class) class MyClass

// ------------ Instanciación ------------

// En Java, un tipo de anotación es una forma de interfaz, por lo que puede
// implementarla y usar una instancia. Como alternativa a este mecanismo,
// Kotlin le permite llamar a un constructor de una clase de anotación en
// código arbitrario y utilizar de manera similar la instancia resultante.

/*
annotation class InfoMarker(val info: String)

fun processInfo(marker: InfoMarker): Unit = TODO()

fun main(args: Array<String>) {
    if (args.isNotEmpty())
        processInfo(getAnnotationReflective(args))
    else
        processInfo(InfoMarker("default"))
}
 */

// Obtenga más información sobre la instanciación de las clases de anotación
// en este KEEP.

// ------------ Lambdas ------------

// Las anotaciones también se pueden usar en lambdas. Se aplicarán al método
// invoke() en el que se genera el cuerpo de la lambda. Esto es útil para
// marcos como Quasar, que utiliza anotaciones para el control de la concurrencia.
/*
annotation class Suspendable

val f = @Suspendable { Fiber.sleep(10) }
*/

// ------------ Objetivos del sitio de uso de anotación ------------

// Cuando está anotando una propiedad o un parámetro de constructor principal,
// hay varios elementos de Java que se generan a partir del elemento Kotlin
// correspondiente y, por lo tanto, múltiples ubicaciones posibles para la
// anotación en el código de bytes de Java generado. Para especificar exactamente
// cómo se debe generar la anotación, utilice la siguiente sintaxis:

/*
class Example(@field:Ann val foo,    // annotate Java field
              @get:Ann val bar,      // annotate Java getter
              @param:Ann val quux)   // annotate Java constructor parameter
 */

// Se puede usar la misma sintaxis para anotar todo el archivo. Para hacer esto,
//  ponga una anotación con el archivo de destino en el nivel superior de un archivo,
//  antes de la directiva del paquete o antes de todas las importaciones si el
//  archivo está en el paquete predeterminado:

/*
class Example(@field:Ann val foo,    // annotate Java field
              @get:Ann val bar,      // annotate Java getter
              @param:Ann val quux)   // annotate Java constructor parameter

 */

// Se puede usar la misma sintaxis para anotar todo el archivo. Para hacer esto,
//  ponga una anotación con el archivo de destino en el nivel superior de un
//  archivo, antes de la directiva del paquete o antes de todas las importaciones
//  si el archivo está en el paquete predeterminado:

/*
@file:JvmName("Foo")

package org.jetbrains.demo
 */

// Se puede usar la misma sintaxis para anotar todo el archivo. Para hacer esto,
// ponga una anotación con el archivo de destino en el nivel superior de un archivo,
// antes de la directiva del paquete o antes de todas las importaciones si el archivo
// está en el paquete predeterminado:

/*
@file:JvmName("Foo")

package org.jetbrains.demo
 */

// Si tiene varias anotaciones con el mismo objetivo, puede evitar repetir el objetivo
// agregando corchetes después del objetivo y poniendo todas las anotaciones dentro de
// los corchetes

/*
class Example {
     @set:[Inject VisibleForTesting]
     var collaborator: Collaborator
}
 */

// La lista completa de los objetivos del sitio de uso compatibles es:

// Ficha

// Propiedad (las anotaciones con este objetivo no son visibles para Java)

// Campo

// Get (property getter)

// Conjunto (property setter)

// Receptor (parámetro del receptor de una función o propiedad de extensión)

// Param (parámetro del constructor)

// Setparam (parámetro de setter de propiedades)

// Delegado (el campo que almacena la instancia de delegado para una propiedad
// delegada)

// Para anotar el parámetro receptor de una función de extensión, utilice la
// siguiente sintaxis:

// fun @receiver:Fancy String.myExtension() { ... }

// Si no especifica un objetivo del sitio de uso, el objetivo se elige de acuerdo con
// la anotación @Target de la anotación que se está utilizando. Si hay varios objetivos
// aplicables, se utiliza el primer objetivo aplicable de la siguiente lista:

// Param

// Propiedad

// Campo

// Anotaciones de Java

// Las anotaciones de Java son 100 % compatibles con Kotlin:

/*
import org.junit.Test
import org.junit.Assert.*
import org.junit.Rule
import org.junit.rules.*

class Tests {
    // apply @Rule annotation to property getter
    @get:Rule val tempFolder = TemporaryFolder()

    @Test fun simple() {
        val f = tempFolder.newFile()
        assertEquals(42, getTheAnswer())
    }
}
 */

// Dado que el orden de los parámetros de una anotación escrita en Java no está
// definido, no se puede usar una sintaxis de llamada de función regular para
// pasar los argumentos. En su lugar, debe usar la sintaxis
// del argumento con nombre:

/*
// Java
public @interface Ann {
    int intValue();
    String stringValue();
}
 */

// Kotlin
// @Ann(intValue = 1, stringValue = "abc") class C

// Al igual que en Java, un caso especial es el parámetro de valor; su valor se
// puede especificar sin un nombre explícito:

/*
// Java
public @interface AnnWithValue {
    String value();
}

// Kotlin
@Ann(intValue = 1, stringValue = "abc") class C
 */

// Al igual que en Java, un caso especial es el parámetro de valor; su valor se puede
// especificar sin un nombre explícito:

/*
// Java
public @interface AnnWithValue {
    String value();
}

// Kotlin
@AnnWithValue("abc") class C
 */


// ------- Matrices como parámetros de anotación -------

// Si el argumento de valor en Java tiene un tipo de matriz, se convierte en un parámetro
// vararg en Kotlin:

/*
// Java
public @interface AnnWithArrayValue {
    String[] value();
}

// Kotlin
@AnnWithArrayValue("abc", "foo", "bar") class C
 */

// Para otros argumentos que tienen un tipo de matriz, debe usar la sintaxis literal
// de matriz o arrayOf(...):

/*
// Java
public @interface AnnWithArrayMethod {
    String[] names();
}

@AnnWithArrayMethod(names = ["abc", "foo", "bar"])
class C
 */

// ------- Acceso a las propiedades de una instancia de anotación -------

// Los valores de una instancia de anotación se exponen como propiedades del código Kotlin:

/*
// Java
public @interface Ann {
    int value();
}

// Kotlin
fun foo(ann: Ann) {
    val i = ann.value
}
 */

// ------- Capacidad para no generar objetivos de anotación JVM 1.8+ -------

// Si una anotación de Kotlin tiene TYPE entre sus objetivos de Kotlin, la anotación
// se asigna a java.lang.annotation.ElementType.TYPE_USE en su lista de objetivos de
// anotación de Java. Esto es como la forma en que el objetivo TYPE_PARAMETER Kotlin
// se asigna al objetivo java.lang.annotation.ElementType.TYPE_PARAMETER Java. Este es
// un problema para los clientes de Android con niveles de API inferiores a 26, que no
// tienen estos objetivos en la API.

// Para evitar generar los objetivos de anotación TYPE_USE y TYPE_PARAMETER, utilice el
// nuevo argumento del compilador -Xno-new-java-annotation-targets.

// -------------- Anotaciones repetibles ------------

// Al igual que en Java, Kotlin tiene anotaciones repetibles, que se pueden aplicar a un
// solo elemento de código varias veces. Para que su anotación sea repetible, marque su
// declaración con la metanotación repetible de @kotlin.annotation. Esto hará que sea
// repetible tanto en Kotlin como en Java. Las anotaciones repetibles de Java también
// son compatibles desde el lado de Kotlin.

// La principal diferencia con el esquema utilizado en Java es la ausencia de una
// anotación que contiene, que el compilador de Kotlin genera automáticamente con
// un nombre predefinido. Para una anotación en el siguiente ejemplo, generará la
// anotación que contiene @Tag.Container:

/*
@Repeatable
annotation class Tag(val name: String)

// The compiler generates the @Tag.Container containing annotation
 */

// Puede establecer un nombre personalizado para una anotación que contiene aplicando
// la metanotación @kotlin.jvm.JvmRepeatable y pasando una clase de anotación que
// contiene declarada explícitamente como argumento:

/*
@JvmRepeatable(Tags::class)
annotation class Tag(val name: String)

annotation class Tags(val value: Array<Tag>)
 */

// Para extraer anotaciones repetibles de Kotlin o Java a través de la reflexión,
// utilice la función KAnnotatedElement.findAnnotations().

// Obtenga más información sobre las anotaciones repetibles de Kotlin en este KEEP.


fun main() {


}