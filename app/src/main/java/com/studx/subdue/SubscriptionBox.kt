package com.studx.subdue

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun Subscription(rowHeight: Int, rowColor: Color, subscription: Subscription){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(rowHeight.dp)
            .background(rowColor)
    ){
        Image(
            painter = painterResource(subscription.image),
            contentDescription = "Custom image",
            modifier = Modifier
                .size(90.dp)
                .background(Color.White)
        )
        Spacer(modifier = Modifier.fillMaxWidth(0.1f))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth(fraction = 0.37f)
                .fillMaxHeight()
        )
        {
            Text(
                text= subscription.name,
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
                text = subscription.cost.toString() + " " + subscription.currency,
                fontSize = 16.sp)
            Text(
                text= "/ ${subscription.timeInterval}",
                fontSize = 16.sp
            )
            Text(
                text= subscription.dueTo,
                fontSize = 15.sp,
                color= Color.Gray
            )
        }
    }
}

@Preview
@Composable
fun PreviewSubscription() {
    Subscription(
        rowHeight = 90,
        rowColor = MaterialTheme.colorScheme.primaryContainer,
        Subscription("AMAZON PRIME", R.drawable.amazon_prime, 10.00, "PLN", "MONTH", "14/02/2023")
    )
}