package edu.virginia.cs.countersapp

class MockCounterRepository {
    private val data: List<Counter> = listOf(
        Counter("Push-ups"),
        Counter("Sit-ups"),
        Counter("Burpies")
    )

    fun getCounters(): List<Counter> {
        return data
    }
}