package com.studx.subdue.logic

import android.content.ContextWrapper
import android.icu.util.GregorianCalendar
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.util.*

@RunWith(AndroidJUnit4::class)
class SubLogicTest {
    @Test
    fun addSubTest() {
        val minSub: com.studx.subdue.logic.Subscription = com.studx.subdue.logic.Subscription(
            name = "test",
            image = 0
        )
        SubLogic.addSub(minSub)
        assertEquals(SubLogic.getSubList()[0], minSub)
    }

    @Test
    fun saveLoadTest() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val minSub: com.studx.subdue.logic.Subscription = com.studx.subdue.logic.Subscription(
            name = "test",
            image = 0
        )

        SubLogic.addSub(minSub)
        SubLogic.saveSubs(appContext)
        SubLogic.loadSubs(appContext)

        assertEquals(SubLogic.getSubList()[0].hashCode(), minSub.hashCode())
    }
}