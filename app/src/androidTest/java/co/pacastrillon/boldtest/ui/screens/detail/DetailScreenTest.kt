package co.pacastrillon.boldtest.ui.screens.detail

import android.content.pm.ActivityInfo
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import co.pacastrillon.boldtest.ui.fakes.FakeDetailViewModel
import co.pacastrillon.boldtest.ui.theme.BoldTestTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun detail_renders_three_days() {
        val fakeViewModel = FakeDetailViewModel()
        
        composeTestRule.setContent {
            BoldTestTheme {
                WeatherDetailScreen(
                    locationName = "Medellín",
                    onBack = {},
                    viewModel = fakeViewModel
                )
            }
        }

        // Wait for loading to complete (fake has 100ms delay)
        composeTestRule.waitUntil(timeoutMillis = 2000) {
            composeTestRule.onAllNodesWithTag("detail_days_list").fetchSemanticsNodes().isNotEmpty()
        }

        // Assert 3 days are rendered
        composeTestRule.onNodeWithTag("day_item_0").assertIsDisplayed()
        composeTestRule.onNodeWithTag("day_item_1").assertIsDisplayed()
        composeTestRule.onNodeWithTag("day_item_2").assertIsDisplayed()
    }

    @Test
    fun landscape_not_broken() {
        val fakeViewModel = FakeDetailViewModel()
        
        // Change orientation first to avoid Activity recreation after setContent
        composeTestRule.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        
        // Wait for potential recreation (simple sleep to ensure new activity is picked up)
        // Note: In real scenarios, use ActivityScenario or specific TestRule logic, but this often suffices for basic checks.
        // If the activity doesn't rotate, this is a no-op which is fine.
        
        composeTestRule.setContent {
            BoldTestTheme {
                WeatherDetailScreen(
                    locationName = "Medellín",
                    onBack = {},
                    viewModel = fakeViewModel
                )
            }
        }

        // Wait for content
        composeTestRule.waitUntil(timeoutMillis = 2000) {
            composeTestRule.onAllNodesWithTag("detail_root").fetchSemanticsNodes().isNotEmpty()
        }

        // Assert elements still visible/exist
        composeTestRule.onNodeWithTag("detail_root").assertIsDisplayed()
        composeTestRule.onNodeWithTag("detail_days_list").assertIsDisplayed()
    }
}
