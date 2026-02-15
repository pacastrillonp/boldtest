package co.pacastrillon.boldtest.ui.screens.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.pacastrillon.boldtest.common.Constants.SearchScreenTags.BOGOTA
import co.pacastrillon.boldtest.common.Constants.SearchScreenTags.COLOMBIA
import co.pacastrillon.boldtest.common.Constants.SearchScreenTags.JAPAN
import co.pacastrillon.boldtest.common.Constants.SearchScreenTags.LONDON
import co.pacastrillon.boldtest.common.Constants.SearchScreenTags.MEDELLIN
import co.pacastrillon.boldtest.common.Constants.SearchScreenTags.NEW_YORK
import co.pacastrillon.boldtest.common.Constants.SearchScreenTags.POPULAR_CITIES
import co.pacastrillon.boldtest.common.Constants.SearchScreenTags.RECENT_SEARCHES
import co.pacastrillon.boldtest.common.Constants.SearchScreenTags.TOKYO
import co.pacastrillon.boldtest.common.Constants.SearchScreenTags.UK
import co.pacastrillon.boldtest.common.Constants.SearchScreenTags.USA

@Composable
fun InitialSearchContent(
    recentSearches: List<LocationUi>,
    onLocationClick: (LocationUi) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        if (recentSearches.isNotEmpty()) {
            Text(RECENT_SEARCHES, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            recentSearches.forEach { location ->
                LocationItem(location = location, onClick = { onLocationClick(location) })
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        Text(POPULAR_CITIES, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        val popular = listOf(
            LocationUi(NEW_YORK, USA),
            LocationUi(LONDON, UK),
            LocationUi(TOKYO, JAPAN),
            LocationUi(BOGOTA, COLOMBIA),
            LocationUi(MEDELLIN, COLOMBIA)
        )
        popular.forEach { location ->
            LocationItem(location = location, onClick = { onLocationClick(location) })
        }
    }
}