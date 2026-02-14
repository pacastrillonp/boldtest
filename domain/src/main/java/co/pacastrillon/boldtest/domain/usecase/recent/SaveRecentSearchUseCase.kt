package co.pacastrillon.boldtest.domain.usecase.recent

import co.pacastrillon.boldtest.domain.model.Location
import co.pacastrillon.boldtest.domain.repository.WeatherRepository
import javax.inject.Inject

class SaveRecentSearchUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(location: Location) {
        repository.saveRecentSearch(location)
    }
}
