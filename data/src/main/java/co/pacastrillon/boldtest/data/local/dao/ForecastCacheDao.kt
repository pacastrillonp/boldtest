package co.pacastrillon.boldtest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.pacastrillon.boldtest.data.local.entities.ForecastCacheEntity

@Dao
interface ForecastCacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: ForecastCacheEntity)

    @Query("SELECT * FROM forecast_cache WHERE locationKey = :locationKey LIMIT 1")
    suspend fun getByKey(locationKey: String): ForecastCacheEntity?
}