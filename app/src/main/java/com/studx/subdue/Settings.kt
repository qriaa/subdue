package com.studx.subdue

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.studx.subdue.logic.SettingsManager
import com.studx.subdue.ui.theme.SubdueTheme

//const? podsumowujac: ???
const val DEFAULT_CURRENCY = "PLN"
const val PAYMENT_DUE_ALERT = "1"
const val DARK_MODE = false

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(navController: NavController, context: Context) {
    SubdueTheme(
        darkTheme = SettingsManager.settings.isDarkmode
    ) {
        Scaffold(
            topBar = {
                SettingsTopBar(navController)
            },
            bottomBar = {},
            content = { innerPadding ->
                LazyColumn(
                    contentPadding = innerPadding
                ) {
                    item {
                        SettingsPage(context)
                    }
                }
            }
        )
    }
}

//@Preview
//@Composable
//fun PreviewSettings() {
//    Settings()
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(navController: NavController) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Settings",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route) {
                        inclusive = true
                    }
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back navigation"
                )
            }
        },
        actions = {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Confirm and save"
                )
            }
        }
    )
}

@Composable
fun SettingsPage(context: Context) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(35.dp, 70.dp)
    ) {
        Row {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.75f),
            ) {
                Text(
                    text = "Dark mode",
                    fontSize = 26.sp
                )
            }
            Column() {
                SwitchModeButton(context)
            }
        }

        Row {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
            ) {
                Text(
                    text = "Default currency",
                    fontSize = 26.sp
                )
            }
            Column() {
                DefaultCurrencyInput()
            }
        }

        Row {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
            ) {
                Text(
                    text = "Payment due alert\nin days before",
                    fontSize = 26.sp,
                    maxLines = 2
                )
            }
            Column() {
                PaymentDueAlertInput()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultCurrencyInput() {
    var text by remember { mutableStateOf(DEFAULT_CURRENCY) }
    OutlinedTextField(value = text,
        onValueChange = { newText ->
            text = newText
        },
        label = {
            Text(text = "")
        },
        modifier = Modifier
            .width(90.dp)
            .padding(3.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentDueAlertInput() {
    var text by remember { mutableStateOf(PAYMENT_DUE_ALERT) }
    OutlinedTextField(value = text,
        onValueChange = { newText ->
            text = newText
        },
        label = {
            Text(text = "")
        },
        modifier = Modifier
            .width(90.dp)
            .padding(3.dp))
}

@Composable
fun SwitchModeButton(context: Context){
    var checkedState = remember {
        mutableStateOf(SettingsManager.settings.isDarkmode)
    }

    Switch(
        checked = checkedState.value,
        onCheckedChange = {
            SettingsManager.loadSettings(context)
            SettingsManager.settings.isDarkmode = !SettingsManager.settings.isDarkmode
            SettingsManager.saveSettings(context)
            SettingsManager.loadSettings(context)
            checkedState.value = SettingsManager.settings.isDarkmode
        }
    )
}