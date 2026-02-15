package co.pacastrillon.boldtest.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.pacastrillon.boldtest.common.Constants.SplashScreenTags.BOLD_WEATHER
import co.pacastrillon.boldtest.common.Constants.SplashScreenTags.SPLASH_ROOT
import co.pacastrillon.boldtest.ui.theme.BoldGradients
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToSearch: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(800)
        onNavigateToSearch()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BoldGradients.gradient)
            .testTag(SPLASH_ROOT),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.WbSunny,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = BOLD_WEATHER,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))
            CircularProgressIndicator(
                color = Color.White
            )
        }
    }
}