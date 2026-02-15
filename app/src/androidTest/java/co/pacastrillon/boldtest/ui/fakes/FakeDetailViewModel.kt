package co.pacastrillon.boldtest.ui.fakes

import androidx.lifecycle.viewModelScope
import co.pacastrillon.boldtest.domain.usecase.GetForecast3DaysUseCase
import co.pacastrillon.boldtest.ui.models.ForecastDayUi
import co.pacastrillon.boldtest.ui.models.ForecastUi
import co.pacastrillon.boldtest.ui.screens.detail.DetailUiState
import co.pacastrillon.boldtest.ui.screens.detail.DetailViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FakeDetailViewModel : DetailViewModel(
    getForecast3Days = GetForecast3DaysUseCase(DummyWeatherRepository())
) {
    private val _fakeUiState = MutableStateFlow(DetailUiState())
    override val uiState: StateFlow<DetailUiState> = _fakeUiState.asStateFlow()

    override fun load(locationName: String) {
        viewModelScope.launch {
            _fakeUiState.update { it.copy(isLoading = true, errorMessage = null) }
            delay(100)

            if (locationName.lowercase() == "error") {
                _fakeUiState.update { it.copy(isLoading = false, errorMessage = "Simulated Error") }
            } else {
                val dummyData = ForecastUi(
                    locationName = locationName,
                    todayLabel = "Today",
                    tempNow = "25",
                    conditionText = "Sunny",
                    conditionIconUrl = "",
                    days = listOf(
                        ForecastDayUi("Today", 25.0, "Sunny", ""),
                        ForecastDayUi("Tomorrow", 22.0, "Cloudy", ""),
                        ForecastDayUi("DayAfter", 20.0, "Rain", "")
                    )
                )
                _fakeUiState.update { it.copy(isLoading = false, data = dummyData) }
            }
        }
    }
}
