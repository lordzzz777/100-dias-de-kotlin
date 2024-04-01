// ------------ Excepciones ------------

// ------- Clases de excepción -------

// Todas las clases de excepción en Kotlin heredan el Throwable clase.
// Cada excepción tiene un mensaje, un rastro de pila y una causa opcional.

// Para lanzar un objeto de excepción, utilice el throw expresión:

fun main() {
    throw Exception("Hi There!")

// Para manejar una excepción, utilice el try... catch expresión:
/*
    try {
        // aquí el código
    } catch (e: SomeException) {
        // aquí lo manejas
    } finally {
        // Opcional, finalmente bloquear
    }
 */

// Puede haber cero o más catch bloques, y la finallyEl bloqueo puede ser
// omitido. Sin embargo, al menos uno catch o o finally bloqueo es necesario.
// Intente ser una expresión

// try es una expresión, lo que significa que puede tener un valor de retorno:

//    val a: Int? = try { input.toInt() } catch (e: NumberFormatException) { null }

// El valor devuelto de un try expresión es la última expresión en el try bloque
// o última expresión en el catch bloque (o bloques). El contenido de la finally
// bloqueo no afecte el resultado de la expresión.

// ------- Excepciones chequeadas -------

// Kotlin no tiene excepciones comprobadas. Hay muchas razones para ello, pero
// proporcionaremos un ejemplo sencillo que ilustra por qué es el caso.

//A continuación se presenta una interfaz de ejemplo del JDK implementado por la
// StringBuilder clase:

//    Appendable append(CharSequence csq) throws IOException;

// Esta firma dice que cada vez que añado una cadena a algo (un StringBuilder,
// algún tipo de registro, una consola, etc.), tengo que captar las IOExceptions.
// ¿Por qué? Porque la implementación podría estar realizando operaciones de E/S
// (Writer también implementa Appendable). El resultado es un código como este
// por todas partes:

/*
try {
    log.append(message)
} catch (IOException e) {
    // Must be safe
}
 */

// Y eso no está bien. Basta con echar un vistazo al Eje efectivo, 3a Edición,
// ítem 77: No ignores excepciones.

//Bruce Eckel dice esto sobre las excepciones comprobáda:

// ************ Info ************
// El examen de los programas pequeños lleva a la conclusión de que requerir
// especificaciones de excepción podría mejorar la productividad del
// desarrollador y mejorar la calidad del código, pero la experiencia con
// grandes proyectos de software sugiere un resultado diferente.

// Y aquí hay algunas reflexos adicionales sobre el asunto:
//
//    Las excepciones verificadas de Java fueron un error (Rod Waldhoff)
//
//    El problema con excepciones chequeadas (Anders Hejlsberg)
//
//Si desea alertar a los llamantes sobre posibles excepciones cuando
// llamas al código Kotlin de Java, Swift o Objective-C, puedes usar el @Throws
// anotación. Leer más sobre el uso de esta anotación para Java y para Swift y Objective-C.

// ------- El tipo Nada -------

// throw es una expresión en Kotlin, por lo que se puede utilizar, por ejemplo,
// como parte de una expresión de Elvis:

//    val s = person.name ?: throw IllegalArgumentException("Name required")

// El throw expresión tiene el tipo Nothing. Este tipo no tiene valores y se utiliza
// para marcar ubicaciones de código que nunca se pueden alcanzar. En su propio código,
// puede utilizar Nothing para marcar una función que nunca devuelve:

    fun fail(message: String): Nothing {
        throw IllegalArgumentException(message)
    }

// Cuando usted llama a esta función, el compilador sabrá que la ejecución no continúa
// más allá de la llamada:

    val s = person.name ?: fail("Name required")
    println(s)     // 's' is known to be initialized at this point


// También puede encontrar este tipo cuando se trata de la inferencia de tipo.
// La variante nula de este tipo, Nothing?, tiene exactamente un valor posible,
// que es null. Si usas null para inicializar un valor de un tipo inferido y no
// hay otra información que se pueda utilizar para determinar un tipo más específico,
// el compilador inferirá el Nothing? tipo:

    val x = null           // 'x' has type `Nothing?`
    val l = listOf(null)   // 'l' has type `List<Nothing?>

// ----------- La interoperabilidad de Java ------------

// Consulte la sección sobre excepciones en la página de interoperabilidad de Java
// para obtener información sobre la interoperabilidad de Java.

}