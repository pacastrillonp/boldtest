package co.pacastrillon.boldtest.domain.usecase

import co.pacastrillon.boldtest.common.Constants.Messages.LOCATION_QUERY_EMPTY
import co.pacastrillon.boldtest.domain.model.Forecast
import co.pacastrillon.boldtest.domain.repository.WeatherRepository
import co.pacastrillon.boldtest.domain.result.WeatherResult
import javax.inject.Inject

class GetForecast3DaysUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(locationQuery: String): WeatherResult<Forecast> {
        if (locationQuery.trim().isEmpty()) {
            throw IllegalArgumentException(LOCATION_QUERY_EMPTY)
        }
        return weatherRepository.getForecast3Days(locationQuery.trim())
    }
}