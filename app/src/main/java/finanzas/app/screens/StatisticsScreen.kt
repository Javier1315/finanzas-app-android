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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(

    expenseViewModel: ExpenseViewModel

) {

    var selectedMonth by remember {
        mutableStateOf("Todos")
    }

    var expandedMonth by remember {
        mutableStateOf(false)
    }

    val filteredExpenses =

        expenseViewModel
            .getFilteredExpenses(

                "Todas",

                selectedMonth
            )

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
                .verticalScroll(
                    rememberScrollState()
                ),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.spacedBy(24.dp)

        ) {

            Text(

                text = "Resumen de Mis Gastos",

                style =
                    MaterialTheme
                        .typography
                        .headlineMedium
            )

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

                    )
                    {

                        DonutChart(
                            data = expensesByCategory
                        )

                        Column(

                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "Total"
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
            }

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

                                selectedMonth =
                                    month

                                expandedMonth =
                                    false
                            }
                        )
                    }
                }
            }

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