package finanzas.app.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import finanzas.app.screens.AddExpenseScreen
import finanzas.app.screens.HomeScreen
import finanzas.app.screens.LoginScreen
import finanzas.app.screens.RegisterScreen
import finanzas.app.screens.StatisticsScreen
import finanzas.app.viewmodels.AuthViewModel
import finanzas.app.viewmodels.ExpenseViewModel

// funcion principal encargada de manejar la navegacion
@Composable
fun AppNavigation() {

    // controlador de navegacion entre pantallas
    val navController =
        rememberNavController()

    // viewmodel para gestionar gastos
    val expenseViewModel:
            ExpenseViewModel = viewModel()

    // viewmodel para autenticacion
    val authViewModel:
            AuthViewModel = viewModel()

    // define la pantalla inicial segun sesion activa
    val startDestination =

        if (authViewModel.isUserLoggedIn()) {

            "home"

        } else {

            "login"
        }

    // contenedor principal de rutas
    NavHost(

        navController = navController,

        startDestination = startDestination

    ) {

        // pantalla de inicio de sesion
        composable("login") {

            LoginScreen(

                authViewModel = authViewModel,

                onLoginSuccess = {

                    // carga gastos al iniciar sesion
                    expenseViewModel.loadExpenses()

                    // navega a home
                    navController.navigate("home") {

                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                },

                // navega a registro
                onNavigateToRegister = {

                    navController.navigate(
                        "register"
                    )
                }
            )
        }

        // pantalla de registro
        composable("register") {

            RegisterScreen(

                authViewModel = authViewModel,

                onRegisterSuccess = {

                    // carga gastos despues del registro
                    expenseViewModel.loadExpenses()

                    // navega a home
                    navController.navigate("home") {

                        popUpTo("register") {
                            inclusive = true
                        }
                    }
                },

                // regresa a login
                onBackToLogin = {

                    navController.popBackStack()
                }
            )
        }

        // pantalla principal
        composable("home") {

            HomeScreen(

                expenseViewModel =
                    expenseViewModel,

                // navega a agregar gasto
                onAddExpenseClick = {

                    navController.navigate(
                        "add_expense"
                    )
                },

                // navega a estadisticas
                onStatisticsClick = {

                    navController.navigate(
                        "statistics"
                    )
                },

                // cierra sesion y limpia datos
                onLogoutClick = {

                    expenseViewModel
                        .clearExpenses()

                    authViewModel.logout()

                    navController.navigate(
                        "login"
                    ) {

                        popUpTo("home") {
                            inclusive = true
                        }
                    }
                },

                // navega a editar gasto
                onEditExpenseClick = {

                        expenseId ->

                    navController.navigate(

                        "edit_expense/$expenseId"
                    )
                }
            )
        }

        // pantalla para agregar gasto
        composable("add_expense") {

            AddExpenseScreen(

                expenseViewModel =
                    expenseViewModel,

                onBack = {

                    navController.popBackStack()
                }
            )
        }

        // pantalla para editar gasto
        composable(

            "edit_expense/{expenseId}"

        ) { backStackEntry ->

            // obtiene el id del gasto seleccionado
            val expenseId =

                backStackEntry
                    .arguments
                    ?.getString(
                        "expenseId"
                    )

            // busca el gasto por id
            val expense =

                expenseViewModel
                    .getExpenseById(

                        expenseId ?: ""
                    )

            // abre pantalla de edicion
            if (expense != null) {

                AddExpenseScreen(

                    expenseViewModel =
                        expenseViewModel,

                    expenseToEdit =
                        expense,

                    onBack = {

                        navController
                            .popBackStack()
                    }
                )
            }
        }

        // pantalla de estadisticas
        composable("statistics") {

            StatisticsScreen(

                expenseViewModel =
                    expenseViewModel
            )
        }
    }
}