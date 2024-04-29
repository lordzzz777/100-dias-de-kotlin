// ------------ Técnicas de programación asíncrona ----------

// Durante décadas, como desarrolladores, nos enfrentamos a un problema que resolver:
// cómo evitar que nuestras aplicaciones se bloqueen. Ya sea que estemos desarrollando
// aplicaciones de escritorio, móviles o incluso del lado del servidor, queremos evitar
// que el usuario espere o, lo que es peor, cause cuellos de botella que impedirían que
// una aplicación se escale.

//      * Ha habido muchos enfoques para resolver este problema, incluyendo:

//      * Depilación con hilo

//      * Devoluciones de llamada

//      * Futuros, promesas y otros

//      * Extensiones reactivas

//      * Corutinas

// Antes de explicar qué son las corrutinas, revisemos brevemente algunas de
// las otras soluciones.

// ------------ Enhebrado ------------

// Los hilos son, con mucho, probablemente el enfoque más conocido para evitar que las
// aplicaciones se bloqueen.

/*
fun postItem(item: Item) {
    val token = preparePost()
    val post = submitPost(token, item)
    processPost(post)
}

fun preparePost(): Token {
    // makes a request and consequently blocks the main thread
    return token
}
 */

// Supongamos en el código anterior que preparePost es un proceso de larga duración y,
// en consecuencia, bloquearía la interfaz de usuario. Lo que podemos hacer es lanzarlo
// en un hilo separado. Esto nos permitiría evitar que la interfaz de usuario se bloquee.
// Esta es una técnica muy común, pero tiene una serie de inconvenientes:

// Los hilos no son baratos. Los hilos requieren cambios de contexto que son costosos.

// Los hilos no son infinitos. El número de subprocesos que se pueden iniciar está
// limitado por el sistema operativo subyacente. En las aplicaciones del lado del
// servidor, esto podría causar un gran cuello de botella.

// Los hilos no siempre están disponibles. Algunas plataformas, como JavaScript, ni
// siquiera admiten hilos.

// Los hilos no son fáciles. Depurar hilos y evitar las condiciones de carrera son
// problemas comunes que sufrimos en la programación multihilo.

// ------------ Devoluciones de llamada ------------

// Con las devoluciones de llamada, la idea es pasar una función como parámetro a otra
// función, y hacer que se invoque una vez que el proceso se haya completado.

/*
fun postItem(item: Item) {
    preparePostAsync { token ->
        submitPostAsync(token, item) { post ->
            processPost(post)
        }
    }
}

fun preparePostAsync(callback: (Token) -> Unit) {
    // make request and return immediately
    // arrange callback to be invoked later
}
 */

// En principio, esto se siente como una solución mucho más elegante, pero una vez más
// tiene varios problemas:

// Dificultad de las devoluciones de llamada anidadas. Por lo general, una función que
// se utiliza como devolución de llamada, a menudo termina necesitando su propia devolución
// de llamada. Esto conduce a una serie de devoluciones de llamada anidadas que conducen a
// un código incomprensible. El patrón a menudo se conoce como el árbol de Navidad titulado
// (los tirantes representan las ramas del árbol).

// El manejo de errores es complicado. El modelo de anidación hace que el manejo de errores
// y la propagación de estos sean un poco más complicados.

// Las devoluciones de llamada son bastante comunes en las arquitecturas de bucle de eventos
// como JavaScript, pero incluso allí, generalmente la gente se ha alejado del uso de otros
// enfoques, como promesas o extensiones reactivas.

// ------------ Futuros, promesas y otros ------------

// La idea detrás de futuros o promesas (también hay otros términos a los que se puede
// referirse dependiendo del idioma/plataforma), es que cuando hacemos una llamada,
// se nos promete que en algún momento volverá con un objeto llamado Promesa, que luego
// se puede operar.

/*
fun postItem(item: Item) {
    preparePostAsync()
        .thenCompose { token ->
            submitPostAsync(token, item)
        }
        .thenAccept { post ->
            processPost(post)
        }

}

fun preparePostAsync(): Promise<Token> {
    // makes request and returns a promise that is completed later
    return promise
}
 */

// Este enfoque requiere una serie de cambios en la forma en que programamos,
// en particular:

// Modelo de programación diferente. Al igual que las devoluciones de llamada,
// el modelo de programación se aleja de un enfoque imperativo de arriba hacia
// abajo a un modelo de composición con llamadas encadenadas. Las estructuras
// de programas tradicionales, como los bucles, el manejo de excepciones, etc.,
// generalmente ya no son válidas en este modelo.

// Diferentes API. Por lo general, hay una necesidad de aprender una API completamente
// nueva, como luego Compose o thenAccept, que también puede variar entre plataformas.

