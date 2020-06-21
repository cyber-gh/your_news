package dev.skyit.yournews.repository.database

import androidx.room.TypeConverter
import dev.skyit.yournews.api.models.Source
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json


class SpecialTypeConverters {

    @UnstableDefault
    @TypeConverter
    fun fromSource(str: String) : Source{
        return Json.parse(Source.serializer(), str)
    }

    @UnstableDefault
    @TypeConverter
    fun sourceToString(source: Source) : String {
        return Json.stringify(Source.serializer(), source)
    }
}