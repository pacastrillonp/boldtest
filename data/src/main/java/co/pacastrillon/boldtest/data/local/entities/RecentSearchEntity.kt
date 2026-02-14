package co.pacastrillon.boldtest.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_search")
data class RecentSearchEntity(
    @PrimaryKey
    val locationKey: String, // e.g. "Medellín, Colombia"
    val name: String,
    val country: String,
    val lastUsed: Long // epoch millis
)
