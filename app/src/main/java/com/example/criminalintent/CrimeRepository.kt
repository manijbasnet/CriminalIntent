package com.example.criminalintent

import androidx.lifecycle.LiveData

class CrimeRepository (private val crimeDao: CrimeDao) {

    val allCrimes: LiveData<List<Crime>> = crimeDao.getAllCrimes()

    suspend fun insert(crime: Crime){
        crimeDao.insert(crime)
    }
}