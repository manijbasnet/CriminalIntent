package com.example.criminalintent

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CrimeDao {

    @Query("SELECT * FROM crime_table ORDER BY id ASC")
    fun getAllCrimes(): LiveData<List<Crime>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(crime: Crime)

    @Query("DELETE FROM crime_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM crime_table WHERE id = :id LIMIT 1")
    fun get(id: String): LiveData<Crime>
}