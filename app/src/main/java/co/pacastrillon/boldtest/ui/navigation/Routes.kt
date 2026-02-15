package co.pacastrillon.boldtest.ui.navigation

import co.pacastrillon.boldtest.common.Constants.Routes.DETAIL
import co.pacastrillon.boldtest.common.Constants.Routes.DETAILS
import co.pacastrillon.boldtest.common.Constants.Routes.SEARCH
import co.pacastrillon.boldtest.common.Constants.Routes.SPLASH

sealed class Routes(val route: String) {
    object Splash : Routes(SPLASH)
    object Search : Routes(SEARCH)
    object Detail : Routes(DETAILS) {
        fun createRoute(query: String): String = "$DETAIL/$query"
    }
}