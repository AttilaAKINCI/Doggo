package com.akinci.doggoappcompose.ui.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.akinci.doggoappcompose.R
import com.akinci.doggoappcompose.common.network.NetworkState
import com.akinci.doggoappcompose.ui.theme.DoggoAppComposeTheme

@Composable
fun OfflineDialog(
    isVisible: Boolean = true,
    networkState: NetworkState = NetworkState.None,
    onRetry: () -> Unit
) {
    if(isVisible){
        if(networkState is NetworkState.NotConnected){
            AlertDialog(
                onDismissRequest = {},
                title = { Text(text = stringResource(R.string.connection_error_title)) },
                text = { Text(text = stringResource(R.string.connection_error_message)) },
                confirmButton = {
                    TextButton(onClick = onRetry) {
                        Text(stringResource(R.string.ok_label))
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOfflineDialog() {
    DoggoAppComposeTheme {
        OfflineDialog(
            networkState = NetworkState.None,
            onRetry = { }
        )
    }
}