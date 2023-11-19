package com.akinci.doggo.ui.features.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.akinci.doggo.R
import com.akinci.doggo.core.compose.UIModePreviews
import com.akinci.doggo.ui.ds.components.TiledBackground
import com.akinci.doggo.ui.ds.theme.DoggoTheme
import com.akinci.doggo.ui.features.dashboard.DashboardViewContract.State
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph
@Destination
@Composable
fun DashboardScreen(
    navigator: DestinationsNavigator,
    vm: DashboardViewModel = hiltViewModel(),
) {
    val uiState: State by vm.stateFlow.collectAsStateWithLifecycle()

    DashboardScreenContent(
        uiState = uiState,
        onBreedSelected = { },
        onSubBreedSelected = { },
        onDetailButtonClick = {
            //      navigator.navigate()
        },
    )

    /*scope.launch {
        if (vm.validate()) {
            onNavigateToDetail.invoke(
                vm.selectedBreedName,
                vm.selectedSubBreedName
            )
        } else {
            scaffoldState.snackbarHostState.showSnackbar(
                message = "Please choose a breed & sub-breed first",
                duration = SnackbarDuration.Short
            )
        }
    }*/

}

@Composable
private fun DashboardScreenContent(
    uiState: State,
    onBreedSelected: () -> Unit,
    onSubBreedSelected: () -> Unit,
    onDetailButtonClick: () -> Unit,
) {
    Surface {
        TiledBackground(
            painter = painterResource(id = R.drawable.ic_pattern_bg),
        ) {
            Column(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.systemBars)
                    .fillMaxSize()
            ) {
                /*Row(
                    modifier = Modifier
                        .padding(20.dp, 20.dp, 20.dp, 10.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            BorderStroke(1.dp, colorResource(R.color.card_border)),
                            RoundedCornerShape(10.dp)
                        )
                        .background(color = colorResource(R.color.teal_200_90)),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.doggo))
                    LottieAnimation(
                        composition,
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp),
                        iterations = animationCount
                    )

                    Text(
                        text = stringResource(R.string.dashboard_welcome_info_text),
                        modifier = Modifier.padding(0.dp, 0.dp, 5.dp, 0.dp),
                        style = MaterialTheme.typography.body1
                    )
                }*/

                // Breed List
                if (uiState.breedList.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.breed_list_title)
                    )

                }


                /* BreedSelector(
                     content = vm.breedListState,
                     headerTitle = stringResource(R.string.breed_list_title),
                     isVisible = vm.breedListState.isNotEmpty(),
                     onItemSelected = { breedName -> vm.selectBreed(breedName) }
                 )

                 */
                /** BREED Container **//*
                BreedSelector(
                    content = vm.subBreedListState,
                    headerTitle = stringResource(R.string.sub_breed_list_title),
                    isVisible = vm.subBreedListState.isNotEmpty(),
                    onItemSelected = { breedName -> vm.selectSubBreed(breedName) },
                    rowCount = 1
                )*/
            }

            FloatingActionButton(
                modifier = Modifier
                    .align(alignment = Alignment.BottomEnd)
                    .padding(0.dp, 0.dp, 30.dp, 50.dp)
                    .testTag("floatingButton"),
                onClick = onDetailButtonClick,
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = "",
                )
            }
        }
    }
}

@UIModePreviews
@Composable
fun DashboardScreenPreview() {
    DoggoTheme {
        DashboardScreenContent(
            uiState = State(),
            onBreedSelected = {},
            onSubBreedSelected = {},
            onDetailButtonClick = {},
        )
    }
}