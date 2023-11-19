package com.akinci.doggoappcompose.ui.components.list.header

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akinci.doggoappcompose.R

@Composable
fun ListHeader(
    headerTitle: String,
    bgColor: Color = colorResource(R.color.teal_200_90)
) {
    Column(
    modifier = Modifier
    .padding(20.dp, 10.dp, 20.dp, 10.dp)
    .fillMaxWidth()
    .clip(RoundedCornerShape(10.dp))
    .background(color = bgColor),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = headerTitle,
            modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 10.dp),
            style = MaterialTheme.typography.body1
        )
    }
}