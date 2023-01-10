package com.studx.subdue


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.studx.subdue.logic.SubLogic
import com.studx.subdue.logic.Subscription
import java.time.temporal.ChronoUnit

const val ROW_HEIGHT = 90


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(currContext: Context, subscriptions: MutableList<Subscription>, navController: NavHostController) {
    Scaffold(
        topBar = {
            TopBar(navController)
        },
        bottomBar = {
            BottomBar()
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddSub.route)
                },
                modifier = Modifier) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Add subscription",
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,

        content = { innerPadding ->
            LazyColumn(
                contentPadding = innerPadding,
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                items(subscriptions.sortedBy { subscription -> subscription.dateAnchor }) { sub ->
                    Surface(shape = RoundedCornerShape(20.dp), onClick = {
                        navController.navigate(Screen.SubscriptionDetails.createRoute(sub.name))
                        Toast.makeText(
                            currContext,
                            "Clicked on ${sub.name}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }) {
                        SubscriptionBox(rowHeight = ROW_HEIGHT, rowColor = MaterialTheme.colorScheme.primaryContainer, subscription = sub)
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun PreviewTopAppBar() {
//    MainPage(subscriptions)
}

@Composable
fun BottomBar() {
    val selectedIndex = remember { mutableStateOf(0) }
    NavigationBar {
        val (alreadyPaid, sumOfPayments)  = SubLogic.calculatePaymentsSum(ChronoUnit.MONTHS)
        NavigationBarItem(icon = {
            Icon(imageVector = Icons.Outlined.KeyboardArrowUp,"Payments summary")
        },
            label = { Text(text = "MONTHLY " + alreadyPaid + " / " + sumOfPayments, fontSize = 13.sp) },
            selected = selectedIndex.value == 0,
            onClick = { /* do sth */ })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavHostController) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Subdue",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Menu"
                )
            }
        },
        actions = {
            IconButton(onClick = {
                navController.navigate(Screen.Settings.route)
            }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings"
                )
            }
        }
    )
}
