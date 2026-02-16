package co.pacastrillon.boldtest.data.local.model

import kotlinx.serialization.Serializable

@Serializable
data class ForecastCacheModel(
    val locationName: String,
    val locationCountry: String,
    val locationLat: Double,
    val locationLon: Double,
    val days: List<ForecastDayCacheModel>
)

@Serializable
data class ForecastDayCacheModel(
    val date: String,
    val avgTempC: Double,
    val conditionText: String,
    val conditionIcon: String
)