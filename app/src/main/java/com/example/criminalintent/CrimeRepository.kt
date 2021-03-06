package com.example.criminalintent

import androidx.lifecycle.LiveData

class CrimeRepository (private val crimeDao: CrimeDao) {

    val allCrimes: LiveData<List<Crime>> = crimeDao.getAllCrimes()

    fun getCrime(crimeId: String) = crimeDao.get(crimeId)

    suspend fun insert(crime: Crime){
        crimeDao.insert(crime)
    }
}