package co.pacastrillon.boldtest.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastDto(
    @SerialName("location") val location: LocationDto,
    @SerialName("forecast") val forecast: ForecastDaysDto
)

@Serializable
data class ForecastDaysDto(
    @SerialName("forecastday") val forecastday: List<ForecastDayDto>
)

@Serializable
data class ForecastDayDto(
    @SerialName("date") val date: String,
    @SerialName("day") val day: DayDto
)

@Serializable
data class DayDto(
    @SerialName("avgtemp_c") val avgTempC: Double,
    @SerialName("condition") val condition: ConditionDto
)

@Serializable
data class ConditionDto(
    @SerialName("text") val text: String,
    @SerialName("icon") val icon: String
)
