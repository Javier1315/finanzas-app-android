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

    data: Map<String, Double>

) {

    val maxValue =
        data.values.maxOrNull() ?: 1.0

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

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),

        horizontalArrangement =
            Arrangement.SpaceEvenly,

        verticalAlignment =
            Alignment.Bottom

    ) {

        data.forEach { (month, value) ->

            val barHeight =

                ((value / maxValue) * 140)
                    .toFloat()

            Column(

                horizontalAlignment =
                    Alignment.CenterHorizontally

            ) {

                Box(

                    modifier = Modifier
                        .width(28.dp)
                        .height(barHeight.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 8.dp,
                                topEnd = 8.dp
                            )
                        )

                        .background(
                            MaterialTheme
                                .colorScheme
                                .primary
                        )
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = monthNames[month] ?: month
                )
            }
        }
    }
}