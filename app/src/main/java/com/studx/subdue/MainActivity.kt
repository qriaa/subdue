package com.studx.subdue

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
import com.studx.subdue.ui.theme.SubdueTheme

sealed class Screen(val route: String) {
    object Home : Screen(route = "home_screen")
    object AddSub : Screen(route = "add_sub_screen")
    object Settings : Screen(route = "settings_screen")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var darkMode by mutableStateOf(false)

        setContent {
            SubdueTheme(darkTheme = darkMode || isSystemInDarkTheme()) { // darkmode do ogarniecia bo to nie bedzie dzialalo
                val navController = rememberNavController()
                SetUpNavGraph(navController = navController)
            }
        }
    }
}

@Composable
fun SetUpNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home_screen") {
        composable(Screen.Home.route) {
            MainPage(subscriptions = subscriptions, navController)
        }
        composable(Screen.AddSub.route) {
            AddSubscription(navController)
        }
        composable(Screen.Settings.route) {
            Settings(navController)
        }
    }
}