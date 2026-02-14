package co.pacastrillon.boldtest.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
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
        topBar = { BoldTopBar(title = "Search Screen") }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                value = uiState.query,
                onValueChange = viewModel::onQueryChanged,
                placeholder = { Text("Enter city name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .testTag("search_input"),
                trailingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.testTag("search_loading"))
                }
            } else if (uiState.errorMessage != null) {
                ErrorState(
                    message = uiState.errorMessage ?: "Unknown error",
                    onRetry = { viewModel.retry() },
                    modifier = Modifier.testTag("search_error")
                )
            } else if (uiState.showInitialState) {
                InitialSearchContent(onCityClick = onNavigateToDetail)
            } else {
                if (uiState.results.isEmpty()) {
                    EmptyState(message = "No results found for \"${uiState.query}\"")
                } else {
                    LazyColumn(modifier = Modifier.testTag("search_list")) {
                        items(uiState.results) { location ->
                            LocationItem(location = location, onClick = { onNavigateToDetail(location.name) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InitialSearchContent(onCityClick: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Recent Searches", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        LocationItem(LocationUi("Bogotá", "Colombia"), onClick = { onCityClick("Bogotá") })
        LocationItem(LocationUi("Medellín", "Colombia"), onClick = { onCityClick("Medellín") })
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("Popular Cities", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        LocationItem(LocationUi("New York", "USA"), onClick = { onCityClick("New York") })
        LocationItem(LocationUi("London", "UK"), onClick = { onCityClick("London") })
        LocationItem(LocationUi("Tokyo", "Japan"), onClick = { onCityClick("Tokyo") })
    }
}

@Composable
fun LocationItem(location: LocationUi, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
            .testTag("location_item_${location.name}")
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = location.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = location.country,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
    }
}
