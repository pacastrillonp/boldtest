package co.pacastrillon.boldtest.ui.navigation

sealed class Routes(val route: String) {
    object Splash : Routes("splash")
    object Search : Routes("search")
    object Detail : Routes("detail/{query}") {
        fun createRoute(query: String): String = "detail/$query"
    }
}
