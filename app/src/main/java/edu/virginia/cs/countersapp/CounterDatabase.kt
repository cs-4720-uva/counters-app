package edu.virginia.cs.countersapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Counter::class], version = 1)
abstract class CounterDatabase: RoomDatabase() {
    abstract val counterDao: CounterDAO

    companion object {
        @Volatile
        private var INSTANCE: CounterDatabase? = null

        fun getDatabase(context: Context): CounterDatabase {
            if (INSTANCE == null) {
                val instance = getDatabaseInstance(context)
                INSTANCE = instance
            }
            return INSTANCE!!
        }

        private fun getDatabaseInstance(context: Context): CounterDatabase {
            return Room.databaseBuilder(
                    context.applicationContext,
                    CounterDatabase::class.java,
                    "counters_database"
                ).build()
        }
    }


}