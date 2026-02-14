package co.pacastrillon.boldtest.ui.screens.detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import co.pacastrillon.boldtest.ui.designsystem.BoldTopBar
import co.pacastrillon.boldtest.ui.designsystem.ErrorState
import co.pacastrillon.boldtest.ui.designsystem.WeatherCard
import co.pacastrillon.boldtest.ui.theme.BoldGradients

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherDetailScreen(
    locationName: String,
    onBack: () -> Unit,
    viewModel: DetailViewModel = viewModel()
) {
    LaunchedEffect(locationName) {
        viewModel.load(locationName)
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            BoldTopBar(
                title = locationName,
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .testTag("detail_root")
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center).testTag("detail_loading"))
            } else if (uiState.errorMessage != null) {
                ErrorState(
                    message = uiState.errorMessage ?: "Unknown Error",
                    onRetry = { viewModel.load(locationName) },
                    modifier = Modifier.testTag("detail_error")
                )
            } else if (uiState.data != null) {
                val data = uiState.data!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Hero Section
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BoldGradients.Hero)
                            .padding(bottom = 32.dp, start = 16.dp, end = 16.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = data.date,
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
                                    contentDescription = "Condition",
                                    modifier = Modifier.size(80.dp)
                                )
                            }
                            Text(
                                text = data.conditionText,
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color.White
                            )
                        }
                    }
                    
                    // Forecast Section
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "3-Day Forecast",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        WeatherCard(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp).testTag("detail_days_list")) {
                                data.days.forEachIndexed { index, day ->
                                    Box(modifier = Modifier.testTag("day_item_$index")) {
                                        DayForecastItem(day)
                                    }
                                    if (index < data.days.size - 1) {
                                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DayForecastItem(day: DayForecast) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = day.date, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        }
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = day.iconUrl,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = day.conditionText, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "${day.avgTempC}°",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
