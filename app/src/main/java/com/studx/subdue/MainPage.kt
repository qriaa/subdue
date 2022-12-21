package com.studx.subdue


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

const val ROW_HEIGHT = 90


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(currContext: Context, subscriptions: List<Subscription>, navController: NavHostController) {
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
                items(subscriptions.sortedBy { subscription -> subscription.date }) { sub ->
                    Surface(onClick = {
//                        navController.navigate(Screen.SubscriptionDetails.route)
                        Toast.makeText(
                            currContext,
                            "Clicked on ${sub.name}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }) {
                        Subscription(rowHeight = ROW_HEIGHT, rowColor = MaterialTheme.colorScheme.primaryContainer, subscription = sub)
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

        NavigationBarItem(icon = {
            Icon(imageVector = Icons.Outlined.KeyboardArrowUp,"")
        },
            label = { Text(text = "MONTHLY", fontSize = 13.sp) },
            selected = (selectedIndex.value == 0),
            onClick = {
                selectedIndex.value = 0
            })
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
