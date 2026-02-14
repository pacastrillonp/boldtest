package co.pacastrillon.boldtest.data.repository

import co.pacastrillon.boldtest.data.local.dao.ForecastCacheDao
import co.pacastrillon.boldtest.data.local.dao.RecentSearchDao
import co.pacastrillon.boldtest.data.local.entities.ForecastCacheEntity
import co.pacastrillon.boldtest.data.local.entities.RecentSearchEntity
import co.pacastrillon.boldtest.data.mappers.toCacheModel
import co.pacastrillon.boldtest.data.mappers.toDomain
import co.pacastrillon.boldtest.data.mappers.toDomain3Days
import co.pacastrillon.boldtest.data.remote.ApiService
import co.pacastrillon.boldtest.data.remote.NetworkApiJson
import co.pacastrillon.boldtest.domain.model.Forecast
import co.pacastrillon.boldtest.domain.model.Location
import co.pacastrillon.boldtest.domain.repository.WeatherRepository
import co.pacastrillon.boldtest.domain.result.WeatherError
import co.pacastrillon.boldtest.domain.result.WeatherResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val recentSearchDao: RecentSearchDao,
    private val forecastCacheDao: ForecastCacheDao,
    @NetworkApiJson private val jsonSerializer: Json
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
        val locationKey = locationQuery.lowercase().trim()
        return try {
            val result = api.getForecast(locationQuery, days = 3).toDomain3Days()

            // Save to cache
            try {
                val cacheModel = result.toCacheModel()
                val jsonString = jsonSerializer.encodeToString(cacheModel)
                forecastCacheDao.upsert(
                    ForecastCacheEntity(
                        locationKey = locationKey,
                        json = jsonString,
                        updatedAt = System.currentTimeMillis()
                    )
                )
            } catch (e: Exception) {
                // Ignore cache save errors, don't block the UI
                e.printStackTrace()
            }

            WeatherResult.Success(result)
        } catch (e: Exception) {
            val weatherError = mapExceptionToWeatherError(e)

            // If network error, try fallback to cache
            if (weatherError == WeatherError.NETWORK) {
                try {
                    val cached = forecastCacheDao.getByKey(locationKey)
                    if (cached != null) {
                        val cacheModel =
                            jsonSerializer.decodeFromString<co.pacastrillon.boldtest.data.local.model.ForecastCacheModel>(
                                cached.json
                            )
                        return WeatherResult.Success(cacheModel.toDomain())
                    }
                } catch (cacheEx: Exception) {
                    cacheEx.printStackTrace()
                }
            }

            WeatherResult.Error(weatherError)
        }
    }

    override fun getRecentSearches(): Flow<List<Location>> {
        return recentSearchDao.observeRecents(limit = 10).map { entities ->
            entities.map { entity ->
                Location(
                    name = entity.name,
                    country = entity.country,
                    region = null, // Not stored in recents
                    lat = 0.0, // Not stored
                    lon = 0.0
                )
            }
        }
    }

    override suspend fun saveRecentSearch(location: Location) {
        val key = "${location.name}|${location.country}".lowercase()
        val entity = RecentSearchEntity(
            locationKey = key,
            name = location.name,
            country = location.country,
            lastUsed = System.currentTimeMillis()
        )
        recentSearchDao.upsert(entity)
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
