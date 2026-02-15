package co.pacastrillon.boldtest.ui.screens.detail

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import co.pacastrillon.boldtest.common.Constants.WeatherDetailScreenTags.DAY_ITEM
import co.pacastrillon.boldtest.common.Constants.WeatherDetailScreenTags.NEXT_DAYS
import co.pacastrillon.boldtest.common.Constants.WeatherDetailScreenTags.TODAY_FORECAST
import co.pacastrillon.boldtest.ui.designsystem.WeatherCard
import co.pacastrillon.boldtest.ui.models.ForecastUi

@Composable
fun LandscapeDetailLayout(data: ForecastUi) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            DetailSection(data)
            Spacer(modifier = Modifier.height(16.dp))
            if (data.days.isNotEmpty()) {
                Text(
                    text = TODAY_FORECAST,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                WeatherCard(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .testTag(DAY_ITEM)
                    ) {
                        DayForecastItem(data.days[0])
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            val nextDays = if (data.days.size > 1) data.days.drop(1) else emptyList()
            DetailForecastSection(
                days = nextDays,
                title = NEXT_DAYS,
                startIndex = 1
            )
        }
    }
}