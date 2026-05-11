package finanzas.app.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import finanzas.app.repository.AuthRepository
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {

    private val repository =
        AuthRepository()

    private val auth =
        FirebaseAuth.getInstance()

    fun isUserLoggedIn(): Boolean {

        return repository.getCurrentUser() != null
    }

    var isLoading =
        mutableStateOf(false)

    var errorMessage =
        mutableStateOf<String?>(null)

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

            if (success) {

                onSuccess()

            } else {

                errorMessage.value = error
            }
        }
    }


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

            if (success) {

                onSuccess()

            } else {

                errorMessage.value = error
            }
        }
    }
    fun logout() {

        repository.logout()
    }

    fun firebaseAuthWithGoogle(

        idToken: String,

        onSuccess: () -> Unit

    ) {

        isLoading.value = true

        val credential =

            GoogleAuthProvider.getCredential(
                idToken,
                null
            )

        auth.signInWithCredential(credential)

            .addOnCompleteListener { task ->

                isLoading.value = false

                if (task.isSuccessful) {

                    errorMessage.value = null

                    onSuccess()

                } else {

                    errorMessage.value =
                        task.exception?.message
                }
            }
    }
}