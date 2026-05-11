package finanzas.app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import finanzas.app.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(

    authViewModel: AuthViewModel,

    onRegisterSuccess: () -> Unit,

    onBackToLogin: () -> Unit

) {

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var passwordError by remember {
        mutableStateOf("")
    }

    val isLoading =
        authViewModel.isLoading.value

    val errorMessage =
        authViewModel.errorMessage.value

    Scaffold(

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
                modifier = Modifier.height(16.dp)
            )

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

            Button(

                onClick = {

                    if (
                        password != confirmPassword
                    ) {

                        passwordError =
                            "Las contraseñas no coinciden"

                        return@Button
                    }

                    passwordError = ""

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

            TextButton(

                onClick = onBackToLogin

            ) {

                Text(
                    "Ya tengo cuenta"
                )
            }

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

            if (isLoading) {

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                CircularProgressIndicator()
            }
        }
    }
}