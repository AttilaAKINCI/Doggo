package com.akinci.doggoappcompose.ui.components.list.breed

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akinci.doggoappcompose.R
import com.akinci.doggoappcompose.common.helper.capitalize
import com.akinci.doggoappcompose.ui.feaute.dashboard.data.Breed
import com.akinci.doggoappcompose.ui.theme.DoggoAppComposeTheme
import java.util.*

@Composable
fun BreedItem(
    item: Breed,
    onItemClick: (String)->Unit
) {

    Column(
        modifier = Modifier
            .padding(5.dp, 5.dp, 5.dp, 5.dp)
            .clip(RoundedCornerShape(18.dp))
            .border(
                BorderStroke(1.dp, colorResource(R.color.card_border)),
                RoundedCornerShape(18.dp)
            )
            .background(
                color = if (item.selected) {
                    colorResource(R.color.teal_200)
                } else {
                    colorResource(R.color.breed_row_bg)
                }
            )
            .clickable(onClick = { onItemClick.invoke(item.name) }),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = item.name.capitalize(),
            modifier = Modifier.padding(10.dp),
            color = if(item.selected){
                colorResource(R.color.white)
            }else{
                colorResource(R.color.card_border)
             },
            style = MaterialTheme.typography.body1
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOfflineDialog() {
    DoggoAppComposeTheme {
        BreedItem(
            item = Breed("Hound", false),
            onItemClick = { }
        )
    }
}