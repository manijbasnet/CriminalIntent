package com.example.criminalintent

import java.util.*

class Crime {

    val id: UUID = UUID.randomUUID()
    var date: Date = Date()
    lateinit var title: String
    var solved: Boolean = false

}