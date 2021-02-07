package mwvdev.berightthere.android.persistence.converters

import androidx.room.TypeConverter
import mwvdev.berightthere.android.model.TransportMode

class TransportModeConverter {

    @TypeConverter
    fun fromTransportMode(value: TransportMode): String = value.name

    @TypeConverter
    fun toTransportMode(value: String): TransportMode = enumValueOf(value)

}