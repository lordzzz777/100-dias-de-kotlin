// ------------ Modificadores de visibilidad ------------

// Las clases, objetos, interfaces, constructores y funciones, así como las p
// ropiedades y sus configuradores, pueden tener modificadores de visibilidad.
// Los Getters siempre tienen la misma visibilidad que sus propiedades.

//Hay cuatro modificadores de visibilidad en Kotlin: privado, protegido,
// interno y público. La visibilidad predeterminada es pública.

//En esta página, aprenderás cómo se aplican los modificadores a los diferentes
// tipos de alcances de declaración.

// ------------ Paquetes ------------

// Las funciones, propiedades, clases, objetos e interfaces se pueden declarar
// en el "nivel superior" directamente dentro de un paquete:

// file name: example.kt
package foo

fun baz() {  }
class Bar {  }

// Si no utiliza un modificador de visibilidad, el público se utiliza de forma
// predeterminada, lo que significa que sus declaraciones serán visibles en todas partes.

//Si marca una declaración como privada, solo será visible dentro del archivo
// que contiene la declaración.

//Si lo marcas como interno, será visible en todas partes en el mismo módulo.

//El modificador protegido no está disponible para las declaraciones de nivel superior.

// ************ info ************
// Para usar una declaración de nivel superior visible de otro paquete, debe importarla.
// ******************************


// file name: example.kt
package foo

private fun foo() {  } // visible inside example.kt

public var bar: Int = 5 // property is visible everywhere
    private set         // setter is visible only in example.kt

internal val baz = 6    // visible inside the same module

// ------------ Miembros de la clase ------------

// Para los miembros declarados dentro de una clase:

//Privado significa que el miembro es visible solo dentro de esta clase (incluidos
// todos sus miembros).

//Protegido significa que el miembro tiene la misma visibilidad que uno marcado como
// privado, pero que también es visible en las subclases.

//Interno significa que cualquier cliente dentro de este módulo que vea la clase
// declarante ve a sus miembros internos.

//Público significa que cualquier cliente que vea la clase declarante ve a sus miembros
// públicos.


// ************ info ************
// En Kotlin, una clase externa no ve miembros privados de sus clases internas.
// ******************************

// Si anula a un miembro protegido o un miembro interno y no especifica explícitamente
// la visibilidad, el miembro anulador también tendrá la misma visibilidad que el original.

//Ejemplos:

open class Outer {
    private val a = 1
    protected open val b = 2
    internal open val c = 3
    val d = 4  // public by default

    protected class Nested {
        public val e: Int = 5
    }
}

class Subclass : Outer() {
    // a is not visible
    // b, c and d are visible
    // Nested and e are visible

    override val b = 5   // 'b' is protected
    override val c = 7   // 'c' is internal
}

class Unrelated(o: Outer) {
    // o.a, o.b are not visible
    // o.c and o.d are visible (same module)
    // Outer.Nested is not visible, and Nested::e is not visible either
}

// ------- Constructores -------

// Utilice la siguiente sintaxis para especificar la visibilidad del constructor
// principal de una clase:

// ************ info ************
// Necesitas añadir una palabra clave de constructor explícita.
// ******************************

class C private constructor(a: Int) {  }

// Aquí el constructor es privado. De forma predeterminada, todos los constructores
// son públicos, lo que efectivamente equivale a que sean visibles en todas partes
// donde la clase es visible (esto significa que un constructor de una clase interna
// solo es visible dentro del mismo módulo).

//Para las clases selladas, los constructores están protegidos de forma predeterminada.
// Para obtener más información, consulte Clases selladas.

// ------- Declaraciones locales -------

// Las variables, funciones y clases locales no pueden tener modificadores de visibilidad.

// ------------ Módulos ------------

// El modificador de visibilidad interna significa que el miembro es visible dentro del
// mismo módulo. Más específicamente, un módulo es un conjunto de archivos Kotlin compilados
// juntos, por ejemplo:

//Un módulo IntelliJ IDEA.

//Un proyecto de Maven.

//Un conjunto de fuentes de Gradle (con la excepción de que el conjunto de fuentes de prueba
// puede acceder a las declaraciones internas de principal).

//Un conjunto de archivos compilados con una invocación de la tarea <kotlinc> Ant.

fun main() {

}