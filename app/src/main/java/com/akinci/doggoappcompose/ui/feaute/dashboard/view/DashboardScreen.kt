package com.akinci.doggoappcompose.ui.feaute.dashboard.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.akinci.doggoappcompose.R
import com.akinci.doggoappcompose.ui.components.list.breed.BreedSelector
import com.akinci.doggoappcompose.ui.components.DoggoAppBar
import com.akinci.doggoappcompose.ui.components.Informer
import com.akinci.doggoappcompose.ui.components.NetworkCheckScreen
import com.akinci.doggoappcompose.ui.components.TiledBackground
import com.akinci.doggoappcompose.ui.feaute.dashboard.viewmodel.DashboardViewModel
import com.akinci.doggoappcompose.ui.theme.DoggoAppComposeTheme
import kotlinx.coroutines.launch

/**
 * Stateful version of the Podcast player
 */
@ExperimentalAnimationApi
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onNavigateToDetail: (String, String) -> Unit,
    animationCount:Int = Int.MAX_VALUE    // Added for compose ui tests.. Lottie is blocking UI for infinite animation.
){
    DashboardScreenBody(
        vm = viewModel,
        onNavigateToDetail = onNavigateToDetail,
        animationCount = animationCount
    )
}

/**
 * Stateless version of the Player screen
 */
@ExperimentalAnimationApi
@Composable
private fun DashboardScreenBody(
    vm: DashboardViewModel,
    onNavigateToDetail: (String, String) -> Unit,
    animationCount: Int
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            DoggoAppBar(
                title = stringResource(R.string.title_dashboard)
            )
        }
    ) {

        /** Dashboard Screen is marked as "Network Dependent Screen" (NDS) **/
        NetworkCheckScreen(
            isVisible = vm.isNetworkWarningDialogVisible,
            buttonAction = {
                scope.launch {
                    vm.networkWarningSeen()
                }
            }
        ) {
            TiledBackground(
                tiledDrawableId = R.drawable.ic_pattern_bg
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ){
                    Row(
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
                            modifier = Modifier.padding(0.dp, 0.dp, 5.dp,0.dp),
                            style = MaterialTheme.typography.body1
                        )
                    }

                    /** BREED Container **/
                    BreedSelector(
                        content = vm.breedListState,
                        headerTitle = stringResource(R.string.breed_list_title),
                        isVisible = vm.breedListState.isNotEmpty(),
                        onItemSelected = { breedName -> vm.selectBreed(breedName) }
                    )

                    /** BREED Container **/
                    BreedSelector(
                        content = vm.subBreedListState,
                        headerTitle = stringResource(R.string.sub_breed_list_title),
                        isVisible = vm.subBreedListState.isNotEmpty(),
                        onItemSelected = { breedName -> vm.selectSubBreed(breedName) },
                        rowCount = 1
                    )
                }

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    FloatingActionButton(
                        onClick = {
                            scope.launch {
                                if(vm.validate()){
                                    onNavigateToDetail.invoke(vm.selectedBreedName, vm.selectedSubBreedName)
                                }else{
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        message = "Please choose a breed & sub-breed first",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .align(alignment = Alignment.BottomEnd)
                            .padding(0.dp, 0.dp, 30.dp, 50.dp)
                            .testTag("floatingButton")
                    ) {
                        Icon(
                            Icons.Filled.KeyboardArrowRight,
                            "",
                            modifier = Modifier.scale(1.2f),
                            tint = colorResource(R.color.white)
                        )
                    }

                    Informer(
                        uiState = vm.informer,
                        showSnackBar = {
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = it,
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DoggoAppComposeTheme {
        DashboardScreen(
            onNavigateToDetail = { _, _ -> }
        )
    }
}