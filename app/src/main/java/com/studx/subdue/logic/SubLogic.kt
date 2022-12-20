package com.studx.subdue.logic

import android.content.Context
import android.icu.math.BigDecimal
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.icu.util.Currency
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File
import java.util.Date

enum class TimeInterval {
    DAY,
    WEEK,
    MONTH,
    YEAR
}

/**
 * This class contains data about a subscription.
 *
 * @property[name] Name of subscription
 * @property[notes] User's notes of subscription (Optional)
 * @property[paymentMethod] User's notes of their payment method (Optional)
 * @property[image] Image (path to image in res?;emoji when no predetermined image?) of subscription
 * @property[cost] Cost of subscription
 * @property[timeMultiplier] How many times should the timeType (month/day/year etc.) be
 * multiplied to produce a new dateAnchor.
 * Can be empty due to it being a one-off.
 * @property[timeInterval] A TimeInterval which will be multiplied (not literally) by timeMultiplier
 * to produce a new dateAnchor.
 * Can be empty due to it being a one-off.
 * @property[dateAnchor]
 * A date anchor can be interpreted as:
 * - An expiration date of an one-off subscription
 * - Date of next payment of a recurring subscription.
 *
 * To determine which one it is - see boolean isOneOff
 * @property[isOneOff] if `true` - the subscription is an one-off; otherwise it is recurring
 */
data class Subscription (
    var name: String,
    var notes: String = "",
    var paymentMethod: String = "",
    var image: Int, // TODO: solve image/emoji problem
    var cost: BigDecimal = BigDecimal(0),
    var currency: Currency = Currency.getInstance("PLN"),
    var timeMultiplier: Int = 0,
    var timeInterval: TimeInterval = TimeInterval.MONTH,
    var dateAnchor: Date,
    var isOneOff: Boolean = true
)

/**
 * This singleton (`object`) manages logic.
 *
 */
object SubLogic {
    private val subList: MutableList<Subscription> = mutableListOf()
    private val saveFilename: String = "SubdueData.json"

    fun getSubList(): MutableList<Subscription>{
        return subList
    }

    fun addSub(subscription: Subscription){
        subList.add(subscription)
    }

    fun removeSub(subscription: Subscription){
        subList.remove(subscription)
    }

    // Utility
    fun calculateSum(timeInterval: TimeInterval){
        // TODO: return tuple of (alreadyPaid, sumOfPayments)
    }

    // Persistence
    fun saveSubs(context: Context){
        val gsonBuilder: GsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(Currency::class.java, CurrencySerializer())
        gsonBuilder.registerTypeAdapter(Currency::class.java, CurrencyDeserializer())
        gsonBuilder.registerTypeAdapter(BigDecimal::class.java, BigDecimalSerializer())
        gsonBuilder.registerTypeAdapter(BigDecimal::class.java, BigDecimalDeserializer())
        val gson: Gson = gsonBuilder.create()
        val outputJSON: String = gson.toJson(subList)
        val file = File(context.filesDir, saveFilename)
        if (!file.exists()) {
            file.createNewFile()
            file.setReadable(true)
            file.setWritable(true)
        }
        file.writeText(outputJSON)
    }

    fun loadSubs(){

    }
}
