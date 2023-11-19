package com.akinci.doggoappcompose.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.akinci.doggoappcompose.common.network.NetworkState
import com.akinci.doggoappcompose.common.network.NetworkStateViewModel

@Composable
fun NetworkCheckScreen(
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    buttonAction: () -> Unit,
    networkStateViewModel: NetworkStateViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    val networkState by networkStateViewModel.networkState.collectAsState(initial = NetworkState.None)

    Box(modifier = modifier){
        OfflineDialog(
            isVisible = isVisible,
            networkState = networkState
        ) { buttonAction() }
        content()
    }
}