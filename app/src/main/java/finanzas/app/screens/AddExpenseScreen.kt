package finanzas.app.screens

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import finanzas.app.models.Expense
import finanzas.app.utils.expenseCategories
import finanzas.app.viewmodels.ExpenseViewModel
import java.util.Calendar
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(

    expenseViewModel: ExpenseViewModel,

    expenseToEdit: Expense? = null,

    onBack: () -> Unit

) {

    var title by remember {

        mutableStateOf(
            expenseToEdit?.title ?: ""
        )
    }

    var amount by remember {

        mutableStateOf(
            expenseToEdit?.amount
                ?.toString() ?: ""
        )
    }

    var selectedCategory by remember {

        mutableStateOf(
            expenseToEdit?.category ?: ""
        )
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    var selectedDate by remember {

        mutableStateOf(
            expenseToEdit?.date ?: ""
        )
    }

    var dateError by remember {

        mutableStateOf<String?>(null)
    }

    var amountError by remember {

        mutableStateOf<String?>(null)
    }

    val calendar = Calendar.getInstance()

    val year =
        calendar.get(Calendar.YEAR)

    val month =
        calendar.get(Calendar.MONTH)

    val day =
        calendar.get(Calendar.DAY_OF_MONTH)

    val context =
        LocalContext.current

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text(

                        if (expenseToEdit != null) {

                            "Editar gasto"

                        } else {

                            "Agregar gasto"
                        }
                    )
                }
            )
        }

    ) { paddingValues ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    MaterialTheme.colorScheme.background
                )
                .padding(20.dp),

            verticalArrangement =
                Arrangement.spacedBy(22.dp)

        ) {

            OutlinedTextField(

                value = title,

                onValueChange = {
                    title = it
                },

                label = {
                    Text("Título")
                },

                shape =
                    RoundedCornerShape(12.dp),

                modifier =
                    Modifier.fillMaxWidth()
            )

            OutlinedTextField(

                value = amount,

                onValueChange = {
                    amount = it
                },

                label = {
                    Text("Monto")
                },

                shape =
                    RoundedCornerShape(12.dp),

                modifier =
                    Modifier.fillMaxWidth()
            )

            amountError?.let {

                Text(

                    text = it,

                    color =
                        MaterialTheme
                            .colorScheme
                            .error
                )
            }

            ExposedDropdownMenuBox(

                expanded = expanded,

                onExpandedChange = {

                    expanded = !expanded
                }

            ) {

                OutlinedTextField(

                    value = selectedCategory,

                    onValueChange = {},

                    readOnly = true,

                    label = {
                        Text("Categoría")
                    },

                    shape =
                        RoundedCornerShape(12.dp),

                    trailingIcon = {

                        ExposedDropdownMenuDefaults
                            .TrailingIcon(

                                expanded =
                                    expanded
                            )
                    },

                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()

                )

                ExposedDropdownMenu(

                    expanded = expanded,

                    onDismissRequest = {
                        expanded = false
                    },

                    modifier = Modifier
                        .background(

                            MaterialTheme
                                .colorScheme
                                .surface
                        )

                ) {

                    expenseCategories
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

                                    selectedCategory =
                                        category

                                    expanded = false
                                }
                            )
                        }
                }
            }

            OutlinedButton(

                onClick = {

                    DatePickerDialog(

                        context,

                        {

                                _: DatePicker,

                                selectedYear: Int,

                                selectedMonth: Int,

                                selectedDay: Int ->

                            selectedDate =

                                "$selectedDay/${selectedMonth + 1}/$selectedYear"

                        },

                        year,
                        month,
                        day

                    ).show()
                },

                shape =
                    RoundedCornerShape(50.dp),

                modifier =
                    Modifier.fillMaxWidth()

            ) {

                Text(

                    text =

                        if (selectedDate.isEmpty()) {

                            "Seleccionar fecha"

                        } else {

                            selectedDate
                        }
                )
            }

            Button(

                onClick = {

                    if (

                        title.isNotBlank()

                        &&

                        amount.isNotBlank()

                        &&

                        selectedCategory.isNotBlank()

                        &&

                        selectedDate.isNotBlank()

                    ) {

                        val amountValue =
                            amount.toDoubleOrNull()

                        if (amountValue == null) {

                            amountError =
                                "Ingresa un monto válido"

                            return@Button
                        }

                        amountError = null

                        val today =
                            Calendar.getInstance()

                        val expenseDate =
                            Calendar.getInstance()

                        val parts =
                            selectedDate.split("/")

                        expenseDate.set(

                            parts[2].toInt(),

                            parts[1].toInt() - 1,

                            parts[0].toInt()
                        )

                        if (expenseDate.after(today)) {

                            dateError =
                                "No puedes agregar gastos futuros"

                            return@Button
                        }

                        dateError = null

                        val expense =

                            Expense(

                                id =

                                    expenseToEdit?.id

                                        ?:

                                        UUID.randomUUID()
                                            .toString(),

                                title = title,

                                amount =
                                    amountValue,

                                category =
                                    selectedCategory,

                                date =
                                    selectedDate
                            )

                        if (expenseToEdit != null) {

                            expenseViewModel
                                .updateExpense(
                                    expense
                                )

                        } else {

                            expenseViewModel
                                .addExpense(
                                    expense
                                )
                        }

                        onBack()
                    }
                },

                shape =
                    RoundedCornerShape(14.dp),

                modifier =
                    Modifier.fillMaxWidth()

            ) {

                Text(

                    if (expenseToEdit != null) {

                        "Actualizar gasto"

                    } else {

                        "Guardar gasto"
                    }
                )
            }

            dateError?.let {

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