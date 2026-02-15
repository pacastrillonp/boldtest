package co.pacastrillon.boldtest.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.pacastrillon.boldtest.common.Constants.WeatherDetailScreenTags.CONDITION
import co.pacastrillon.boldtest.ui.models.ForecastUi
import co.pacastrillon.boldtest.ui.theme.BoldGradients
import coil.compose.AsyncImage

@Composable
fun DetailSection(data: ForecastUi) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(BoldGradients.gradient)
            .padding(bottom = 32.dp, start = 16.dp, end = 16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = data.todayLabel,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${data.tempNow}°",
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = 80.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(16.dp))
                AsyncImage(
                    model = data.conditionIconUrl,
                    contentDescription = CONDITION,
                    modifier = Modifier.size(80.dp)
                )
            }
            Text(
                text = data.conditionText,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                maxLines = 1,
                overflow = Ellipsis
            )
        }
    }
}