package edu.virginia.cs.countersapp

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Entity(tableName = "counters")
@Serializable
data class Counter(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "value") var value: Int = 0,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(value)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Counter> {
        override fun createFromParcel(parcel: Parcel): Counter {
            return Counter(parcel)
        }

        override fun newArray(size: Int): Array<Counter?> {
            return arrayOfNulls(size)
        }
    }
}