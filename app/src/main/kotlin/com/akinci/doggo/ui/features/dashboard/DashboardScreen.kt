package com.akinci.doggo.ui.features.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.akinci.doggo.R
import com.akinci.doggo.core.compose.UIModePreviews
import com.akinci.doggo.ui.ds.components.InfiniteLottieAnimation
import com.akinci.doggo.ui.ds.components.Shimmer
import com.akinci.doggo.ui.ds.components.StaggeredGrid
import com.akinci.doggo.ui.ds.components.TiledBackground
import com.akinci.doggo.ui.ds.theme.DoggoTheme
import com.akinci.doggo.ui.ds.theme.bodyLargeBold
import com.akinci.doggo.ui.ds.theme.oval
import com.akinci.doggo.ui.features.dashboard.DashboardViewContract.State
import com.akinci.doggo.ui.features.destinations.DetailScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.collections.immutable.PersistentList
import kotlin.random.Random

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
        onBreedSelected = {
            // validate next button visibility for each selection.
        },
        onSubBreedSelected = {
            // validate next button visibility for each selection.
        },
        onDetailButtonClick = {
            if (uiState.selectedBreed != null) {
                navigator.navigate(
                    DetailScreenDestination(
                        breed = uiState.selectedBreed!!,
                        subBreed = uiState.selectedSubBreed,
                    )
                )
            }
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
        TiledBackground(painter = painterResource(id = R.drawable.ic_pattern_bg)) {
            Column(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.systemBars)
                    .fillMaxSize()
            ) {
                DashboardScreen.TopBar()

                DashboardScreen.Title(title = stringResource(id = R.string.breed_list_title))

                if (uiState.breedList.isNotEmpty()) {
                    DashboardScreen.ListSection(items = uiState.breedList)
                }

                if (uiState.selectedBreed != null) {
                    DashboardScreen.Title(title = stringResource(id = R.string.sub_breed_list_title))

                    if (uiState.subBreedList.isNotEmpty()) {
                        DashboardScreen.ListSection(items = uiState.breedList)
                    }
                }

                if (uiState.isShimmerLoading) {
                    DashboardScreen.ShimmerLoading()
                }
            }

            if (uiState.isDetailButtonActive) {
                FloatingActionButton(
                    modifier = Modifier
                        .align(alignment = Alignment.BottomEnd)
                        .padding(end = 30.dp, bottom = 50.dp)
                        .testTag("floatingButton"),
                    shape = MaterialTheme.shapes.oval,
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = onDetailButtonClick,
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

typealias DashboardScreen = Unit

@Composable
private fun DashboardScreen.TopBar() {
    Card(
        modifier = Modifier.padding(16.dp),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            InfiniteLottieAnimation(
                modifier = Modifier.size(100.dp),
                animationId = R.raw.doggo
            )

            Text(
                modifier = Modifier.padding(16.dp),
                text = stringResource(R.string.dashboard_welcome_info_text),
                style = MaterialTheme.typography.bodyLargeBold
            )
        }
    }
}

@Composable
private fun DashboardScreen.Title(
    title: String,
) {
    Card(
        modifier = Modifier.padding(16.dp),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            text = title,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun DashboardScreen.ShimmerLoading() {
    StaggeredGrid(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        rows = 4
    ) {
        (1..40).toList().forEach { _ ->
            Shimmer(
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .padding(start = 8.dp, end = 8.dp)
                    .height(48.dp)
                    .width(Random.nextInt(100, 150).dp)
                    .clip(MaterialTheme.shapes.oval)
            )
        }
    }
}

@Composable
private fun DashboardScreen.ListSection(
    items: PersistentList<String>,
) {
    StaggeredGrid(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        rows = 4
    ) {
        items.forEach {
            Box(
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .padding(start = 8.dp, end = 8.dp)
                    .height(48.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = MaterialTheme.shapes.oval
                    )
                    .clip(MaterialTheme.shapes.oval)
                    .background(color = MaterialTheme.colorScheme.secondary),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = it
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