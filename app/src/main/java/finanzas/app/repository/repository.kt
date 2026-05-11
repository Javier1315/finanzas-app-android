package finanzas.app.repository

import com.google.firebase.auth.FirebaseAuth

class AuthRepository {

    private val auth =
        FirebaseAuth.getInstance()

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

                if (task.isSuccessful) {

                    onResult(true, null)

                } else {

                    onResult(
                        false,
                        task.exception?.message
                    )
                }
            }
    }

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

                if (task.isSuccessful) {

                    onResult(true, null)

                } else {

                    onResult(
                        false,
                        task.exception?.message
                    )
                }
            }
    }

    fun getCurrentUser() =
        auth.currentUser

    fun logout() {
        auth.signOut()
    }
}