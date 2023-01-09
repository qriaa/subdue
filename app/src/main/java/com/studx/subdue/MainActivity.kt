package com.studx.subdue

import android.content.Context
import android.icu.math.BigDecimal
import android.icu.util.Currency
import android.os.Bundle
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
import com.studx.subdue.ui.theme.SubdueTheme
import java.time.LocalDate
import java.time.temporal.ChronoUnit

sealed class Screen(val route: String) {
    object Home : Screen(route = "home_screen")
    object AddSub : Screen(route = "add_sub_screen")
    object Settings : Screen(route = "settings_screen")
    object SubscriptionDetails : Screen(route = "details_screen/{subscriptionId}") {
        fun createRoute(subscriptionId: Int) = "details_screen/$subscriptionId"
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var darkMode by mutableStateOf(false)

        setContent {
            SubdueTheme(darkTheme = darkMode || isSystemInDarkTheme()) {
                val navController = rememberNavController()
                SetUpNavGraph(this, navController = navController)
            }
        }
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
            AddSubscription(navController)
        }
        composable(Screen.Settings.route) {
            Settings(navController)
        }
        composable(Screen.SubscriptionDetails.route) {
//            navBackStackEntry ->
//            val subscription = navBackStackEntry.arguments.get //get what?
//            SubscriptionDetails(subscription = subscription)
        }
    }
}