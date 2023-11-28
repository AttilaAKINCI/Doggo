package com.akinci.doggo.ui.features.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.akinci.doggo.R
import com.akinci.doggo.core.compose.UIModePreviews
import com.akinci.doggo.ui.ds.components.InfiniteLottieAnimation
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
        onBreedSelected = { vm.selectBreed(it) },
        onSubBreedSelected = { vm.selectSubBreed(it) },
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
    onBreedSelected: (String) -> Unit,
    onSubBreedSelected: (String) -> Unit,
    onDetailButtonClick: () -> Unit,
) {
    Surface {
        TiledBackground(painter = painterResource(id = R.drawable.ic_pattern_bg)) {
            Column(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.systemBars)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                DashboardScreen.TopBar()

                AnimatedVisibility(
                    visible = !uiState.isConnected,
                    enter = fadeIn(animationSpec = tween(250)),
                    exit = fadeOut(animationSpec = tween(250)),
                ) {
                    DashboardScreen.ConnectionIssue()
                }

                DashboardScreen.Title(title = stringResource(id = R.string.dashboard_screen_breed_title))

                when {
                    uiState.isBreedLoading -> DashboardScreen.BreedLoading()
                    uiState.isBreedNoData -> DashboardScreen.BreedNoData()
                    uiState.isBreedError -> DashboardScreen.BreedError()
                }

                AnimatedVisibility(
                    visible = uiState.breedList.isNotEmpty(),
                    enter = fadeIn(animationSpec = tween(250)),
                ) {
                    DashboardScreen.StaggeredGrid(
                        items = uiState.breedList,
                        onSelect = { onBreedSelected(it) },
                        rowCount = 4,
                    )
                }

                if (uiState.isSubBreedError) {
                    DashboardScreen.SubBreedError()
                }

                AnimatedVisibility(
                    visible = uiState.subBreedList.isNotEmpty(),
                    enter = fadeIn(animationSpec = tween(250)),
                    exit = fadeOut(animationSpec = tween(250)),
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        DashboardScreen.Title(title = stringResource(id = R.string.dashboard_screen_sub_breed_title))

                        DashboardScreen.StaggeredGrid(
                            items = uiState.subBreedList,
                            onSelect = { onSubBreedSelected(it) },
                            rowCount = 1,
                        )
                    }
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
                text = stringResource(R.string.dashboard_screen_welcome_info),
                style = MaterialTheme.typography.bodyLargeBold
            )
        }
    }
}

@Composable
private fun DashboardScreen.ConnectionIssue() {
    Card(
        modifier = Modifier.padding(16.dp),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.padding(16.dp),
                imageVector = Icons.Default.Warning,
                contentDescription = null,
            )

            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp)
                    .padding(end = 16.dp),
                text = stringResource(id = R.string.dashboard_screen_connection_problem),
                style = MaterialTheme.typography.bodyLarge
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
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )
    }
}

@Composable
private fun DashboardScreen.BreedLoading() {
    Box(
        modifier = Modifier
            .size(96.dp)
            .clip(MaterialTheme.shapes.oval)
            .background(color = MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            strokeWidth = 5.dp,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Composable
private fun DashboardScreen.BreedError() = DashboardScreen.Info(
    title = stringResource(id = R.string.general_error_title),
    message = stringResource(id = R.string.dashboard_screen_error_breed_message),
)

@Composable
private fun DashboardScreen.SubBreedError() = DashboardScreen.Info(
    title = stringResource(id = R.string.general_error_title),
    message = stringResource(id = R.string.dashboard_screen_error_sub_breed_message),
)

@Composable
private fun DashboardScreen.BreedNoData() = DashboardScreen.Info(
    title = stringResource(id = R.string.general_info_title),
    message = stringResource(id = R.string.dashboard_screen_no_data_breed_message),
)

@Composable
private fun DashboardScreen.Info(
    title: String,
    message: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(shape = MaterialTheme.shapes.extraLarge)
            .background(color = MaterialTheme.colorScheme.secondary)
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .padding(top = 16.dp),
            text = title,
            style = MaterialTheme.typography.titleLarge,
        )

        Text(
            modifier = Modifier
                .padding(16.dp)
                .padding(bottom = 16.dp),
            text = message,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun DashboardScreen.StaggeredGrid(
    items: PersistentList<BreedListItem>,
    onSelect: (String) -> Unit,
    rowCount: Int,
) {
    val heightDp = ((rowCount * 56) + 24).dp

    LazyHorizontalStaggeredGrid(
        modifier = Modifier.height(heightDp),
        rows = StaggeredGridCells.Adaptive(minSize = 48.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalItemSpacing = 8.dp,
    ) {
        items(items) {
            val containerColor = if (it.selected) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.secondary
            }

            TextButton(
                modifier = Modifier.height(48.dp),
                shape = MaterialTheme.shapes.oval,
                colors = ButtonDefaults.buttonColors(
                    containerColor = containerColor,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                ),
                onClick = { onSelect(it.name) }
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = it.name
                )
            }
        }
    }
}


@UIModePreviews
@Composable
private fun DashboardScreenPreview() {
    DoggoTheme {
        DashboardScreenContent(
            uiState = State(),
            onBreedSelected = {},
            onSubBreedSelected = {},
            onDetailButtonClick = {},
        )
    }
}