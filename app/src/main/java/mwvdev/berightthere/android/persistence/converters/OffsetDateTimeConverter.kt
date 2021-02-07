package mwvdev.berightthere.android.persistence.converters

import androidx.room.TypeConverter
import java.time.OffsetDateTime

class OffsetDateTimeConverter {

    @TypeConverter
    fun fromOffsetDateTime(value: OffsetDateTime): String {
        return value.toString()
    }

    @TypeConverter
    fun toOffsetDateTime(value: String): OffsetDateTime {
        return OffsetDateTime.parse(value)
    }

}