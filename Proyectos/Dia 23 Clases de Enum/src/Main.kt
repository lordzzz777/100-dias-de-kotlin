// ------------ Clases de Enum ------------

// El caso de uso más básico para las clases de enumeración es la implementación
// de enumeraciones de tipo seguro:

enum class Direction {
    NORTH, SOUTH, WEST, EAST
}

// Cada constante de enumeración es un objeto. Las constantes de enumeraciones
// están separadas por comas.

// Dado que cada enumeración es una instancia de la clase de enumeración,
// se puede inicializar como:

enum class Color(val rgb: Int) {
    RED(0xFF0000),
    GREEN(0x00FF00),
    BLUE(0x0000FF)
}


// ------------ Clases anónimas ------------

// Las constantes de enumeración pueden declarar sus propias clases anónimas con
// sus métodos correspondientes, así como con métodos de base anulantes.

enum class ProtocolState {
    WAITING {
        override fun signal() = TALKING
    },

    TALKING {
        override fun signal() = WAITING
    };

    abstract fun signal(): ProtocolState
}

// Si la clase enum define a algún miembro, separe las definiciones constantes de las
// definiciones de los miembros con un punto y coma.



// ------------Implementación de interfaces en clases de enumeración ------------

// Una clase de enumeración puede implementar una interfaz (pero no puede derivarse de
// una clase), proporcionando una implementación común de los miembros de la interfaz
// para todas las entradas, o implementaciones separadas para cada entrada dentro de su
// clase anónima. Esto se hace agregando las interfaces que desea implementar a la
// declaración de la clase enum de la siguiente manera:

import java.util.function.BinaryOperator
import java.util.function.IntBinaryOperator

enum class IntArithmetics : BinaryOperator<Int>, IntBinaryOperator {
    PLUS {
        override fun apply(t: Int, u: Int): Int = t + u
    },
    TIMES {
        override fun apply(t: Int, u: Int): Int = t * u
    };

    override fun applyAsInt(t: Int, u: Int) = apply(t, u)
}

fun main() {
    val a = 13
    val b = 31
    for (f in IntArithmetics.entries) {
        println("$f($a, $b) = ${f.apply(a, b)}")
    }
}

// Todas las clases de enumerar implementan la interfaz comparable de forma predeterminada.
// Las constantes en la clase de enumeración se definen en el orden natural. Para obtener
// más información, consulte Pedidos.



// ------------ Trabajar con constantes de enumeraciones ------------

// Las clases de enumeración en Kotlin tienen propiedades y métodos sintéticos para enumerar
// las constantes de enumeración definidas y obtener una constante de enumeración por su nombre.
// Las firmas de estos métodos son las siguientes (suponiendo que el nombre de la
// clase enum sea EnumClass):

EnumClass.valueOf(value: String): EnumClass
EnumClass.entries: EnumEntries<EnumClass> // specialized List<EnumClass>

// A continuación se muestra un ejemplo de ellos en acción:

enum class RGB { RED, GREEN, BLUE }

fun main() {
    for (color in RGB.entries) println(color.toString()) // prints RED, GREEN, BLUE
    println("The first color is: ${RGB.valueOf("RED")}") // prints "The first color is: RED"
}


// El método valueOf() lanza una IllegalArgumentException si el nombre especificado no coincide
// con ninguna de las constantes de enumeración definidas en la clase.

// Antes de la introducción de las entradas en Kotlin 1.9.0, se utilizó la función values() para
// recuperar una matriz de constantes de enumeración.

// Cada constante de enumeración también tiene propiedades: nombre y ordinal, para obtener su
// nombre y posición (a partir de 0) en la declaración de la clase de enumeración:

println(RGB.RED.name)    // prints RED
println(RGB.RED.ordinal) // prints 0


// Puede acceder a las constantes de una clase de enumeración de una manera genérica utilizando las
// funciones enumValues<T>() y enumValueOf<T>():


enum class RGB { RED, GREEN, BLUE }

inline fun <reified T : Enum<T>> printAllValues() {
    println(enumValues<T>().joinToString { it.name })
}

printAllValues<RGB>() // prints RED, GREEN, BLUE


// **********************************************************************************************//
// Para obtener más información sobre las funciones en línea y los parámetros de tipo reificado, //
// consulte Funciones en línea.                                                                  //
// **********************************************************************************************//


// En Kotlin 1.9.20, la función enumEntries<T>() se introduce como un reemplazo futuro de la función
// enumValues<T>().

// La función enumValues<T>() sigue siendo compatible, pero le recomendamos que utilice la función
// enumEntries<T>() en su lugar porque tiene menos impacto en el rendimiento. Cada vez que llamas a
// enumValues<T>() se crea una nueva matriz, mientras que cada vez que llamas a enumEntries<T>() se
// devuelve la misma lista cada vez, lo cual es mucho más eficiente.

// Por ejemplo:

enum class RGB { RED, GREEN, BLUE }

@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T : Enum<T>> printAllValues() {
    println(enumEntries<T>().joinToString { it.name })
}

printAllValues<RGB>()
// RED, GREEN, BLUE


// ******************************************* Atención ********************************************//
// La función enumEntries<T>() es Experimental. Para usarlo, opte por @OptIn(ExperimentalStdlibApi) //
// y establezca la versión de idioma en al menos 1.9.                                               //
// *************************************************************************************************//
