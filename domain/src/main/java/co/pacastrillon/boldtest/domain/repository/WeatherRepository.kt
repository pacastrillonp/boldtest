package co.pacastrillon.boldtest.domain.repository

import co.pacastrillon.boldtest.domain.model.Forecast
import co.pacastrillon.boldtest.domain.model.Location

import co.pacastrillon.boldtest.domain.result.WeatherResult

interface WeatherRepository {
    suspend fun searchLocations(query: String): WeatherResult<List<Location>>
    suspend fun getForecast3Days(locationQuery: String): WeatherResult<Forecast>
}
