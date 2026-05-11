package finanzas.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun BarChart(

    // Recibe un mapa con el mes y el total gastado
    data: Map<String, Double>

) {

    // Obtiene el valor más alto para escalar las barras proporcionalmente
    val maxValue =
        data.values.maxOrNull() ?: 1.0

    // Conversión de número de mes a abreviatura
    val monthNames = mapOf(

        "1" to "Ene",
        "2" to "Feb",
        "3" to "Mar",
        "4" to "Abr",
        "5" to "May",
        "6" to "Jun",
        "7" to "Jul",
        "8" to "Ago",
        "9" to "Sep",
        "10" to "Oct",
        "11" to "Nov",
        "12" to "Dic"
    )

    // Contenedor principal del grafico
    Row(

        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),

        horizontalArrangement =
            Arrangement.SpaceEvenly,

        verticalAlignment =
            Alignment.Bottom

    ) {

        // Recorre cada mes y genera una barra
        data.forEach { (month, value) ->

            // Calcula la altura proporcional de la barra
            val barHeight =

                ((value / maxValue) * 140)
                    .toFloat()

            Column(

                horizontalAlignment =
                    Alignment.CenterHorizontally

            ) {

                // Barra visual del gráfico
                Box(

                    modifier = Modifier
                        .width(28.dp)
                        .height(barHeight.dp)

                        // Bordes redondeados superiores
                        .clip(
                            RoundedCornerShape(
                                topStart = 8.dp,
                                topEnd = 8.dp
                            )
                        )

                        // Color principal de la aplicacion
                        .background(
                            MaterialTheme
                                .colorScheme
                                .primary
                        )
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                // Nombre abreviado del mes
                Text(
                    text = monthNames[month] ?: month
                )
            }
        }
    }
}