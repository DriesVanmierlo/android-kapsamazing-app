package org.vanmierlo.kapsamazing_app

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface KapsalonDao {

    @Query("SELECT * FROM kapsalons")
    fun getAll():List<KapsalonRow>

    @Insert
    fun insertAll(vararg kapsalons: KapsalonRow)

    @Insert
    fun insertOne(vararg kapsalon: KapsalonRow)

    @Delete
    fun delete(kapsalon: KapsalonRow)

}