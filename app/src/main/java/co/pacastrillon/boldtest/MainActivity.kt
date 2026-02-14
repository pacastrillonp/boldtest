package co.pacastrillon.boldtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import co.pacastrillon.boldtest.ui.navigation.WeatherNavGraph
import co.pacastrillon.boldtest.ui.theme.BoldTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BoldTestTheme {
                WeatherNavGraph()
            }
        }
    }
}