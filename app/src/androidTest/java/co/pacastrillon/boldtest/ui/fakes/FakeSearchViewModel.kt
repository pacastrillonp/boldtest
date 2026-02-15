package co.pacastrillon.boldtest.ui.fakes

import androidx.lifecycle.viewModelScope
import co.pacastrillon.boldtest.ui.screens.search.LocationUi
import co.pacastrillon.boldtest.ui.screens.search.SearchUiState
import co.pacastrillon.boldtest.ui.screens.search.SearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import co.pacastrillon.boldtest.domain.usecase.SearchLocationsUseCase
import co.pacastrillon.boldtest.domain.usecase.recent.GetRecentSearchesUseCase
import co.pacastrillon.boldtest.domain.usecase.recent.SaveRecentSearchUseCase

class FakeSearchViewModel : SearchViewModel(
    searchLocations = SearchLocationsUseCase(DummyWeatherRepository()),
    getRecentSearches = GetRecentSearchesUseCase(DummyWeatherRepository()),
    saveRecentSearch = SaveRecentSearchUseCase(DummyWeatherRepository())
) {
    private val _fakeUiState = MutableStateFlow(SearchUiState())
    override val uiState: StateFlow<SearchUiState> = _fakeUiState.asStateFlow()

    override fun onQueryChanged(query: String) {
        _fakeUiState.update { it.copy(query = query) }
        
        if (query.length < 2) {
            _fakeUiState.update { it.copy(showInitialState = true, results = emptyList(), errorMessage = null, isLoading = false) }
            return
        }

        viewModelScope.launch {
            _fakeUiState.update { it.copy(isLoading = true, showInitialState = false, errorMessage = null) }

            delay(100) 

            if (query.lowercase() == "error") {
                _fakeUiState.update { it.copy(isLoading = false, errorMessage = "Simulated Error") }
            } else {
                 val dummyResults = listOf(
                    LocationUi("Bogotá", "Colombia"),
                    LocationUi("Medellín", "Colombia"),
                    LocationUi("Cali", "Colombia")
                ).filter { it.name.contains(query, ignoreCase = true) }
                
                _fakeUiState.update { it.copy(isLoading = false, results = dummyResults) }
            }
        }
    }
}
