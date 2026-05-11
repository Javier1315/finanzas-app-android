package finanzas.app.screens

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import finanzas.app.R
import finanzas.app.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(

    authViewModel: AuthViewModel,

    onLoginSuccess: () -> Unit,

    onNavigateToRegister: () -> Unit

) {

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    val isLoading =
        authViewModel.isLoading.value

    val errorMessage =
        authViewModel.errorMessage.value

    val context = LocalContext.current

    fun Context.findActivity(): Activity {

        var currentContext = this

        while (currentContext is ContextWrapper) {

            if (currentContext is Activity) {

                return currentContext
            }

            currentContext =
                currentContext.baseContext
        }

        throw IllegalStateException(
            "Activity no encontrada"
        )
    }

    val googleSignInClient =

        GoogleSignIn.getClient(

            context,

            GoogleSignInOptions.Builder(

                GoogleSignInOptions
                    .DEFAULT_SIGN_IN

            )

                .requestIdToken(
                    context.getString(
                        R.string.default_web_client_id
                    )
                )

                .requestEmail()

                .build()
        )

    val launcher =

        rememberLauncherForActivityResult(

            contract =
                ActivityResultContracts
                    .StartActivityForResult()

        ) { result ->

            val task =

                GoogleSignIn
                    .getSignedInAccountFromIntent(
                        result.data
                    )

            try {

                val account =
                    task.result

                val idToken =
                    account.idToken

                if (idToken != null) {

                    authViewModel
                        .firebaseAuthWithGoogle(

                            idToken = idToken,

                            onSuccess = {

                                onLoginSuccess()
                            }
                        )
                }

            } catch (e: Exception) {

                authViewModel.errorMessage.value =
                    e.message
            }
        }

    Scaffold(

        topBar = {

            TopAppBar(
                title = {
                    Text("Finanzas App")
                }
            )
        }

    ) { paddingValues ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.Center

        ) {

            Text(

                text = "Bienvenido",

                style =
                    MaterialTheme
                        .typography
                        .headlineMedium

            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            OutlinedTextField(

                value = email,

                onValueChange = {
                    email = it
                },

                label = {
                    Text("Correo electrónico")
                },

                modifier = Modifier.fillMaxWidth()

            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            OutlinedTextField(

                value = password,

                onValueChange = {
                    password = it
                },

                label = {
                    Text("Contraseña")
                },

                visualTransformation =
                    PasswordVisualTransformation(),

                modifier = Modifier.fillMaxWidth()

            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Button(

                onClick = {

                    authViewModel.login(

                        email = email,

                        password = password,

                        onSuccess = {

                            onLoginSuccess()
                        }
                    )
                },

                modifier = Modifier.fillMaxWidth()

            ) {

                Text("Iniciar sesión")
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            OutlinedButton(

                onClick = {

                    val signInIntent =
                        googleSignInClient
                            .signInIntent

                    launcher.launch(
                        signInIntent
                    )
                },

                modifier = Modifier.fillMaxWidth()

            ) {

                Text(
                    "Continuar con Google"
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            TextButton(

                onClick = onNavigateToRegister

            ) {

                Text(
                    "¿No tienes cuenta? Crear cuenta"
                )
            }

            if (isLoading) {

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                CircularProgressIndicator()
            }

            errorMessage?.let {

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(

                    text = it,

                    color =
                        MaterialTheme
                            .colorScheme
                            .error

                )
            }
        }
    }
}