package co.pacastrillon.boldtest.data.repository

import co.pacastrillon.boldtest.data.mappers.toDomain
import co.pacastrillon.boldtest.data.mappers.toDomain3Days
import co.pacastrillon.boldtest.data.remote.ApiService
import co.pacastrillon.boldtest.domain.model.Forecast
import co.pacastrillon.boldtest.domain.model.Location
import co.pacastrillon.boldtest.domain.repository.WeatherRepository
import co.pacastrillon.boldtest.domain.result.WeatherError
import co.pacastrillon.boldtest.domain.result.WeatherResult
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: ApiService
) : WeatherRepository {

    override suspend fun searchLocations(query: String): WeatherResult<List<Location>> {
        return try {
            val result = api.searchLocations(query).map { it.toDomain() }
            WeatherResult.Success(result)
        } catch (e: Exception) {
            WeatherResult.Error(mapExceptionToWeatherError(e))
        }
    }

    override suspend fun getForecast3Days(locationQuery: String): WeatherResult<Forecast> {
        return try {
            val result = api.getForecast(locationQuery, days = 3).toDomain3Days()
            WeatherResult.Success(result)
        } catch (e: Exception) {
            WeatherResult.Error(mapExceptionToWeatherError(e))
        }
    }

    private fun mapExceptionToWeatherError(e: Exception): WeatherError {
        return when (e) {
            is HttpException -> {
                when (e.code()) {
                    401, 403 -> WeatherError.UNAUTHORIZED
                    in 500..599 -> WeatherError.SERVER
                    else -> WeatherError.UNKNOWN
                }
            }

            is SocketTimeoutException, is UnknownHostException, is IOException -> WeatherError.NETWORK
            else -> WeatherError.UNKNOWN
        }
    }
}
