package co.pacastrillon.boldtest.ui.screens.detail

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import co.pacastrillon.boldtest.common.Constants.Messages.UNKNOWN_ERROR_MESSAGE
import co.pacastrillon.boldtest.common.Constants.WeatherDetailScreenTags.DETAIL_ERROR
import co.pacastrillon.boldtest.common.Constants.WeatherDetailScreenTags.DETAIL_LOADING
import co.pacastrillon.boldtest.common.Constants.WeatherDetailScreenTags.DETAIL_ROOT
import co.pacastrillon.boldtest.ui.designsystem.BoldTopBar
import co.pacastrillon.boldtest.ui.designsystem.ErrorState
import co.pacastrillon.boldtest.ui.models.ForecastUi

@Composable
fun WeatherDetailScreen(
    locationName: String,
    onBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    LaunchedEffect(locationName) {
        viewModel.load(locationName)
    }

    val uiState by viewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        topBar = {
            BoldTopBar(
                title = locationName,
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .testTag(DETAIL_ROOT)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .testTag(DETAIL_LOADING)
                )
            } else if (uiState.errorMessage != null) {
                ErrorState(
                    message = uiState.errorMessage ?: UNKNOWN_ERROR_MESSAGE,
                    onRetry = { viewModel.retry() },
                    modifier = Modifier.testTag(DETAIL_ERROR)
                )
            } else if (uiState.data != null) {
                WeatherDetailContent(
                    data = uiState.data!!,
                    isLandscape = isLandscape
                )
            }
        }
    }
}

@Composable
fun WeatherDetailContent(
    data: ForecastUi,
    isLandscape: Boolean
) {
    if (isLandscape) {
        LandscapeDetailLayout(data)
    } else {
        PortraitDetailLayout(data)
    }
}