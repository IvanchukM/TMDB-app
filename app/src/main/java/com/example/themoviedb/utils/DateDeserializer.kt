package com.example.themoviedb.utils

import com.google.gson.*
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

private const val DATE_TEMPLATE = "yyyy-MM-dd"

object DateDeserializer : JsonDeserializer<Date?> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Date? {
        return try {
            SimpleDateFormat(DATE_TEMPLATE, Locale.ENGLISH).parse(json.asString)
        } catch (e: ParseException) {
            null
        }
    }
}
