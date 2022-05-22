package org.vanmierlo.kapsamazing_app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.vanmierlo.kapsamazing_app.Kapsalon

@Database(entities = [Kapsalon::class], version = 1)
public abstract class KapsalonDatabase: RoomDatabase() {

    abstract fun kapsalonDao(): KapsalonRoomDao

    companion object {
        @Volatile
        private var INSTANCE: KapsalonDatabase? = null

        fun getDatabase(context: Context): KapsalonDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KapsalonDatabase::class.java,
                    "kapsalonsdatabank"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}