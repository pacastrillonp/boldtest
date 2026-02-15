package co.pacastrillon.boldtest.domain.usecase

import co.pacastrillon.boldtest.domain.model.Forecast
import co.pacastrillon.boldtest.domain.model.ForecastDay
import co.pacastrillon.boldtest.domain.model.Location
import co.pacastrillon.boldtest.domain.repository.WeatherRepository
import co.pacastrillon.boldtest.domain.result.WeatherResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class GetForecast3DaysUseCaseTest {

    private val repository: WeatherRepository = mock(WeatherRepository::class.java)
    private val useCase = GetForecast3DaysUseCase(repository)

    @Test(expected = IllegalArgumentException::class)
    fun `invoke with blank query throws exception`() = runTest {
        // Given
        val query = " "

        // When
        useCase(query)
    }

    @Test
    fun `invoke with valid query calls repository and returns success`() = runTest {
        // Given
        val query = "Bogota"
        val location = Location("Bogota", "Colombia", "Cundinamarca", 4.0, -74.0)
        val days = listOf(
            ForecastDay("2023-10-01", 15.0, "Rainy", "//icon1"),
            ForecastDay("2023-10-02", 16.0, "Cloudy", "//icon2"),
            ForecastDay("2023-10-03", 17.0, "Sunny", "//icon3")
        )
        val expectedForecast = Forecast(location, days)
        `when`(repository.getForecast3Days(query)).thenReturn(WeatherResult.Success(expectedForecast))

        // When
        val result = useCase(query)

        // Then
        assertTrue(result is WeatherResult.Success)
        assertEquals(expectedForecast, (result as WeatherResult.Success).data)
        verify(repository).getForecast3Days(query)
    }
}
