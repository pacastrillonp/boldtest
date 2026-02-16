package co.pacastrillon.boldtest.common

object Constants {

    object Routes {
        const val SPLASH = "splash"
        const val SEARCH = "search"
        const val DETAILS = "detail/{query}"
        const val DETAIL = "detail"
    }

    object Network {
        const val NO_INTERNET = "No internet connection. Try again."
        const val SERVER_ERROR = "Server error. Try again."
        const val UNKNOWN_ERROR = "Unexpected error. Please retry."
        const val API_ERROR = "Invalid API key."
    }

    object SplashScreenTags {
        const val SPLASH_ROOT = "splash_root"
        const val BOLD_WEATHER = "Bold Weather App"
    }

    object WeatherDetailScreenTags {
        const val DETAIL_ROOT = "detail_root"
        const val DETAIL_LOADING = "detail_loading"
        const val DETAIL_ERROR = "detail_error"
        const val DAY_ITEM = "day_item_0"
        const val DETAIL_DAYS_LIST = "detail_days_list"
        const val DETAIL_TODAY_ITEM = "day_item_"
        const val THREE_DAYS_FORECAST = "3-Day Forecast"
        const val TODAY_FORECAST = "Today's Forecast"
        const val NEXT_DAYS = "Next Days"
        const val CONDITION = "Condition"
    }

    object SearchScreenTags {
        const val SEARCH_TITTLE = "Search Screen"
        const val ENTER_CITY = "Enter city name"
        const val SEARCH_INPUT = "search_input"
        const val SEARCH_LOADING = "search_loading"
        const val SEARCH_ERROR = "search_error"
        const val NO_RESULTS = "No results found for"
        const val SEARCH_LIST = "search_list"
        const val RECENT_SEARCHES = "Recent Searches"
        const val POPULAR_CITIES = "Popular Cities"
        const val NEW_YORK = "New York"
        const val LONDON = "London"
        const val TOKYO = "Tokyo"
        const val BOGOTA = "Bogotá"
        const val MEDELLIN = "Medellín"
        const val USA = "USA"
        const val UK = "UK"
        const val JAPAN = "Japan"
        const val COLOMBIA = "Colombia"
        const val LOCATION_ITEM = "location_item_"
    }


    object Messages {
        const val UNKNOWN_ERROR_MESSAGE = "Unknown Error"
        const val LOCATION_QUERY_EMPTY = "locationQuery is blank"
    }

    object Defaults {
        const val TODAY = "Today"
        const val EMPTY_STRING = ""
        const val EMPTY_DASH = "--"
        const val AGE_FORMAT = "EEEE"
        const val DATE_FORMAT = "yyyy-MM-dd"
    }
}