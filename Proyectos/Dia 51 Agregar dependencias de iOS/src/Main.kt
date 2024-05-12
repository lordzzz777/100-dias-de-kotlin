// ------- Añadir dependencias de iOS -------

// Las dependencias de Apple SDK (como Foundation o Core Bluetooth) están disponibles
// como un conjunto de bibliotecas preconstruidas en los proyectos multiplataforma de
// Kotlin. No requieren ninguna configuración adicional.

//También puedes reutilizar otras bibliotecas y marcos del ecosistema de iOS en tus
// conjuntos de fuentes de iOS. Kotlin admite la interoperabilidad con las dependencias
// de Objective-C y las dependencias de Swift si sus API se exportan a Objective-C con
// el atributo @objc. Las dependencias de Pure Swift aún no son compatibles.
//
//La integración con el administrador de dependencias de CocoaPods también es compatible
// con la misma limitación: no se pueden usar pods Swift puros.

//Recomendamos usar CocoaPods para manejar las dependencias de iOS en los proyectos
// multiplataforma de Kotlin. Administre las dependencias manualmente solo si desea
// ajustar el proceso de interoperación específicamente o si tiene alguna otra razón
// sólida para hacerlo.

// ------- Con CocoaPods -------
//
// 1. Realice la configuración inicial de integración de CocoaPods.
//
// 2. Agregue una dependencia de una biblioteca de Pods del repositorio CocoaPods que
//    desea utilizar incluyendo la llamada a la función pod() en build.gradle(.kts)
//    de su proyecto.



 /*
    kotlin {
        cocoapods {
            //..
            pod("FirebaseAuth") {
                version = "10.16.0"
            }
        }
    }
*/

// Puedes añadir las siguientes dependencias a una biblioteca de Pods:

// * Desde el repositorio de CocoaPods

// * En una biblioteca almacenada localmente

// * Desde un repositorio Git personalizado

// * Desde un repositorio personalizado de Podspec

// 3. Con opciones de cinterop personalizadas

// Vuelva a importar el proyecto.

// Para usar la dependencia en su código Kotlin, importe el paquete cocoapods.<nombre de
// la biblioteca>. Para el ejemplo anterior, es:

//    import cocoapods.FirebaseAuth.*

// ------------ Sin CocoaPods ------------

// Si no quieres usar CocoaPods, puedes usar la herramienta cinterop para crear enlaces Kotlin
// para declaraciones Objective-C o Swift. Esto te permitirá llamarlos desde el código de Kotlin.

// Los pasos difieren un poco para las bibliotecas y los marcos, pero la idea sigue siendo la misma.

// 1. Descarga tu dependencia.

// 2. Constrúyelo para obtener sus binarios.

// 3. Crea un archivo .def especial que describa esta dependencia a cinterop.

// 4. Ajuste su script de compilación para generar enlaces durante la compilación.


// ------- Añade una biblioteca sin CocoaPods -------

// 1. Descarga el código fuente de la biblioteca y colócalo en algún lugar donde puedas hacer
//    referencia a él desde tu proyecto.

// 2. Construya una biblioteca (los autores de la biblioteca suelen proporcionar una guía sobre
//    cómo hacer esto) y obtenga una ruta a los binarios.

// 3. En tu proyecto, crea un archivo .def, por ejemplo, DateTools.def.

// 4. Añade una primera cadena a este archivo: language = Objective-C. Si desea utilizar una
//    dependencia C pura, omita la propiedad del idioma.

// 5. Proporcione valores para dos propiedades obligatorias:

//    * Los encabezados describen qué encabezados serán procesados por cinterop.

//    * Package establece el nombre del paquete en el que se deben poner estas declaraciones.

// Por ejemplo:
   // headers = DateTools.h
   // package = DateTools

// 6. Añade información sobre la interoperabilidad con esta biblioteca al script de compilación:

//  * Pase la ruta al archivo .def. Esta ruta se puede omitir si su archivo .def tiene el mismo
//    nombre que cinterop y se coloca en el directorio src/nativeInterop/cinterop/.

//  * Dígale a cinterop dónde buscar los archivos de encabezado usando la opción includeDirs.

//  * Configurar el enlace a los binarios de la biblioteca.

/*
kotlin {
    iosX64() {
        compilations.getByName("main") {
            val DateTools by cinterops.creating {
                // Path to .def file
                defFile("src/nativeInterop/cinterop/DateTools.def")

                // Directories for header search (an analogue of the -I<path> compiler option)
                includeDirs("include/this/directory", "path/to/another/directory")
            }
            val anotherInterop by cinterops.creating { /* ... */ }
        }

        binaries.all {
            // Linker options required to link to the library.
            linkerOpts("-L/path/to/library/binaries", "-lbinaryname")
        }
    }
}
 */


// 7. Construye el proyecto.

// Ahora puedes usar esta dependencia en tu código Kotlin. Para ello, importe el paquete que ha
// configurado en la propiedad del paquete en el archivo .def. Para el ejemplo anterior, esto será:

// import DateTools.*

// ------- Añade un marco sin CocoaPods -------

// 1. Descargue el código fuente del marco y colóquelo en algún lugar donde pueda hacer referencia
//    a él desde su proyecto.

// 2. Construya el marco (los autores del marco suelen proporcionar una guía sobre cómo hacer esto)
//    y obtenga un camino hacia los binarios.

// 3. En su proyecto, cree un archivo .def, por ejemplo, MyFramework.def.

// 4. Añade la primera cadena a este archivo: idioma = Objetivo-C. Si desea utilizar una dependencia C
//    pura, omita la propiedad del idioma.

// 5. Proporcione valores para estas dos propiedades obligatorias:

//     * modules: - el nombre del marco que debe ser procesado por el cinterop.
//
//     * package: - el nombre del paquete en el que se deben poner estas declaraciones.

//     For example:

//       modules = MyFramework
//       package = MyFramework

// 6. Añade información sobre la interoperabilidad con el marco al script de compilación:

//    * Pase la ruta al archivo .def. Esta ruta se puede omitir si su archivo .def tiene el mismo
//      nombre que el cinterop y se coloca en el directorio src/nativeInterop/cinterop/.
//
//    * Pase el nombre del marco al compilador y al enlazador usando la opción -framework. Pasa el camino t

/*
kotlin {
    iosX64() {
        compilations.getByName("main") {
            val DateTools by cinterops.creating {
                // Path to .def file
                defFile("src/nativeInterop/cinterop/DateTools.def")

                compilerOpts("-framework", "MyFramework", "-F/path/to/framework/")
            }
            val anotherInterop by cinterops.creating { /* ... */ }
        }

        binaries.all {
            // Tell the linker where the framework is located.
            linkerOpts("-framework", "MyFramework", "-F/path/to/framework/")
        }
   }
}
 */

// 7. Construye el proyecto.

// Ahora puedes usar esta dependencia en tu código Kotlin. Para ello, importe el paquete que ha configurado
// en la propiedad del paquete en el archivo .def. Para el ejemplo anterior, esto será:

// import MyFramework.*

// Obtenga más información sobre la interoperación Objective-C y Swift y la configuración de
// la interoperación de Gradle.
fun main() {
}