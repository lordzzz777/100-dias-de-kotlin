// ------------ Arrays ------------
//Editar páginaModificado por última vez: 20 de Septiembre

//Un array es una estructura de datos que contiene un número fijo de valores
// del mismo tipo o de sus subtipos. El tipo más común de matriz en Kotlin es
// la matriz de tipo objeto, representada por el Arrayclase.

// ************ Info ************
//Si usas primitivos en una matriz tipo objeto, esto tiene un impacto de rendimiento
// porque tus primitivos están en caja en objetos. Para evitar el boxeo por encima,
// utilice matrices de tipo primitivo en su lugar.


// ------------ Cuándo usar arrays ------------

//Utiliza arrays en Kotlin cuando tengas requisitos especializados de bajo nivel que
// necesites cumplir. Por ejemplo, si tienes requisitos de rendimiento más allá de lo
// que se necesita para aplicaciones regulares, o necesitas construir estructuras de
// datos personalizadas. Si no tienes este tipo de restricciones, usa colecciones en
// su lugar.

// Las colecciones tienen las siguientes prestaciones en comparación con los arrays:

// Las colecciones se pueden leer solo, lo que le da más control y le permite escribir
// un código robusto que tenga una intención clara.

// Es fácil añadir o eliminar elementos de las colecciones. En comparación,
// los arrays se fijan en tamaño. La única manera de añadir o eliminar elementos de un
// array es crear una nueva matriz cada vez, lo cual es muy ineficiente:

