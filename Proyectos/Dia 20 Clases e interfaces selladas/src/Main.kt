// ------------ Clases e interfaces selladas ------------

// Las clases e interfaces selladas proporcionan una herencia controlada de sus
// jerarquías de clases. Todas las subclases directas de una clase sellada se
// conocen en tiempo de compilación. No puede aparecer ninguna otra subclase
// fuera del módulo y el paquete dentro del cual se define la clase sellada.
// La misma lógica se aplica a las interfaces selladas y sus implementaciones:
// una vez que se compila un módulo con una interfaz sellada, no se pueden crear
// nuevas implementaciones.

// ********************************* info ***************************************
// Las subclases directas son clases que heredan inmediatamente de su superclase.

// Las subclases indirectas son clases que heredan de más de un nivel inferior
// de su superclase.
// ******************************************************************************

// Cuando combina clases e interfaces selladas con la expresión when, puede cubrir
// el comportamiento de todas las subclases posibles y asegurarse de que no se
// creen nuevas subclases que afecten negativamente a su código.

//Las clases selladas se utilizan mejor para escenarios en los que:

//Se desea una herencia de clase limitada: tiene un conjunto predefinido y finito
// de subclases que extienden una clase, todas las cuales se conocen en tiempo de
// compilación.

//Se requiere un diseño seguro: la seguridad y la coincidencia de patrones son
// cruciales en su proyecto. Particularmente para la gestión estatal o el manejo de
// la lógica condicional compleja. Por ejemplo, echa un vistazo a Usar clases selladas
// con expresiones when.

//Trabajar con API cerradas: Desea API públicas robustas y mantenibles para bibliotecas
// que garanticen que los clientes de terceros utilicen las API según lo previsto.

//Para obtener aplicaciones prácticas más detalladas,
// consulte Escenarios de casos de uso.


// Java 15 introdujo un concepto similar, donde las clases selladas utilizan la palabra
// clave sellada junto con la cláusula de permisos para definir jerarquías restringidas.

// ------------ Declarar una clase o interfaz sellada ------------

// Para declarar una clase o interfaz sellada, utilice el modificador sellado:

// Create a sealed interface
sealed interface Error

// Create a sealed class that implements sealed interface Error
sealed class IOError(): Error

// Define subclasses that extend sealed class 'IOError'
class FileReadError(val file: File): IOError()
class DatabaseError(val source: DataSource): IOError()

// Create a singleton object implementing the 'Error' sealed interface
object RuntimeError : Error


// Este ejemplo podría representar la API de una biblioteca que contiene clases de error
// para permitar a los usuarios de la biblioteca manejar los errores que puede generar.
// Si la jerarquía de dichas clases de error incluye interfaces o clases abstractas
// visibles en la API pública, entonces nada impide que otros desarrolladores las
// implementen o extiendan en el código del cliente. Dado que la biblioteca no conoce
// los errores declarados fuera de ella, no puede tratarlos de manera consistente con
// sus propias clases. Sin embargo, con una jerarquía sellada de clases de error,
// los autores de la biblioteca pueden estar seguros de que conocen todos los tipos de error
// posibles y que otros tipos de error no pueden aparecer más tarde.


// ------- Constructores -------

// Una clase sellada en sí misma es siempre una clase abstracta y, como resultado, no se puede
// instanciar directamente. Sin embargo, puede contener o heredar constructores.
// Estos constructores no son para crear instancias de la propia clase sellada, sino para
// sus subclases. Considere el siguiente ejemplo con una clase sellada llamada Error y sus
// varias subclases, que instanciamos:

/*
sealed class Error(val message: String) {
    class NetworkError : Error("Network failure")
    class DatabaseError : Error("Database cannot be reached")
    class UnknownError : Error("An unknown error has occurred")
}

fun main() {
    val errors = listOf(Error.NetworkError(), Error.DatabaseError(), Error.UnknownError())
    errors.forEach { println(it.message) }
}
 Network failure
 Database cannot be reached
 An unknown error has occurred
 */

// Puede usar clases de enumeraciones dentro de sus clases selladas para usar constantes de
// enumeraciones para representar estados y proporcionar detalles adicionales. Cada constante
// de enumeración existe solo como una sola instancia, mientras que las subclases de una clase
// sellada pueden tener varias instancias. En el ejemplo, la clase sellada Error, junto con sus
// varias subclases, emplea una enumeración para denotar la gravedad del error.
// Cada constructor de subclase inicializa la gravedad y puede alterar su estado:

enum class ErrorSeverity { MINOR, MAJOR, CRITICAL }

sealed class Error2(val severity: ErrorSeverity) {
    class FileReadError(val file: File): Error2(ErrorSeverity.MAJOR)
    class DatabaseError(val source: DataSource): Error2(ErrorSeverity.CRITICAL)
    object RuntimeError : Error2(ErrorSeverity.CRITICAL)
    // Additional error types can be added here
}

