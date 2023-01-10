package com.studx.subdue

import android.icu.math.BigDecimal
import android.icu.util.Currency
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studx.subdue.logic.Subscription
import java.time.LocalDate
import java.time.temporal.ChronoUnit


@Composable
fun SubscriptionBox(rowHeight: Int, rowColor: Color, subscription: Subscription){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(rowHeight.dp)
            .background(rowColor)
    ){
        // jezeli emoji to wyswietl zamiast ikonki emoji, czyli zapisany w image tekst
        if (subscription.isEmojiImg) {
            Text(
                text = subscription.image,
                modifier = Modifier
                    .size(90.dp)
                    .background(Color.White)
                    .clip(CircleShape),
            )
        }
        else {
            Image(
                painter = painterResource(R.drawable.recurring_subscription_icon),  //#TODO przypisac odpowiedni zasob z res/drawable do wyswietlenia
                contentDescription = "Subscription icon",
                modifier = Modifier
                    .size(90.dp)
                    .background(Color.White)
                    .clip(CircleShape),
                )
        }

        Spacer(modifier = Modifier.fillMaxWidth(0.1f))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth(fraction = 0.37f)
                .fillMaxHeight()
        )
        {
            Text(
                text= subscription.name.uppercase(),
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.fillMaxWidth(0.20f))

        Column(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
        ){
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = subscription.cost.toFloat().toString() + " " + subscription.currency, //#TODO bigdecimal do zaokraglenia
                fontSize = 16.sp)
            Text(
                text= "/ ${subscription.timeInterval}",
                fontSize = 16.sp
            )
            Text(
                text= "Due " + subscription.dateAnchor.toString(),
                fontSize = 15.sp,
                color= Color.Gray
            )
        }
    }
}

@Preview
@Composable
fun PreviewSubscription() {
    val sub = Subscription(
        name = "Netflix",
        cost = BigDecimal(15.99),
        currency = Currency.getInstance("PLN"),
        timeMultiplier = 1,
        paymentMethod = "Card",
        timeInterval = ChronoUnit.MONTHS,
        dateAnchor = LocalDate.now().plus(1, ChronoUnit.MONTHS),
        image = "R.drawable.netflix_logo",
        isEmojiImg = false,
        isOneOff = false
    )
    MaterialTheme {
        SubscriptionBox(90, Color.White, sub)
    }
}