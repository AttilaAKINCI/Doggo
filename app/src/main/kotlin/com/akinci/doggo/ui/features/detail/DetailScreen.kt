package com.akinci.doggo.ui.features.detail

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.akinci.doggo.R
import com.akinci.doggo.core.compose.UIModePreviews
import com.akinci.doggo.ui.ds.components.TiledBackground
import com.akinci.doggo.ui.ds.theme.DoggoTheme
import com.akinci.doggo.ui.features.detail.DetailViewContract.ScreenArgs
import com.akinci.doggo.ui.features.detail.DetailViewContract.State
import com.akinci.doggo.ui.navigation.animation.SlideInOutHorizontally
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph
@Destination(
    style = SlideInOutHorizontally::class,
    navArgsDelegate = ScreenArgs::class
)
@Composable
fun DetailScreen(
    navigator: DestinationsNavigator,
    vm: DetailViewModel = hiltViewModel(),
) {
    val uiState: State by vm.stateFlow.collectAsStateWithLifecycle()

    DetailScreenContent(
        uiState = uiState,
    )
}

/**
 * Stateless version of the Player screen
 */
@Composable
private fun DetailScreenContent(
    uiState: State,
) {
    Surface {
        TiledBackground(
            painter = painterResource(id = R.drawable.ic_pattern_bg),
        ) {

        }


        /* TiledBackground(
             modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars),
             tiledDrawableId = R.drawable.ic_pattern_bg
         ) {
             Column(
                 modifier = Modifier
                     .fillMaxSize()
             ) {

                 */
        /** Header of doggo image list **//*
                */
        /** Header of doggo image list **//*
                ListHeader(
                    headerTitle = if (args.subBreedName.isNotBlank()) {
                        stringResource(
                            R.string.detail_title_long,
                            args.breedName,
                            args.subBreedName
                        ).allCaps()
                    } else {
                        stringResource(R.string.detail_title_short, args.breedName).allCaps()
                    }
                )

                val contentList = remember { mutableStateListOf<Content>() }
                contentList.addAll(vm.breedImageListState)

                */
        /** Doggo Images **//*

                */
        /** Doggo Images **//*
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(contentList) { contentItem ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .padding(10.dp)
                                .clip(RoundedCornerShape(18.dp))
                        ) {
                            Image(
                                painter = rememberImagePainter(
                                    data = contentItem.imageUrl,
                                    builder = {
                                        crossfade(true)
                                    }
                                ),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .background(colorResource(R.color.white_40))
                            ) {
                                Text(
                                    text = contentItem.dogName,
                                    color = colorResource(R.color.card_border),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(0.dp, 5.dp, 0.dp, 10.dp),
                                    style = MaterialTheme.typography.h2,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
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

        }*/
    }
}

@UIModePreviews
@Composable
fun DefaultPreview() {
    DoggoTheme {
        DetailScreenContent(
            uiState = State()
        )
    }
}