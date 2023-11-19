package com.akinci.doggoappcompose.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.akinci.doggoappcompose.R
import com.akinci.doggoappcompose.common.helper.state.UIState

@Composable
fun Informer(
    uiState: UIState,
    showSnackBar: (String)->Unit
) {
    when(uiState){
        /** Loading info comes so recently, I decided to not to show it. :) **/
//        is UIState.OnLoading -> {
//            showSnackBar(stringResource(R.string.list_loading_msg))
//        }
        is UIState.OnServiceError -> {
            showSnackBar(stringResource(R.string.generic_error_msg))
        }
        else -> { /** No Operation **/ }
    }
}