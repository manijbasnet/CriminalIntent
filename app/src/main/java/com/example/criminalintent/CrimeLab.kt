package com.example.criminalintent

import java.util.*
import kotlin.collections.ArrayList

object CrimeLab {

    val mCrimes: ArrayList<Crime> = ArrayList()

    fun addCrime(c: Crime) {
        mCrimes.add(c)
    }

    fun getCrime (id: UUID) : Crime? {
        for (crime in mCrimes){
            if(crime.id == id){
                return crime
            }
        }
        return null
    }

}