// ------------ Funciones en línea ------------

// El uso de funciones de orden superior impone ciertas penalizaciones
// en tiempo de ejecución: cada función es un objeto y captura un cierre.
// Un cierre es un alcance de variables a las que se puede acceder en el
// cuerpo de la función. Las asignaciones de memoria (tanto para objetos
// de función como para clases) y las llamadas virtuales introducen una
// sobrecarga en tiempo de ejecución.

// Pero parece que en muchos casos este tipo de sobrecarga se puede eliminar
// alineando las expresiones lambda. Las funciones que se muestran a
// continuación son buenos ejemplos de esta situación. La función lock() se
// podría alinear fácilmente en los sitios de llamada.
// Considere el siguiente caso:

// lock(l) { foo() }

// En lugar de crear un objeto de función para el parámetro y generar una llamada,
// el compilador podría emitir el siguiente código:

/*
l.lock()
try {
    foo()
} finally {
    l.unlock()
}
 */

// Para que el compilador haga esto, marque la función lock() con el
// modificador en línea:

// inline fun <T> lock(lock: Lock, body: () -> T): T { ... }

// El modificador en línea afecta tanto a la función en sí como a los lambdas
// que se le pasan: todos ellos estarán en línea en el sitio de llamada.

//El inlining puede hacer que el código generado crezca. Sin embargo, si lo
// haces de una manera razonable (evitando incluir funciones grandes), valdrá
// la pena en rendimiento, especialmente en los sitios de llamada "megamórficos"
// dentro de los bucles.

// ----------- Noinline ------------

// Si no desea que todas las lambdas pasadas a una función en línea sean en línea,
// marque algunos de sus parámetros de función con el modificador de línea no en línea:

// inline fun foo(inlined: () -> Unit, noinline notInlined: () -> Unit) { ... }

// Los lambdas en línea solo se pueden llamar dentro de las funciones en línea o pasar como
// argumentos en línea. Sin embargo, los lambdas en línea no se pueden manipular de la manera
// que desee, incluyendo ser almacenados en campos o pasados.


// ******************************** info ********************************
// Si una función en línea no tiene parámetros de función en línea y no tiene parámetros
// de tipo reificados, el compilador emitirá una advertencia, ya que es muy poco probable
// que la alineación de tales funciones sea beneficiosa (puede usar la anotación @Suppress
// ("NOTHING_TO_INLINE") para suprimir la advertencia si está seguro
// de que se necesita la línea).

// ------------ Devoluciones no locales ------------

// En Kotlin, solo puedes usar un retorno normal y no calificado para salir de una función
// con nombre o una función anónima. Para salir de un lambda, usa una etiqueta. Un retorno
// desnudo está prohibido dentro de un lambda porque un lambda no puede hacer que la función
// adjunta devuelva:

/*
fun ordinaryFunction(block: () -> Unit) {
    println("hi!")
}
fun foo() {
    ordinaryFunction {
        return // ERROR: cannot make `foo` return here
    }
}
fun main() {
    foo()
}
 */

// Pero si la función a la que se pasa el lambda está en línea, el retorno también se puede
// en línea. Así que está permitido:

/*
inline fun inlined(block: () -> Unit) {
    println("hi!")
}
fun foo() {
    inlined {
        return // OK: the lambda is inlined
    }
}
fun main() {
    foo()
}
 */

// Dichos retornos (ubicados en un lambda, pero saliendo de la función de cierre) se
// denominan retornos no locales. Este tipo de construcción suele ocurrir en bucles,
// que las funciones en línea a menudo encierran:

/*
fun hasZeros(ints: List<Int>): Boolean {
    ints.forEach {
        if (it == 0) return true // returns from hasZeros
    }
    return false
}
 */

// Tenga en cuenta que algunas funciones en línea pueden llamar a las lambdas que se
// les pasan como parámetros no directamente desde el cuerpo de la función, sino desde
// otro contexto de ejecución, como un objeto local o una función anidada. En tales
// casos, tampoco se permite el flujo de control no local en las lambdas. Para indicar
// que el parámetro lambda de la función en línea no puede usar retornos no locales,
// marque el parámetro lambda con el modificador en línea cruzada:

