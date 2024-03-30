// ------------ Condiciones y bucles ------------

// ------- if se expresa -------

// En Kotlin, ifes una expresión: devuelve un valor. Por lo tanto, no hay
// operador ternar (condition ? then : else) porque iffunciona bien en este papel.

fun main() {
    var max = a
    if (a < b) max = b

// With else
    if (a > b) {
        max = a
    } else {
        max = b
    }

// As expression
    max = if (a > b) a else b

// You can also use `else if` in expressions:
    val maxLimit = 1
    val maxOrLimit = if (maxLimit > a) maxLimit else if (a > b) a else b

// Ramas de un ifla expresión puede ser bloques. En este caso, la última expresión
// es el valor de un bloque:

    val max = if (a > b) {
        print("Choose a")
        a
    } else {
        print("Choose b")
        b
    }

// Si estás usando ifcomo expresión, por ejemplo, para devolver su valor o asignarla
// a una variable, elsela rama es obligatoria

// ------- Cuando la expresión -------

//whendefine una expresión condicional con múltiples ramas. Es similar a la
// switchdeclaración en idiomas similares a C. Su forma simple se ve así.

    when (x) {
        1 -> print("x == 1")
        2 -> print("x == 2")
        else -> {
            print("x is neither 1 nor 2")
        }
    }

//  whencoincide con su argumento contra todas las ramas secuencialmente hasta
//  que se satisface alguna condición de rama.

//whenpuede ser utilizado como expresión o como declaración. Si se utiliza como
// expresión, el valor de la primera rama de juego se convierte en el valor de la
// expresión general. Si se utiliza como una declaración, se ignoran los valores
// de las ramas individuales. Idemos como con if, cada rama puede ser un bloque,
// y su valor es el valor de la última expresión en el bloque.

//El elsela rama se evalúa si ninguna de las otras condiciones de la rama se cumple.

//Si whense utiliza como expresión, la elsela rama es obligatoria, a menos que el
// compilador pueda probar que todos los casos posibles están cubiertos con
// condiciones de rama, por ejemplo, con enumentradas de clase y sealedsubtipos de clase).

    enum class Bit {
        ZERO, ONE
    }

    val numericValue = when (getRandomBit()) {
        Bit.ZERO -> 0
        Bit.ONE -> 1
        // 'else' is not required because all cases are covered
    }

// En whendeclaraciones, la elsela rama es obligatoria en las siguientes condiciones:

// whentiene un tema de una Boolean, enum, o sealedtipo, o sus contrapartes nulas.

// ramas de whenno cubra todos los casos posibles para este tema.

    enum class Color {
        RED, GREEN, BLUE
    }

    when (getColor()) {
        Color.RED -> println("red")
        Color.GREEN -> println("green")
        Color.BLUE -> println("blue")
        // 'else' is not required because all cases are covered
    }

    when (getColor()) {
        Color.RED -> println("red") // no branches for GREEN and BLUE
        else -> println("not red") // 'else' is required
    }

// Para definir un comportamiento común para múltiples casos, combine sus condiciones
// en una sola línea con una coma:

    when (x) {
        0, 1 -> print("x == 0 or x == 1")
        else -> print("otherwise")
    }
// Puedes usar expresiones arbitrarias (no sólo constantes) como condiciones de rama

    when (x) {
        s.toInt() -> print("s encodes x")
        else -> print("s does not encode x")
    }
// También puede comprobar un valor para ser ino o !inuna gama o una colección:
    when (x) {
        in 1..10 -> print("x is in the range")
        in validNumbers -> print("x is valid")
        !in 10..20 -> print("x is outside the range")
        else -> print("none of the above")
    }
// Otra opción es comprobar que un valor iso o !isde un tipo particular. Tenga en
// cuenta que, debido a los elencos inteligentes, puede acceder a los métodos y
// propiedades del tipo sin ningún chequeo adicional.

    fun hasPrefix(x: Any) = when(x) {
        is String -> x.startsWith("prefix")
        else -> false
    }
// whentambién se puede utilizar como reemplazo para una if- - elseifCaenda. Si no
// se proporciona ningún argumento, las condiciones de la rama son simplemente
// expresiones boolenas, y una rama se ejecuta cuando su condición es cierta:
    when {
        x.isOdd() -> print("x is odd")
        y.isEven() -> print("y is even")
        else -> print("x+y is odd")
    }
// Puede capturar cuando se somete en una variable usando la siguiente sintaxis:

    fun Request.getBody() =
        when (val response = executeRequest()) {
            is Success -> response.body
            is HttpError -> throw HttpException(response.status)
        }

// El alcance de la variable introducido en el momento en que el sujeto se limita
// al cuerpo de esto cuando.

// ------- Para bucles -------

// El forloop iterates a través de cualquier cosa que proporcione un iterador.
// Esto es equivalente a la foreachen idiomas como C. La sintaxis de fores el siguiente:

    for (item in collection) print(item)

// Como se ha mencionado anteriormente, foritera a través de cualquier cosa que proporcione un iterador. Esto significa que:
//
//    tiene un miembro o una función de extensión iterator()que devuelve Iterator<>, que:
//
//        tiene un miembro o una función de extensión next()
//
//        tiene un miembro o una función de extensión hasNext()que devuelve Boolean.
//
//Todas estas tres funciones deben marcarse como operator.
//
//Para iterarar en una gama de números, utilice una expresión de rango:

    for (i in 1..3) {
        println(i)
    }
    for (i in 6 downTo 0 step 2) {
        println(i)
    }

// A forEl bucle sobre un rango o una matriz se compila a un bucle basado en el
// índice que no crea un objeto iterador.
//
//Si quieres iterar a través de un array o una lista con un índice, puedes
// hacerlo de esta manera:

    for (i in array.indices) {
        println(array[i])
    }

// Alternativamente, puedes usar el withIndexFunción de la biblioteca:

    for ((index, value) in array.withIndex()) {
        println("the element at $index is $value")
    }

// ------- Mientras bucles -------

//  whiley do-whileLos bucles ejecutan su cuerpo continuamente mientras su condición
//  se satisface. La diferencia entre ellos es la condición de tiempo de control:
//
//    whilecomprueba la condición y, si está satisfecha, ejecuta el cuerpo y luego
//    vuelve a la comprobación de la condición.
//
//    do-whileejecuta el cuerpo y luego comprueba la condición. Si está satisfecho,
//    el bucle se repite. Así que, el cuerpo de do-whileejecuta al menos una vez
//    independientemente de la condición.

    while (x > 0) {
        x--
    }

    do {
        val y = retrieveData()
    } while (y != null) // y is visible here!

 // ------- Rompe y continúa en bucles -------

// Kotlin apoya a los tradicionales breaky continueoperadores en bucles. Ver Devoluciones
// y saltos.
}