// ------------ Clases de datos ------------

// Las clases de datos en Kotlin se utilizan principalmente para contener datos.
// Para cada clase de datos, el compilador genera automáticamente funciones de
// miembro adicionales que le permiten imprimir una instancia en una salida
// legible, comparar instancias, copiar instancias y más. Las clases de datos
// están marcadas con datos:

data class User(val name: String, val age: Int)

// El compilador deriva automáticamente los siguientes miembros de todas las
// propiedades declaradas en el constructor principal:

// -. .equals()/.hashCode() pair.

// -. .toString() de la forma "Usuario(nombre=John, edad=42)".

// -. .componentN() functions corresponding to the properties in their order of declaration.

// -. Función .copy() (ver más abajo).

// Para garantizar la coherencia y el comportamiento significativo del código generado,
// las clases de datos tienen que cumplir con los siguientes requisitos:

// El constructor principal debe tener al menos un parámetro.

// Todos los parámetros principales del constructor deben marcarse como val o var.

// Las clases de datos no pueden ser abstractas, abiertas, selladas o internas.

// Además, la generación de miembros de la clase de datos sigue estas reglas con respecto
// a la herencia de los miembros:

//Si hay implementaciones explícitas de .equals(), .hashCode() o .toString() en el cuerpo
// de la clase de datos o implementaciones finales en una superclase, entonces estas
// funciones no se generan y se utilizan las implementaciones existentes.

//Si un supertipo tiene funciones .componentN() que están abiertas y devuelven tipos
// compatibles, las funciones correspondientes se generan para la clase de datos y
// anulan las del supertipo. Si las funciones del supertipo no se pueden anular debido
// a firmas incompatibles o debido a que son definitivas, se informa de un error.

//No se permite proporcionar implementaciones explícitas para las funciones .componentN()
// y .copy().

//Las clases de datos pueden extender otras clases (consulte Clases selladas
// para ver ejemplos).

// ******************************** info ************************************************
// En la JVM, si la clase generada necesita tener un constructor sin parámetros, se deben
// especificar los valores predeterminados para las propiedades (ver Constructores):
// data class User(val name: String = "", val age: Int = 0)
// **************************************************************************************

// ------------ Propiedades declaradas en el cuerpo de la clase ------------

// El compilador solo utiliza las propiedades definidas dentro del constructor principal
// para las funciones generadas automáticamente. Para excluir una propiedad de las
// implementaciones generadas, declárala dentro del cuerpo de la clase:

data class PersonA(val name: String) {
    var age: Int = 0
}

// En el siguiente ejemplo, solo la propiedad name se utiliza de forma predeterminada
// dentro de las implementaciones .toString(), .equals(), .hashCode() y .copy(), y
// solo hay una función componente, .component1(). La propiedad de edad se declara
// dentro del cuerpo de la clase y se excluye. Por lo tanto, dos objetos Person con
// el mismo nombre pero diferentes valores de edad se consideran iguales, ya que
// .equals() solo evalúa las propiedades del constructor primario:


data class Person(val name: String) {
    var age: Int = 0
}
fun main() {
    val person1 = Person("John")
    val person2 = Person("John")
    person1.age = 10
    person2.age = 20

    println("person1 == person2: ${person1 == person2}")
    // person1 == person2: true

    println("person1 with age ${person1.age}: ${person1}")
    // person1 with age 10: Person(name=John)

    println("person2 with age ${person2.age}: ${person2}")
    // person2 with age 20: Person(name=John)

// ------------ Copiando ------------

// Utilice la función .copy() para copiar un objeto, lo que le permite alterar algunas
// de sus propiedades mientras mantiene el resto sin cambios. La implementación de esta
// función para la clase de usuario anterior sería la siguiente:

    fun copy(name: String = this.name, age: Int = this.age) = User(name, age)

// A continuación, puede escribir lo siguiente:

    val jack = User(name = "Jack", age = 1)
    val olderJack = jack.copy(age = 2)

// ------------ Clases de datos y declaraciones de desestructuración ------------

// Las funciones de componentes generadas para las clases de datos permiten utilizarlas en
// la desestructuración de declaraciones:

    val jane = User("Jane", 35)
    val (name, age) = jane
    println("$name, $age years of age")
// Jane, 35 years of age


// ------------ Clases de datos estándar ------------

// La biblioteca estándar ofrece las clases de par y triple. Sin embargo, en la mayoría de
// los casos, las clases de datos con nombre son una mejor opción de diseño porque hacen que
// el código sea más fácil de leer al proporcionar nombres significativos para las propiedades.

}