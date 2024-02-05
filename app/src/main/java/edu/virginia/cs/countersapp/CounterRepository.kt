package edu.virginia.cs.countersapp

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
class CounterRepository(
    private val counterDao: CounterDAO
) {
    fun getAllCounters(): Flow<List<Counter>> {
        return counterDao.getAllCounters()
    }

    @WorkerThread
    suspend fun insert(counter: Counter) {
        counterDao.insertCounters(counter)
    }

    @WorkerThread
    suspend fun delete(counter: Counter) {
        counterDao.delete(counter)
    }

    @WorkerThread
    suspend fun updateAll(vararg counters: Counter) {
        counterDao.updateCounters(*counters)
    }
}