package finanzas.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import finanzas.app.components.DonutChart
import finanzas.app.utils.months
import finanzas.app.viewmodels.ExpenseViewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

// pantalla de estadisticas de gastos
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(

    // viewmodel encargado de gestionar gastos
    expenseViewModel: ExpenseViewModel

) {

    // mes seleccionado para filtrar
    var selectedMonth by remember {
        mutableStateOf("Todos")
    }

    // controla apertura del menu desplegable
    var expandedMonth by remember {
        mutableStateOf(false)
    }

    // obtiene gastos filtrados por mes
    val filteredExpenses =

        expenseViewModel
            .getFilteredExpenses(

                "Todas",

                selectedMonth
            )

    // agrupa gastos por categoria y suma montos
    val expensesByCategory =

        filteredExpenses

            .groupBy {
                it.category
            }

            .mapValues { entry ->

                entry.value.sumOf {
                    it.amount
                }
            }

    Scaffold(

        // barra superior
        topBar = {

            TopAppBar(

                title = {
                    Text("Estadísticas")
                }
            )
        }

    ) { paddingValues ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)

                // permite scroll vertical
                .verticalScroll(
                    rememberScrollState()
                ),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.spacedBy(24.dp)

        ) {

            // titulo principal
            Text(

                text = "Resumen de Mis Gastos",

                style =
                    MaterialTheme
                        .typography
                        .headlineMedium
            )

            // tarjeta principal del grafico
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
                        Modifier.padding(20.dp),

                    horizontalAlignment =
                        Alignment.CenterHorizontally

                ) {

                    Box(

                        modifier = Modifier
                            .fillMaxWidth(),

                        contentAlignment =
                            Alignment.Center

                    ) {

                        // grafico tipo dona
                        DonutChart(
                            data = expensesByCategory
                        )

                        // informacion central del grafico
                        Column(

                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "Total"
                            )

                            // total general de gastos
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
            }

            // filtro por mes
            ExposedDropdownMenuBox(

                expanded = expandedMonth,

                onExpandedChange = {

                    expandedMonth =
                        !expandedMonth
                }

            ) {

                OutlinedTextField(

                    value = selectedMonth,

                    onValueChange = {},

                    readOnly = true,

                    label = {
                        Text("Filtrar mes")
                    },

                    shape =
                        RoundedCornerShape(12.dp),

                    trailingIcon = {

                        ExposedDropdownMenuDefaults
                            .TrailingIcon(

                                expanded =
                                    expandedMonth
                            )
                    },

                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()

                )

                // menu desplegable de meses
                ExposedDropdownMenu(

                    expanded = expandedMonth,

                    onDismissRequest = {

                        expandedMonth =
                            false
                    },

                    modifier = Modifier
                        .background(

                            MaterialTheme
                                .colorScheme
                                .surface
                        )

                ) {

                    months.forEach { month ->

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
                                Text(month)
                            },

                            onClick = {

                                // actualiza mes seleccionado
                                selectedMonth =
                                    month

                                expandedMonth =
                                    false
                            }
                        )
                    }
                }
            }

            // tarjeta de porcentajes por categoria
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
                        Modifier.padding(20.dp),

                    verticalArrangement =
                        Arrangement.spacedBy(14.dp)

                ) {

                    Text(

                        text =
                            "Distribución por categoría",

                        style =
                            MaterialTheme
                                .typography
                                .titleMedium
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    // muestra porcentaje de cada categoria
                    expensesByCategory.forEach {

                        val percentage =

                            expenseViewModel
                                .getCategoryPercentage(
                                    it.value
                                )

                        Text(

                            text =

                                "${it.key} - ${percentage.toInt()}%"
                        )
                    }
                }
            }
        }
    }
}
