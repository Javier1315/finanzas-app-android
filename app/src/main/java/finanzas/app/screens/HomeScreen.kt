package finanzas.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import finanzas.app.components.BarChart
import finanzas.app.components.ExpenseCard
import finanzas.app.utils.expenseCategories
import finanzas.app.viewmodels.ExpenseViewModel
import androidx.compose.foundation.shape.RoundedCornerShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(

    expenseViewModel: ExpenseViewModel,

    onAddExpenseClick: () -> Unit,

    onStatisticsClick: () -> Unit,
    onEditExpenseClick: (String) -> Unit,

    onLogoutClick: () -> Unit

) {

    var selectedFilter by remember {
        mutableStateOf("Todas")
    }

    var expandedFilter by remember {
        mutableStateOf(false)
    }

    val filteredExpenses =

        expenseViewModel.getFilteredExpenses(

            selectedFilter,

            "Todos"
        )

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text("Mis Gastos")
                },

                actions = {

                    IconButton(
                        onClick = onStatisticsClick
                    ) {

                        Icon(

                            Icons.Default.Menu,

                            contentDescription =
                                "Estadísticas"
                        )
                    }

                    IconButton(
                        onClick = onLogoutClick
                    ) {

                        Icon(

                            Icons.Default.ExitToApp,

                            contentDescription =
                                "Cerrar sesión"
                        )
                    }
                }
            )
        },

        floatingActionButton = {

            FloatingActionButton(
                onClick = onAddExpenseClick
            ) {

                Icon(

                    Icons.Default.Add,

                    contentDescription = null
                )
            }
        }

    ) { paddingValues ->

        LazyColumn(

            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    MaterialTheme
                        .colorScheme
                        .background
                )

                .padding(

                    start = 16.dp,

                    top = 16.dp,

                    end = 16.dp,

                    bottom = 100.dp
                ),

            verticalArrangement =
                Arrangement.spacedBy(16.dp)

        ) {

            item {

                Text(

                    text = "App de Finanzas DSM",

                    style =
                        MaterialTheme
                            .typography
                            .headlineMedium
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                ExposedDropdownMenuBox(

                    expanded = expandedFilter,

                    onExpandedChange = {

                        expandedFilter =
                            !expandedFilter
                    }

                ) {

                    OutlinedTextField(

                        value = selectedFilter,

                        onValueChange = {},

                        readOnly = true,

                        label = {
                            Text(
                                "Filtrar categoría"
                            )
                        },

                        trailingIcon = {

                            ExposedDropdownMenuDefaults
                                .TrailingIcon(

                                    expanded =
                                        expandedFilter
                                )
                        },

                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()

                    )

                    ExposedDropdownMenu(

                        expanded = expandedFilter,

                        onDismissRequest = {

                            expandedFilter = false
                        },

                        modifier = Modifier
                            .background(

                                MaterialTheme
                                    .colorScheme
                                    .surface
                            )

                    ) {

                        (
                                listOf("Todas")
                                        + expenseCategories

                                )

                            .forEach { category ->

                                DropdownMenuItem(

                                    colors =
                                        MenuDefaults
                                            .itemColors(

                                                textColor =

                                                    MaterialTheme
                                                        .colorScheme
                                                        .onSurface
                                            ),

                                    text = {

                                        Text(category)
                                    },

                                    onClick = {

                                        selectedFilter =
                                            category

                                        expandedFilter =
                                            false
                                    }
                                )
                            }
                    }
                }
            }

            item {

                Card(

                    modifier = Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(12.dp),

                    colors =

                        CardDefaults.cardColors(

                            containerColor =

                                MaterialTheme
                                    .colorScheme
                                    .surface
                                    .copy(alpha = 0.35f)
                        )
                ) {

                    Column(

                        modifier =
                            Modifier.padding(20.dp)

                    ) {

                        Text(

                            text = "Total gastado",

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .secondary
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(

                            text =
                                "$${filteredExpenses.sumOf { it.amount }}",

                            style =
                                MaterialTheme
                                    .typography
                                    .headlineLarge,

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .primary
                        )
                    }
                }
            }

            item {

                Card(

                    modifier = Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(12.dp),

                    colors =

                        CardDefaults.cardColors(

                            containerColor =

                                MaterialTheme
                                    .colorScheme
                                    .surface
                                    .copy(alpha = 0.35f)
                        )
                ) {

                    Column(

                        modifier =
                            Modifier.padding(20.dp)

                    ) {

                        Text(

                            text =
                                "Distribución de gastos",

                            style =
                                MaterialTheme
                                    .typography
                                    .titleMedium
                        )

                        Spacer(
                            modifier = Modifier.height(20.dp)
                        )

                        BarChart(

                            data =

                                expenseViewModel
                                    .getExpensesByMonth()
                        )
                    }
                }
            }

            items(filteredExpenses) { expense ->

                ExpenseCard(

                    expense = expense,

                    onDelete = {

                        expenseViewModel
                            .deleteExpense(
                                expense
                            )
                    },

                    onEdit = {

                        onEditExpenseClick(
                            expense.id
                        )
                    }
                )
            }
        }
    }
}