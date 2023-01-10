package com.studx.subdue

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
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.studx.subdue.logic.SubLogic
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.studx.subdue.logic.Subscription
import com.studx.subdue.logic.DateUpdateWorker
import com.studx.subdue.ui.SubscriptionDetails
import com.studx.subdue.ui.theme.SubdueTheme
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.google.GoogleEmojiProvider
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

sealed class Screen(val route: String) {
    object Home : Screen(route = "home_screen")
    object AddSub : Screen(route = "add_sub_screen")
    object Settings : Screen(route = "settings_screen")
    object SubscriptionDetails : Screen(route = "details_screen") {
        const val subIdArg = "sub_id_arg"
        val routeWithArgs = "$route/{$subIdArg}"
        val arguments = listOf(
            navArgument(subIdArg) {type = NavType.StringType}
        )
    }
}

class MainActivity : ComponentActivity() {
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
        var darkMode by mutableStateOf(false)
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
        SubLogic.loadSubs(this)
        setContent {
            SubdueTheme(darkTheme = darkMode || isSystemInDarkTheme()) {
                val navController = rememberNavController()
                SetUpNavGraph(navController = navController)
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
fun SetUpNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home_screen") {
        composable(Screen.Home.route) {
            //#TODO wczytaj subskrypcje z bazy danych i przekaz do mainpage
            MainPage(navController)
        }
        composable(Screen.AddSub.route) {
            AddSubscription(navController)
        }
        composable(Screen.Settings.route) {
            Settings(navController)
        }
        //TODO bez ID subskrypcji nie da się przejść do szczegółów
        composable(
            route = Screen.SubscriptionDetails.routeWithArgs,
            arguments = Screen.SubscriptionDetails.arguments
        ) { navBackStackEntry ->
            val subToShowDetails =
                navBackStackEntry.arguments?.getString(Screen.SubscriptionDetails.subIdArg)
            if (subToShowDetails != null) {
                SubscriptionDetails(navController, subToShowDetails)
            } else {
                //DO NOT UNCOMMENT OR ELSE SUFFER
                //Toast.makeText(LocalContext.current, "Subscription not found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}