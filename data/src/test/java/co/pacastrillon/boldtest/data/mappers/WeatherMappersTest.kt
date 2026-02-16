package co.pacastrillon.boldtest.data.mappers

import co.pacastrillon.boldtest.data.dto.ConditionDto
import co.pacastrillon.boldtest.data.dto.DayDto
import co.pacastrillon.boldtest.data.dto.ForecastDaysDto
import co.pacastrillon.boldtest.data.dto.ForecastDto
import co.pacastrillon.boldtest.data.dto.ForecastDayDto
import co.pacastrillon.boldtest.data.dto.LocationDto
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherMappersTest {

    @Test
    fun `LocationDto toDomain maps correctly`() {
        // Given
        val dto = LocationDto(
            name = "Medellin",
            country = "Colombia",
            region = "Antioquia",
            lat = 6.25,
            lon = -75.56
        )

        // When
        val result = dto.toDomain()

        // Then
        assertEquals("Medellin", result.name)
        assertEquals("Colombia", result.country)
        assertEquals("Antioquia", result.region)
        assertEquals(6.25, result.lat, 0.0)
        assertEquals(-75.56, result.lon, 0.0)
    }

    @Test
    fun `ForecastDto toDomain3Days maps correctly and normalizes icon`() {
        // Given
        val locationDto = LocationDto("Test", "TestCountry")
        val days = listOf(
            createForecastDayDto("2023-10-01", "//cdn.weatherapi.com/icon1.png"),
            createForecastDayDto("2023-10-02", "https://cdn.weatherapi.com/icon2.png"),
            createForecastDayDto("2023-10-03", "//cdn.weatherapi.com/icon3.png")
        )
        val dto = ForecastDto(
            location = locationDto,
            forecast = ForecastDaysDto(forecastDay = days)
        )

        // When
        val result = dto.toDomain3Days()

        // Then
        assertEquals(3, result.days.size)
        assertEquals("https://cdn.weatherapi.com/icon1.png", result.days[0].conditionIcon)
        assertEquals("https://cdn.weatherapi.com/icon2.png", result.days[1].conditionIcon)
        assertEquals("https://cdn.weatherapi.com/icon3.png", result.days[2].conditionIcon)
    }

    @Test(expected = IllegalStateException::class)
    fun `ForecastDto toDomain3Days throws exception when less than 3 days`() {
        // Given
        val locationDto = LocationDto("Test", "TestCountry")
        val days = listOf(
            createForecastDayDto("2023-10-01", "//icon")
        )
        val dto = ForecastDto(
            location = locationDto,
            forecast = ForecastDaysDto(forecastDay = days)
        )

        // When
        dto.toDomain3Days()
    }

    private fun createForecastDayDto(date: String, icon: String): ForecastDayDto {
        return ForecastDayDto(
            date = date,
            day = DayDto(
                avgTempC = 20.0,
                condition = ConditionDto(text = "Sunny", icon = icon)
            )
        )
    }
}
