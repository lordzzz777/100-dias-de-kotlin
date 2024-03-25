// ------- Tipos de enteros sin signo -------

// Además de los tipos enteros, Kotlin proporciona los siguientes tipos para números
// enteros sin signo:

/*
Type     Size (bits)         Min value         Max value

UByte        8                    0              255

UShort       16                   0              65,535

UInt         32                   0              4,294,967,295 (232 - 1)

ULong        64                   0              18,446,744,073,709,551,615 (264 - 1)
 */

// Los tipos sin firmar admiten la mayoría de las operaciones de sus contrapartes firmadas.

// ********* INFO **********
// Los números sin signo se implementan como clases en línea con una sola propiedad de
// almacenamiento que contiene el tipo de contraparte firmada correspondiente del mismo ancho.
// Si desea convertir entre tipos de enteros sin signo y sin signo, asegúrese de actualizar su
// código para que cualquier llamada de función y operación admita el nuevo tipo.

// ------- Matrices y rangos sin firmar -------

// ********* Atención **********
// Las matrices sin firmar y las operaciones en ellas están en versión beta.
// Se pueden cambiar de forma incompatible en cualquier momento. Es necesario
// optar por no Participar (consulte los detalles a continuación)


// Al igual que para las primitivas, cada tipo sin signo tiene un tipo correspondiente que
// representa matrices de ese tipo:

//.- UByteArray: an array of unsigned bytes. (Una matriz de bytes sin firmar.)
//.- UShortArray: an array of unsigned shorts.
//.- UIntArray: an array of unsigned ints. (Una serie de ints sin firmar.)
//.-  ULongArray: an array of unsigned longs. (Una serie de largos sin firmar.)

// Al igual que para las matrices enteras con signo, proporcionan una API similar a la clase
// Array sin sobrecarga el contenedor.

// Cuando utiliza matrices sin firmar, recibe una advertencia que indica que esta función aún
// no es estable. Para eliminar la advertencia, opte por participar con la anotación
// @ExperimentalUnsignedTypes. Depende de usted decidir si sus clientes tienen que optar
// explícitamente por el uso de su API, pero tenga en cuenta que las matrices sin firmar no
// son una característica estable, por lo que una API que las utiliza puede romperse por cambios
// en el idioma. Obtenga más información sobre los requisitos de suscripción.

// Los rangos y progresiones son compatibles con UInt y ULong por las clases UIntRange,
// UIntProgression, ULongRange y ULongProgression. Junto con los tipos de enteros sin signo,
// estas clases son estables.

// ------- Literales de enteros sin signo -------

// Para hacer que los enteros sin signo sean más fáciles de usar, Kotlin proporciona la
// capacidad de etiquetar un literal entero con un sufijo que indique un tipo específico
// sin signo (similar a Float o Long):

fun main() {

//.- La etiqueta u y U es para literales sin firmar. El tipo exacto se determina en función
// del tipo esperado. Si no se proporciona ningún tipo esperado, el compilador usará UInt
// o ULong dependiendo del tamaño del literal:

    val b: UByte = 1u  // UByte, expected type provided
    val s: UShort = 1u // UShort, expected type provided
    val l: ULong = 1u  // ULong, expected type provided

    val a1 = 42u // UInt: no expected type provided, constant fits in UInt
    val a2 = 0xFFFF_FFFF_FFFFu // ULong: no expected type provided, constant doesn't fit in UInt

// uL y UL etiquetan explícitamente el literal como largo sin signo:
    val a = 1UL // ULong, even though no expected type provided and constant fits into UInt

// ------- Casos de uso -------

// El principal caso de uso de los números sin signo es utilizar el rango completo de bits
// de un entero para representar valores positivos.
//Por ejemplo, para representar constantes hexadecimales que no encajan en tipos con signos,
// como el color en formato AARRGGBB de 32 bits:

    data class Color(val representation: UInt)

    val yellow = Color(0xFFCC00CCu)

// Puede usar números sin signo para inicializar matrices de bytes sin lanzamientos literales
// toByte() explícitos:

    val byteOrderMarkUtf8 = ubyteArrayOf(0xEFu, 0xBBu, 0xBFu)

// Otro caso de uso es la interoperabilidad con las API nativas. Kotlin permite representar
// declaraciones nativas que contienen tipos sin signo en la firma. El mapeo no sustituirá
// los enteros sin signo por otros con signo manteniendo la semántica inalterada.

/// **** Non-goals ****

// Si bien los enteros sin signo solo pueden representar números positivos y cero, no es un
// objetivo usarlos donde el dominio de la aplicación requiere números enteros no negativos.
// Por ejemplo, como un tipo de tamaño de colección o valor de índice de colección.

// Hay un par de razones:

//.- El uso de enteros con signo puede ayudar a detectar desbordamientos accidentales y
//   condiciones de error de señal, como que List.lastIndex sea -1 para una lista vacía.

//.- Los enteros sin signo no se pueden tratar como una versión de rango limitado de los
//   con signo porque su rango de valores no es un subconjunto del rango de números enteros
//   con signo. Ni los números enteros sin signo ni los no son subtipos entre sí.

}
