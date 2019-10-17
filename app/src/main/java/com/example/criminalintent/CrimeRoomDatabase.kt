package com.example.criminalintent

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

@Database(entities = [Crime::class], version = 1)
@TypeConverters(com.example.criminalintent.TypeConverters::class)
abstract class CrimeRoomDatabase: RoomDatabase() {
    abstract fun crimeDao(): CrimeDao

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val crimeDao = database.crimeDao()

                    // Delete all content here.
                    crimeDao.deleteAll()

                    val calendar = GregorianCalendar()
                    calendar.set(2019,1,15,5,30,15)

                    var crime = Crime("crime1")
                    crime.title = "Crime 1"
                    crime.date = calendar.time
                    crime.solved = true
                    crimeDao.insert(crime)

                    crime = Crime("crime2")
                    crime.title = "Crime 2"
                    crimeDao.insert(crime)

                    crime = Crime("crime3")
                    crime.title = "Crime 3"
                    crime.solved = true
                    crimeDao.insert(crime)

                    crime = Crime("crime4")
                    crime.title = "Crime 4"
                    crimeDao.insert(crime)
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: CrimeRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): CrimeRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CrimeRoomDatabase::class.java,
                    "crime_database"
                )
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
