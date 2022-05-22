package org.vanmierlo.kapsamazing_app

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [KapsalonRow::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun kapsalonDao(): KapsalonDao
}