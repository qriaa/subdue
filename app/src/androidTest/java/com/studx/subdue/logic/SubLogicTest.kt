package com.studx.subdue.logic

import android.icu.math.BigDecimal
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import java.util.*

import com.studx.subdue.logic.*
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@RunWith(AndroidJUnit4::class)
class SubLogicTest {

    private fun prepareSubs() {
        SubLogic.removeAllSubs()
        SubLogic.addSub(Subscription(
            name = "TwoEachDaySub",
            image = "a",
            isEmojiImg = false,
            cost = BigDecimal(2),
            timeMultiplier = 1,
            timeInterval = ChronoUnit.DAYS,
            isOneOff = false
        ))
        SubLogic.addSub(Subscription(
            name = "ThreeEachWeekSub",
            image = "a",
            isEmojiImg = false,
            cost = BigDecimal(3),
            timeMultiplier = 1,
            timeInterval = ChronoUnit.WEEKS,
            isOneOff = false
        ))
        SubLogic.addSub(Subscription(
            name = "FiveEachMonthSub",
            image = "a",
            isEmojiImg = false,
            cost = BigDecimal(5),
            timeMultiplier = 1,
            timeInterval = ChronoUnit.MONTHS,
            isOneOff = false
        ))
        SubLogic.addSub(Subscription(
            name = "SevenEachYearSub",
            image = "a",
            isEmojiImg = false,
            cost = BigDecimal(7),
            timeMultiplier = 1,
            timeInterval = ChronoUnit.YEARS,
            isOneOff = false
        ))
    }

    fun addOneOffs(){
        SubLogic.addSub(Subscription(
            name = "ElevenOneOffTomorrow",
            image = "a",
            isEmojiImg = false,
            cost = BigDecimal(7),
            timeMultiplier = 1,
            timeInterval = ChronoUnit.YEARS,
            dateAnchor = LocalDate.now().plusDays(1),
            isOneOff = false
        ))
        SubLogic.addSub(Subscription(
            name = "ThirteenOneOffYesterday",
            image = "a",
            isEmojiImg = false,
            cost = BigDecimal(7),
            timeMultiplier = 1,
            timeInterval = ChronoUnit.YEARS,
            dateAnchor = LocalDate.now().plusDays(-1),
            isOneOff = false
        ))
    }

    @Test
    fun addSubTest() {
        val minSub = Subscription(
            name = "test",
            image = "a",
            isEmojiImg = false
        )
        SubLogic.addSub(minSub)
        assertEquals(SubLogic.getSubList()[0], minSub)
    }

    @Test
    fun saveLoadTest() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val minSub = Subscription(
            name = "test",
            image = "a",
            isEmojiImg = false
        )

        SubLogic.addSub(minSub)
        SubLogic.saveSubs(appContext)
        SubLogic.loadSubs(appContext)

        assertEquals(SubLogic.getSubList()[0].hashCode(), minSub.hashCode())
    }

    @Test
    fun testCalcRecSum() {
        prepareSubs()

        val today = LocalDate.now()

        val weekPair = SubLogic.calculatePaymentsSum(ChronoUnit.WEEKS)
        val monthStart = today.withDayOfMonth(1)
        val monthEnd = monthStart.plusMonths(1)
        val monthPair = SubLogic.calculatePaymentsSum(ChronoUnit.MONTHS)
        val yearStart = today.withDayOfYear(1)
        val yearEnd = yearStart.plusYears(1)
        val yearPair = SubLogic.calculatePaymentsSum(ChronoUnit.YEARS)

        assertEquals(BigDecimal((2*7) + 3 + 5 + 7), weekPair.second)

        val expectedMonthResult = BigDecimal(
            (ChronoUnit.DAYS.between(monthStart, monthEnd) * 2)
            + (ChronoUnit.WEEKS.between(monthStart, monthEnd) * 3)
            + 5 + 7
        )
        assertEquals(expectedMonthResult, monthPair.second)

        val expectedYearResult = BigDecimal(
            (ChronoUnit.DAYS.between(yearStart, yearEnd) * 2)
            + (ChronoUnit.WEEKS.between(yearStart, yearEnd) * 3)
            + (ChronoUnit.MONTHS.between(yearStart, yearEnd) * 5)
            + 7
        )
        assertEquals(expectedYearResult, yearPair.second)
    }
}