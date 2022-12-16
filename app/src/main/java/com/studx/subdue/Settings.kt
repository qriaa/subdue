package com.studx.subdue

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

//const? podsumowujac: ???
const val DEFAULT_CURRENCY = "PLN"
const val PAYMENT_DUE_ALERT = "1"
const val DARK_MODE = false

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(navController: NavController) {
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
                    SettingsPage()
                }
            }
        }
    )
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
fun SettingsPage() {
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
                SwitchModeButton()
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
fun SwitchModeButton() {
    var checkedState = remember {
        mutableStateOf(false)
    }

    Switch(
        checked = checkedState.value,
        onCheckedChange = {
            checkedState.value = it
        }
    )
}