package mwvdev.berightthere.android.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import mwvdev.berightthere.android.persistence.entity.Trip
import mwvdev.berightthere.android.persistence.entity.TripWithLocations

@Dao
interface TripDao {

    @Query("SELECT * FROM trip WHERE tripIdentifier = :identifier")
    fun get(identifier: String): LiveData<Trip>

    @Transaction
    @Query("SELECT * FROM trip WHERE tripIdentifier = :identifier")
    fun getTripWithLocations(identifier: String): LiveData<TripWithLocations>

    @Transaction
    @Query("SELECT * FROM trip ORDER BY createdAt DESC")
    fun getTripsWithLocations(): LiveData<List<TripWithLocations>>

    @Insert
    suspend fun insert(trip: Trip)

    @Insert
    suspend fun insertAll(vararg trips: Trip)

    @Delete
    suspend fun delete(trip: Trip)

    @Query("DELETE FROM trip")
    suspend fun deleteAll()

}