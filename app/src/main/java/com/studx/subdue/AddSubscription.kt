package com.studx.subdue

import android.annotation.SuppressLint
import androidx.compose.foundation.*
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


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

@SuppressLint("SuspiciousIndentation")
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
                Spacer(modifier = Modifier.padding(16.dp, 16.dp))

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
    DateDialog("Due to")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurringDetails() {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, 0.dp)
    ) {

        Row() {
            Text(
                text = "Every",
                fontSize = 20.sp,
                modifier = Modifier
                    .width(110.dp)
                    .padding(30.dp)
            )

            // billing cycle - change into numbers only
            var billing_period_number by remember { mutableStateOf("1") }
            OutlinedTextField(
                value = billing_period_number,
                onValueChange = { newText ->
                    billing_period_number = newText
                },
                label = {
                    Text(text = "")
                },
                modifier = Modifier
                    .width(75.dp)
                    .padding(10.dp)
            )

            // change into list (month/year)
            var billing_period by remember { mutableStateOf("MONTH") }
            OutlinedTextField(
                value = billing_period,
                onValueChange = { newText ->
                    billing_period = newText
                },
                label = {
                    Text(text = "")
                },
                modifier = Modifier
                    .width(120.dp)
                    .padding(10.dp)
            )
        }

        DateDialog("First payment date")
    }
}

@Composable
fun DateDialog(specifed_value : String) {
    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }
    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("MM/dd/yyyy")
                .format(pickedDate)
        }
    }

    val dateDialogState = rememberMaterialDialogState()

    Button(
        onClick = {
            dateDialogState.show()
        }
    ) {
        Text(text = specifed_value + ": " + formattedDate)
    }

    MaterialDialog(
        dialogState = dateDialogState,
        properties = DialogProperties(
            dismissOnClickOutside = true
        ),
        buttons = {
            positiveButton(text = "OK")
            negativeButton(text = "CANCEL")
        }
    ) {
        datepicker(
            initialDate = LocalDate.now(),
            locale = Locale("POLISH", "POLAND"),
            title = "Pick a date of first payment"
        ) {
            pickedDate = it
        }
    }

//    TextField(value = pickedDate,
//        onValueChange = { newText ->
//            pickedDate = formattedDate
//        },
//        label = {
//            Text(text = "First payment")
//        },
//        trailingIcon = {
//            IconButton(onClick = {
//                date = ""
//            }) {
//                Icon(
//                    imageVector = Icons.Filled.DateRange,
//                    contentDescription = "Clear icon"
//                )
//            }
//        },
//        modifier = Modifier.padding(15.dp)
//    )
//    val date_picker = MaterialDatePicker.Builder.datePicker().build()
//    date_picker.show(LocalContext.current, date_picker.toString())
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
    var text by remember { mutableStateOf("Price") }
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
    var text by remember { mutableStateOf("Name") }
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
        modifier = Modifier.padding(3.dp)
    )

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