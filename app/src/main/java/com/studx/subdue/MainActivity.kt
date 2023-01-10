package com.studx.subdue

import android.content.Context
import android.icu.math.BigDecimal
import android.icu.util.Currency
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.studx.subdue.logic.SubLogic
import com.studx.subdue.logic.Subscription
import java.time.temporal.ChronoUnit
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.studx.subdue.ui.SubscriptionDetails
import com.studx.subdue.ui.theme.SubdueTheme
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

sealed class Screen(val route: String) {
    object Home : Screen(route = "home_screen")
    object AddSub : Screen(route = "add_sub_screen")
    object Settings : Screen(route = "settings_screen")
    object SubscriptionDetails : Screen(route = "details_screen/{subscriptionId}") {
        fun createRoute(subscriptionId: String) = "details_screen/$subscriptionId"
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val workManager = WorkManager.getInstance(applicationContext)
        val work = PeriodicWorkRequestBuilder<PaymentReminder>(
            15,
            TimeUnit.SECONDS
        ) //minimalny czas to 15minut
            //  .setInitialDelay(calculateTimeDifference(8), TimeUnit.SECONDS) //
            .build()
        var darkMode by mutableStateOf(false)
        workManager.enqueueUniquePeriodicWork(
            "Subscription due reminder",
            ExistingPeriodicWorkPolicy.REPLACE,
            work
        )
        setContent {
            SubdueTheme(darkTheme = darkMode || isSystemInDarkTheme()) {
                val navController = rememberNavController()
                SetUpNavGraph(this, navController = navController)
            }
        }
    }

    fun calculateTimeDifference(hourOfReminder: Int): Long {
        val currentTime = LocalDateTime.now()
        val hoursInSeconds = currentTime.hour * 3600
        val minutesInSeconds = currentTime.minute * 60
        val totalTimeInSeconds = hoursInSeconds + minutesInSeconds + currentTime.second
        val waitTime = (86400 - (totalTimeInSeconds - hourOfReminder * 3600)) % 86400
        return waitTime.toLong()
    }
}

@Composable
fun SetUpNavGraph(context: Context, navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home_screen") {
        composable(Screen.Home.route) {
            //#TODO wczytaj subskrypcje z bazy danych i przekaz do mainpage
            SubLogic.loadSubs(context)
            val subscriptions = SubLogic.getSubList()
            MainPage(context, subscriptions, navController)
        }
        composable(Screen.AddSub.route) {
            AddSubscription(navController, context)
        }
        composable(Screen.Settings.route) {
            Settings(navController)
        }
        //TODO bez ID subskrypcji nie da się przejść do szczegółów
        composable(Screen.SubscriptionDetails.route) { navBackStackEntry ->
            val subscriptionName = navBackStackEntry.arguments?.getString("subscriptionName")
                val subToShowDetails = SubLogic.getSubList().find { sub -> sub.name == subscriptionName }
                if (subToShowDetails != null) {
                    SubscriptionDetails(navController, subToShowDetails)
                } else {
                    Toast.makeText(context, "Subscription not found", Toast.LENGTH_SHORT).show()
                }
        }
    }
}