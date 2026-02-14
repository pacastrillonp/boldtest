package co.pacastrillon.boldtest.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(
    @SerialName("name") val name: String,
    @SerialName("country") val country: String,
    @SerialName("region") val region: String? = null,
    @SerialName("lat") val lat: Double? = null,
    @SerialName("lon") val lon: Double? = null,
)
