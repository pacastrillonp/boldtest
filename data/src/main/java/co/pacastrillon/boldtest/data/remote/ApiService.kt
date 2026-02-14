package co.pacastrillon.boldtest.data.remote

import co.pacastrillon.boldtest.data.dto.ForecastDto
import co.pacastrillon.boldtest.data.dto.LocationDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("v1/search.json")
    suspend fun searchLocations(
        @Query("q") query: String
    ): List<LocationDto>

    @GET("v1/forecast.json")
    suspend fun getForecast(
        @Query("q") query: String,
        @Query("days") days: Int = 3
    ): ForecastDto
}