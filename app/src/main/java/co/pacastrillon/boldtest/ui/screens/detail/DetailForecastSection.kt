package co.pacastrillon.boldtest.ui.screens.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import co.pacastrillon.boldtest.common.Constants.WeatherDetailScreenTags.DETAIL_DAYS_LIST
import co.pacastrillon.boldtest.common.Constants.WeatherDetailScreenTags.DETAIL_TODAY_ITEM
import co.pacastrillon.boldtest.ui.designsystem.WeatherCard
import co.pacastrillon.boldtest.ui.models.ForecastDayUi

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
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .testTag(DETAIL_DAYS_LIST)
            ) {
                days.forEachIndexed { index, day ->
                    val realIndex = startIndex + index
                    Box(modifier = Modifier.testTag("$DETAIL_TODAY_ITEM$realIndex")) {
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