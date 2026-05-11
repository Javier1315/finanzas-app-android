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

// pantalla de inicio de sesion
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(

    // viewmodel encargado de autenticacion
    authViewModel: AuthViewModel,

    // funcion ejecutada al iniciar sesion correctamente
    onLoginSuccess: () -> Unit,

    // navega a pantalla de registro
    onNavigateToRegister: () -> Unit

) {

    // estado del correo electronico
    var email by remember {
        mutableStateOf("")
    }

    // estado de la contraseña
    var password by remember {
        mutableStateOf("")
    }

    // estado de carga
    val isLoading =
        authViewModel.isLoading.value

    // mensaje de error
    val errorMessage =
        authViewModel.errorMessage.value

    // contexto actual de la aplicacion
    val context = LocalContext.current

    // obtiene la activity actual desde el contexto
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
            "activity no encontrada"
        )
    }

    // cliente de autenticacion con google
    val googleSignInClient =

        GoogleSignIn.getClient(

            context,

            GoogleSignInOptions.Builder(

                GoogleSignInOptions
                    .DEFAULT_SIGN_IN

            )

                // solicita token de autenticacion
                .requestIdToken(

                    context.getString(
                        R.string.default_web_client_id
                    )
                )

                // solicita acceso al correo
                .requestEmail()

                .build()
        )

    // launcher para iniciar sesion con google
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

                // obtiene cuenta de google
                val account =
                    task.result

                val idToken =
                    account.idToken

                // autentica en firebase usando google
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

                // muestra error en caso de fallo
                authViewModel.errorMessage.value =
                    e.message
            }
        }

    Scaffold(

        // barra superior
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

            // titulo principal
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

            // campo de correo electronico
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

            // campo de contraseña
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

            // boton para iniciar sesion
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

            // boton para iniciar sesion con google
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

            // boton para navegar a registro
            TextButton(

                onClick = onNavigateToRegister

            ) {

                Text(
                    "No tienes cuenta Crear cuenta"
                )
            }

            // indicador de carga
            if (isLoading) {

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                CircularProgressIndicator()
            }

            // muestra errores de autenticacion
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