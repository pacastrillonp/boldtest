package co.pacastrillon.boldtest.data.repository

import co.pacastrillon.boldtest.data.mappers.toDomain
import co.pacastrillon.boldtest.data.mappers.toDomain3Days
import co.pacastrillon.boldtest.data.remote.ApiService
import co.pacastrillon.boldtest.domain.model.Forecast
import co.pacastrillon.boldtest.domain.model.Location
import co.pacastrillon.boldtest.domain.repository.WeatherRepository
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: ApiService
) : WeatherRepository {

    override suspend fun searchLocations(query: String): List<Location> {
        return try {
            api.searchLocations(query).map { it.toDomain() }
        } catch (e: Exception) {
            throw mapException(e)
        }
    }

    override suspend fun getForecast3Days(locationQuery: String): Forecast {
        return try {
            api.getForecast(locationQuery, days = 3).toDomain3Days()
        } catch (e: Exception) {
            throw mapException(e)
        }
    }

    private fun mapException(e: Exception): Exception {
        return when (e) {
            is HttpException -> {
                when (e.code()) {
                    401, 403 -> SecurityException("Unauthorized access to Weather API", e)
                    in 500..599 -> IOException("Server error: ${e.code()}", e)
                    else -> IOException("HTTP Error: ${e.code()}", e)
                }
            }
            is SocketTimeoutException, is UnknownHostException -> IOException("Network error", e)
            else -> e
        }
    }
}
