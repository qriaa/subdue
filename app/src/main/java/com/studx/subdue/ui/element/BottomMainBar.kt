package com.studx.subdue.ui.element

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.studx.subdue.ui.theme.SubdueTheme

@Composable
fun BottomMainBar() {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            IconButton(
                onClick = { /*TODO*/ }
            ) {
                Icon(Icons.Filled.KeyboardArrowUp,
                    "ArrowUp"
                    )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomMainBarPreview() {
    SubdueTheme {
        BottomMainBar()
    }
}