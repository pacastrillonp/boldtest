package co.pacastrillon.boldtest.ui.screens.detail

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import co.pacastrillon.boldtest.ui.designsystem.BoldTopBar
import co.pacastrillon.boldtest.ui.designsystem.ErrorState
import co.pacastrillon.boldtest.ui.designsystem.WeatherCard
import co.pacastrillon.boldtest.ui.models.ForecastDayUi
import co.pacastrillon.boldtest.ui.theme.BoldGradients
import coil.compose.AsyncImage

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherDetailScreen(
    locationName: String,
    onBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    LaunchedEffect(locationName) {
        viewModel.load(locationName)
    }

    val uiState by viewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

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
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .testTag("detail_loading")
                )
            } else if (uiState.errorMessage != null) {
                ErrorState(
                    message = uiState.errorMessage ?: "Unknown Error",
                    onRetry = { viewModel.retry() },
                    modifier = Modifier.testTag("detail_error")
                )
            } else if (uiState.data != null) {
                WeatherDetailContent(
                    data = uiState.data!!,
                    isLandscape = isLandscape
                )
            }
        }
    }
}

@Composable
fun WeatherDetailContent(
    data: co.pacastrillon.boldtest.ui.models.ForecastUi,
    isLandscape: Boolean
) {
    if (isLandscape) {
        LandscapeDetailLayout(data)
    } else {
        PortraitDetailLayout(data)
    }
}

@Composable
fun PortraitDetailLayout(data: co.pacastrillon.boldtest.ui.models.ForecastUi) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        DetailHeroSection(data)
        DetailForecastSection(
            days = data.days,
            title = "3-Day Forecast",
            startIndex = 0
        )
    }
}

@Composable
fun LandscapeDetailLayout(data: co.pacastrillon.boldtest.ui.models.ForecastUi) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Left Pane: Summary + Today
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            DetailHeroSection(data)
            Spacer(modifier = Modifier.height(16.dp))
            if (data.days.isNotEmpty()) {
                Text(
                    text = "Today's Details",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                WeatherCard(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier
                        .padding(16.dp)
                        .testTag("day_item_0")) {
                        DayForecastItem(data.days[0])
                    }
                }
            }
        }

        // Right Pane: Next Days
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            val nextDays = if (data.days.size > 1) data.days.drop(1) else emptyList()
            DetailForecastSection(
                days = nextDays,
                title = "Next Days",
                startIndex = 1
            )
        }
    }
}

@Composable
fun DetailHeroSection(data: co.pacastrillon.boldtest.ui.models.ForecastUi) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(BoldGradients.Hero)
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
                    contentDescription = "Condition",
                    modifier = Modifier.size(80.dp)
                )
            }
            Text(
                text = data.conditionText,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun DetailForecastSection(
    days: List<ForecastDayUi>,
    title: String,
    startIndex: Int
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        WeatherCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier
                .padding(16.dp)
                .testTag("detail_days_list")) {
                days.forEachIndexed { index, day ->
                    // Calculate the real index based on start position for stable tags
                    val realIndex = startIndex + index
                    Box(modifier = Modifier.testTag("day_item_$realIndex")) {
                        DayForecastItem(day)
                    }
                    if (index < days.size - 1) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun DayForecastItem(day: ForecastDayUi) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = day.date,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
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
