package com.example.criminalintent

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CrimeViewModel(application: Application): AndroidViewModel(application) {

    private val repository: CrimeRepository

    val allWords: LiveData<List<Crime>>
    val crime = MutableLiveData<Crime>()

    init {
        val wordsDao = CrimeRoomDatabase.getDatabase(application, viewModelScope).crimeDao()
        repository = CrimeRepository(wordsDao)
        allWords = repository.allCrimes
    }

    fun insert(word: Crime) = viewModelScope.launch {
        repository.insert(word)
    }

    fun get(crimeId: String): LiveData<Crime> {
        return repository.getCrime(crimeId)
    }
}