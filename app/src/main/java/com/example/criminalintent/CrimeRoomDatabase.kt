package com.example.criminalintent

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Crime::class], version = 1)
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

                    var crime = Crime()
                    crime.title = "Crime 1"
                    crimeDao.insert(crime)

                    crime = Crime()
                    crime.title = "Crime 2"
                    crimeDao.insert(crime)

                    crime = Crime()
                    crime.title = "Crime 3"
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
