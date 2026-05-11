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

// pantalla utilizada para agregar o editar gastos
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(

    // viewmodel encargado de manejar gastos
    expenseViewModel: ExpenseViewModel,

    // gasto opcional para modo edicion
    expenseToEdit: Expense? = null,

    // funcion para regresar a la pantalla anterior
    onBack: () -> Unit

) {

    // estado del titulo del gasto
    var title by remember {

        mutableStateOf(
            expenseToEdit?.title ?: ""
        )
    }

    // estado del monto
    var amount by remember {

        mutableStateOf(
            expenseToEdit?.amount
                ?.toString() ?: ""
        )
    }

    // categoria seleccionada
    var selectedCategory by remember {

        mutableStateOf(
            expenseToEdit?.category ?: ""
        )
    }

    // controla apertura del menu desplegable
    var expanded by remember {
        mutableStateOf(false)
    }

    // fecha seleccionada
    var selectedDate by remember {

        mutableStateOf(
            expenseToEdit?.date ?: ""
        )
    }

    // mensaje de error para fechas
    var dateError by remember {

        mutableStateOf<String?>(null)
    }

    // mensaje de error para montos invalidos
    var amountError by remember {

        mutableStateOf<String?>(null)
    }

    // obtiene fecha actual
    val calendar = Calendar.getInstance()

    val year =
        calendar.get(Calendar.YEAR)

    val month =
        calendar.get(Calendar.MONTH)

    val day =
        calendar.get(Calendar.DAY_OF_MONTH)

    // contexto actual de la aplicacion
    val context =
        LocalContext.current

    Scaffold(

        // barra superior de la pantalla
        topBar = {

            TopAppBar(

                title = {

                    Text(

                        // cambia titulo segun modo agregar o editar
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

            // campo para titulo
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

            // campo para monto
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

            // muestra error de monto invalido
            amountError?.let {

                Text(

                    text = it,

                    color =
                        MaterialTheme
                            .colorScheme
                            .error
                )
            }

            // menu desplegable de categorias
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

                // opciones de categorias
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

            // boton para seleccionar fecha
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

            // boton principal para guardar o actualizar
            Button(

                onClick = {

                    // valida campos vacios
                    if (

                        title.isNotBlank()

                        &&

                        amount.isNotBlank()

                        &&

                        selectedCategory.isNotBlank()

                        &&

                        selectedDate.isNotBlank()

                    ) {

                        // convierte monto a numero
                        val amountValue =
                            amount.toDoubleOrNull()

                        // valida monto numerico
                        if (amountValue == null) {

                            amountError =
                                "Ingresa un monto válido"

                            return@Button
                        }

                        amountError = null

                        // obtiene fecha actual
                        val today =
                            Calendar.getInstance()

                        // convierte fecha seleccionada
                        val expenseDate =
                            Calendar.getInstance()

                        val parts =
                            selectedDate.split("/")

                        expenseDate.set(

                            parts[2].toInt(),

                            parts[1].toInt() - 1,

                            parts[0].toInt()
                        )

                        // evita registrar fechas futuras
                        if (expenseDate.after(today)) {

                            dateError =
                                "No puedes agregar gastos futuros"

                            return@Button
                        }

                        dateError = null

                        // crea objeto expense
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

                        // actualiza o crea gasto
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

                        // regresa a pantalla anterior
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

            // muestra error de fecha
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