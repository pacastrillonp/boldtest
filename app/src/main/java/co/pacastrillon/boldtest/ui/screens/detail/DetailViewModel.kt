package co.pacastrillon.boldtest.ui.screens.detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class DayForecast(
    val date: String,
    val iconUrl: String,
    val conditionText: String,
    val avgTempC: Int
)

data class ForecastUi(
    val cityName: String,
    val date: String,
    val tempNow: Int,
    val conditionText: String,
    val conditionIconUrl: String,
    val days: List<DayForecast>
)

data class DetailUiState(
    val isLoading: Boolean = false,
    val data: ForecastUi? = null,
    val errorMessage: String? = null
)

class DetailViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun load(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            delay(1000) // Simular delay

            if (query.lowercase() == "error") {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Failed to load weather data.") }
            } else {
                val dummyData = createDummyData(query)
                _uiState.update { it.copy(isLoading = false, data = dummyData) }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDummyData(objName: String): ForecastUi {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("EEEE, MMM dd")
        
        return ForecastUi(
            cityName = objName,
            date = today.format(formatter),
            tempNow = 26,
            conditionText = "Mostly Sunny",
            conditionIconUrl = "https://cdn.weatherapi.com/weather/64x64/day/116.png",
            days = listOf(
                DayForecast(
                    date = "Today",
                    iconUrl = "https://cdn.weatherapi.com/weather/64x64/day/116.png",
                    conditionText = "Mostly Sunny",
                    avgTempC = 24
                ),
                DayForecast(
                    date = today.plusDays(1).format(DateTimeFormatter.ofPattern("EEEE")),
                    iconUrl = "https://cdn.weatherapi.com/weather/64x64/day/176.png",
                    conditionText = "Patchy rain possible",
                    avgTempC = 22
                ),
                DayForecast(
                    date = today.plusDays(2).format(DateTimeFormatter.ofPattern("EEEE")),
                    iconUrl = "https://cdn.weatherapi.com/weather/64x64/day/113.png",
                    conditionText = "Sunny",
                    avgTempC = 27
                )
            )
        )
    }
}
