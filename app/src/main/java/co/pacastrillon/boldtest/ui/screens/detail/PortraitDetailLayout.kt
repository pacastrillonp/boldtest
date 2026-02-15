package co.pacastrillon.boldtest.ui.screens.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.pacastrillon.boldtest.common.Constants.WeatherDetailScreenTags.THREE_DAYS_FORECAST
import co.pacastrillon.boldtest.ui.models.ForecastUi

@Composable
fun PortraitDetailLayout(data: ForecastUi) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        DetailSection(data)
        DetailForecastSection(
            days = data.days,
            title = THREE_DAYS_FORECAST,
            startIndex = 0
        )
    }
}