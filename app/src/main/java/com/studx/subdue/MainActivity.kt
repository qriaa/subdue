package com.studx.subdue

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.studx.subdue.logic.*
import com.studx.subdue.ui.SubscriptionDetails
import com.studx.subdue.ui.theme.SubdueTheme
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

sealed class Screen(val route: String) {
    object Home : Screen(route = "home_screen")
    object AddSub : Screen(route = "add_sub_screen")
    object Settings : Screen(route = "settings_screen")
    object SubscriptionDetails : Screen(route = "details_screen/{subscriptionId}") {
        fun createRoute(subscriptionId: String?) = "details_screen/$subscriptionId"
    }
}

class MainActivity : ComponentActivity() {
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        EmojiManager.install(GoogleEmojiProvider())

        val workManager = WorkManager.getInstance(applicationContext)
        val workNotifications = PeriodicWorkRequestBuilder<PaymentReminder>(
            15,
            TimeUnit.SECONDS
        )
            //  .setInitialDelay(calculateTimeDifference(8), TimeUnit.SECONDS) //
            .build()
        val workUpdateJSON = PeriodicWorkRequestBuilder<DateUpdateWorker>(
            15,
            TimeUnit.SECONDS
        )
            //  .setInitialDelay(calculateTimeDifference(8), TimeUnit.SECONDS) //
            .build()
        // minimalny interwal pomiedzy wykonaniami workera to 15 minut
        SettingsManager.loadSettings(this)
        workManager.enqueueUniquePeriodicWork(
            "Subscription notification work",
            ExistingPeriodicWorkPolicy.REPLACE,
            workNotifications
        )
        workManager.enqueueUniquePeriodicWork(
            "Update subscriptions dates work",
            ExistingPeriodicWorkPolicy.REPLACE,
            workUpdateJSON
        )
        setContent {
            SubdueTheme(darkTheme = SettingsManager.settings.isDarkmode) {
                SubLogic.loadSubs(this)
                val subscriptions = SubLogic.getSubList()
                val navController = rememberNavController()
                SetUpNavGraph(this, navController = navController, subscriptions)
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
fun SetUpNavGraph(context: Context, navController: NavHostController, subscriptions: MutableList<Subscription>) {
    NavHost(navController = navController, startDestination = "home_screen") {
        composable(Screen.Home.route) {
            //#TODO wczytaj subskrypcje z bazy danych i przekaz do mainpage
            MainPage(context, subscriptions, navController)
        }
        composable(Screen.AddSub.route) {
            AddSubscription(navController, context)
        }
        composable(Screen.Settings.route) {
            Settings(navController, context)
        }
        //TODO bez ID subskrypcji nie da się przejść do szczegółów
        composable(Screen.SubscriptionDetails.route) { navBackStackEntry ->
            val subToShowDetails = navBackStackEntry.arguments?.getString("subscriptionId")
            val subToShow = subscriptions.find { it.id == subToShowDetails }
            if (subToShow != null) {
                SubscriptionDetails(navController, subToShow)
            } else {
                Toast.makeText(context, "Subscription not found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}