package com.studx.subdue.ui.element

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.studx.subdue.R
import com.studx.subdue.ui.theme.SubdueTheme

@Composable
fun SubEntry(){ // TODO: add subscription data as argument
    Box(
        modifier = Modifier.fillMaxWidth()
            .height(110.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.spotify_logo_without_text),
                contentDescription = "icon",
                modifier = Modifier.size(58.dp)
            )
        }
    }


}

@Preview (showBackground = true)
@Composable
fun Preview(){
    SubdueTheme {
        SubEntry()
    }
}