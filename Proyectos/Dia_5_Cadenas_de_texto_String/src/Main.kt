// ------- String -------

// Las cadenas en Kotlin están representadas por el tipo String

// *********** Info ***********
// En la JVM, un objeto de tipo String en codificación UTF-16
// utiliza aproximadamente 2 bytes por carácter.

// Por lo general, un valor de cadena es una secuencia de caracteres
// entre comillas dobles ("):

fun main() {

    val str = "abcd 123"

// Los elementos de una cadena son caracteres a los que se puede acceder
// a través de la operación de indexación: s[i]. Puedes repetir estos
// caracteres con un bucle for:
    for (c in str){
        print(c)
    }

// Las cuerdas son inmutables. Una vez que inicializas una cadena,
// no puedes cambiar su valor ni asignarle un nuevo valor.
// Todas las operaciones que transforman cadenas devuelven sus resultados
// en un nuevo objeto String, dejando la cadena original sin cambios:
    val str = "abcd"

// Crea e imprime un nuevo objeto de cadena
    println(str.uppercase())
// ABCD

// La cadena original sigue siendo la misma
    println(str)
// abcd

// Para concatenar cadenas, use el operador +. Esto también funciona para
// concatenar cadenas con valores de otros tipos, siempre y cuando el primer
// elemento de la expresión sea una cadena:

    val s = "abc" + 1
    println(s + "def")
// abc1def


// *********** Info ***********
// En la mayoría de los casos, es preferible usar plantillas de cadenas o
// cadenas multilíneas a la concatenación de cadenas.

// ------------ Cadenas de texto Literales ------------
// Kotlin tiene dos tipos de literales de cadena:

//.- Escaped strings:
//   Puede contener caracteres escapados.
//   Aquí hay un ejemplo de una cadena escapada:

    val st = "Hello, world!\n"

//   Escapar se realiza de la manera convencional, con una barra invertida (\).
//   Consulte la página de caracteres para ver la lista de secuencias de escape compatibles.

//.- Multiline strings:
//   Puede contener nuevas líneas y texto arbitrario. Está delimitado por una comilla
//   triple ("""), no contiene escape y puede contener nuevas líneas y cualquier otro
//   carácter:

    val text = """
    for (c in "foo")
        print(c)
    """
//   Para eliminar el espacio en blanco principal de las cadenas de varias líneas, utilice
//   la función trimMargin():
    val text = """
    |Tell me and I forget.
    |Teach me and I remember.
    |Involve me and I learn.
    |(Benjamin Franklin)
    """.trimMargin()

//   De forma predeterminada, un símbolo de tubería | se utiliza como prefijo de margen,
//   pero puedes elegir otro carácter y pasarlo como parámetro, como trimMargin(">").


// ------------ String templates ------------

// Los literales de cadena pueden contener expresiones de plantilla, piezas de código que
// se evalúan y cuyos resultados se concatenan en la cadena. Una expresión de plantilla
// comienza con un signo de dólar ($) y consiste en un nombre:

    val i = 10
    println("i = $i")
    // i = 10

// O una expresión en llaves rizadas:
    val ste = "abc"
    println("$s.length is ${s.length}")
    // abc.length is 3

// Puedes usar plantillas tanto en cadenas multilínea como en cadenas de escape.
// Para insertar el signo de dólar $ en una cadena multilínea (que no admite el
// escape de la barra invertida) antes de cualquier símbolo, que está permitido
// como comienzo de un identificador, utilice la siguiente sintaxis:

    val price = """
    ${'$'}_9.99
     """


 // ------------ String formatting ------------

// *********** Info ***********
// El formato de cadena con la función String.format() solo está disponible en Kotlin/JVM.

// Para dar formato a una cadena según sus requisitos específicos,
// utilice la función String.format().

// La función String.format() acepta una cadena de formato y uno o más argumentos.
// La cadena de formato contiene un marcador de posición (indicado por %) para un
// argumento dado, seguido de especificadores de formato. Los especificadores de
// formato son instrucciones de formato para el argumento respectivo, que consisten
// en banderas, ancho, precisión y tipo de conversión. Colectivamente, los especificadores
// de formato dan forma al formato de la salida. Los especificadores de formato comunes
// incluyen %d para números enteros, %f para números de coma flotante y %s para cadenas.
// También puede usar la sintaxis argument_index$ para hacer referencia al mismo argumento
// varias veces dentro de la cadena de formato en diferentes formatos.

// *********** Info ***********
// Para obtener una comprensión detallada y una extensa lista de especificadores de formato,
// consulte la documentación del formato de Java.

// Echemos un vistazo a un ejemplo:

// Ormats un entero, añadiendo ceros iniciales para alcanzar una longitud de siete caracteres
    val integerNumber = String.format("%07d", 31416)
    println(integerNumber)
// 0031416

// Formatea un número de coma flotante para mostrar con un signo + y cuatro decimales
    val floatNumber = String.format("%+.4f", 3.141592)
    println(floatNumber)
// +3.1416

// Da formato a dos cadenas en mayúsculas, cada una con un marcador de posición
    val helloString = String.format("%S %S", "hello", "world")
    println(helloString)
// HELLO WORLD

// Formatea un número negativo que se incluirá entre paréntesis, luego repite el mismo
// número en un formato diferente (sin paréntesis) usando `argument_index$`.
    val negativeNumberInParentheses = String.format("%(d means %1\$d", -31416)
    println(negativeNumberInParentheses)
//(31416) means -31416

// La función String.format() proporciona una funcionalidad similar a las plantillas de
// cadenas. Sin embargo, la función String.format() es más versátil porque hay más opciones
// de formato disponibles.

// Además, puede asignar la cadena de formato desde una variable. Esto puede ser útil cuando
// la cadena de formato cambia, por ejemplo, en casos de localización que dependen de la
// configuración regional del usuario.

// Tenga cuidado al usar la función String.format() porque puede ser fácil desigualar el
// número o la posición de los argumentos con sus marcadores de posición correspondientes.
}