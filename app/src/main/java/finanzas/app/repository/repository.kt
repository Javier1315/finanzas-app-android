package finanzas.app.repository

import com.google.firebase.auth.FirebaseAuth

class AuthRepository {

    // instancia de firebase authentication
    private val auth =
        FirebaseAuth.getInstance()

    // registra un nuevo usuario
    fun register(

        email: String,

        password: String,

        onResult: (Boolean, String?) -> Unit

    ) {

        auth.createUserWithEmailAndPassword(
            email,
            password
        )

            .addOnCompleteListener { task ->

                // verifica si el registro fue exitoso
                if (task.isSuccessful) {

                    onResult(true, null)

                } else {

                    // devuelve mensaje de error
                    onResult(
                        false,
                        task.exception?.message
                    )
                }
            }
    }

    // inicia sesion con correo y contraseña
    fun login(

        email: String,

        password: String,

        onResult: (Boolean, String?) -> Unit

    ) {

        auth.signInWithEmailAndPassword(
            email,
            password
        )

            .addOnCompleteListener { task ->

                // verifica si el inicio de sesion fue exitoso
                if (task.isSuccessful) {

                    onResult(true, null)

                } else {

                    // devuelve mensaje de error
                    onResult(
                        false,
                        task.exception?.message
                    )
                }
            }
    }

    // obtiene el usuario autenticado actualmente
    fun getCurrentUser() =
        auth.currentUser

    // cierra la sesion del usuario
    fun logout() {
        auth.signOut()
    }
}