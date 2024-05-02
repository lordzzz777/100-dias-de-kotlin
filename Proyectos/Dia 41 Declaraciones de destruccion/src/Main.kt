// ------------ Declaraciones de destrucción ------------

// A veces es conveniente desestructurar un objeto en una serie de variables,
// por ejemplo:

// val (name, age) = person

// Esta sintaxis se llama declaración de desestructuración. Una declaración de
// desestructuración crea varias variables a la vez. Has declarado dos nuevas
// variables: nombre y edad, y puedes usarlas de forma independiente:

/*
println(name)
println(age)
 */


// Se compila una declaración de desestructuración con el siguiente código:


/*
val name = person.component1()
val age = person.component2()
 */

// Las funciones component1() y component2() son otro ejemplo del principio de
// las convenciones ampliamente utilizadas en Kotlin (ver operadores como + y *,
// for-loops como ejemplo). Cualquier cosa puede estar en el lado derecho de una
// declaración de desestructuración, siempre y cuando se pueda llamar al número
// requerido de funciones de componentes. Y, por supuesto, puede haber component3()
// y component4() y así sucente.

// ************************************* Info **************************************
// Las funciones componentN() deben estar marcadas con la palabra clave del operador
// para permitir su uso en una declaración de destrucción
// *********************************************************************************

// Las declaraciones de destrucción también funcionan en for-loops:

/*
for ((a, b) in collection) { ... }
 */

// Las variables a y b obtienen los valores devueltos por component1() y component2()
// llamados a los elementos de la colección.

// ------------ Ejemplo: devolver dos valores de una función ------------

// Supongamos que necesita devolver dos cosas de una función, por ejemplo, un objeto de
// resultado y un estado de algún tipo. Una forma compacta de hacer esto en Kotlin es
// declarar una clase de datos y devolver su instancia:

/*
data class Result(val result: Int, val status: Status)
fun function(...): Result {
    // computations

    return Result(result, status)
}

// Now, to use this function:
val (result, status) = function(...)
 */

// Dado que las clases de datos declaran automáticamente las funciones componentN(),
// las declaraciones de desestructuración funcionan aquí.

// ************************************* Info **************************************
// También podría usar la clase Pair y hacer que function() devuelva
// Pair<Int, Status>, pero a menudo es mejor tener sus datos nombrados
// correctamente.
// *********************************************************************************

// ------------ Ejemplo: desestructuración de declaraciones y mapas ------------

// Probablemente la mejor manera de atravesar un mapa es esta:
/*
for ((key, value) in map) {
   // do something with the key and the value
}
 */

// Para que esto funcione, deberías

// Presente el mapa como una secuencia de valores proporcionando una función iterator().

// Presente cada uno de los elementos como un par proporcionando las funciones component1()
// y component2().

// Y, de hecho, la biblioteca estándar proporciona tales extensiones:

/*
operator fun <K, V> Map<K, V>.iterator(): Iterator<Map.Entry<K, V>> = entrySet().iterator()
operator fun <K, V> Map.Entry<K, V>.component1() = getKey()
operator fun <K, V> Map.Entry<K, V>.component2() = getValue()
 */

// Por lo tanto, puede usar libremente declaraciones de desestructuración en for-loops con
// mapas (así como colecciones de instancias de clase de datos o similares).

// ------- Subrayado para variables no utilizadas -------

// Si no necesita una variable en la declaración de desestructuración, puede colocar un guión
// bajo en lugar de su nombre:

// val (_, status) = getResult()

// Las funciones del operador componentN() no se llaman para los componentes que se omiten de
// esta manera.

// ------------ Destrucción en lambdas ------------

// Puedes usar la sintaxis de declaraciones de desestructuración para los parámetros lambda.
// Si un lambda tiene un parámetro del tipo de par (o Map.Entry, o cualquier otro tipo que
// tenga las funciones componentN apropiadas), puede introducir varios parámetros nuevos en
// lugar de uno poniéndolos entre paréntesis:

/*
map.mapValues { entry -> "${entry.value}!" }
map.mapValues { (key, value) -> "$value!" }
 */

// Tenga en cuenta la diferencia entre declarar dos parámetros y declarar un par de
// desestructuración en lugar de un parámetro:

/*
{ a -> ... } // one parameter
{ a, b -> ... } // two parameters
{ (a, b) -> ... } // a destructured pair
{ (a, b), c -> ... } // a destructured pair and another parameter
 */

// Si un componente del parámetro desestructurado no se utiliza, puede reemplazarlo
// con el guión bajo para evitar inventar su nombre:

// map.mapValues { (_, value) -> "$value!" }

// Puede especificar el tipo para todo el parámetro desestructurado o para un componente
//  específico por separado:

/*
map.mapValues { (_, value): Map.Entry<Int, String> -> "$value!" }

map.mapValues { (_, value: String) -> "$value!" }
 */

fun main() {

}