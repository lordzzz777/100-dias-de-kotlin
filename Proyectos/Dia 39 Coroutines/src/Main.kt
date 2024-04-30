// ------------ Coroutines ------------

// La programación asíncrona o sin bloqueo es una parte importante
// del panorama del desarrollo. Al crear aplicaciones del lado del servidor,
// de escritorio o móviles, es importante proporcionar una experiencia que
// no solo sea fluida desde la perspectiva del usuario, sino también escalable
// cuando sea necesario.

// Kotlin resuelve este problema de una manera flexible al proporcionar soporte
// de co-rutina a nivel de idioma y delegar la mayor parte de la funcionalidad
// a las bibliotecas.

// Además de abrir las puertas a la programación asíncrona, las corrutinas también
// ofrecen una gran cantidad de otras posibilidades, como la concurrencia y los actores.

// ------------ Cómo empezar ------------

// ¿Nuevo en Kotlin? Echa un vistazo a la página de inicio.

// ------- Documentación -------

// Guía de co-rutinas

// Fundamentos

// Canal

// Contexto de la corrutina y despachadores

//Estado mutable compartido y concurrencia

// Flujo asíncrono

// ------- Tutoriales -------

// Técnicas de programación asíncrona

// Introducción a las corrutinas y canales

// Depurar corutinas usando IntelliJ IDEA

// Depurar Kotlin Flow usando IntelliJ IDEA - tutorial

// Probando las corrutinas de Kotlin en Android

// Proyectos de muestra

// Ejemplos y fuentes de kotlinx.coroutines

// Aplicación KotlinConf


fun main() {
    val name = "Kotlin"
    //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
    // to see how IntelliJ IDEA suggests fixing it.
    println("Hello, " + name + "!")

    for (i in 1..5) {
        //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
        // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
        println("i = $i")
    }
}