// ------- Comprobaciones de tipo y lanzamientos -------


// En Kotlin, puedes realizar comprobaciones de tipo para comprobar el tipo de un
// objeto en tiempo de ejecución. Los lanzamientos de tipo convierten los objetos
// en un tipo diferente.


// ************ Info ************
// Para aprender específicamente sobre las comprobaciones y lanzamientos de tip
// o genéricos, por ejemplo, List<T>, Map<K,V>, consulte Comprobaciones y
// lanzamientos de tipo genéricos.


// ¡Es y! Son operadores

// ¡Usa el operador o su forma negada! Es realizar una comprobación en tiempo de
// ejecución que identifique si un objeto se ajusta a un tipo determinado:

/*
if (obj is String) {
    print(obj.length)
}

if (obj !is String) {  // Same as !(obj is String)
    print("Not a String")
} else {
    print(obj.length)
}
 */

fun main() {
// ------- Los moldes inteligentes -------


    fun demo(x: Any) {
        if (x is String) {
            print(x.length) // x is automatically cast to String
        }
    }

 // El compilador es lo suficientemente inteligente como para saber que un reparto
// es seguro si una comprobación negativa conduce a un retorno:

    if (x !is String) return

    print(x.length) // x is automatically cast to String

 // O si está en el lado derecho de && o || y la comprobación adecuada
// (regular o negativa) está en el lado izquierdo:

    // x is automatically cast to String on the right-hand side of `||`
    if (x !is String || x.length == 0) return

    // x is automatically cast to String on the right-hand side of `&&`
    if (x is String && x.length > 0) {
        print(x.length) // x is automatically cast to String
    }

// Los lanzamientos inteligentes también funcionan para las expresiones when y
// los bucles while:

    when (x) {
        is Int -> print(x + 1)
        is String -> print(x.length + 1)
        is IntArray -> print(x.sum())
    }


// *************** Atención ***************
// Tenga en cuenta que los lanzamientos inteligentes solo funcionan cuando el
// compilador puede garantizar que la variable no cambiará
// entre la comprobación y su uso.

// Los moldes inteligentes se pueden utilizar en las siguientes condiciones:

// val local variables                 Siempre, excepto las propiedades delegadas locales.

// Propiedades val                     Si la propiedad es privada, interna o si la comprobación
//                                     se realiza en el mismo módulo donde se declara la propiedad.
//                                     Los moldes inteligentes no se pueden usar en propiedades
//                                     abiertas o propiedades que tengan getters personalizados.
// Var variables locales               Si la variable no se modifica entre la comprobación
//                                     y su uso, no se captura en un lambda que la modifica
//                                     y no es una propiedad delegada local.
// Var properties                      Nunca, porque la variable se puede modificar en cualquier
//                                     momento mediante otro código.


// ------- "Unsafe" cast operator -------

// Por lo general, el operador de reparto lanza una excepción si el reparto no es posible.
// Por lo tanto, se llama inseguro. El molde inseguro en Kotlin es realizado por el operador
// de infix como.

    val x: String = y as String


// Tenga en cuenta que null no se puede lanzar a String, ya que este tipo no se puede anular.
// Si y es nulo, el código anterior lanza una excepción. Para hacer que el código como este
// sea correcto para los valores nulos, utilice el tipo nulo en el lado derecho del reparto:

    val x: String? = y as String?

// ------- Operador de reparto "seguro" (anulado) -------

// Para evitar excepciones, utilice el operador de fundición segura como?,
// que devuelve nulo en caso de error.

    val x: String? = y as? String

// Tenga en cuenta que a pesar del hecho de que el lado derecho de como? Es un tipo de cadena
// no nulo, el resultado del lanzamiento es nulo.



}