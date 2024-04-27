// ------------ Igualdad ------------

// En Kotlin, hay dos tipos de igualdad:

//Igualdad estructural (==) - una comprobación de la función equals()

// Igualdad referencial (===) - una comprobación de dos referencias que apuntan al mismo objeto

// ----------- Igualdad estructural ------------

// La igualdad estructural verifica si dos objetos tienen el mismo contenido o estructura.
// ¡La igualdad estructural es verificada por la operación == y su contraparte negada! =.
// Por convención, una expresión como a == b se traduce a:

// a?.equals(b) ?: (b === null)

// Si a no es nulo, llama a los iguales (¿Alguno?) Función. De lo contrario (a es nulo),
// comprueba que b es referencialmente igual a nulo:

/*
fun main() {
    var a = "hello"
    var b = "hello"
    var c = null
    var d = null
    var e = d

    println(a == b)
    // true
    println(a == c)
    // false
    println(c == e)
    // true
}
*/
// Tenga en cuenta que no tiene sentido optimizar su código cuando se compara con null
// explícitamente: un == null se traducirá automáticamente a un === null.

// En Kotlin, la función equals() es heredada por todas las clases de la clase Any.
// De forma predeterminada, la función equals() implementa la igualdad referencial.
// Sin embargo, las clases en Kotlin pueden anular la función equals() para proporcionar
// una lógica de igualdad personalizada y, de esta manera, implementar la igualdad estructural.

// Las clases de valores y las clases de datos son dos tipos específicos de Kotlin que
// anulan automáticamente la función equals(). Es por eso que implementan la igualdad
// estructural por defecto.

// Sin embargo, en el caso de las clases de datos, si la función equals() se marca como
// final en la clase principal, su comportamiento permanece sin cambios.

// Distintamente, las clases que no son de datos (aquellas que no se declaran con el
// modificador de datos) no anulan la función equals() de forma predeterminada.
// En su lugar, las clases que no son de datos implementan un comportamiento de igualdad
// referencial heredado de la clase Any. Para implementar la igualdad estructural,
// las clases que no son de datos requieren una lógica de igualdad personalizada para
// anular la función equals().

// Para proporcionar una implementación de verificación de iguales personalizada,
// anule los iguales (otro: ¿Cualquier?): Función booleana:

class Point(val x: Int, val y: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Point) return false

        // Compares properties for structural equality
        return this.x == other.x && this.y == other.y
    }
}

// *********************************+ Info **********************************************
// Al anular la función equals(), también debe anular la función hashCode() para mantener
// la coherencia entre la igualdad y el hash y garantizar un comportamiento adecuado de
// estas funciones.
// **************************************************************************************

// Las funciones con el mismo nombre y otras firmas (como iguales (otros: Foo)) no afectan
// a las comprobaciones de igualdad con los operadores == y ! =.

// La igualdad estructural no tiene nada que ver con la comparación definida por la interfaz
// Comparable<...>, por lo que solo una costumbre es igual a (¿Alguna?) La implementación
// puede afectar el comportamiento del operador.


// ------------ Igualdad referencial ----------

// La igualdad referencial verifica las direcciones de memoria de dos objetos para determinar
// si son la misma instancia.

// ¡La igualdad referencial es verificada por la operación === y su contraparte negada!
// ==. a === b evalúa como verdadero si y solo si a y b apuntan al mismo objeto:

fun main() {
    var a = "Hello"
    var b = a
    var c = "world"
    var d = "world"

    println(a === b)
    // true
    println(a === c)
    // false
    println(c === d)
    // true

}

// Para los valores representados por tipos primitivos en tiempo de ejecución
// (por ejemplo, Int), la comprobación de igualdad === es equivalente
// a la comprobación ==.

// -------------------------------------------------------------------------------------
// La igualdad referencial se implementa de manera diferente en Kotlin/JS. Para obtener
// más información sobre la igualdad, consulte la documentación de Kotlin/JS.
// -------------------------------------------------------------------------------------

// Igualdad de números de coma flotante

// Cuando se sabe que los operandos de una comprobación de igualdad son estáticamente
// flotantes o dobles (nulos o no), la comprobación sigue el estándar IEEE 754 para
// la aritmética de coma flotante.

// El comportamiento es diferente para los operandos que no se escriben estáticamente
// como números de coma flotante. En estos casos, se implementa la igualdad estructural.
// Como resultado, las comprobaciones con operandos no tipificados estáticamente como
// números de coma flotante difieren del estándar IEEE. En este escenario:

// NaN es igual a sí mismo

// NaN es mayor que cualquier otro elemento (incluyendo POSITIVE_INFINITY)

// -0.0 no es igual a 0.0

// Para obtener más información, consulte Comparación de números de coma flotante.


// ------------ Igualdad de matriz ------------

// Para comparar si dos matrices tienen los mismos elementos en el mismo orden, use
// contentEquals().

// Para obtener más información, consulte Comparar matrices.