// Los constructores de clases selladas pueden tener una de dos visibilidades:
// protegida (por defecto) o privada:

sealed class IOError2 {
    // A sealed class constructor has protected visibility by default. It's visible inside this class and its subclasses
    constructor() { /*...*/ }

    // Private constructor, visible inside this class only.
    // Using a private constructor in a sealed class allows for even stricter control over instantiation,
    // enabling specific initialization procedures within the class.

    private constructor(description: String): this() { /*...*/ }

    // This will raise an error because public and internal constructors are not allowed in sealed classes
    // public constructor(code: Int): this() {}
}

// ------------ Inheritance ------------

// Las subclases directas de clases e interfaces selladas deben declararse en el mismo paquete.
// Pueden ser de nivel superior o anidados dentro de cualquier número de otras clases con nombre,
// interfaces con nombre u objetos con nombre. Las subclases pueden tener cualquier visibilidad
// siempre y cuando sean compatibles con las reglas normales de herencia en Kotlin.

//Las subclases de clases selladas deben tener un nombre debidamente calificado. No pueden ser
// objetos locales o anónimos.

// ********************************* info ***************************************
//enum classes can't extend a sealed class, or any other class. However,
// they can implement sealed interfaces:
sealed interface Error3

// enum class extending the sealed interface Error
enum class ErrorType : Error3 {
    FILE_ERROR, DATABASE_ERROR
}
// ******************************************************************************


// Estas restricciones no se aplican a las subclases indirectas. Si una subclase directa de una
// clase sellada no está marcada como sellada, se puede ampliar de cualquier manera que sus
// modificadores permitan:

// Sealed interface 'Error' has implementations only in the same package and module
sealed interface Error4

// Sealed class 'IOError' extends 'Error' and is extendable only within the same package
sealed class IOError1(): Error4

// Open class 'CustomError' extends 'Error' and can be extended anywhere it's visible
open class CustomError(): Error4

// ------- Herencia en proyectos multiplataforma -------

// Hay una restricción de herencia más en los proyectos multiplataforma: las subclases directas
// de las clases selladas deben residir en el mismo conjunto de fuentes. Se aplica a las clases
// selladas sin los modificadores esperados y reales.

// Si una clase sellada se declara como se espera en un conjunto de fuentes común y tiene
// implementaciones reales en conjuntos de fuentes de la plataforma, tanto las versiones
// expect como las reales pueden tener subclases en sus conjuntos de fuentes. Además,
// si utiliza una estructura jerárquica, puede crear subclases en cualquier conjunto de
// fuentes entre las declaraciones expect y reales.

// Obtenga más información sobre la estructura jerárquica de los proyectos multiplataforma.

// ------------ Usa clases selladas con la expresión cuando ------------

// El beneficio clave de usar clases selladas entra en juego cuando las usas en una expresión when.
// La expresión when, utilizada con una clase sellada, permite al compilador de Kotlin comprobar
// exhaustivamente que todos los casos posibles están cubiertos. En tales casos, no es necesario
// añadir una cláusula else:

/*
// Sealed class and its subclasses
sealed class Error {
    class FileReadError(val file: String): Error()
    class DatabaseError(val source: String): Error()
    object RuntimeError : Error()
}

// Function to log errors
fun log(e: Error) = when(e) {
    is Error.FileReadError -> println("Error while reading file ${e.file}")
    is Error.DatabaseError -> println("Error while reading from database ${e.source}")
    Error.RuntimeError -> println("Runtime error")
    // No `else` clause is required because all the cases are covered
}

// List all errors
fun main() {
    val errors = listOf(
        Error.FileReadError("example.txt"),
        Error.DatabaseError("usersDatabase"),
        Error.RuntimeError
    )

    errors.forEach { log(it) }
}
 */

// ********************************* info ***************************************
// En proyectos multiplataforma, si tiene una clase sellada con una expresión when
// como una declaración esperada en su código común, todavía necesita una rama else.
// Esto se debe a que las subclases de las implementaciones reales de la plataforma
// pueden extender las clases selladas que no se conocen en el código común.
// ******************************************************************************

// ------------ Escenarios de casos de uso ------------

// Exploremos algunos escenarios prácticos en los que las clases e interfaces selladas
// pueden ser particularmente útiles.

// ------- Gestión del estado en aplicaciones de interfaz de usuario -------

// Puedes usar clases selladas para representar diferentes estados de la interfaz de usuario
// en una aplicación. Este enfoque permite un manejo estructurado y seguro de los cambios en
// la interfaz de usuario. Este ejemplo muestra cómo administrar varios estados de la interfaz
// de usuario:

