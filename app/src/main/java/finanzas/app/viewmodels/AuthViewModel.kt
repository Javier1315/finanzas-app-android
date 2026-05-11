package finanzas.app.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import finanzas.app.repository.AuthRepository
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseAuth

// viewmodel encargado de la autenticacion
class AuthViewModel : ViewModel() {

    // repositorio de autenticacion
    private val repository =
        AuthRepository()

    // instancia de firebase authentication
    private val auth =
        FirebaseAuth.getInstance()

    // verifica si existe una sesion iniciada
    fun isUserLoggedIn(): Boolean {

        return repository.getCurrentUser() != null
    }

    // estado de carga
    var isLoading =
        mutableStateOf(false)

    // mensaje de error
    var errorMessage =
        mutableStateOf<String?>(null)

    // inicia sesion con correo y contraseña
    fun login(

        email: String,

        password: String,

        onSuccess: () -> Unit

    ) {

        isLoading.value = true

        repository.login(
            email,
            password
        ) { success, error ->

            isLoading.value = false

            // verifica resultado del login
            if (success) {

                onSuccess()

            } else {

                errorMessage.value = error
            }
        }
    }

    // registra un nuevo usuario
    fun register(

        email: String,

        password: String,

        onSuccess: () -> Unit

    ) {

        isLoading.value = true

        repository.register(
            email,
            password
        ) { success, error ->

            isLoading.value = false

            // verifica resultado del registro
            if (success) {

                onSuccess()

            } else {

                errorMessage.value = error
            }
        }
    }

    // cierra sesion del usuario
    fun logout() {

        repository.logout()
    }

    // autentica usuario usando google
    fun firebaseAuthWithGoogle(

        idToken: String,

        onSuccess: () -> Unit

    ) {

        isLoading.value = true

        // crea credencial de google
        val credential =

            GoogleAuthProvider.getCredential(
                idToken,
                null
            )

        // inicia sesion en firebase con google
        auth.signInWithCredential(credential)

            .addOnCompleteListener { task ->

                isLoading.value = false

                // verifica autenticacion exitosa
                if (task.isSuccessful) {

                    errorMessage.value = null

                    onSuccess()

                } else {

                    // guarda mensaje de error
                    errorMessage.value =
                        task.exception?.message
                }
            }
    }
}