fun main() {

    var riversArray = arrayOf("Nile", "Amazon", "Yangtze")
// Using the += assignment operation creates a new riversArray,
// copies over the original elements and adds "Mississippi"
    riversArray += "Mississippi"
    println(riversArray.joinToString())
// Nile, Amazon, Yangtze, Mississippi

//.- Puede utilizar el operador de igualdad (==) para comprobar si las colecciones son
//   estructuralmente iguales. No puedes usar este operador para arrays. En su lugar,
//   tienes que usar una función especial, que puedes leer más en Comparar arrays.

// Para más información sobre las colecciones, consulte Colección panorama.

// ------------ Crear arrays ------------

// Para crear arrays en Kotlin, puede utilizar:
//
//    funciones, tales como arrayOf(), arrayOfNulls()o o emptyArray().
//
//    el ArrayConstructor.
//
// Este ejemplo utiliza la arrayOf()función y pasa los valores de los elementos a ella:
// Creates an array with values [1, 2, 3]
    val simpleArray = arrayOf(1, 2, 3)
    println(simpleArray.joinToString())
// 1 y 2, 3

// Este ejemplo utiliza la función arrayOfNulls() para crear una matriz de un tamaño dado
// lleno de elementos nulos:

    // Crea una matriz con valores [null, null, null]
    val nullArray: Array<Int?> = arrayOfNulls(3)
    println(nullArray.joinToString())
    // null, null, null

// Este ejemplo utiliza la función emptyArray() para crear una matriz vacía:
    var exampleArray = emptyArray<String>()

// ************ Info ************
// Puede especificar el tipo de la matriz vacía en el lado izquierdo o derecho
// de la asignación debido a la inferencia de tipo de Kotlin.
// Por ejemplo:
//    var exampleArray = emptyArray<String>()
//
//    var exampleArray: Array<String> = emptyArray()

// El constructor de la matriz toma el tamaño de la matriz y una función que devuelve valores
// para los elementos de la matriz dado su índice:

// Crea una matriz<Int> que se inicializa con ceros [0, 0, 0]
    val initArray = Array<Int>(3) { 0 }
    println(initArray.joinToString())
// 0, 0, 0

// Crea una matriz<Cadena> con valores ["0", "1", "4", "9", "16"]
    val asc = Array(5) { i -> (i * i).toString() }
    asc.forEach { print(it) }
// 014916

// ************ Info ************
// Como en la mayoría de los lenguajes de programación,
// los índices comienzan desde 0 en Kotlin.

// ***** Nested arrays *****

// Las matrices se pueden anidar entre sí para crear matrices multidimensionales:

// Crea una matriz bidimensional
    val twoDArray = Array(2) { Array<Int>(2) { 0 } }
    println(twoDArray.contentDeepToString())
// [[0, 0], [0, 0]]

// Creates a three-dimensional array
    val threeDArray = Array(3) { Array(3) { Array<Int>(3) { 0 } } }
    println(threeDArray.contentDeepToString())
// [[[0, 0, 0], [0, 0, 0], [0, 0, 0]], [[0, 0, 0], [0, 0, 0], [0, 0, 0]], [[0, 0, 0], [0, 0, 0], [0, 0, 0]]]

// ************ Info ************
// Las matrices anidadas no tienen que ser del mismo tipo o tamaño.

// ------------ Acceder y modificar elementos ------------

// Las matrices siempre son mutables. Para acceder y modificar elementos de una matriz,
// utilice el operador de acceso indexado[]:

    val simpleArray3 = arrayOf(1, 2, 3)
    val twoDArray3 = Array(2) { Array<Int>(2) { 0 } }

// Accesses the element and modifies it
    simpleArray3[0] = 10
    twoDArray3[0][0] = 2

// Prints the modified element
    println(simpleArray3[0].toString()) // 10
    println(twoDArray3[0][0].toString()) // 2

// Las matrices en Kotlin son invariantes. Esto significa que Kotlin no le permite asignar una
// matriz<String> a una matriz<Any> para evitar un posible fallo en el tiempo de ejecución.
// En su lugar, puedes usar Array<out Any>. Para obtener más información, consulte Tipo de proyecciones.

// ------- Trabajar con matrices -------

// En Kotlin, puede trabajar con matrices usándolas para pasar un número variable de argumentos a una
// función o realizar operaciones en las propias matrices. Por ejemplo, comparar matrices, transformar
// su contenido o convertirlas en colecciones.


// **** Pasa el número variable de argumentos a una función

// En Kotlin, puedes pasar un número variable de argumentos a una función a través del parámetro vararg.
// Esto es útil cuando no sabes el número de argumentos de antemano, como al formatear un mensaje o crear
// una consulta SQL.

// Para pasar una matriz que contiene un número variable de argumentos a una función, utilice el operador
// de propagación (*). El operador de propagación pasa cada elemento de la matriz como argumentos individuales
// a la función elegida:

    fun main() {
        val lettersArray = arrayOf("c", "d")
        printAllStrings("a", "b", *lettersArray)
        // abcd
    }

    fun printAllStrings(vararg strings: String) {
        for (string in strings) {
            print(string)
        }
    }

// Para obtener más información, consulte Número variable de argumentos (varargs).

// *** Comparar matrices

// Para comparar si dos matrices tienen los mismos elementos en el mismo orden, utilice las funciones
// .contentEquals() y .contentDeepEquals():

    val simpleArray = arrayOf(1, 2, 3)
    val anotherArray = arrayOf(1, 2, 3)

// Compara el contenido de las matrices
    println(simpleArray.contentEquals(anotherArray))
// Verdadero

// Usando la notación infix, compara el contenido de las matrices después de un elemento
// Ha cambiado
    simpleArray[0] = 10
    println(simpleArray contentEquals anotherArray)
// falso

// ************ Atención ************
// ¡No uses la igualdad (==) y la desigualdad (! =) operadores para comparar el contenido
// de las matrices. Estos operadores comprueban si las variables asignadas apuntan al mismo objeto.
// Para obtener más información sobre por qué las matrices en Kotlin se comportan de esta manera,
// consulte nuestra publicación de blog.

// ***** Transformar matrices

// Kotlin tiene muchas funciones útiles para transformar matrices. Este documento destaca algunos,
// pero esta no es una lista exhaustiva. Para ver la lista completa de funciones,
// consulte nuestra referencia de la API.

//  ***** Suma

// Para devolver la suma de todos los elementos de una matriz, utilice la función .sum():

    val sumArray = arrayOf(1, 2, 3)

// Suma los elementos de la matriz
    println(sumArray.sum())
// 6


// ************ Info ************
// La función .sum() solo se puede utilizar con matrices de tipos de datos numéricos, como Int.

// ***** Shuffle

// Para mezclar aleatoriamente los elementos de una matriz, utilice la función .shuffle():
    val simpleArray2 = arrayOf(1, 2, 3)

 // Mezcla elementos [3, 2, 1]
    simpleArray2.shuffle()
    println(simpleArray.joinToString())

// Mezcla elementos de nuevo [2, 3, 1]
    simpleArray2.shuffle()
    println(simpleArray2.joinToString())


// ********* Convertir matrices en colecciones *********

// Si trabaja con diferentes API donde algunas usan matrices y otras usan colecciones, entonces
// puede convertir sus matrices en colecciones y viceversa.

// ********* Convertir en lista o conjunto *********

// Para convertir una matriz en una lista o conjunto, utilice las funciones .toList() y .toSet().

    val simpleArray4 = arrayOf("a", "b", "c", "c")

// Se convierte en un conjunto
    println(simpleArray4.toSet())
// [a, b, c]

// Convierte en una lista
    println(simpleArray4.toList())
// [a, b, c, c]


// ********* Convertir en mapa *********

// Para convertir una matriz en un mapa, utilice la función .toMap().

//Solo una matriz de pares<K,V> se puede convertir en un mapa. El primer valor de una instancia
// de par se convierte en una clave, y el segundo se convierte en un valor. Este ejemplo utiliza
// la notación de infijo para llamar a la función to para crear tuplas de par:

// Para convertir una matriz en un map, utilizar el .toMap()función.
//
// Sólo una serie de Pair<K,V>se puede convertir a una map. El primer valor de una Pairinstance
// se convierte en una clave, y la segunda se convierte en un valor. Este ejemplo utiliza la
// notación de infix para llamar a la tofunción para crear tuples de Pair:

    val pairArray = arrayOf("apple" to 120, "banana" to 150, "cherry" to 90, "apple" to 140)

// Converts to a Map
// The keys are fruits and the values are their number of calories
// Note how keys must be unique, so the latest value of "apple"
// overwrites the first
    println(pairArray.toMap())
// {apple=140, banana=150, cherry=90}

// ------------ Conjuntos de tipo primitivo ------------

// Si usas el Arrayclase con valores primitivos, estos valores se encajan en objetos.
// Como alternativa, puede utilizar matrices de tipo primitivo, que le permiten almacenar
// primitivos en una matriz sin el efecto secundario del boxeo por encima:

// Conjunto de tipo primitivo                          Equivalente en Java
// BooleanArray                                        boolean[]
// ByteArray                                           byte[]
// CharArray                                           char[]
// DoubleArray                                         double[]
// FloatArray                                          float[]
// IntArray                                            int[]
// LongArray                                           long[]
// ShortArray                                          short[]

//Estas clases no tienen relación de herencia con la Arrayclase, pero tienen el mismo conjunto de funciones y propiedades.

//Este ejemplo crea una instancia de la IntArrayclase:

// Creates an array of Int of size 5 with values
    val exampleArray5 = IntArray(5)
    println(exampleArray5.joinToString())
// 0, 0, 0, 0, 0

// ************ Info ************
// Para convertir matrices de tipo primitivo a los arrays de tipo objeto, utilice el .toTypedArray()función.
//
// Para convertir matrices de tipo objeto en matrices de tipo primitivo, utilice .toBooleanArray(),
// .toByteArray(), .toCharArray(), y así es así.
}