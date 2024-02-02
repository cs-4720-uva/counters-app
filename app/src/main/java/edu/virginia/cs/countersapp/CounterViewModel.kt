package edu.virginia.cs.countersapp

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CounterViewModel: ViewModel() {
    private var _counters = MutableStateFlow<List<Counter>>(emptyList())
    val counters = _counters.asStateFlow()

    init {
        viewModelScope.launch {
            _counters.emit(
                CounterSample.data
            )
        }
    }

    fun add(item: Counter) {
        _counters.value = _counters.value + item
    }

    fun addByName(name: String) {
        val nextId = _counters.value
            .maxBy { counter -> counter.id }
            .id + 1
        val newCounter = Counter(nextId, name)
        _counters.value = _counters.value + newCounter
    }

    fun remove(item: Counter) {
        _counters.value = _counters.value - item
    }

    fun update(index: Int, newItem: Counter) {
        val newList = _counters.value.toMutableList()
        newList[index] = newItem
        _counters.value = newList
    }
}