package finanzas.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import finanzas.app.navigation.AppNavigation
import finanzas.app.ui.theme.Finanzas_AppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Finanzas_AppTheme {
                AppNavigation()
            }
        }
    }
}