package co.pacastrillon.boldtest.domain.model

data class Location(
    val name: String,
    val country: String,
    val region: String? = null,
    val lat: Double,
    val lon: Double
)
