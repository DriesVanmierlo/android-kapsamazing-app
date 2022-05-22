package org.vanmierlo.kapsamazing_app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.vanmierlo.kapsamazing_app.Kapsalon

@Dao
interface KapsalonRoomDao {
    @Query("SELECT * FROM kapsalons")
    fun getAllKapsalons(): Flow<List<Kapsalon>>

    @Insert
    suspend fun insertKapsalon(kapsalon: Kapsalon)

    @Query("DELETE FROM kapsalons")
    suspend fun deleteAll()

}