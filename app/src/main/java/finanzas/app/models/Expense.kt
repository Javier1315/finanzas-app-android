package finanzas.app.models

// modelo que representa un gasto dentro de la aplicacion
data class Expense(

    // identificador unico del gasto
    val id: String = "",

    // titulo o descripcion del gasto
    val title: String = "",

    // monto del gasto
    val amount: Double = 0.0,

    // categoria asignada al gasto
    val category: String = "",

    // fecha del gasto
    val date: String = ""

)