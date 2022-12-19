package com.studx.subdue.logic

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
 * @property[image] Image of subscription
 * @property[cost] Cost of subscription
 * @property[timeMultiplier] How many times should the timeType (month/day/year etc.) be
 * multiplied to produce a new dateAnchor
 * @property[timeInterval] A TimeInterval which will be multiplied (not literally) by timeMultiplier
 * to produce a new dateAnchor
 * @property[dateAnchor]
 * A date anchor can be interpreted as:
 * - An expiration date of an one-off subscription
 * - Date of next payment of a recurring subscription
 * To determine which one it is - see boolean isOneOff
 * @property[isOneOff] if `true` - the subscription is an one-off; otherwise it is recurring
 */
data class Subscription (
    val name: String,
    val image: Int,
    val cost: Double,
    val currency: Currency,
    val timeMultiplier: Int,
    val timeInterval: TimeInterval,
    val dateAnchor: GregorianCalendar,
    val isOneOff: Boolean
)

/**
 * This singleton (`object`) manages business logic.
 *
 */
object SubLogic {
    val subList: MutableList<Subscription> = mutableListOf()

    public fun saveSubs(){

    }
}
