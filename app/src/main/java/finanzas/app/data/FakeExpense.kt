package finanzas.app.data

import finanzas.app.models.Expense

//utilizada unicamente para pruebas, realmente no hace nada más que cargar datos
val fakeExpenses = listOf(

    Expense(
        id = "1",
        title = "Supermercado",
        amount = 49.50,
        category = "Comida",
        date = "9 May"
    ),

    Expense(
        id = "2",
        title = "Netflix",
        amount = 12.99,
        category = "Entretenimiento",
        date = "8 May"
    ),

    Expense(
        id = "3",
        title = "Gasolina",
        amount = 35.00,
        category = "Transporte",
        date = "7 May"
    ),

    Expense(
        id = "4",
        title = "Pizza",
        amount = 18.75,
        category = "Comida",
        date = "6 May"
    )

)