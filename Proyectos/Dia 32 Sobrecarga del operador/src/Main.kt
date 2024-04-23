// ------------ Sobrecarga del operador ------------

// Kotlin le permite proporcionar implementaciones personalizadas para
// el conjunto predefinido de operadores en tipos. Estos operadores
// tienen una representación simbólica predefinida (como + o *) y
// precedencia. Para implementar un operador, proporcione una función
// miembro o una función de extensión con un nombre específico para el
// tipo correspondiente. Este tipo se convierte en el tipo del lado
// izquierdo para las operaciones binarias y el tipo de argumento
// para las unarias.

// Para sobrecargar a un operador, marque la función correspondiente con
// el modificador del operador:

interface IndexedContainer {
    operator fun get(index: Int)
}

// Al anular las sobrecargas de su operador, puede omitir al operador:

class OrdersList: IndexedContainer {
    override fun get(index: Int) { /*...*/ }
}

// ------------ Operaciones unarias ------------

// ------- Operadores de prefijos unarios -------

// ------------------------------------------------------
// ||  Expresión            |    Traducido a           ||
// ------------------------------------------------------
// || +a                    |    a.unaryPlus()         ||
// ------------------------------------------------------
// || -a                    |    a.unaryMinus()        ||
// ------------------------------------------------------
// || !a                    |    a.not()               ||
// ------------------------------------------------------

// Esta tabla dice que cuando el compilador procesa, por ejemplo, una expresión
// +a, realiza los siguientes pasos:

// Determina el tipo de a, que sea T.

// Busca una función unaryPlus() con el modificador del operador y sin parámetros
// para el receptor T, lo que significa una función miembro o una función de extensión.

// Si la función está ausente o es ambigua, es un error de compilación.

// Si la función está presente y su tipo de retorno es R, la expresión +a tiene el tipo R.

// ******************************** Info **********************************
// Estas operaciones, así como todas las demás, están optimizadas para
// tipos básicos y no introducen la sobrecarga de las llamadas de
// función para ellas.
// ************************************************************************

// Como ejemplo, así es como puedes sobrecargar el operador unario menos:

data class Point(val x: Int, val y: Int)

operator fun Point.unaryMinus() = Point(-x, -y)

val point = Point(10, 20)

fun main() {
    println(-point)  // prints "Point(x=-10, y=-20)"
}

// ------- Incrementos y decrementos -------

// ------------------------------------------------------
// ||  Expresión            |    Traducido a           ||
// ------------------------------------------------------
// || a++                   |    a.inc() + see below   ||
// ------------------------------------------------------
// || a--                   |    a.dec() + see below   ||
// ------------------------------------------------------

// Las funciones inc() y dec() deben devolver un valor, que se asignará
// a la variable en la que se utilizó la operación ++ o --. No deberían
// mutar el objeto en el que se invocó el inc o dec.

// El compilador realiza los siguientes pasos para la resolución de un
// operador en el formulario postfix, por ejemplo, a++:

// Determina el tipo de a, que sea T.

// Busca una función inc() con el modificador de operador y sin parámetros,
// aplicable al receptor de tipo T.

// Comprueba que el tipo de retorno de la función es un subtipo de T.

// El efecto de calcular la expresión es:

// Almacene el valor inicial de a en un almacenamiento temporal a0.

// Asigna el resultado de a0.inc() a a.

// Devuelve a0 como resultado de la expresión.

// Para a... los pasos son completamente análogos.

// Para las formas de prefijo ++a y --a, la resolución funciona de la misma
// manera, y el efecto es:

// Asigna el resultado de a.inc() a a.

// Devuelve el nuevo valor de a como resultado de la expresión.

// ------------ Operaciones binarias ------------

// ------- Operadores aritméticos -------

// ------------------------------------------------------
// ||  Expresión            |    Traducido a           ||
// ------------------------------------------------------
// || a + b                 |    a.plus(b)             ||
// ------------------------------------------------------
// || a - b                 |   a.minus(b)             ||
// ------------------------------------------------------
// || a * b                 |    a.times(b)            ||
// ------------------------------------------------------
// || a / b                 |    a.div(b)              ||
// ------------------------------------------------------
// || a % b                 |    a.rem(b)              ||
// ------------------------------------------------------
// || a..b                  |    a.rangeTo(b)          ||
// ------------------------------------------------------
// || a..<b                 |    a.rangeUntil(b)       ||
// ------------------------------------------------------

// Para las operaciones de esta tabla, el compilador solo resuelve la expresión
// en la columna Traducido a.

// A continuación se muestra un ejemplo de clase Counter que comienza en un valor
// dado y se puede incrementar utilizando el operador + sobrecargado:

data class Counter(val dayIndex: Int) {
    operator fun plus(increment: Int): Counter {
        return Counter(dayIndex + increment)
    }
}

// ------- En el operador -------

// ------------------------------------------------------
// ||  Expresión            |    Traducido a           ||
// ------------------------------------------------------
// || a in b                |    b.contains(a)         ||
// ------------------------------------------------------
// || a !in b               |   !b.contains(a)         ||
// ------------------------------------------------------

