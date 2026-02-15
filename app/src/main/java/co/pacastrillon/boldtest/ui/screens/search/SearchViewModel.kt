package co.pacastrillon.boldtest.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.pacastrillon.boldtest.domain.model.Location
import co.pacastrillon.boldtest.domain.usecase.SearchLocationsUseCase
import co.pacastrillon.boldtest.domain.usecase.recent.GetRecentSearchesUseCase
import co.pacastrillon.boldtest.domain.usecase.recent.SaveRecentSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import co.pacastrillon.boldtest.domain.result.WeatherError
import co.pacastrillon.boldtest.domain.result.WeatherResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LocationUi(val name: String, val country: String)

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val results: List<LocationUi> = emptyList(),
    val errorMessage: String? = null,
    val showInitialState: Boolean = true,
    val recentSearches: List<LocationUi> = emptyList()
)

@HiltViewModel
open class SearchViewModel @Inject constructor(
    private val searchLocations: SearchLocationsUseCase,
    private val getRecentSearches: GetRecentSearchesUseCase,
    private val saveRecentSearch: SaveRecentSearchUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    open val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val queryFlow = MutableStateFlow("")
    private val retryTrigger = MutableSharedFlow<Unit>(replay = 1).apply { tryEmit(Unit) }

    init {
        observeQuery()
        observeRecentSearches()
    }

    private fun observeRecentSearches() {
        getRecentSearches()
            .onEach { recents ->
                _uiState.update { it.copy(recentSearches = recents.map { loc -> loc.toUi() }) }
            }
            .launchIn(viewModelScope)
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun observeQuery() {
        combine(queryFlow, retryTrigger) { query, _ -> query }
            .onEach { query ->
                _uiState.update { it.copy(query = query) }
                if (query.trim().length < 2) {
                    _uiState.update {
                        it.copy(
                            showInitialState = true,
                            results = emptyList(),
                            errorMessage = null,
                            isLoading = false
                        )
                    }
                }
            }
            .map { it.trim() }
            .filter { it.length >= 2 }
            .debounce(350)
            .distinctUntilChanged()
            .onEach {
                _uiState.update { it.copy(isLoading = true, showInitialState = false, errorMessage = null) }
            }
            .flatMapLatest { query ->
                flow {
                    emit(searchLocations(query))
                }
            }
            .onEach { result ->
                when (result) {
                    is WeatherResult.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                results = result.data.map { loc -> loc.toUi() },
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
                            it.copy(isLoading = false, errorMessage = message)
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    open fun onQueryChanged(query: String) {
        queryFlow.value = query
    }

    fun retry() {
        val currentQuery = queryFlow.value
        if (currentQuery.trim().length >= 2) {
            retryTrigger.tryEmit(Unit)
        }
    }

    fun onLocationSelected(locationUi: LocationUi) {
        viewModelScope.launch {
            saveRecentSearch(locationUi.toDomain())
        }
    }
}

private fun Location.toUi(): LocationUi = LocationUi(name, country)
private fun LocationUi.toDomain(): Location = Location(name, country, null, 0.0, 0.0)

