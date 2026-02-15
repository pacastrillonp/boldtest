package co.pacastrillon.boldtest.domain.usecase

import co.pacastrillon.boldtest.domain.model.Location
import co.pacastrillon.boldtest.domain.repository.WeatherRepository
import co.pacastrillon.boldtest.domain.result.WeatherResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class SearchLocationsUseCaseTest {

    private val repository: WeatherRepository = mock(WeatherRepository::class.java)
    private val useCase = SearchLocationsUseCase(repository)

    @Test
    fun `invoke with query less than 2 chars returns empty list and no interaction`() = runTest {
        // Given
        val query = "a"

        // When
        val result = useCase(query)

        // Then
        assertTrue(result is WeatherResult.Success)
        assertTrue((result as WeatherResult.Success).data.isEmpty())
        verify(repository, never()).searchLocations(query)
    }

    @Test
    fun `invoke with valid query calls repository and returns result`() = runTest {
        // Given
        val query = "Medellin"
        val expectedLocations = listOf(
            Location("Medellin", "Colombia", "Antioquia", 6.0, -75.0)
        )
        `when`(repository.searchLocations(query)).thenReturn(WeatherResult.Success(expectedLocations))

        // When
        val result = useCase(query)

        // Then
        assertTrue(result is WeatherResult.Success)
        assertEquals(expectedLocations, (result as WeatherResult.Success).data)
        verify(repository).searchLocations(query)
    }
}
