package com.studx.subdue.logic

import android.content.Context
import android.icu.math.BigDecimal
import android.icu.util.Currency
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit


/**
 * This class contains data about a subscription.
 *
 * @property[name] Name of subscription
 * @property[notes] User's notes of subscription (Optional)
 * @property[paymentMethod] User's notes of their payment method (Optional)
 * @property[image] Image (path to image in res, emoji when no predetermined image) of subscription
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
    var image: String,
    var isEmojiImg: Boolean,
    var cost: BigDecimal = BigDecimal(0),
    var currency: Currency = Currency.getInstance("PLN"),
    var timeMultiplier: Long = 0,
    var timeInterval: ChronoUnit = ChronoUnit.MONTHS,
    var dateAnchor: LocalDate = LocalDate.now(),
    var isOneOff: Boolean = true
)
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Subscription

        if(
            name != other.name ||
            notes != other.notes ||
            paymentMethod != other.paymentMethod ||
            image != other.image ||
            cost != other.cost ||
            currency != other.currency ||
            timeMultiplier != other.timeMultiplier ||
            timeInterval != other.timeInterval ||
            dateAnchor != other.dateAnchor ||
            isOneOff != other.isOneOff
        )
            return false
        return true
    }
}
/**
 * This singleton (`object`) manages logic.
 *
 */
object SubLogic {
    private var subList: MutableList<Subscription> = mutableListOf()
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
    fun updatePayment(sub: Subscription){
        if(sub.isOneOff){
            removeSub(sub)
            return
        }
        val newDate = sub.dateAnchor
        newDate.plus(sub.timeMultiplier, sub.timeInterval)
        sub.dateAnchor = newDate
    }

    fun updateAllPayments(){
        val today = LocalDate.now()
        for(sub in subList){
            if(sub.dateAnchor.isEqual(today)){
                updatePayment(sub)
            }
        }
    }

    fun calculateSum(timeInterval: ChronoUnit){
        // TODO: return tuple of (alreadyPaid, sumOfPayments) (this is hell)
        val today = LocalDate.now()
        var alreadyPaid: BigDecimal = BigDecimal(0)
        var sumOfPayments: BigDecimal = BigDecimal(0)
        if(timeInterval == ChronoUnit.WEEKS){
            val weekStart = today.with(DayOfWeek.MONDAY)
            val weekEnd = weekStart.plusWeeks(1)
            for(sub in subList){
                if((sub.dateAnchor.isAfter(weekStart) || sub.dateAnchor.isEqual(weekStart))
                           && sub.dateAnchor.isBefore(weekEnd)){
                    if(sub.dateAnchor.isBefore(today)) {
                        alreadyPaid.add(sub.cost)
                    }
                    }
            }

        } else if (timeInterval == ChronoUnit.MONTHS) {

        } else if (timeInterval == ChronoUnit.YEARS) {

        }
    }

    // Persistence
    fun saveSubs(context: Context){
        val gsonBuilder: GsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(Currency::class.java, CurrencySerializer())
        gsonBuilder.registerTypeAdapter(BigDecimal::class.java, BigDecimalSerializer())
        gsonBuilder.registerTypeAdapter(LocalDate::class.java, LocalDateSerializer())
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

    fun loadSubs(context: Context){
        val gsonBuilder: GsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(Currency::class.java, CurrencyDeserializer())
        gsonBuilder.registerTypeAdapter(BigDecimal::class.java, BigDecimalDeserializer())
        gsonBuilder.registerTypeAdapter(LocalDate::class.java, LocalDateDeserializer())
        val gson: Gson = gsonBuilder.create()
        val file = File(context.filesDir, saveFilename)
        if (!file.exists()) {
            return
        }
        val jsonText = file.readText()
        val listType: Type = object : TypeToken<MutableList<Subscription>>(){}.type
        this.subList = gson.fromJson<MutableList<Subscription>>(jsonText, listType)
    }
}
