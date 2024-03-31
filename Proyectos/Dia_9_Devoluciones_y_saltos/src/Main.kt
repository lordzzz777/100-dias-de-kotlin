// ------------ Devoluciones y saltos ------------

// Kotlin tiene tres expresiones de salto estructural:
//
//    returnpor defecto devuelve la función de cierre más cercana o función anónima.
//
//    breaktermina el bucle de cierre más cercano.
//
//    continueprocede al siguiente paso del bucle de cierre más cercano.
//
//Todas estas expresiones pueden ser usadas como parte de expresiones más grandes:

//      val s = person.name ?: return

// El tipo de estas expresiones es el tipo Nada.

// ------------ Rompe y continúa las etiquetas ------------

// Cualquier expresión en Kotlin puede estar marcada con una etiqueta.
// Las etiquetas tienen la forma de un identificador seguido por la @signo,
// como abc@o o fooBar@. Para etiquetar una expresión, sólo tienes que añadir
// una etiqueta delante de ella.

/*

loop@ for (i in 1..100) {
    // ...
}

  Ahora, podemos calificar a breaka continuecon una etiqueta:

  loop@ for (i in 1..100) {
    for (j in 1..100) {
        if (...) break@loop
    }
}
 */

// A breakcalificado con una etiqueta salta al punto de ejecución justo después del
// lazo marcado con esa etiqueta. A continueprocede a la siguiente iteración de ese bucle.

fun main() {
// ------- Vuelve a las etiquetas -------

// En Kotlin, las funciones se pueden anidar usando las características de las funciones,
// funciones locales y expresiones de objetos. Calificado returnnos permiten regresar de
// una función externa. El caso de uso más importante es volver de una expresión de lambda.
// Recuerde que cuando escribimos lo siguiente, la return-La expresión vuelve de la función
// de cierre más cercana - foo:

    fun foo() {
        listOf(1, 2, 3, 4, 5).forEach {
            if (it == 3) return // non-local return directly to the caller of foo()
            print(it)
        }
        println("this point is unreachable")
    }

// Tenga en cuenta que tales devoluciones no locales se apoyan sólo para las expresiones
// de lambda pasadas a funciones en línea. Para volver de una expresión lambda, etiquetarla
// y calificar la return:

    fun foo1() {
        listOf(1, 2, 3, 4, 5).forEach lit@{
            if (it == 3) return@lit // local return to the caller of the lambda - the forEach loop
            print(it)
        }
        print(" done with explicit label")
    }

// Ahora, regresa sólo de la expresión lambda. A menudo es más conveniente usar etiquetas
// implícitas, porque tal etiqueta tiene el mismo nombre que la función a la que se pasa
// la lambda.

    fun foo2() {
        listOf(1, 2, 3, 4, 5).forEach {
            if (it == 3) return@forEach // local return to the caller of the lambda - the forEach loop
            print(it)
        }
        print(" done with implicit label")
    }

// Alternativamente, puedes reemplazar la expresión lambda por una función anónima.
// A returnLa declaración en una función anónima regresará de la propia función anónima.

    fun foo3() {
        listOf(1, 2, 3, 4, 5).forEach(fun(value: Int) {
            if (value == 3) return  // local return to the caller of the anonymous function - the forEach loop
            print(value)
        })
        print(" done with anonymous function")
    }

// Tenga en cuenta que el uso de devoluciones locales en los tres ejemplos anteriores es
// similar al uso de continueen bucles regulares.

//No hay equivalente directo para break, pero se puede simular añadiendo otro lambda
// anidante y no-localmente regresando de ella:

    fun foo4() {
        run loop@{
            listOf(1, 2, 3, 4, 5).forEach {
                if (it == 3) return@loop // non-local return from the lambda passed to run
                print(it)
            }
        }
        print(" done with nested loop")
    }

//  Al devolver un valor, el analizador da preferencia a la rentabilidad cualificada:

 //   return@a 1
// Esto significa "volved vuelta 1en la etiqueta @a" en lugar de "devolved a una
// expresión etiquetada (@a 1)".
}