sealed class UIState {
    data object Loading : UIState()
    data class Success(val data: String) : UIState()
    data class Error(val exception: Exception) : UIState()
}

fun updateUI(state: UiState) {
    when (state) {
        is UIState.Loading -> showLoadingIndicator()
        is UIState.Success -> showData(state.data)
        is UIState.Error -> showError(state.exception)
    }
}

// ------- Manejo del método de pago -------

// En aplicaciones comerciales prácticas, el manejo eficiente de varios métodos de pago es un
// requisito común. Puedes usar clases selladas con expresiones when para implementar dicha
// ógica empresarial. Al representar diferentes métodos de pago como subclases de una clase
// sellada, establece una estructura clara y manejable para procesar las transacciones:

sealed class Payment {
    data class CreditCard(val number: String, val expiryDate: String) : Payment()
    data class PayPal(val email: String) : Payment()
    data object Cash : Payment()
}

fun processPayment(payment: Payment) {
    when (payment) {
        is Payment.CreditCard -> processCreditCardPayment(payment.number, payment.expiryDate)
        is Payment.PayPal -> processPayPalPayment(payment.email)
        is Payment.Cash -> processCashPayment()
    }
}

// El pago es una clase sellada que representa diferentes métodos de pago en un sistema de comercio
// electrónico: CreditCard, PayPal y Cash. Cada subclase puede tener sus propiedades específicas,
// como el número y la fecha de caducidad de la tarjeta de crédito, y el correo electrónico de PayPal.

//La función processPayment() muestra cómo manejar los diferentes métodos de pago.
// Este enfoque garantiza que se consideren todos los tipos de pago posibles, y el sistema sigue
// siendo flexible para que se agreguen nuevos métodos de pago en el futuro.

// ------- Manejo de solicitudes y respuestas de la API -------

// Puede utilizar clases selladas e interfaces selladas para implementar un sistema de autenticación
// de usuario que maneje las solicitudes y respuestas de la API. El sistema de autenticación del
// usuario tiene funcionalidades de inicio y cierre de sesión. La interfaz sellada ApiRequest define
// tipos de solicitud específicos: Solicitud de inicio de sesión para el inicio de sesión y Solicitud
// de cierre de sesión para las operaciones de cierre de sesión. La clase sellada, ApiResponse,
// encapsula diferentes escenarios de respuesta: UserSuccess con datos de usuario, UserNotFound para
// usuarios ausentes y Error para cualquier fallo. La función handleRequest procesa estas solicitudes
// de una manera segura utilizando una expresión when, mientras que getUserById simula la recuperación
// del usuario:


// Import necessary modules
import io.ktor.server.application.*
import io.ktor.server.resources.*

import kotlinx.serialization.*

// Define the sealed interface for API requests using Ktor resources
@Resource("api")
sealed interface ApiRequest

@Serializable
@Resource("login")
data class LoginRequest(val username: String, val password: String) : ApiRequest


@Serializable
@Resource("logout")
object LogoutRequest : ApiRequest

// Define the ApiResponse sealed class with detailed response types
sealed class ApiResponse {
    data class UserSuccess(val user: UserData) : ApiResponse()
    data object UserNotFound : ApiResponse()
    data class Error(val message: String) : ApiResponse()
}

// User data class to be used in the success response
data class UserData(val userId: String, val name: String, val email: String)

// Function to validate user credentials (for demonstration purposes)
fun isValidUser(username: String, password: String): Boolean {
    // Some validation logic (this is just a placeholder)
    return username == "validUser" && password == "validPass"
}

// Function to handle API requests with detailed responses
fun handleRequest(request: ApiRequest): ApiResponse {
    return when (request) {
        is LoginRequest -> {
            if (isValidUser(request.username, request.password)) {
                ApiResponse.UserSuccess(UserData("userId", "userName", "userEmail"))
            } else {
                ApiResponse.Error("Invalid username or password")
            }
        }
        is LogoutRequest -> {
            // Assuming logout operation always succeeds for this example
            ApiResponse.UserSuccess(UserData("userId", "userName", "userEmail")) // For demonstration
        }
    }
}

// Function to simulate a getUserById call
fun getUserById(userId: String): ApiResponse {
    return if (userId == "validUserId") {
        ApiResponse.UserSuccess(UserData("validUserId", "John Doe", "john@example.com"))
    } else {
        ApiResponse.UserNotFound
    }
    // Error handling would also result in an Error response.
}

// Main function to demonstrate the usage
fun main() {
    val loginResponse = handleRequest(LoginRequest("user", "pass"))
    println(loginResponse)

    val logoutResponse = handleRequest(LogoutRequest)
    println(logoutResponse)

    val userResponse = getUserById("validUserId")
    println(userResponse)

    val userNotFoundResponse = getUserById("invalidId")
    println(userNotFoundResponse)
}