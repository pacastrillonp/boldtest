package co.pacastrillon.boldtest.ui.screens.search

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.composable
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import co.pacastrillon.boldtest.ui.fakes.FakeDetailViewModel
import co.pacastrillon.boldtest.ui.fakes.FakeSearchViewModel
import co.pacastrillon.boldtest.ui.navigation.Routes
import co.pacastrillon.boldtest.ui.theme.BoldTestTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun search_input_updates_text() {
        val fakeViewModel = FakeSearchViewModel()

        composeTestRule.setContent {
            BoldTestTheme {
                SearchScreen(
                    onNavigateToDetail = {},
                    viewModel = fakeViewModel
                )
            }
        }

        composeTestRule.onNodeWithTag("search_input")
            .performTextInput("Med")

        composeTestRule.onNodeWithTag("search_input")
            .assert(hasText("Med"))
    }

    @Test
    fun search_loading_appears_when_typing() {
        val fakeViewModel = FakeSearchViewModel()

        composeTestRule.setContent {
            BoldTestTheme {
                SearchScreen(
                    onNavigateToDetail = {},
                    viewModel = fakeViewModel
                )
            }
        }

        composeTestRule.mainClock.autoAdvance = false

        composeTestRule.onNodeWithTag("search_input")
            .performTextInput("Me")

        // Advance slightly to trigger coroutine launch but stay in delay
        composeTestRule.mainClock.advanceTimeBy(50)

        composeTestRule.onNodeWithTag("search_loading")
            .assertIsDisplayed()

        // Advance to finish loading
        composeTestRule.mainClock.advanceTimeBy(1000)
        composeTestRule.mainClock.autoAdvance = true

        // Ensure loading is gone (optional but good)
        composeTestRule.onNodeWithTag("search_loading")
            .assertDoesNotExist()
    }

    @Test
    fun search_list_click_navigates_to_detail() {
        val fakeSearchViewModel = FakeSearchViewModel()
        val fakeDetailViewModel = FakeDetailViewModel()

        composeTestRule.setContent {
            val navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())

            BoldTestTheme {
                // Replicating NavGraph logic for test but injecting fakes not possible directly
                // because WeatherNavGraph instantiates screens.
                // So we manually build the NavHost here with the components we want to test
                androidx.navigation.compose.NavHost(
                    navController = navController,
                    startDestination = Routes.Search.route
                ) {
                    composable(Routes.Search.route) {
                        SearchScreen(
                            onNavigateToDetail = { query ->
                                navController.navigate(Routes.Detail.createRoute(query))
                            },
                            viewModel = fakeSearchViewModel
                        )
                    }
                    composable(Routes.Detail.route) { backStackEntry ->
                        val query = backStackEntry.arguments?.getString("query") ?: ""
                        co.pacastrillon.boldtest.ui.screens.detail.WeatherDetailScreen(
                            locationName = query,
                            onBack = {},
                            viewModel = fakeDetailViewModel
                        )
                    }
                }
            }
        }

        // Trigger search to get results
        composeTestRule.onNodeWithTag("search_input")
            .performTextInput("Medellín")

        // Wait for results
        composeTestRule.waitUntil(timeoutMillis = 2000) {
            composeTestRule.onAllNodesWithTag("location_item_Medellín").fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule.onNodeWithTag("location_item_Medellín")
            .performClick()

        composeTestRule.onNodeWithTag("detail_root")
            .assertIsDisplayed()
    }
}
