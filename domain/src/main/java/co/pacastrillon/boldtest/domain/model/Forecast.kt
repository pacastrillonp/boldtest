package co.pacastrillon.boldtest.domain.model

data class Forecast(
    val location: Location,
    val days: List<ForecastDay>
)
