
// -------- Booleanos --------

// El tipo booleano representa objetos booleanos que pueden tener dos valores:
// verdadero y falso.
// ¿Booleano tiene una contraparte nula declarada como Booleana?

// ****** Info ******
// En la JVM, los booleanos almacenados como el tipo booleano primitivo suelen usar 8 bits

// Las operaciones integradas en booleanos incluyen:

//.- || - disyunción (OR lógico)
//.- && – Conjunción (lógica And)
//.- ! – Negación (lógica NOT o lo contraria de)

// Por ejemplo:

fun main() {
    val myTrue: Boolean = true
    val myFalse: Boolean = false
    val boolNull: Boolean? = null

    println(myTrue || myFalse)
// verdadero
    println(myTrue && myFalse)
// falso
    println(!myTrue)
// falso
    println(boolNull)
// no tiene nada o es nulo

// Los operadores || y && trabajan con peza, lo que significa:
//
//Si el primer operando es verdadero, el operador || no evalúa el segundo operando.
//
//Si el primer operando es falso, el operador && no evalúa el segundo operando.

// ****** Info ******
// En la JVM, las referencias nulas a objetos booleanos están en caja en clases de Java,
// al igual que con los números.


// -------- Caracteres --------

// Están representados por el tipo Char. Los literales de caracteres van entre comillas
// simples: '1'.

// ****** Info ******
// On the JVM, a character stored as primitive type: char, represents a 16-bit Unicode character.

// Los caracteres especiales comienzan con una barra invertida que se escapa \. Se admiten las
// siguientes secuencias de escape:

// \t – tab
//\b – Retroceder un espacio
//\n – Nueva línea (LF)
//\r – Retorno del carro (CR)
//\' – Comillas simples
//\" – Comillas dobles
//\\ – Barra invertida
//\$ – Símbolo del dólar

// Para codificar cualquier otro carácter, utilice la sintaxis de la
// secuencia de escape Unicode: '\uFF00'.

    val aChar: Char = 'a'

    println(aChar)
    println('\n') // Imprime un carácter de nueva línea adicional
    println('\uFF00')

// Si un valor de variable de carácter es un dígito, puede convertirlo explícitamente en un
// número Int utilizando la función digitToInt().

// ****** Info ******
// En la JVM, los caracteres están en caja en las clases de Java cuando se necesita
// una referencia nula, al igual que con los números. La operación de boxeo no conserva
// la identidad.

}