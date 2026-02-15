package co.pacastrillon.boldtest.data.repository

import co.pacastrillon.boldtest.data.local.dao.ForecastCacheDao
import co.pacastrillon.boldtest.data.local.dao.RecentSearchDao
import co.pacastrillon.boldtest.data.remote.ApiService
import co.pacastrillon.boldtest.domain.result.WeatherError
import co.pacastrillon.boldtest.domain.result.WeatherResult
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import retrofit2.Retrofit

class WeatherRepositoryImplTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService
    private lateinit var recentSearchDao: RecentSearchDao
    private lateinit var forecastCacheDao: ForecastCacheDao
    private lateinit var repository: WeatherRepositoryImpl
    private val json = Json { ignoreUnknownKeys = true }

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val contentType = "application/json".toMediaType()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(ApiService::class.java)

        recentSearchDao = mock(RecentSearchDao::class.java)
        forecastCacheDao = mock(ForecastCacheDao::class.java)

        repository = WeatherRepositoryImpl(
            api = apiService,
            recentSearchDao = recentSearchDao,
            forecastCacheDao = forecastCacheDao,
            jsonSerializer = json
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `search success returns locations`() = runTest {
        // Given
        val responseBody = """
            [
                {
                    "id": 1,
                    "name": "Medellin",
                    "region": "Antioquia",
                    "country": "Colombia",
                    "lat": 6.25,
                    "lon": -75.56,
                    "url": "medellin-antioquia-colombia"
                }
            ]
        """.trimIndent()
        mockWebServer.enqueue(MockResponse().setBody(responseBody).setResponseCode(200))

        // When
        val result = repository.searchLocations("Med")

        // Then
        assertTrue(result is WeatherResult.Success)
        assertEquals(1, (result as WeatherResult.Success).data.size)
        assertEquals("Medellin", result.data[0].name)
    }

    @Test
    fun `forecast success returns 3 days`() = runTest {
        // Given
        val responseBody = """
            {
                "location": {
                    "name": "London",
                    "region": "City of London, Greater London",
                    "country": "United Kingdom",
                    "lat": 51.52,
                    "lon": -0.11,
                    "tz_id": "Europe/London",
                    "localtime_epoch": 1696434314,
                    "localtime": "2023-10-04 16:45"
                },
                "current": {
                    "temp_c": 15.0,
                    "condition": { "text": "Partly cloudy", "icon": "//cdn.weatherapi.com/weather/64x64/day/116.png", "code": 1003 }
                },
                "forecast": {
                    "forecastday": [
                        { "date": "2023-10-04", "day": { "avgtemp_c": 15.0, "condition": { "text": "Partly cloudy", "icon": "//cdn.weatherapi.com/icon1.png" } } },
                        { "date": "2023-10-05", "day": { "avgtemp_c": 16.0, "condition": { "text": "Sunny", "icon": "//cdn.weatherapi.com/icon2.png" } } },
                        { "date": "2023-10-06", "day": { "avgtemp_c": 14.0, "condition": { "text": "Rainy", "icon": "//cdn.weatherapi.com/icon3.png" } } }
                    ]
                }
            }
        """.trimIndent()
        mockWebServer.enqueue(MockResponse().setBody(responseBody).setResponseCode(200))

        // When
        val result = repository.getForecast3Days("London")

        // Then
        assertTrue(result is WeatherResult.Success)
        assertEquals(3, (result as WeatherResult.Success).data.days.size)
        assertEquals("London", result.data.location.name)
    }

    @Test
    fun `forecast 401 returns unauthorized error`() = runTest {
        // Given
        mockWebServer.enqueue(MockResponse().setResponseCode(401))

        // When
        val result = repository.getForecast3Days("InvalidKey")

        // Then
        assertTrue(result is WeatherResult.Error)
        assertEquals(WeatherError.UNAUTHORIZED, (result as WeatherResult.Error).error)
    }
}
