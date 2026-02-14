package co.pacastrillon.boldtest.domain.usecase

import co.pacastrillon.boldtest.domain.model.Forecast
import co.pacastrillon.boldtest.domain.repository.WeatherRepository

class GetForecast3DaysUseCase(private val repo: WeatherRepository) {
    suspend operator fun invoke(locationQuery: String): Forecast {
        if (locationQuery.trim().isEmpty()) {
            throw IllegalArgumentException("locationQuery is blank")
        }
        return repo.getForecast3Days(locationQuery.trim())
    }
}
