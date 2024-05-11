//  ------------ Adición de dependencias de Android ------------

// El flujo de trabajo para agregar dependencias específicas de Android a un módulo
// multiplataforma de Kotlin es el mismo que para los proyectos puros de Android:
// declare la dependencia en su archivo Gradle e importe el proyecto. Después de eso,
// puedes usar esta dependencia en tu código Kotlin.

// Recomendamos declarar las dependencias de Android en los proyectos multiplataforma
// de Kotlin añadiéndolas a un conjunto de fuentes específico de Android. Para ello,
// actualice su archivo build.gradle(.kts) en el directorio compartido de su proyecto:

/*
Kotlin:

sourceSets["androidMain"].dependencies {
    implementation("com.example.android:app-magic:12.3")
}
 */

/*
Groovy

sourceSets {
    androidMain {
        dependencies {
            implementation 'com.example.android:app-magic:12.3'
        }
    }
}
 */

// Mover lo que era una dependencia de nivel superior en un proyecto de Android a un
// conjunto de fuentes específica en un proyecto multiplataforma podría ser difícil
// si la dependencia de nivel superior tuviera un nombre de configuración no trivial.
// Por ejemplo, para mover una dependencia de debugImplementation del nivel superior
// de un proyecto de Android, tendrás que añadir una dependencia de implementación al
// conjunto de origen llamado androidDebug. Para minimizar el esfuerzo que tienes que
// hacer para lidiar con problemas de migración como este, puedes añadir un bloque de
// dependencias dentro del bloque de Android:

/*

Kotlin:

android {
    //...
    dependencies {
        implementation("com.example.android:app-magic:12.3")
    }
}

Groovy:

android {
    //...
    dependencies {
        implementation 'com.example.android:app-magic:12.3'
    }
}
 */

// Las dependencias declaradas aquí se tratarán exactamente igual que las dependencias
// del bloque de nivel superior, pero al declararlas de esta manera también separará
// visualmente las dependencias de Android en su script de compilación
// y lo hará menos confuso.

// También se admite la poner dependencias en un bloque de dependencias independiente
// al final del script, de una manera que sea idiomática para los proyectos de Android.
// Sin embargo, recomendamos encarecidamente no hacerlo porque es probable que la
// configuración de un script de compilación con dependencias de Android en el bloque
// de nivel superior y otras dependencias de destino en cada conjunto
// de fuentes cause confusión.



fun main() {
print("Hola mundo")
}