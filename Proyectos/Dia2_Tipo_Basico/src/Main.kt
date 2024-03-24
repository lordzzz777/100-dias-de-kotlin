
// Tipo Basico (en el buscador Spell)

/*
En Kotlin, todo es un objeto en el sentido de que puedes llamar a las funciones
y propiedades de los miembros en cualquier variable. Si bien ciertos tipos tienen
una representación interna optimizada como valores primitivos en tiempo de
 ejecución (como números, caracteres, booleanos y otros), aparecen y se comportan
 como clases normales para ti.
 */

// Esta sección describe los tipos básicos utilizados en Kotlin:

/*
 - Números y sus contrapartes sin firmar

 - Booleans
 - Characters
 - Strings
 - Arrays
 */


//  ******** Numbers **************

// Integer type:
/* Kotlin proporciona un conjunto de tipos integrados que representan números.
Para los números enteros, hay cuatro tipos con diferentes tamaños y, por lo tanto,
 rangos de valores:
 */
/*
  .- 8 byte   -128 -------------------------------> 127
  .- 16 byte  -32768 -----------------------------> 32767
  .- 32 byte  -2,147,483,648 (-231) --------------> 2,147,483,647 (231 - 1)
  .- 64 byte  -9,223,372,036,854,775,808 (-263)---> 9,223,372,036,854,775,807 (263 - 1)

 */

/*
Cuando se inicializa una variable sin una especificación de tipo explícita,
el compilador infiere automáticamente el tipo con el rango más pequeño suficiente
para representar el valor a partir de Int. Si no excede el rango de Int, el tipo es Int.
Si excede, el tipo es Long. Para especificar el valor Long explícitamente, añada el sufijo
L al valor. La especificación de tipo explícito hace que el compilador compruebe que el
valor no exceda el rango del tipo especificado.
*/


