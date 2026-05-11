package finanzas.app.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import finanzas.app.models.Expense

@Composable
fun ExpenseCard(

    // recibe el gasto a mostrar
    expense: Expense,

    // funcion para eliminar gasto
    onDelete: () -> Unit,

    // funcion para editar gasto
    onEdit: () -> Unit

) {

    // tarjeta principal del gasto
    Card(

        onClick = { },

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(12.dp),

        colors = CardDefaults.cardColors(

            containerColor =

                MaterialTheme
                    .colorScheme
                    .surface
                    .copy(alpha = 0.35f)
        )

    ) {

        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            horizontalArrangement =
                Arrangement.SpaceBetween,

            verticalAlignment =
                Alignment.CenterVertically

        ) {

            // informacion principal del gasto
            Column(

                modifier = Modifier.weight(1f)

            ) {

                Text(
                    text = expense.title
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                // categoria del gasto
                Text(

                    text = expense.category,

                    color =
                        MaterialTheme
                            .colorScheme
                            .secondary

                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                // fecha del gasto
                Text(

                    text = expense.date,

                    color =
                        MaterialTheme
                            .colorScheme
                            .secondary

                )
            }

            Spacer(
                modifier = Modifier.width(16.dp)
            )

            // seccion derecha con monto y acciones
            Column(

                horizontalAlignment =
                    Alignment.End

            ) {

                // monto total del gasto
                Text(

                    text = "$${expense.amount}",

                    color =
                        MaterialTheme
                            .colorScheme
                            .primary

                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Row {

                    // boton para editar gasto
                    IconButton(
                        onClick = onEdit
                    ) {

                        Icon(

                            Icons.Default.Edit,

                            contentDescription = "Editar"
                        )
                    }

                    // boton para eliminar gasto
                    IconButton(
                        onClick = onDelete
                    ) {

                        Icon(

                            Icons.Default.Delete,

                            contentDescription = "Eliminar",

                            tint =
                                MaterialTheme
                                    .colorScheme
                                    .error
                        )
                    }
                }
            }
        }
    }
}