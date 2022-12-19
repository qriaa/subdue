package com.studx.subdue

import android.annotation.SuppressLint
import android.content.res.Resources.Theme
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSubscription(navController: NavController) {
    Scaffold(
        topBar = {
            AddTopBar(navController)
        },
        bottomBar = {},
        content = { innerPadding ->
            LazyColumn(
                contentPadding = innerPadding
            ) {
                item {
                    AddPage()
                }
            }
        }
    )
}

//@Preview
//@Composable
//fun PreviewEditSubscriptionOneOff() {
//    EditSubscriptionOneOff()
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTopBar(navController: NavController) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Add subscription",
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
fun AddPage() {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, 0.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = "Subscription image",
            modifier = Modifier
                .size(140.dp)
                .background(Color.White)
                .clip(CircleShape)
                .padding(15.dp)
                .clickable(
                    enabled = true,
                    onClickLabel = "Clickable image",
                    onClick = {

                    }
                )
        )

        NameInput()

        Row() {
            PriceInput()
            CurrencyList()
        }

        PagerView()

        LabeledInput("Labels", "e.g. Music")
        LabeledInput("Payment method", "e.g. Card")
        LabeledInput("Notes", "")



    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable fun PagerView() {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color.White)
//    ) {
        TabRow(
            selectedTabIndex = pagerState.currentPage
        ) {
            Tab(
                selected = pagerState.currentPage == 0,
                onClick = {
                      coroutineScope.launch {
                          pagerState.animateScrollToPage(0)
                      }
                },
                text = {
                    Text(
                        text = "One-off",
                        color = if (pagerState.currentPage!=0) Color.Gray else MaterialTheme.colorScheme.primary
                    )
                }
            )
            Tab(
                selected = pagerState.currentPage == 1,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
                text = {
                    Text(
                        text = "Recurring",
                        color = if (pagerState.currentPage!=1) Color.Gray else MaterialTheme.colorScheme.primary
                    )
                }
            )
        }
        PagerContent(pagerState = pagerState)
//    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerContent(pagerState: PagerState) {
    HorizontalPager(
        pageCount = 2,
        modifier = Modifier.fillMaxWidth(),
        state = pagerState
    ) { pager ->
        when (pager) {

            0 -> {
                OneOffDetails()
            }

            1 -> {
                RecurringDetails()
            }
        }

    }
}


@Composable
fun OneOffDetails() {
    //#TODO calendar - Date
    LabeledInput("Date", "12/17/2022")
}

@Composable
fun RecurringDetails() {
    //#TODO calendar - first payment

    LabeledInput("First payment", "12/17/2022")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyList() {
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
fun PriceInput() {
    var text by remember { mutableStateOf("") }
    OutlinedTextField(value = text,
        onValueChange = { newText ->
            text = newText
        },
        label = {
            Text(text = "")
        },
        modifier = Modifier
            .width(190.dp)
            .padding(3.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameInput() {
    var text by remember { mutableStateOf("") }
    OutlinedTextField(value = text,
        onValueChange = { newText ->
            text = newText
        },
        trailingIcon = {
            IconButton(onClick = {
                text = ""
            }) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Clear icon"
                )
            }
        },
        label = {
            Text(text = "")
        },
        modifier = Modifier.padding(3.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabeledInput(label: String, placeholder: String) {
    var text by remember { mutableStateOf("") }
    TextField(value = text,
        onValueChange = { newText ->
            text = newText
        },
        label = {
            Text(text = label)
        },
        trailingIcon = {
            IconButton(onClick = {
                text = ""
            }) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Clear icon"
                )
            }
        },
        placeholder = {
            Text(text = placeholder)
        },
        modifier = Modifier.padding(15.dp)
    )
}

@Preview
@Composable
fun PreviewAddSubsctiption() {
    AddPage()
}