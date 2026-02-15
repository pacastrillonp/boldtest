package co.pacastrillon.boldtest.ui.screens.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import co.pacastrillon.boldtest.common.Constants.Messages.UNKNOWN_ERROR_MESSAGE
import co.pacastrillon.boldtest.common.Constants.SearchScreenTags.ENTER_CITY
import co.pacastrillon.boldtest.common.Constants.SearchScreenTags.NO_RESULTS
import co.pacastrillon.boldtest.common.Constants.SearchScreenTags.SEARCH_ERROR
import co.pacastrillon.boldtest.common.Constants.SearchScreenTags.SEARCH_INPUT
import co.pacastrillon.boldtest.common.Constants.SearchScreenTags.SEARCH_LIST
import co.pacastrillon.boldtest.common.Constants.SearchScreenTags.SEARCH_LOADING
import co.pacastrillon.boldtest.common.Constants.SearchScreenTags.SEARCH_TITTLE
import co.pacastrillon.boldtest.ui.designsystem.BoldTopBar
import co.pacastrillon.boldtest.ui.designsystem.EmptyState
import co.pacastrillon.boldtest.ui.designsystem.ErrorState

@Composable
fun SearchScreen(
    onNavigateToDetail: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { BoldTopBar(title = SEARCH_TITTLE) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                value = uiState.query,
                onValueChange = viewModel::onQueryChanged,
                placeholder = { Text(ENTER_CITY) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .testTag(SEARCH_INPUT),
                trailingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.testTag(SEARCH_LOADING))
                }
            } else if (uiState.errorMessage != null) {
                ErrorState(
                    message = uiState.errorMessage ?: UNKNOWN_ERROR_MESSAGE,
                    onRetry = { viewModel.retry() },
                    modifier = Modifier.testTag(SEARCH_ERROR)
                )
            } else if (uiState.showInitialState) {
                InitialSearchContent(
                    recentSearches = uiState.recentSearches,
                    onLocationClick = { location ->
                        viewModel.onLocationSelected(location)
                        onNavigateToDetail(location.name)
                    }
                )
            } else {
                if (uiState.results.isEmpty()) {
                    EmptyState(message = "$NO_RESULTS \"${uiState.query}\"")
                } else {
                    LazyColumn(modifier = Modifier.testTag(SEARCH_LIST)) {
                        items(uiState.results) { location ->
                            LocationItem(
                                location = location,
                                onClick = {
                                    viewModel.onLocationSelected(location)
                                    onNavigateToDetail(location.name)
                                })
                        }
                    }
                }
            }
        }
    }
}