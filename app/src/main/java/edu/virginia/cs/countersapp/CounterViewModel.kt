package edu.virginia.cs.countersapp

data class CounterViewModel(val currentValue:Int = 0) {
    fun increment() = CounterViewModel(currentValue + 1)

    fun decrement() = CounterViewModel(currentValue - 1)

    fun reset() = CounterViewModel(0)
}