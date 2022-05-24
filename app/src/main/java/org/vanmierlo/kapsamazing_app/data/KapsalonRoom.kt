package org.vanmierlo.kapsamazing_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kapsalons")
data class KapsalonRoom(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val city: String,
    val restaurant: String,
    val type: String,
    val delivered: List<String>,
    val price: Double,
    val kapid: String,
    val latestGeneralRating: Double,
    val image: String
) {
}