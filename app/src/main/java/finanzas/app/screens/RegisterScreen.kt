package finanzas.app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import finanzas.app.viewmodels.AuthViewModel

// pantalla de registro de usuarios
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(

    // viewmodel encargado de autenticacion
    authViewModel: AuthViewModel,

    // funcion ejecutada al registrarse correctamente
    onRegisterSuccess: () -> Unit,

    // navega de regreso al login
    onBackToLogin: () -> Unit

) {

    // estado del correo
    var email by remember {
        mutableStateOf("")
    }

    // estado de la contraseña
    var password by remember {
        mutableStateOf("")
    }

    // estado de confirmacion de contraseña
    var confirmPassword by remember {
        mutableStateOf("")
    }

    // mensaje de error para contraseñas
    var passwordError by remember {
        mutableStateOf("")
    }

    // estado de carga
    val isLoading =
        authViewModel.isLoading.value

    // mensaje de error de firebase
    val errorMessage =
        authViewModel.errorMessage.value

    Scaffold(

        // barra superior
        topBar = {

            TopAppBar(
                title = {
                    Text("Crear cuenta")
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

                text = "Registro",

                style =
                    MaterialTheme
                        .typography
                        .headlineMedium

            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            // campo de correo
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
                modifier = Modifier.height(16.dp)
            )

            // campo para confirmar contraseña
            OutlinedTextField(

                value = confirmPassword,

                onValueChange = {
                    confirmPassword = it
                },

                label = {
                    Text("Confirmar contraseña")
                },

                visualTransformation =
                    PasswordVisualTransformation(),

                modifier = Modifier.fillMaxWidth()

            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // boton para registrar usuario
            Button(

                onClick = {

                    // valida que las contraseñas coincidan
                    if (
                        password != confirmPassword
                    ) {

                        passwordError =
                            "Las contraseñas no coinciden"

                        return@Button
                    }

                    passwordError = ""

                    // registra usuario en firebase
                    authViewModel.register(

                        email = email,

                        password = password,

                        onSuccess = {

                            onRegisterSuccess()
                        }
                    )
                },

                modifier = Modifier.fillMaxWidth()

            ) {

                Text("Crear cuenta")
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // boton para regresar al login
            TextButton(

                onClick = onBackToLogin

            ) {

                Text(
                    "Ya tengo cuenta"
                )
            }

            // muestra error de contraseñas
            if (passwordError.isNotEmpty()) {

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(

                    text = passwordError,

                    color =
                        MaterialTheme
                            .colorScheme
                            .error
                )
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

            // indicador de carga
            if (isLoading) {

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                CircularProgressIndicator()
            }
        }
    }
}