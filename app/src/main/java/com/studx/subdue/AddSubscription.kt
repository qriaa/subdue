package com.studx.subdue

import android.annotation.SuppressLint
import android.content.Context
import android.icu.math.BigDecimal
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.studx.subdue.logic.SettingsManager
import com.studx.subdue.logic.SubLogic
import com.studx.subdue.logic.Subscription
import com.studx.subdue.ui.theme.SubdueTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*


var newSubscription: Subscription = Subscription(image = "", isEmojiImg = false, name = "NEW SUB")

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSubscription(navController: NavController, context: Context) {
    SubdueTheme(
        darkTheme = SettingsManager.settings.isDarkmode
    ){
        newSubscription = Subscription(image = "", isEmojiImg = false, name = "NEW SUB")
        Scaffold(
            topBar = {
                AddTopBar(navController, context)
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
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTopBar(navController: NavController, context: Context) {
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
            IconButton(onClick = {
                newSubscription.isEmojiImg = false
                newSubscription.image = ""
                SubLogic.addSub(newSubscription)
                SubLogic.saveSubs(context)
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route) {
                        inclusive = true
                    }
                }
            }) {
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
            painter = painterResource(id = R.drawable.subscribe),
            contentDescription = "Subscription image",
            modifier = Modifier
                .size(140.dp)
                .background(Color.White)
                .clip(CircleShape)
                .padding(15.dp)
        )

        NameInput()

        Row {
            PriceInput()
            CurrencyList()
        }

        PagerView()

        PaymentMethodInput("Payment method", "e.g. Card")
        NotesInput("Notes", "")
    }
}

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalFoundationApi::class)
@Composable fun PagerView() {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
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
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerContent(pagerState: PagerState) {
    HorizontalPager(
        pageCount = 2,
        modifier = Modifier.fillMaxWidth(),
        state = pagerState,
        contentPadding = PaddingValues(10.dp)
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

        Row {
            Text(
                text = "Every",
                fontSize = 20.sp,
                modifier = Modifier
                    .width(120.dp)
                    .padding(30.dp)
            )

            //#TODO wywala sie na zmiennoprzecinkowych
            var billing_period_number by remember { mutableStateOf("1") }
            OutlinedTextField(
                value = billing_period_number,
                onValueChange = { newText ->
                    billing_period_number = newText
                },
                label = {
                    Text(text = "")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .width(75.dp)
                    .padding(10.dp)
            )
            if (billing_period_number == "") {
                billing_period_number = "1"
            }
            newSubscription.timeMultiplier = billing_period_number.toLong()

            var expanded by remember {
                mutableStateOf(false)
            }
            var selectedItem by remember {
                mutableStateOf("MONTH")
            }
            val listOfChronoUnits= listOf("MONTH", "YEAR", "DAY", "WEEK")

            Box {
                TextButton(onClick = { expanded = true},
                    Modifier.padding(0.dp, 15.dp)) {
                    Row {
                        Text(text = selectedItem, fontSize = 17.sp)
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown icon")
                    }
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, Modifier.height(150.dp)) {
                    listOfChronoUnits.forEach {
                        DropdownMenuItem(text = { Text(text = it)}, onClick = { selectedItem = it; expanded = false })
                    }
                }
            }

            newSubscription.timeInterval = when (selectedItem) {
                "MONTH" -> ChronoUnit.MONTHS
                "YEAR" -> ChronoUnit.YEARS
                "DAY" -> ChronoUnit.DAYS
                "WEEK" -> ChronoUnit.WEEKS
                else -> ChronoUnit.MONTHS
            }

            newSubscription.isOneOff = false
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

    newSubscription.dateAnchor = pickedDate
}

@Composable
fun CurrencyList() {
    var expanded by remember {
        mutableStateOf(false)
    }
    var selectedItem by remember {
        mutableStateOf(Currency.getInstance(DEFAULT_CURRENCY))
    }
    val listOfCurrencies = Currency.getAvailableCurrencies().toList()

    Box {
        TextButton(onClick = { expanded = true},
        Modifier.padding(0.dp, 15.dp)) {
            Row {
                Text(text = selectedItem.currencyCode)
                Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown icon")
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, Modifier.height(330.dp)) {
            listOfCurrencies.forEach {
                DropdownMenuItem(text = { Text(text = it.toString())}, onClick = { selectedItem = it; expanded = false })
            }
        }
    }

    newSubscription.currency = android.icu.util.Currency.getInstance(selectedItem.currencyCode)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceInput() {
    var text by remember { mutableStateOf("19.99") }
    OutlinedTextField(
        value = text,
        onValueChange = { newText ->
            text = newText
        },
        label = {
            Text(text = "Price")
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier
            .width(190.dp)
            .padding(3.dp)
    )

    if (text != "") {
        newSubscription.cost = BigDecimal(text).setScale(2, BigDecimal.ROUND_DOWN)
    } else {
        newSubscription.cost = BigDecimal(0.00).setScale(2, BigDecimal.ROUND_DOWN)
    }
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
    newSubscription.name = text.uppercase()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodInput(label: String, placeholder: String) {
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

    newSubscription.paymentMethod = text
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesInput(label: String, placeholder: String) {
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

    newSubscription.notes = text
}
