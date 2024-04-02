// ------------ Paquetes e importaciones ------------

// Un archivo fuente puede comenzar con una declaración de paquete:

fun main() {
/*
    package org.example

    fun printMessage() { /*...*/ }
    class Message { /*...*/ }
// ...
*/
    
// Todos los contenidos, como clases y funciones, del archivo de
// origen están incluidos en este paquete. Así que, en el ejemplo
// anterior, el nombre completo de printMessage()es org.example.
// printMessage, y el nombre completo de Message es org.example.Message.

//Si el paquete no se especifica, el contenido de dicho archivo pertenece
// al paquete por defecto sin nombre.

// ------- Importaciones por defecto -------

// Una serie de paquetes se importan en cada archivo de Kotlin por defecto:
//
//    kotlin.*
//
//    kotlin. anotación.*
//
//    kotlin.collections.*
//
//    kotlin.comparisons.*
//
//    kotlin.io.*
//
//    kotlin.ranges.*
//
//    kotlin.sequencias.*
//
//    kotlin.text.*
//
// Los paquetes adicionales se importan en función de la plataforma de destino:
//
//     JVM:
//
//        java.lang.*
//
//        kotlin.jvm.*
//
//    JS:
//
//        kotlin.js.*


// ------- Importaciones -------

// Aparte de las importaciones por defecto, cada archivo puede contener su propio
// import directivas.

//Puede importar un solo nombre:
//     import org.example.Message // Message is now accessible without qualification

// o todo el contenido accesible de un ámbito de aplicación: paquete, clase, objeto, etc.:

//    import org.example.* // everything in 'org.example' becomes accessible

// Si hay un choque de nombre, puedes desambiguate usando as palabra clave para cambiar
// el nombre local de la entidad chocante:

/*
import org.example.Message // Message is accessible
import org.test.Message as TestMessage // TestMessage stands for 'org.test.Message'
*/

// El importLa palabra clave no se limita a la importación de clases; también se puede
// utilizar para importar otras declaraciones:
//
//    Funciones y propiedades de primer nivel
//
//    funciones y propiedades declaradas en declaraciones de objetos
//
//    enum constantes

// ------- Visibilidad de las declaraciones de primer nivel -------

// Si se marca una declaración de primer nivel private, es privado al archivo en el que
// se declara (ver modificadores de visibilidad).
}