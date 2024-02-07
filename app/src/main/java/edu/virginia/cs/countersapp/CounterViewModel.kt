package edu.virginia.cs.countersapp

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CounterViewModel(
    private val counterRepository: CounterRepository
): ViewModel() {
    private val _countersList = mutableStateListOf<Counter>()
    val countersList: List<Counter> = _countersList

    init {
        viewModelScope.launch {
            counterRepository.getAllCounters().collect() {counters ->
                _countersList.clear()
                _countersList.addAll(counters)
            }
        }
    }

    private fun getCountersFromDatabase() = counterRepository.getAllCounters()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(),
                mutableListOf()
            )

    fun get(index: Int): Counter {
        return _countersList[index]
    }

    fun add(newCounter: Counter) {
        viewModelScope.launch {
            counterRepository.insert(newCounter)
        }
    }

    fun addByName(name: String) {
        val newCounter = Counter(name)
        add(newCounter)
    }

    fun remove(counter: Counter) {
        _countersList.remove(counter)
        viewModelScope.launch {
            counterRepository.delete(counter)
        }
    }

    fun increment(counter: Counter) {
        counter.increment()
        viewModelScope.launch {
            counterRepository.updateAll(counter)
        }
    }

    fun decrement(counter: Counter) {
        counter.decrement()
        viewModelScope.launch {
            counterRepository.updateAll(counter)
        }
    }

    fun rename(counter: Counter, newName: String) {
        counter.name = newName
        viewModelScope.launch {
            counterRepository.updateAll(counter)
        }
    }

    fun reset(counter: Counter) {
        counter.reset()
        viewModelScope.launch {
            counterRepository.updateAll(counter)
        }
    }
}

class CounterViewModelFactory(
    private val repository: CounterRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CounterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CounterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

