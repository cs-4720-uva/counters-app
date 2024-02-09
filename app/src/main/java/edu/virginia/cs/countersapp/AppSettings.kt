package edu.virginia.cs.countersapp

import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

object AppSettingsSerializer: Serializer<AppSettings> {
    override val defaultValue: AppSettings = AppSettings.getDefaultSettings()

    override suspend fun readFrom(input: InputStream): AppSettings {
        return try {
            Json.decodeFromString(
                deserializer = AppSettings.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: AppSettings, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = AppSettings.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }

}

@Serializable
data class AppSettings(
    var isDarkTheme: Boolean?,
    var totalOperations: Int,
    var lastLocation: Location,
    var lastCounterModified: Counter?
) {
    companion object {
        fun getDefaultSettings(): AppSettings {
            return AppSettings(
                null,
                0,
                Location(Double.MIN_VALUE, Double.MIN_VALUE),
                null
            )
        }
    }
}

@Serializable
data class Location(
    val latitude: Double,
    val longitude: Double
)