package com.studx.subdue.logic

import android.icu.math.BigDecimal
import android.icu.util.Calendar
import android.icu.util.Currency
import android.icu.util.GregorianCalendar
import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDate

class CurrencySerializer : JsonSerializer<Currency>{
    override fun serialize(
        src: Currency,
        typeOfSrc: Type,
        context: JsonSerializationContext)
    : JsonElement
    {
        return JsonPrimitive(src.toString())
    }
}

class CurrencyDeserializer : JsonDeserializer<Currency> {
    override fun deserialize(
        json: JsonElement,
        typeOfSrc: Type,
        context: JsonDeserializationContext
    )
    : Currency
    {
        return Currency.getInstance(json.asJsonPrimitive.asString)
    }
}

class BigDecimalSerializer : JsonSerializer<BigDecimal>{
    override fun serialize(
        src: BigDecimal,
        typeOfSrc: Type,
        context: JsonSerializationContext)
            : JsonElement
    {
        return JsonPrimitive(src.toString())
    }
}

class BigDecimalDeserializer : JsonDeserializer<BigDecimal> {
    override fun deserialize(
        json: JsonElement,
        typeOfSrc: Type,
        context: JsonDeserializationContext
    )
            : BigDecimal
    {
        return BigDecimal(json.asJsonPrimitive.asBigDecimal)
    }
}

class LocalDateSerializer : JsonSerializer<LocalDate>{
    override fun serialize(
        src: LocalDate,
        typeOfSrc: Type,
        context: JsonSerializationContext)
            : JsonElement
    {
        return JsonPrimitive(src.toString())
    }
}

class LocalDateDeserializer : JsonDeserializer<LocalDate> {
    override fun deserialize(
        json: JsonElement,
        typeOfSrc: Type,
        context: JsonDeserializationContext
    )
            : LocalDate
    {
        return LocalDate.parse(json.toString().trim('"'))
    }
}