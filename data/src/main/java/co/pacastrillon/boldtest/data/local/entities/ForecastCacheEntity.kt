package co.pacastrillon.boldtest.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecast_cache")
data class ForecastCacheEntity(
    @PrimaryKey
    val locationKey: String,
    val json: String,
    val updatedAt: Long // epoch millis
)
