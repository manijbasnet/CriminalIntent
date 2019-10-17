package com.example.criminalintent

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "crime_table")
data class Crime(@PrimaryKey @ColumnInfo(name = "id") val id: String = UUID.randomUUID().toString()) {

    @ColumnInfo(name = "date") var date: Date = Date()
    @ColumnInfo(name = "title") var title: String? = null
    @ColumnInfo(name = "solved") var solved: Boolean = false

}