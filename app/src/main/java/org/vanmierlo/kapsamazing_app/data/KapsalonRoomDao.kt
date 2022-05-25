package org.vanmierlo.kapsamazing_app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.vanmierlo.kapsamazing_app.data.KapsalonRoom

@Dao
interface KapsalonRoomDao {

    @Query("SELECT * FROM kapsalonsroom")
    fun getAllKapsalons(): Flow<List<KapsalonRoom>>

    @Insert
    suspend fun insertKapsalon(kapsalon: KapsalonRoom)

    @Query("DELETE FROM kapsalonsroom")
    suspend fun deleteAll()

}