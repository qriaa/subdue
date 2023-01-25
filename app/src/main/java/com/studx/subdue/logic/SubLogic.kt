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
import java.util.*


/**
 * This class contains data about a subscription.
 *
 * @property[id] ID of the subscription (UUID)
 * @property[name] Name of subscription
 * @property[notes] User's notes of subscription (Optional)
 * @property[paymentMethod] User's notes of their payment method (Optional)
 * @property[image] Image (path to image in res, emoji when no predetermined image) of subscription
 * @property[cost] Cost of subscription
 * @property[timeMultiplier] How many times should the timeType (month/day/year etc.) be
 * multiplied to produce a new dateAnchor.
 * Can be empty due to it being a one-off.
 * @property[timeInterval] A TimeInterval which will be multiplied by timeMultiplier
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
    val id: String = UUID.randomUUID().toString(),
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
            id != other.id ||
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
 * This class contains settings for the app
 *
 * @property[isDarkmode] decides whether the app is in darkmode
 * @property[sendNotifications] decides whether the app sends notifications
 * @property[defaultCurrency] decides on which currency to use by default
 * @property[daysBeforePaymentAlert] decides how early an alert is sent that a subscription payment
 * is approaching (for instance, if it was 2, then the payment alert would be sent 2 days before
 * the payment date)
 */
data class SubdueSettings (
    var isDarkmode: Boolean = false,
    var sendNotifications: Boolean = true,
    var defaultCurrency: Currency = Currency.getInstance("PLN"),
    var daysBeforePaymentAlert: Long = 2
        )

/**
 * This singleton (`object`) manages settings.
 */
object SettingsManager {
    var settings: SubdueSettings = SubdueSettings()

    private const val settingsFilename: String = "SubdueCfg.json"

    //Utility
    fun revertToDefault() {
        settings = SubdueSettings()
    }

    //Persistence
    fun saveSettings(context: Context) {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(Currency::class.java, CurrencySerializer())
        val gson: Gson = gsonBuilder.create()
        val outputJSON: String = gson.toJson(settings)
        val file = File(context.filesDir, settingsFilename)
        if (!file.exists()) {
            file.createNewFile()
            file.setReadable(true)
            file.setWritable(true)
        }
        file.writeText(outputJSON)
    }

    fun loadSettings(context: Context) {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(Currency::class.java, CurrencyDeserializer())
        val gson: Gson = gsonBuilder.create()
        val file = File(context.filesDir, settingsFilename)
        if (!file.exists()) {
            return
        }
        val jsonText = file.readText()
        this.settings = gson.fromJson(jsonText, SubdueSettings::class.java)
    }
}

/**
 * This singleton (`object`) manages logic.
 */
object SubLogic {
    private var subList: MutableList<Subscription> = mutableListOf()
    private const val saveFilename: String = "SubdueData.json"

    fun getSubList(): MutableList<Subscription>{
        return subList
    }

    fun addSub(subscription: Subscription){
        subList.add(subscription)
    }

    fun removeSub(subscription: Subscription){
        subList.remove(subscription)
    }

    fun removeAllSubs(){
        subList.clear()
    }

    //#TODO codzienny update listy subskrypcji
    // Utility
    fun updatePayment(sub: Subscription){
        if(sub.isOneOff){
            removeSub(sub)
            return
        }
        var newDate = sub.dateAnchor
        newDate = newDate.plus(sub.timeMultiplier, sub.timeInterval)
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

    fun isNearPayment(sub: Subscription, daysBefore: Long): Boolean {
        return LocalDate.now() <= sub.dateAnchor &&
                sub.dateAnchor <= LocalDate.now().plusDays(daysBefore)
    }

    /**
     * This function calculates the payments (already and to be) made in this time interval
     * @param[timeInterval] Time interval in which to calculate
     * @return a pair of already paid subscriptions and sum of payments
     */
    fun calculatePaymentsSum(timeInterval: ChronoUnit): Pair<BigDecimal, BigDecimal> {
        val today = LocalDate.now()
        var alreadyPaid = BigDecimal(0)
        var sumOfPayments = BigDecimal(0)
        val rangeStart: LocalDate
        val rangeStop: LocalDate

        when (timeInterval) { // god forgive me for this
            ChronoUnit.WEEKS -> {
                rangeStart = today.with(DayOfWeek.MONDAY)
                rangeStop = rangeStart.plusWeeks(1)
            }
            ChronoUnit.MONTHS -> {
                rangeStart = today.withDayOfMonth(1)
                rangeStop = rangeStart.plusMonths(1)
            }
            ChronoUnit.YEARS -> {
                rangeStart = today.withDayOfYear(1)
                rangeStop = rangeStart.plusYears(1)
            }
            else -> {
                return Pair(BigDecimal(0), BigDecimal(0))
            }
        }

        for(sub in subList){
            if((sub.dateAnchor.isAfter(rangeStart) || sub.dateAnchor.isEqual(rangeStart))
                && sub.dateAnchor.isBefore(rangeStop)) {

                if(sub.isOneOff) {
                    if(sub.dateAnchor.isBefore(today))
                        alreadyPaid = alreadyPaid.add(sub.cost)
                    sumOfPayments = sumOfPayments.add(sub.cost)
                    continue
                }

                //check past repetitions
                var checkedDate = sub.dateAnchor
                while(!checkedDate.isBefore(rangeStart)) {
                    if(checkedDate.isBefore(today)){
                        alreadyPaid = alreadyPaid.add(sub.cost)
                    }
                    sumOfPayments = sumOfPayments.add(sub.cost)
                    checkedDate = checkedDate.minus(sub.timeMultiplier, sub.timeInterval)
                }

                //check future repetitions
                checkedDate = sub.dateAnchor.plus(sub.timeMultiplier, sub.timeInterval)
                while(checkedDate.isBefore(rangeStop)) {
                    if(checkedDate.isAfter(today)) {
                        sumOfPayments = sumOfPayments.add(sub.cost)
                    }
                    checkedDate = checkedDate.plus(sub.timeMultiplier, sub.timeInterval)
                }
            }
        }
        return Pair(alreadyPaid, sumOfPayments)
    }

    // Persistence
    fun saveSubs(context: Context){
        val gsonBuilder = GsonBuilder()
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
        val gsonBuilder = GsonBuilder()
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
        this.subList = gson.fromJson(jsonText, listType)
    }
}
