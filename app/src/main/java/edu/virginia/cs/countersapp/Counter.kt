package edu.virginia.cs.countersapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class Counter(
    val id: Int,
    private val initialName: String,
    private val initialValue: Int = 0
) {
    var name: String by mutableStateOf(initialName)
    var value: Int by mutableIntStateOf(initialValue)
    fun increment(amount: Int = 1) {
        value += amount
    }

    fun decrement(amount: Int = 1) {
        value -= amount
    }

    fun reset(newValue: Int = 0) {
        value = newValue
    }
}