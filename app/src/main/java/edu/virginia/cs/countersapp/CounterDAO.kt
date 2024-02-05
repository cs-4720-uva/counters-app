package edu.virginia.cs.countersapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CounterDAO {
    @Query("SELECT * FROM counters ORDER BY name ASC")
    fun getAllCounters(): Flow<List<Counter>>

    @Query("SELECT * FROM counters WHERE id = :id")
    fun findByID(id: Int): Counter?

    @Query("SELECT * FROM counters WHERE name = :searchName")
    fun findByName(searchName: String): Flow<List<Counter>>

    @Insert
    suspend fun insertCounters(vararg counters: Counter)

    @Update
    suspend fun updateCounters(vararg counters: Counter)

    @Delete
    suspend fun delete(counter: Counter)
}