package co.pacastrillon.boldtest.domain.model

data class Forecast(
    val locationName: String,
    val days: List<ForecastDay>
)
