package co.pacastrillon.boldtest.domain.model

data class ForecastDay(
    val date: String,
    val avgTempC: Double,
    val conditionText: String,
    val conditionIconUrl: String
)
