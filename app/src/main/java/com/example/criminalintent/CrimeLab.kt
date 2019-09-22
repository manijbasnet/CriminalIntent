package com.example.criminalintent

import java.util.*
import kotlin.collections.ArrayList

object CrimeLab {

    val mCrimes: ArrayList<Crime> = ArrayList()

    init {
        for (i in 1..100){
            val crime = Crime()
            crime.title = ("Crime #" + i)
            crime.solved = i % 2 == 0
            mCrimes.add(crime)
        }
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