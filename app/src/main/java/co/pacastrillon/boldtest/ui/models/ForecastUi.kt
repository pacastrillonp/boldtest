package co.pacastrillon.boldtest.ui.models

import co.pacastrillon.boldtest.common.Constants.Defaults.AGE_FORMAT
import co.pacastrillon.boldtest.common.Constants.Defaults.DATE_FORMAT
import co.pacastrillon.boldtest.common.Constants.Defaults.EMPTY_DASH
import co.pacastrillon.boldtest.common.Constants.Defaults.EMPTY_STRING
import co.pacastrillon.boldtest.common.Constants.Defaults.TODAY
import co.pacastrillon.boldtest.domain.model.Forecast
import co.pacastrillon.boldtest.domain.model.ForecastDay
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class ForecastUi(
    val locationName: String,
    val todayLabel: String,
    val tempNow: String,
    val conditionText: String,
    val conditionIconUrl: String,
    val days: List<ForecastDayUi>
)

data class ForecastDayUi(
    val date: String,
    val avgTempC: Double,
    val conditionText: String,
    val iconUrl: String
)

fun Forecast.toUi(): ForecastUi {

    val currentDay = days.firstOrNull()

    val uiDays = days.take(3).map { it.toUi(isToday = it == currentDay) }

    return ForecastUi(
        locationName = location.name,
        todayLabel = TODAY,
        tempNow = currentDay?.avgTempC?.toString() ?: EMPTY_DASH,
        conditionText = currentDay?.conditionText ?: EMPTY_STRING,
        conditionIconUrl = currentDay?.conditionIcon ?: EMPTY_STRING,
        days = uiDays
    )
}

fun ForecastDay.toUi(isToday: Boolean): ForecastDayUi {
    val dateParsed = try {
        LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT))
    } catch (_: Exception) {
        null
    }

    val dateLabel = if (isToday) {
        TODAY
    } else {
        dateParsed?.format(DateTimeFormatter.ofPattern(AGE_FORMAT, Locale.getDefault())) ?: date
    }

    return ForecastDayUi(
        date = dateLabel,
        avgTempC = avgTempC,
        conditionText = conditionText,
        iconUrl = conditionIcon
    )
}
