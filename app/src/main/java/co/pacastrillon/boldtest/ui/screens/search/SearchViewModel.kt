package co.pacastrillon.boldtest.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LocationUi(val name: String, val country: String)

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val results: List<LocationUi> = emptyList(),
    val errorMessage: String? = null,
    val showInitialState: Boolean = true
)

open class SearchViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    open val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val allLocations = listOf(
        LocationUi("Bogotá", "Colombia"),
        LocationUi("Medellín", "Colombia"),
        LocationUi("Cali", "Colombia"),
        LocationUi("New York", "USA"),
        LocationUi("London", "UK"),
        LocationUi("Tokyo", "Japan"),
        LocationUi("Madrid", "Spain"),
        LocationUi("Paris", "France")
    )

    open fun onQueryChanged(query: String) {
        _uiState.update { it.copy(query = query) }
        
        if (query.length < 2) {
            _uiState.update { it.copy(showInitialState = true, results = emptyList(), errorMessage = null, isLoading = false) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, showInitialState = false, errorMessage = null) }
            delay(500) // Simular network delay

            if (query.lowercase() == "error") {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Simulated error occurred") }
            } else {
                val filtered = allLocations.filter { 
                    it.name.contains(query, ignoreCase = true) 
                }
                _uiState.update { it.copy(isLoading = false, results = filtered) }
            }
        }
    }
}