// ¡Para dentro y! En el procedimiento es el mismo, pero el orden de los
// argumentos se invierte.


// ------- Operador de acceso indexado -------

// -------------------------------------------------------
// ||  Expresión            |    Traducido a            ||
// -------------------------------------------------------
// || a[i]                  |    a.get(i)               ||
// -------------------------------------------------------
// || a[i, j]               |   a.get(i, j)             ||
// -------------------------------------------------------
// || a[i_1, ..., i_n]      |    a.get(i_1, ..., i_n)   ||
// -------------------------------------------------------
// || a[i] = b              |    a.set(i, b)            ||
// -------------------------------------------------------
// || a[i, j] = b           |    a.set(i, j, b)         ||
// -------------------------------------------------------
// || a[i_1, ..., i_n] = b  |    a.set(i_1, ..., i_n, b)||
// -------------------------------------------------------

// Los corchetes se traducen en llamadas para obtener y establecer con el número
// apropiado de argumentos.

// ------- Invocar al operador -------

// -------------------------------------------------------
// ||  Expresión            |    Traducido a            ||
// -------------------------------------------------------
// || a()                   |    a.invoke()             ||
// -------------------------------------------------------
// || a(i)                  |   a.invoke(i)             ||
// -------------------------------------------------------
// || a(i, j)               |    a.invoke(i, j)         ||
// -------------------------------------------------------
// || a(i_1, ..., i_n)      |    a.invoke(i_1, ..., i_n)||
// -------------------------------------------------------

// Parentheses are translated to calls to invoke with appropriate number of arguments.


// ------- Asignaciones aumentadas -------

// -------------------------------------------------------
// ||  Expresión            |    Traducido a            ||
// -------------------------------------------------------
// || a += b                |    a.plusAssign(b)        ||
// -------------------------------------------------------
// || a -= b                |   a.minusAssign(b)        ||
// -------------------------------------------------------
// || a *= b                |    a.timesAssign(b)       ||
// -------------------------------------------------------
// || a /= b                |    a.divAssign(b)         ||
// -------------------------------------------------------
// || a %= b                |    a.remAssign(b)         ||
// -------------------------------------------------------

// Para las operaciones de asignación, por ejemplo a += b, el compilador realiza los
// siguientes pasos:

// Si la función de la columna de la derecha está disponible:

// Si la función binaria correspondiente (eso significa plus() para plusAssign())
// también está disponible, a es una variable mutable, y el tipo de retorno de plus
// es un subtipo del tipo de a, informe de un error (ambigüedad).

// Asegúrese de que su tipo de devolución sea Unidad e informe de un error de lo
// contrario.

// Generar código para a.plusAssign(b).

// De lo contrario, intente generar código para a = a + b (esto incluye una comprobación
// de tipo: el tipo de a + b debe ser un subtipo de a).

// ******************* Info ********************
// Las asignaciones NO son expresiones en Kotlin
// *********************************************

// ------- Operadores de igualdad y desigualdad -------

// ------------------------------------------------------------
// ||  Expresión           |     Traducido a                 ||
// ------------------------------------------------------------
// || a == b               | a?.equals(b) ?: (b === null)    ||
// ------------------------------------------------------------
// || a != b               | !(a?.equals(b) ?: (b === null)) ||
// ------------------------------------------------------------

// Estos operadores solo funcionan con la función iguales (otro: ¿Anguno?): Booleano,
// que se puede anular para proporcionar una implementación de verificación de igualdad
// personalizada. Cualquier otra función con el mismo nombre (como iguales (otro: Foo))
// no será llamada.

// ********************************** Info **********************************************
// === ¡y ! == (comprobaciones de identidad) no son sobrecargables, por lo que no existen
// convenciones para ellas
// ***************************************************************************************

// La operación == es especial: se traduce a una expresión compleja que selecciona
// null's. null == null siempre es verdadero, y x == null para un no-null x siempre
// es falso y no invocará x.equals().

// ------- Operadores de comparación -------

// -------------------------------------------------------
// ||  Expresión            |    Traducido a            ||
// -------------------------------------------------------
// || a > b                 |    a.compareTo(b) > 0     ||
// -------------------------------------------------------
// || a < b                 |   a.compareTo(b) < 0      ||
// -------------------------------------------------------
// || a >= b                |    a.compareTo(b) >= 0    ||
// -------------------------------------------------------
// || a <= b                |    a.compareTo(b) <= 0    ||
// -------------------------------------------------------

// Todas las comparaciones se traducen en llamadas a compareTo, que es necesario para
// devolver Int.

// ------- Operadores de delegación de propiedades -------

// Las funciones de operador provideDelegate, getValue y setValue se describen en
// Propiedades delegadas.

// ------------ Llamadas de infijo para funciones con nombre ------------

// Puedes simular operaciones de infix personalizadas utilizando llamadas a
// funciones de infix.