package co.pacastrillon.boldtest.domain.usecase

import co.pacastrillon.boldtest.domain.model.Location
import co.pacastrillon.boldtest.domain.repository.WeatherRepository
import co.pacastrillon.boldtest.domain.result.WeatherResult
import javax.inject.Inject

class SearchLocationsUseCase @Inject constructor(private val repo: WeatherRepository) {
    suspend operator fun invoke(query: String): WeatherResult<List<Location>> {
        return if (query.trim().length < 2) {
            WeatherResult.Success(emptyList())
        } else {
            repo.searchLocations(query.trim())
        }
    }
}
