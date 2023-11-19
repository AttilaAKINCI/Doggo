package com.akinci.doggoappcompose.ui.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.akinci.doggoappcompose.R

@Composable
fun DoggoAppBar(
    title: String,
    isBackEnabled: Boolean = false,
    onBackPress: (() -> Unit)? = null
){
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = if(isBackEnabled){
            {
                IconButton(onClick = { onBackPress?.invoke() }) {
                    Icon(Icons.Filled.ArrowBack, stringResource(R.string.cd_back))
                }
            }
        }else{
            null
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.White,
        elevation = 10.dp
    )
}