/*
inline fun f(crossinline body: () -> Unit) {
    val f = object: Runnable {
        override fun run() = body()
    }
    // ...
}
 */


// ******************************** info ********************************
// Break y continue aún no están disponibles en lambdas en línea, pero también estamos
// planeando apoyarlos.

// ------------ Parámetros de tipo reificado ------------

// A veces necesitas acceder a un tipo pasado como parámetro:

/*
fun <T> TreeNode.findParentOfType(clazz: Class<T>): T? {
    var p = parent
    while (p != null && !clazz.isInstance(p)) {
        p = p.parent
    }
    @Suppress("UNCHECKED_CAST")
    return p as T?
}
 */

// Aquí, subes por un árbol y usas la reflexión para comprobar si un nodo tiene un
// cierto tipo. Todo está bien, pero el sitio de llamadas no es muy bonito:

// treeNode.findParentOfType(MyTreeNode::class.java)

// Para habilitar esto, las funciones en línea admiten parámetros de tipo reificado,
// por lo que puede escribir algo como esto:

/*
inline fun <reified T> TreeNode.findParentOfType(): T? {
    var p = parent
    while (p != null && p !is T) {
        p = p.parent
    }
    return p as T?
}
 */

// El código anterior califica el parámetro de tipo con el modificador reificado para
// hacerlo accesible dentro de la función, casi como si fuera una clase normal.
// Dado que la función está en línea, no se necesita ningún reflejo y a los operadores
// normales les gusta! Es y como están ahora disponibles para que lo uses. Además, puede
// llamar a la función como se muestra arriba: myTree.findParentOfType<MyTreeNodeType>().

// Aunque la reflexión puede no ser necesaria en muchos casos, aún puede usarla con un
// parámetro de tipo reificado:

/*
inline fun <reified T> membersOf() = T::class.members

fun main(s: Array<String>) {
    println(membersOf<StringBuilder>().joinToString("\n"))
}
 */

// Las funciones normales (no marcadas como en línea) no pueden tener parámetros reificados.
// Un tipo que no tiene una representación en tiempo de ejecución (por ejemplo, un parámetro
// de tipo no reificado o un tipo ficticio como Nothing) no se puede utilizar como argumento
// para un parámetro de tipo reificado.
//
// ------------ Propiedades en línea ------------

// El modificador en línea se puede utilizar en los accesorios de propiedades que no tienen
// campos de respaldo. Puede anotar a los accesores de propiedades individuales:

/*
val foo: Foo
    inline get() = Foo()

var bar: Bar
    get() = ...
    inline set(v) { ... }
 */

// También puede anotar una propiedad completa, que marca sus dos accesores como en línea:
/*
inline var bar: Bar
    get() = ...
    set(v) { ... }
 */

// En el sitio de llamada, los accesorios en línea están en línea como funciones regulares
// en línea.

// ------------ Restricciones para las funciones públicas en línea de la API ------------

// Cuando una función en línea es pública o protegida, pero no forma parte de una declaración
// privada o interna, se considera la API pública de un módulo. Se puede llamar en otros
// módulos y también está en línea en dichos sitios de llamada.

// Esto impone ciertos riesgos de incompatibilidad binaria causados por cambios en el módulo
// que declara una función en línea en caso de que el módulo de llamada no se vuelva a
// compilar después del cambio.

// Para eliminar el riesgo de que dicha incompatibilidad se introduzca por un cambio en una
// API no pública de un módulo, las funciones públicas en línea de la API no pueden utilizar
// declaraciones de API no públicas, es decir, declaraciones privadas e internas y sus partes,
// en sus cuerpos.

// Una declaración interna se puede anotar con @PublishedApi, que permite su uso en funciones
// públicas en línea de la API. Cuando una función interna en línea está marcada como
// @PublishedApi, su cuerpo también se comprueba, como si fuera público.

fun main() {


}