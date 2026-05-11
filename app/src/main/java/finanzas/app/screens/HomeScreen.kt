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

// pantalla principal de la aplicacion
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(

    // viewmodel encargado de gestionar gastos
    expenseViewModel: ExpenseViewModel,

    // navega a pantalla para agregar gastos
    onAddExpenseClick: () -> Unit,

    // navega a estadisticas
    onStatisticsClick: () -> Unit,

    // navega a editar gasto
    onEditExpenseClick: (String) -> Unit,

    // cierra sesion del usuario
    onLogoutClick: () -> Unit

) {

    // categoria seleccionada para filtrar
    var selectedFilter by remember {
        mutableStateOf("Todas")
    }

    // controla apertura del menu desplegable
    var expandedFilter by remember {
        mutableStateOf(false)
    }

    // obtiene lista filtrada de gastos
    val filteredExpenses =

        expenseViewModel.getFilteredExpenses(

            selectedFilter,

            "Todos"
        )

    Scaffold(

        // barra superior de navegacion
        topBar = {

            TopAppBar(

                title = {
                    Text("Mis Gastos")
                },

                actions = {

                    // boton para abrir estadisticas
                    IconButton(
                        onClick = onStatisticsClick
                    ) {

                        Icon(

                            Icons.Default.Menu,

                            contentDescription =
                                "Estadísticas"
                        )
                    }

                    // boton para cerrar sesion
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

        // boton flotante para agregar gasto
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

        // lista principal scrolleable
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

                // titulo principal
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

                // filtro por categoria
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

                    // menu de categorias
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

                                        // actualiza filtro seleccionado
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

                // tarjeta con total gastado
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

                        // muestra suma total de gastos
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

                // tarjeta del grafico mensual
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

                        // grafico de barras por mes
                        BarChart(

                            data =

                                expenseViewModel
                                    .getExpensesByMonth()
                        )
                    }
                }
            }

            // lista de gastos registrados
            items(filteredExpenses) { expense ->

                ExpenseCard(

                    expense = expense,

                    // elimina gasto seleccionado
                    onDelete = {

                        expenseViewModel
                            .deleteExpense(
                                expense
                            )
                    },

                    // abre pantalla para editar
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