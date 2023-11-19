package com.akinci.doggoappcompose.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.akinci.doggoappcompose.ui.theme.DoggoAppComposeTheme
import com.akinci.doggoappcompose.R

@Composable
fun PageNavigator(
    navigatorMessageId: Int = R.string.welcome_message,
    navigateButtonMessageId: Int = R.string.button_next_page,
    onClick : ()->Unit
) {
    Column(
        Modifier
            .padding(30.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = stringResource(id = navigatorMessageId))
        Spacer(Modifier.size(30.dp))
        Button(
            onClick = onClick
        ) {
            Text(text = stringResource(id = navigateButtonMessageId))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPageNavigator() {
    DoggoAppComposeTheme {
        PageNavigator(onClick = { })
    }
}