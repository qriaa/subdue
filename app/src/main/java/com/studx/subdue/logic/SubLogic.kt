package com.studx.subdue.logic

import android.icu.math.BigDecimal
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.icu.util.Currency

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
    var dateAnchor: GregorianCalendar,
    var isOneOff: Boolean = true
)

/**
 * This singleton (`object`) manages business logic.
 *
 */
object SubLogic {
    private val subList: MutableList<Subscription> = mutableListOf()

    public fun getSubList(): MutableList<Subscription>{
        return subList
    }

    public fun addSub(subscription: Subscription){
        subList.add(subscription)
    }

    public fun removeSub(subscription: Subscription){
        subList.remove(subscription)
    }

    // Utility
    public fun calculateSum(timeInterval: TimeInterval){
        // TODO: return tuple of (alreadyPaid, sumOfPayments)
    }

    // Persistence
    public fun saveSubs(){
        // TODO: save to JSON - Gson
    }

    public fun loadSubs(){

    }
}
