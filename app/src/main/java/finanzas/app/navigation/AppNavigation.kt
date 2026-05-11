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

@Composable
fun AppNavigation() {

    val navController =
        rememberNavController()

    val expenseViewModel:
            ExpenseViewModel = viewModel()

    val authViewModel:
            AuthViewModel = viewModel()

    val startDestination =

        if (authViewModel.isUserLoggedIn()) {

            "home"

        } else {

            "login"
        }

    NavHost(

        navController = navController,

        startDestination = startDestination

    ) {

        composable("login") {

            LoginScreen(

                authViewModel = authViewModel,

                onLoginSuccess = {

                    expenseViewModel.loadExpenses()

                    navController.navigate("home") {

                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                },

                onNavigateToRegister = {

                    navController.navigate(
                        "register"
                    )
                }
            )
        }

        composable("register") {

            RegisterScreen(

                authViewModel = authViewModel,

                onRegisterSuccess = {

                    expenseViewModel.loadExpenses()

                    navController.navigate("home") {

                        popUpTo("register") {
                            inclusive = true
                        }
                    }
                },

                onBackToLogin = {

                    navController.popBackStack()
                }
            )
        }

        composable("home") {

            HomeScreen(

                expenseViewModel =
                    expenseViewModel,

                onAddExpenseClick = {

                    navController.navigate(
                        "add_expense"
                    )
                },

                onStatisticsClick = {

                    navController.navigate(
                        "statistics"
                    )
                },

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

                onEditExpenseClick = {

                        expenseId ->

                    navController.navigate(

                        "edit_expense/$expenseId"
                    )
                }
            )
        }

        composable("add_expense") {

            AddExpenseScreen(

                expenseViewModel =
                    expenseViewModel,

                onBack = {

                    navController.popBackStack()
                }
            )
        }

        composable(

            "edit_expense/{expenseId}"

        ) { backStackEntry ->

            val expenseId =

                backStackEntry
                    .arguments
                    ?.getString(
                        "expenseId"
                    )

            val expense =

                expenseViewModel
                    .getExpenseById(

                        expenseId ?: ""
                    )

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

        composable("statistics") {

            StatisticsScreen(

                expenseViewModel =
                    expenseViewModel
            )
        }
    }
}