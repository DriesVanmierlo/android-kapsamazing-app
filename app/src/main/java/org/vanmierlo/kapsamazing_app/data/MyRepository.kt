package org.vanmierlo.kapsamazing_app.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import org.vanmierlo.kapsamazing_app.Kapsalon

class MyRepository(private val kapsalonRoomDao: KapsalonRoomDao) {
    val allKapsalons: Flow<List<KapsalonRoom>> = kapsalonRoomDao.getAllKapsalons()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertKapsalon(kapsalon: KapsalonRoom){
        kapsalonRoomDao.insertKapsalon(kapsalon)
    }

    @WorkerThread
    suspend fun deleteAllKapsalons(){
        kapsalonRoomDao.deleteAll()
    }
}