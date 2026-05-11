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

    // Recibe categorías y montos para generar el gráfico
    data: Map<String, Double>

) {

    // Lista de colores utilizados en las secciones del gráfico
    val colors = listOf(

        Color(0xFF4285F4), // azul
        Color(0xFFEA4335), // rojo
        Color(0xFFFBBC05), // amarillo
        Color(0xFF34A853), // verde
        Color(0xFFAB47BC), // morado
        Color(0xFF00ACC1), // cyan

    )

    // Calcula el total general de gastos
    val total = data.values.sum()

    // Área de dibujo del gráfico
    Canvas(

        modifier = Modifier.size(220.dp)

    ) {

        // Ángulo inicial del gráfico
        var startAngle = -90f

        // Recorre cada categoría para dibujar una sección
        data.entries.forEachIndexed { index, entry ->

            // Calcula el tamaño proporcional de cada sección
            val sweepAngle =
                ((entry.value / total) * 360).toFloat()

            // Dibuja el arco correspondiente
            drawArc(

                color = colors[index % colors.size],

                startAngle = startAngle,

                sweepAngle = sweepAngle,

                useCenter = false,

                size = Size(size.width, size.height),

                // Define grosor y estilo del gráfico
                style = Stroke(
                    width = 42f,
                    cap = StrokeCap.Round
                )

            )

            // Actualiza el ángulo inicial para la siguiente sección
            startAngle += sweepAngle
        }
    }
}