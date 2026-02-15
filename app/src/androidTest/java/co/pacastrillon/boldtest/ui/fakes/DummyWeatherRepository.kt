package co.pacastrillon.boldtest.ui.fakes

import co.pacastrillon.boldtest.domain.model.Forecast
import co.pacastrillon.boldtest.domain.model.Location
import co.pacastrillon.boldtest.domain.repository.WeatherRepository
import co.pacastrillon.boldtest.domain.result.WeatherResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DummyWeatherRepository : WeatherRepository {
    override suspend fun searchLocations(query: String): WeatherResult<List<Location>> {
        return WeatherResult.Success(emptyList())
    }

    override suspend fun getForecast3Days(locationQuery: String): WeatherResult<Forecast> {
        throw NotImplementedError("Not used in FakeViewModel")
    }

    override fun getRecentSearches(): Flow<List<Location>> {
        return flowOf(emptyList())
    }

    override suspend fun saveRecentSearch(location: Location) {
        // No-op
    }
}
