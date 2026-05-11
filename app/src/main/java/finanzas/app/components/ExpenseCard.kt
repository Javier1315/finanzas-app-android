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

    expense: Expense,

    onDelete: () -> Unit,

    onEdit: () -> Unit

) {

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

            Column(

                modifier = Modifier.weight(1f)

            ) {

                Text(
                    text = expense.title
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

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

            Column(

                horizontalAlignment =
                    Alignment.End

            ) {

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

                    IconButton(
                        onClick = onEdit
                    ) {

                        Icon(

                            Icons.Default.Edit,

                            contentDescription = "Editar"
                        )
                    }

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