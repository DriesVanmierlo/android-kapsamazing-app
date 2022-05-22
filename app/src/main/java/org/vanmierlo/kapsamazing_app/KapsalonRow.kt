package org.vanmierlo.kapsamazing_app

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class KapsalonRow (
    @PrimaryKey val kapid: Int,
    @ColumnInfo(name = "name") val kapsalonName: String?,
    @ColumnInfo(name = "restaurant") val restaurant: String?
        )