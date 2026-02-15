package co.pacastrillon.boldtest.ui.models

import android.os.Build
import androidx.annotation.RequiresApi
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

@RequiresApi(Build.VERSION_CODES.O)
fun Forecast.toUi(): ForecastUi {

    val currentDay = days.firstOrNull()

    val uiDays = days.take(3).map { it.toUi(isToday = it == currentDay) }

    return ForecastUi(
        locationName = location.name,
        todayLabel = "Today",
        tempNow = currentDay?.avgTempC?.toString() ?: "--",
        conditionText = currentDay?.conditionText ?: "",
        conditionIconUrl = currentDay?.conditionIcon ?: "",
        days = uiDays
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun ForecastDay.toUi(isToday: Boolean): ForecastDayUi {
    val dateParsed = try {
        LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    } catch (_: Exception) {
        null
    }

    val dateLabel = if (isToday) {
        "Today"
    } else {
        dateParsed?.format(DateTimeFormatter.ofPattern("EEEE", Locale.getDefault())) ?: date
    }

    return ForecastDayUi(
        date = dateLabel,
        avgTempC = avgTempC,
        conditionText = conditionText,
        iconUrl = conditionIcon
    )
}
