package finanzas.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import finanzas.app.navigation.AppNavigation
import finanzas.app.ui.theme.Finanzas_AppTheme

// actividad principal de la aplicacion
class MainActivity : ComponentActivity() {

    // metodo ejecutado al iniciar la app
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // establece el contenido visual de la aplicacion
        setContent {

            // aplica el tema personalizado
            Finanzas_AppTheme {

                // inicia la navegacion entre pantallas
                AppNavigation()
            }
        }
    }
}