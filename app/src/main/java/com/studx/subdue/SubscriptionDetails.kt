package com.studx.subdue.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.studx.subdue.*
import com.studx.subdue.logic.Subscription
import kotlin.math.round

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionDetails(navController: NavController, subscription: Subscription) {
    Scaffold(
        topBar = {
            SubscriptionDetailsTopBar(navController)
        },
        bottomBar = {},
        content = { innerPadding ->
            LazyColumn(
                contentPadding = innerPadding
            ) {
                item {
                    SubscriptionDetailsPage(subscription)
                }
            }
        }
    )
}

@Preview
@Composable
fun SubscriptionDetailsPreview() {
//    SubscriptionDetails(subscription = subscriptions[1])
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionDetailsPage(subscription: Subscription) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, 0.dp)
    ) {
        if (subscription.isEmojiImg) {
            Text(
                text = subscription.image,
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.White)
                    .clip(CircleShape),
            )
        }
        else {
            Image(
                painter = painterResource(subscription.image.toInt()),  //#TODO przypisac odpowiedni zasob z res/drawable do wyswietlenia
                contentDescription = "Subscription icon",
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.White)
                    .clip(CircleShape),
            )
        }

        Text(
            text = subscription.name,
            modifier = Modifier.padding(0.dp, 18.dp),
            fontSize = 30.sp,
        )

        Text(
            text = subscription.cost.toString() + " " + subscription.currency,
            modifier = Modifier.padding(0.dp, 8.dp),
            fontSize = 22.sp,
        )

        Text(
            text = "/ " + subscription.timeInterval,
            modifier = Modifier.padding(0.dp, 4.dp),
            fontSize = 14.sp,
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (subscription.isOneOff) {
            Text(
                text = "One-off",
                modifier = Modifier.padding(0.dp, 7.dp),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(value = subscription.dateAnchor.toString(), onValueChange = {},
                label = { Text("Date of expiry", Modifier.background(color = Color.White)) },
                modifier = Modifier
                    .padding(10.dp, 0.dp),
                enabled = false,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                    disabledBorderColor = MaterialTheme.colorScheme.primary,
                    disabledLabelColor = MaterialTheme.colorScheme.primary,
                    disabledTextColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                    textColor = Color.Black,
                )
            )

        } else {
            Text(
                text = "Recurring",
                modifier = Modifier.padding(0.dp, 7.dp),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(value = subscription.dateAnchor.toString(), onValueChange = {},
                label = { Text("Due date", Modifier.background(color = Color.White)) },
                modifier = Modifier
                    .padding(10.dp, 0.dp),
                enabled = false,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                    disabledBorderColor = MaterialTheme.colorScheme.primary,
                    disabledLabelColor = MaterialTheme.colorScheme.primary,
                    disabledTextColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                    textColor = Color.Black,
                )
            )
        }

        OutlinedTextField(value = subscription.paymentMethod, onValueChange = {},
            label = { Text("Payment method", Modifier.background(color = Color.White)) },
            modifier = Modifier
                .padding(10.dp, 0.dp),
            enabled = false,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                disabledBorderColor = MaterialTheme.colorScheme.primary,
                disabledLabelColor = MaterialTheme.colorScheme.primary,
                disabledTextColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                textColor = Color.Black,
            )
        )

        OutlinedTextField(value = subscription.notes, onValueChange = {},
            label = { Text("Notes", Modifier.background(color = Color.White)) },
            modifier = Modifier
                .padding(10.dp, 0.dp),
            enabled = false,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                disabledBorderColor = MaterialTheme.colorScheme.primary,
                disabledLabelColor = MaterialTheme.colorScheme.primary,
                disabledTextColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                textColor = Color.Black,
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionDetailsTopBar(navController: NavController) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Subscription details",
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
            IconButton(onClick = {

                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route) {
                        inclusive = true
                    }
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    )
}
