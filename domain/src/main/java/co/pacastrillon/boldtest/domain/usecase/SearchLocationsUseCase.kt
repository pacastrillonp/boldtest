package co.pacastrillon.boldtest.domain.usecase

import co.pacastrillon.boldtest.domain.model.Location
import co.pacastrillon.boldtest.domain.repository.WeatherRepository

class SearchLocationsUseCase(private val repo: WeatherRepository) {
    suspend operator fun invoke(query: String): List<Location> {
        return if (query.trim().length < 2) {
            emptyList()
        } else {
            repo.searchLocations(query.trim())
        }
    }
}
