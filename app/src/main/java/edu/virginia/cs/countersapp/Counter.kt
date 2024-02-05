package edu.virginia.cs.countersapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "counters")
data class Counter(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "value") var value: Int = 0,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) {
    fun increment(amount: Int = 1) {
        value += amount
    }

    fun decrement(amount: Int = 1) {
        value -= amount
    }

    fun reset(newValue: Int = 0) {
        value = newValue
    }

    override fun hashCode(): Int {
        return id
    }

    override fun equals(other: Any?): Boolean {
        return (other is Counter) && this.id == other.id
    }
}