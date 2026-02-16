package co.pacastrillon.boldtest.data.mappers

import co.pacastrillon.boldtest.common.Constants.Defaults.EXPECTED_FORECAST
import co.pacastrillon.boldtest.data.dto.ForecastDayDto
import co.pacastrillon.boldtest.data.dto.ForecastDto
import co.pacastrillon.boldtest.data.dto.LocationDto
import co.pacastrillon.boldtest.domain.model.Forecast
import co.pacastrillon.boldtest.domain.model.ForecastDay
import co.pacastrillon.boldtest.domain.model.Location

fun LocationDto.toDomain(): Location {
    return Location(
        name = this.name,
        country = this.country,
        region = this.region,
        lat = this.lat ?: 0.0,
        lon = this.lon ?: 0.0
    )
}

fun ForecastDto.toDomain3Days(): Forecast {
    val forecastDays = this.forecast.forecastDay
    if (forecastDays.size < 3) {
        throw IllegalStateException("$EXPECTED_FORECAST ${forecastDays.size}")
    }

    val threeDays = forecastDays.take(3).map { it.toDomain() }

    return Forecast(
        location = this.location.toDomain(),
        days = threeDays
    )
}

fun ForecastDayDto.toDomain(): ForecastDay {
    val iconUrl = this.day.condition.icon.let {
        if (it.startsWith("//")) "https:$it" else it
    }

    return ForecastDay(
        date = this.date,
        avgTempC = this.day.avgTempC,
        conditionText = this.day.condition.text,
        conditionIcon = iconUrl
    )
}
