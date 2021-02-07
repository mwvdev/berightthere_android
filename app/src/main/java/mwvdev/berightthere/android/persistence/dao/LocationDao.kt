package mwvdev.berightthere.android.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import mwvdev.berightthere.android.persistence.entity.Location

@Dao
interface LocationDao {

    @Insert
    suspend fun insert(location: Location)

    @Insert
    suspend fun insertAll(vararg locations: Location)

    @Query("DELETE FROM trip")
    suspend fun deleteAll()
    
}