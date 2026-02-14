package co.pacastrillon.boldtest.ui.fakes

import androidx.lifecycle.viewModelScope
import co.pacastrillon.boldtest.ui.screens.detail.DayForecast
import co.pacastrillon.boldtest.ui.screens.detail.DetailUiState
import co.pacastrillon.boldtest.ui.screens.detail.DetailViewModel
import co.pacastrillon.boldtest.ui.screens.detail.ForecastUi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FakeDetailViewModel : DetailViewModel() {
    private val _fakeUiState = MutableStateFlow(DetailUiState())
    override val uiState: StateFlow<DetailUiState> = _fakeUiState.asStateFlow()

    override fun load(query: String) {
        viewModelScope.launch {
            _fakeUiState.update { it.copy(isLoading = true, errorMessage = null) }
            delay(100) 

            if (query.lowercase() == "error") {
                _fakeUiState.update { it.copy(isLoading = false, errorMessage = "Simulated Error") }
            } else {
                val dummyData = ForecastUi(
                    cityName = query,
                    date = "Today",
                    tempNow = 25,
                    conditionText = "Sunny",
                    conditionIconUrl = "",
                    days = listOf(
                        DayForecast("Today", "", "Sunny", 25),
                        DayForecast("Tomorrow", "", "Cloudy", 22),
                        DayForecast("DayAfter", "", "Rain", 20)
                    )
                )
                _fakeUiState.update { it.copy(isLoading = false, data = dummyData) }
            }
        }
    }
}
