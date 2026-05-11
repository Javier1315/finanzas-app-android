package finanzas.app.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun DonutChart(

    data: Map<String, Double>

) {

    val colors = listOf(

        Color(0xFF4285F4), // azul
        Color(0xFFEA4335), // rojo
        Color(0xFFFBBC05), // amarillo
        Color(0xFF34A853), // verde
        Color(0xFFAB47BC), // morado
        Color(0xFF00ACC1), // cyan

    )

    val total = data.values.sum()

    Canvas(

        modifier = Modifier.size(220.dp)

    ) {

        var startAngle = -90f

        data.entries.forEachIndexed { index, entry ->

            val sweepAngle =
                ((entry.value / total) * 360).toFloat()

            drawArc(

                color = colors[index % colors.size],

                startAngle = startAngle,

                sweepAngle = sweepAngle,

                useCenter = false,

                size = Size(size.width, size.height),

                style = Stroke(
                    width = 42f,
                    cap = StrokeCap.Round
                )

            )

            startAngle += sweepAngle
        }
    }
}