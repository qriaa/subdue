package com.studx.subdue.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.studx.subdue.*

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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionDetailsPage(subscription: Subscription) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, 0.dp)
    ) {
        Image(
            painter = painterResource(subscription.image),
            contentDescription = "Subscription image",
            modifier = Modifier
                .size(140.dp)
                .background(Color.White)
                .clip(CircleShape)
                .padding(15.dp)
        )

        Text(
            text = subscription.name,
            modifier = Modifier.padding(10.dp, 0.dp)
        )


//        NameInput()
//
//        Row() {
//            PriceInput()
//            CurrencyList()
//        }
//
//        PagerView()
//
//        LabeledInput("Labels", "e.g. Music")
//        LabeledInput("Payment method", "e.g. Card")
//        LabeledInput("Notes", "")
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
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    )
}
