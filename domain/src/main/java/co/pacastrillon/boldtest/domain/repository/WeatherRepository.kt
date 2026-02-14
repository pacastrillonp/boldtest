package co.pacastrillon.boldtest.domain.repository

import co.pacastrillon.boldtest.domain.model.Forecast
import co.pacastrillon.boldtest.domain.model.Location

interface WeatherRepository {
    suspend fun searchLocations(query: String): List<Location>
    suspend fun getForecast3Days(locationQuery: String): Forecast
}
