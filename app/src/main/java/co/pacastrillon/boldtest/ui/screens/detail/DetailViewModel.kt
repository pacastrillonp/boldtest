package co.pacastrillon.boldtest.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.pacastrillon.boldtest.domain.result.WeatherError
import co.pacastrillon.boldtest.domain.result.WeatherResult
import co.pacastrillon.boldtest.domain.usecase.GetForecast3DaysUseCase
import co.pacastrillon.boldtest.ui.models.ForecastUi
import co.pacastrillon.boldtest.ui.models.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailUiState(
    val isLoading: Boolean = false,
    val data: ForecastUi? = null,
    val errorMessage: String? = null
)

@HiltViewModel
open class DetailViewModel @Inject constructor(
    private val getForecast3Days: GetForecast3DaysUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    open val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private var lastQuery: String? = null

    open fun load(locationName: String) {
        lastQuery = locationName
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            when (val result = getForecast3Days(locationName)) {
                is WeatherResult.Success -> {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            data = result.data.toUi(),
                            errorMessage = null
                        )
                    }
                }
                is WeatherResult.Error -> {
                    val message = when (result.error) {
                        WeatherError.NETWORK -> "No internet connection. Try again."
                        WeatherError.UNAUTHORIZED -> "Invalid API key."
                        WeatherError.SERVER -> "Server error. Please retry."
                        WeatherError.UNKNOWN -> "Unexpected error. Please retry."
                    }
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            errorMessage = message
                        ) 
                    }
                }
            }
        }
    }

    fun retry() {
        lastQuery?.let { load(it) }
    }
}