// Tipo de devolución específico. El tipo de retorno se aleja de los datos reales que
// necesitamos y, en su lugar, devuelve un nuevo tipo de promesa que tiene que ser
// introspeccionado.

// El manejo de errores puede ser complicado. La propagación y el encadenamiento de
// errores no siempre son sencillos.

// ------------ Extensiones reactivas ------------

// Erik Meijer introdujo las extensiones reactivas (Rx) en C#. Aunque definitivamente
// se usó en el . La plataforma NET realmente no llegó a la adopción convencional hasta
// que Netflix la portó a Java, nombrándolo RxJava. A partir de entonces, se han
// proporcionado numerosos puertos para una variedad de plataformas, incluyendo JavaScript
// (RxJS).

// La idea detrás de Rx es avanzar hacia lo que se llama flujos observables, por lo que
// ahora pensamos en los datos como flujos (cantidades infinitas de datos) y estos flujos
// se pueden observar. En términos prácticos, Rx es simplemente el patrón de observador
// con una serie de extensiones que nos permiten operar con los datos.

// En el enfoque, es bastante similar a los futuros, pero se puede pensar en un futuro como
// una devolución de un elemento discreto, mientras que Rx devuelve un flujo. Sin embargo,
// al igual que el anterior, también introduce una forma completamente nueva de pensar sobre
// nuestro modelo de programación, expresado como

// "Todo es una corriente, y es observable"

// Esto implica una forma diferente de abordar los problemas y un cambio bastante significativo
// de lo que estamos acostumbrados al escribir código síncrono. Un beneficio a diferencia de
// Futures es que, dado que está portado a tantas plataformas, generalmente podemos encontrar
// una experiencia de API consistente sin importar lo que usemos, ya sea C#, Java, JavaScript
// o cualquier otro lenguaje donde Rx esté disponible.

// Además, Rx introduce un enfoque algo mejor para el manejo de errores.

// ------------ Corutinas ------------

// El enfoque de Kotlin para trabajar con código asíncrono es el uso de corrutinas, que es la
// idea de cálculos suspendibles, es decir, la idea de que una función puede suspender su
// ejecución en algún momento y reanudarse más adelante.

// Sin embargo, uno de los beneficios de las corrutinas es que cuando se trata del desarrollador,
// escribir código sin bloqueo es esencialmente lo mismo que escribir código de bloqueo.
// El modelo de programación en sí mismo no cambia realmente.

// Tome, por ejemplo, el siguiente código:

/*
fun postItem(item: Item) {
    launch {
        val token = preparePost()
        val post = submitPost(token, item)
        processPost(post)
    }
}

suspend fun preparePost(): Token {
    // makes a request and suspends the coroutine
    return suspendCoroutine { /* ... */ }
}
 */

// Este código iniciará una operación de larga duración sin bloquear el hilo principal.
// El preparePost es lo que se llama una función suspendible, por lo que la palabra clave
// suspend la prefija. Lo que esto significa, como se indicó anteriormente, es que la
// función se ejecutará, pausará la ejecución y se reanudará en algún momento.

// La firma de la función sigue siendo exactamente la misma. La única diferencia es que se
// le añade la suspensión. Sin embargo, el tipo de devolución es el tipo que queremos que
// se devuelva.

// El código todavía está escrito como si estuviéramos escribiendo código síncrono, de arriba
// hacia abajo, sin la necesidad de ninguna sintaxis especial, más allá del uso de una función
// llamada lanzamiento que esencialmente inicia la corutina (cubierta en otros tutoriales).

// El modelo de programación y las API siguen siendo los mismos. Podemos seguir usando bucles,
// manejo de excepciones, etc. y no hay necesidad de aprender un conjunto completo de nuevas API.

// Es independiente de la plataforma. Ya sea que nos dirijamos a JVM, JavaScript o cualquier
// otra plataforma, el código que escribimos es el mismo. Bajo las cubiertas, el compilador
// se encarga de adaptarlo a cada plataforma.

// Las corutinas no son un concepto nuevo, y mucho menos inventado por Kotlin. Han existido
// durante décadas y son populares en algunos otros lenguajes de programación como Go.
// Sin embargo, lo que es importante tener en cuenta es que la forma en que se implementan
// en Kotlin, la mayor parte de la funcionalidad se delega en las bibliotecas. De hecho,
// más allá de la palabra clave suspender, no se añade ninguna otra palabra clave al idioma.
// Esto es algo diferente de los lenguajes como C# que tienen asíncrono y esperan como parte
// de la sintaxis. Con Kotlin, estas son solo funciones de biblioteca.

// Para obtener más información, consulte la referencia de Coroutines.

fun main() {
// ...
}