fun main() {
    val one = 1 // Int
    val threeBillion = 3000000000 // Long
    val oneLong = 1L // Long
    val oneByte: Byte = 1

// Además de los tipos enteros, Kotlin también proporciona tipos enteros sin signo.
//  Para obtener más información, consulte Tipos de enteros sin signo.

// ------ Floating-point types -----

// Para los números reales, Kotlin proporciona tipos de coma flotante Float y Double
// que se adhieren al estándar IEEE 754. Float refleja la precisión única de IEEE 754,
// mientras que Double refleja la doble precisión.

// Estos tipos difieren en su tamaño y proporcionan almacenamiento para números de coma
// flotante con diferente precisión:


// Float -------- 32 ---- 24 ---- 8 ---- 6-7
// Double ------- 64 ---- 53 ---- 11 --- 15-16

// Puedes inicializar las variables doble y flotante con números que tengan una parte
// fraccionada. Está separado de la parte entera por un punto (.) Para las variables
// inicializadas con números fraccionarios, el compilador infiere el tipo Doble:



        fun printDouble(d: Double) { print(d) }

        val i = 1
        val d = 1.0
        val f = 1.0f

        printDouble(d)
//    printDouble(i) // Error: Type mismatch
//    printDouble(f) // Error: Type mismatch

// Para convertir valores numéricos a diferentes tipos,
// utilice conversiones explícitas.

// ------ Constantes literales para números -----

//Hay los siguientes tipos de constantes literales para valores integrales:
//
//Decimales: 123
//
//Los largos están etiquetados con una L mayúscula: 123L
//
//Hexadecimales: 0x0F
//
//Binarios: 0b00001011

/// Los literales octales no son compatibles con Kotlin. ///

// Kotlin también admite una notación convencional para números de coma flotante:
//
//Dobles por defecto: 123.5, 123.5e10
//
//Los flotadores están etiquetados por f o F: 123.5f
//
//Puedes usar guiones bajos para hacer que las constantes numéricas sean más legibles:

    val oneMillion = 1_000_000
    val creditCardNumber = 1234_5678_9012_3456L
    val socialSecurityNumber = 999_99_9999L
    val hexBytes = 0xFF_EC_DE_5E
    val bytes = 0b11010010_01101001_10010100_10010010

// También hay etiquetas especiales para literales de enteros sin signo.
//
//Lea más sobre los literales para tipos de enteros sin signo.

// ------ Representación de números en la JVM -----

// En la plataforma JVM, los números se almacenan como tipos primitivos: int, doble, etc.
// Las excepciones son los casos en los que se crea una referencia de número anulable,
// como Int. O usar genéricos. En estos casos, los números están en caja en las clases
// Java Entero, Doble, et.
//
//Las referencias nulas al mismo número pueden referirse a diferentes objetos:

    val a: Int = 100
    val boxedA: Int? = a
    val anotherBoxedA: Int? = a

    val b: Int = 10000
    val boxedB: Int? = b
    val anotherBoxedB: Int? = b

    println(boxedA === anotherBoxedA) // true
    println(boxedB === anotherBoxedB) // false

//  Todas las referencias nulas a a son en realidad el mismo objeto debido a la optimización
//  de la memoria que JVM aplica a los enteros entre -128 y 127. No se aplica a las
//  referencias b, por lo que son objetos diferentes.
//
//Por otro lado, siguen siendo iguales:

    val b2: Int = 10000
    println(b == b) // Prints 'true'
    val boxedB2: Int? = b
    val anotherBoxedB2: Int? = b
    println(boxedB2 == anotherBoxedB2) // Prints 'true'

// ------- Conversiones de números explícitos -------
// Debido a las diferentes representaciones, los tipos más pequeños no son subtipos
// de los más grandes. Si lo fueran, tendríamos problemas del siguiente tipo


// Hypothetical code, does not actually compile:
    val a2: Int? = 1 // A boxed Int (java.lang.Integer)
    val b3: Long? = a2 // Implicit conversion yields a boxed Long (java.lang.Long)
    print(b2 == a2) // Surprise! This prints "false" as Long's equals() checks whether the other is Long as well

// Así que la igualdad se habría perdido en silencio, por no mencionar la identidad.
//
//Como consecuencia, los tipos más pequeños NO se convierten implícitamente en tipos
// más grandes. Esto significa que asignar un valor de tipo Byte a una variable Int
// requiere una conversión explícita:

    val b4: Byte = 1 // OK, literals are checked statically
// val i: Int = b // ERROR
    val i1: Int = b4.toInt()

// Todos los tipos de números admiten conversiones a otros tipos:
//
//toByte(): Byte
//
//toShort(): Corto
//
//toInt(): Int
//
//toLong(): Largo
//
//toFloat(): Float
//
//toDouble(): Doble
//
//En muchos casos, no hay necesidad de conversiones explícitas porque el tipo se infiere
// del contexto, y las operaciones aritméticas están sobrecargadas para las conversiones
// apropiadas, por ejemplo:

    val l = 1L + 3 // Long + Int => Long

// ------- Operaciones en números -------

// Kotlin admite el conjunto estándar de operaciones aritméticas sobre números: +, -, *, /, %.
// Son declarados como miembros de las clases apropiadas:

    println(1 + 2)
    println(2_500_000_000L - 1L)
    println(3.14 * 2.71)
    println(10.0 / 3)

// También puede anular estos operadores para las clases personalizadas. Consulte Sobrecarga del
// operador para más detalles.

// **** División de números enteros ****

// La división entre números enteros siempre devuelve un número entero. Cualquier parte fraccionada
// se descarta.

    val r = 5 / 2
//println(x == 2.5) // ERROR: Operator '==' cannot be applied to 'Int' and 'Double'
    println(r == 2)

// Esto es cierto para una división entre dos tipos de enteros:

    val x = 5L / 2
    println(x == 2L)

// Para devolver un tipo de coma flotante, convierta explícitamente uno de los argumentos en un tipo
// de coma flotante:

    val o = 5 / 2.toDouble()
    println(o == 2.5)

// ----- Operaciones en bits -----

// Kotlin proporciona un conjunto de operaciones por bits en números enteros. Operan a nivel
// binario directamente con bits de la representación de los números. Las operaciones por bits están
// representadas por funciones que se pueden llamar en forma de infijo. Se pueden aplicar solo a Int y Long:

    val s = (1 shl 2) and 0x000FF000

// Aquí está la lista completa de operaciones por bits:
//
//Shl (bits) - cambio firmado a la izquierda
//
//Shr(bits) - cambio firmado a la derecha
//
//Ushr(bits) - cambio sin firmar a la derecha
//
//Y(bits) - bit a bit Y
//
//O (bits) - bit a bit O
//
//Xor(bits) - XOR a bits
//
//Inv() - inversión por bits

// ----- Comparación de números de coma flotante -----

// Las operaciones sobre los números de coma flotante que se discuten en esta sección son:
//
//Comprobaciones de igualdad: a == b y a ! = b
//
//Operadores de comparación: a < b, a > b, a <= b, a >= b
//
//Instanciación de rango y comprobaciones de rango: a..b, x en a..b, x ! En a..b
//
//Cuando se sabe que los operandos a y b son estáticamente flotantes o dobles o sus contrapartes
// anulables (el tipo se declara o se infiere o es el resultado de un lanzamiento inteligente),
// las operaciones sobre los números y el rango que forman siguen el estándar IEEE 754 para la
// aritmética de coma flotante.
//
//Sin embargo, para admitir casos de uso genéricos y proporcionar un orden total, el comportamiento
// es diferente para los operandos que no se escriben estáticamente como números de coma flotante.
// Por ejemplo, tipos Any, Comparable<...> o Collection<T>. En este caso, las operaciones utilizan
// las implementaciones de iguales y compareTo para Float y Double. Como resultado:
//
//NaN se considera igual a sí mismo
//
//NaN se considera mayor que cualquier otro elemento, incluyendo POSITIVE_INFINITY
//
//-0.0 se considera inferior a 0.0
//
//Aquí hay un ejemplo que muestra la diferencia de comportamiento entre los operandos tipados
// estáticamente como números de coma flotante (Double.NaN) y los operandos no escritos estáticamente
// como números de coma flotante (listOf(T)).

// Operand statically typed as floating-point number
    println(Double.NaN == Double.NaN)                 // false
// Operand NOT statically typed as floating-point number
// So NaN is equal to itself
    println(listOf(Double.NaN) == listOf(Double.NaN)) // true

// Operand statically typed as floating-point number
    println(0.0 == -0.0)                              // true
// Operand NOT statically typed as floating-point number
// So -0.0 is less than 0.0
    println(listOf(0.0) == listOf(-0.0))              // false

    println(listOf(Double.NaN, Double.POSITIVE_INFINITY, 0.0, -0.0).sorted())
// [-0.0, 0.0, Infinity, NaN]


}