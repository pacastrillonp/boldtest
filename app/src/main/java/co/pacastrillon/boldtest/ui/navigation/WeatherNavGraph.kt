package co.pacastrillon.boldtest.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.pacastrillon.boldtest.ui.screens.detail.WeatherDetailScreen
import co.pacastrillon.boldtest.ui.screens.search.SearchScreen
import co.pacastrillon.boldtest.ui.screens.splash.SplashScreen

@Composable
fun WeatherNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {
        composable(Routes.Splash.route) {
            SplashScreen(
                onNavigateToSearch = {
                    navController.navigate(Routes.Search.route) {
                        popUpTo(Routes.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.Search.route) {
            SearchScreen(
                onNavigateToDetail = { query ->
                    navController.navigate(Routes.Detail.createRoute(query))
                }
            )
        }

        composable(Routes.Detail.route) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            WeatherDetailScreen(
                locationName = query,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
