package co.pacastrillon.boldtest.data.mappers

import co.pacastrillon.boldtest.data.local.model.ForecastCacheModel
import co.pacastrillon.boldtest.data.local.model.ForecastDayCacheModel
import co.pacastrillon.boldtest.domain.model.Forecast
import co.pacastrillon.boldtest.domain.model.ForecastDay
import co.pacastrillon.boldtest.domain.model.Location

fun Forecast.toCacheModel(): ForecastCacheModel {
    return ForecastCacheModel(
        locationName = location.name,
        locationCountry = location.country,
        locationLat = location.lat,
        locationLon = location.lon,
        days = days.map { it.toCacheModel() }
    )
}

fun ForecastDay.toCacheModel(): ForecastDayCacheModel {
    return ForecastDayCacheModel(
        date = date,
        avgTempC = avgTempC,
        conditionText = conditionText,
        conditionIcon = conditionIcon
    )
}

fun ForecastCacheModel.toDomain(): Forecast {
    return Forecast(
        location = Location(
            name = locationName,
            country = locationCountry,
            region = null, // Cache logic simplification
            lat = locationLat,
            lon = locationLon
        ),
        days = days.map { it.toDomain() }
    )
}

fun ForecastDayCacheModel.toDomain(): ForecastDay {
    return ForecastDay(
        date = date,
        avgTempC = avgTempC,
        conditionText = conditionText,
        conditionIcon = conditionIcon
    )
}
