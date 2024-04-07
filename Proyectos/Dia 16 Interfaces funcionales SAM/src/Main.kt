// -------- Interfaces funcionales (SAM) --------

// Una interfaz con un solo método abstracto se llama interfaz
// funcional o interfaz de método abstracto único (SAM).
// La interfaz funcional puede tener varios miembros no abstractos,
// pero solo un miembro abstracto.

//Para declarar una interfaz funcional en Kotlin, usa el modificador de diversión.

fun interface KRunnable {
    fun invoke()
}

// ------------ Conversiones SAM ------------

// Para las interfaces funcionales, puede usar conversiones SAM que ayudan
// a que su código sea más conciso y legible mediante el uso de expresiones lambda.
// En lugar de crear una clase que implemente una interfaz funcional manualmente,
// puedes usar una expresión lambda. Con una conversión SAM, Kotlin puede convertir
// cualquier expresión lambda cuya firma coincida con la firma del método único de
// la interfaz en el código, que instancia dinámicamente la implementación de la interfaz.

//Por ejemplo, considere la siguiente interfaz funcional de Kotlin:

fun interface IntPredicate {
    fun accept(i: Int): Boolean
}

// Si no utilizas una conversión SAM, tendrás que escribir un código como este:

// Creating an instance of a class
val isEven = object : IntPredicate {
    override fun accept(i: Int): Boolean {
        return i % 2 == 0
    }
}

// Al aprovechar la conversión SAM de Kotlin, puede escribir el siguiente
// código equivalente en su lugar:

// Creating an instance using lambda
val isEven2 = IntPredicate { it % 2 == 0 }

// Una expresión lambda corta reemplaza todo el código innecesario.

fun interface IntPredicate2 {
    fun accept(i: Int): Boolean
}

val isEven3 = IntPredicate { it % 2 == 0 }


// También puedes usar conversiones SAM para interfaces Java.

// ------- Migración de una interfaz con función de constructor a una interfaz funcional -------

// A partir de 1.6.20, Kotlin admite referencias invocables a constructores
// de interfaces funcionales, lo que añade una forma compatible con la fuente
// de migrar de una interfaz con una función de constructor a una interfaz funcional.
// Considere el siguiente código:

interface Printer {
    fun print()
}

fun Printer(block: () -> Unit): Printer = object : Printer { override fun print() = block() }

// Con habilitadas las referencias invocables a los constructores de la interfaz funcional,
// este código se puede reemplazar con solo una declaración de interfaz funcional:

fun interface Printer2 {
    fun print()
}

// Preserve la compatibilidad binaria marcando la función heredada Impresora con la anotación
// @Deprecated con DeprecationLevel.HIDDEN:

@Deprecated(message = "Your message about the deprecation", level = DeprecationLevel.HIDDEN)
fun Printer(...) {...}

// ------- Interfaces funcionales vs. alias de tipo -------

// También puede simplemente reescribir lo anterior usando un
// alias de tipo para un tipo funcional:

typealias IntPredicate = (i: Int) -> Boolean

val isEven: IntPredicate = { it % 2 == 0 }

fun main() {
    println("Is 7 even? - ${isEven3.accept(7)}")
    println("Is 7 even? - ${isEven(7)}")
}

// Sin embargo, las interfaces funcionales y los alias de tipo sirven para diferentes
// propósitos. Los alias de tipo son solo nombres para los tipos existentes: no crean
// un nuevo tipo, mientras que las interfaces funcionales sí. Puede proporcionar
// extensiones que sean específicas de una interfaz funcional en particular para que
// no sean aplicables a las funciones simples o a sus alias de tipo.

// Los alias de tipo pueden tener solo un miembro, mientras que las interfaces
// funcionales pueden tener varios miembros no abstractos y un miembro abstracto.
// Las interfaces funcionales también pueden implementar y ampliar otras interfaces.

// Las interfaces funcionales son más flexibles y proporcionan más capacidades que los
// alias de tipo, pero pueden ser más costosas tanto sintácticamente como en tiempo de
// ejecución porque pueden requerir conversiones a una interfaz específica. Cuando
// elijas cuál usar en tu código, ten en cuenta tus necesidades:

// Si su API necesita aceptar una función (cualquier función) con algún parámetro
// específico y tipos de retorno, utilice un tipo funcional simple o defina un alias
// de tipo para dar un nombre más corto al tipo funcional correspondiente.

// Si su API acepta una entidad más compleja que una función, por ejemplo, tiene
// contratos y/u operaciones no triviales que no se pueden expresar en la firma
// de un tipo funcional, declare una interfaz funcional separada para ella.

