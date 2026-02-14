package co.pacastrillon.boldtest.domain.result

sealed class WeatherResult<out T> {
    data class Success<T>(val data: T) : WeatherResult<T>()
    data class Error(val error: WeatherError) : WeatherResult<Nothing>()
}

enum class WeatherError {
    NETWORK,
    UNAUTHORIZED,
    SERVER,
    UNKNOWN
}
