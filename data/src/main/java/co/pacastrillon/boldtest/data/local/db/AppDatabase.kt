package co.pacastrillon.boldtest.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import co.pacastrillon.boldtest.data.local.dao.ForecastCacheDao
import co.pacastrillon.boldtest.data.local.dao.RecentSearchDao
import co.pacastrillon.boldtest.data.local.entities.ForecastCacheEntity
import co.pacastrillon.boldtest.data.local.entities.RecentSearchEntity

@Database(
    entities = [RecentSearchEntity::class, ForecastCacheEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recentSearchDao(): RecentSearchDao
    abstract fun forecastCacheDao(): ForecastCacheDao
}
