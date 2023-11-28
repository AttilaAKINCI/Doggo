package com.akinci.doggo.ui.features.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.akinci.doggo.R
import com.akinci.doggo.core.compose.UIModePreviews
import com.akinci.doggo.domain.ImageListItem
import com.akinci.doggo.ui.ds.components.Shimmer
import com.akinci.doggo.ui.ds.components.TiledBackground
import com.akinci.doggo.ui.ds.theme.DoggoTheme
import com.akinci.doggo.ui.features.detail.DetailViewContract.ScreenArgs
import com.akinci.doggo.ui.features.detail.DetailViewContract.State
import com.akinci.doggo.ui.navigation.animation.SlideInOutHorizontally
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.Dispatchers

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
        onBackPress = { navigator.popBackStack() }
    )
}

@Composable
private fun DetailScreenContent(
    uiState: State,
    onBackPress: () -> Unit,
) {
    Surface {
        TiledBackground(
            painter = painterResource(id = R.drawable.ic_pattern_bg),
        ) {
            Column(
                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
            ) {
                DetailScreen.TopBar(
                    uiState.title,
                    onBackPress = onBackPress,
                )

                when {
                    uiState.isLoading -> DetailScreen.Loading()
                    else -> DetailScreen.Images(
                        images = uiState.images,
                    )
                }
            }
        }
    }
}

typealias DetailScreen = Unit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen.TopBar(
    name: String,
    onBackPress: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = name)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
        ),
        navigationIcon = {
            IconButton(onClick = onBackPress) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                )
            }
        }
    )
}

@Composable
fun DetailScreen.Loading() {
    LazyColumn(
        state = rememberLazyListState(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items((1..9).toList()) {
            Shimmer(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.4f)
                    .clip(MaterialTheme.shapes.extraLarge)
            )
        }
    }
}

@Composable
fun DetailScreen.Images(
    images: PersistentList<ImageListItem>,
) {
    val context = LocalContext.current

    LazyColumn(
        state = rememberLazyListState(),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 32.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(images) {
            Box(
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.extraLarge)
            ) {

                val imageRequest = ImageRequest.Builder(context)
                    .data(it.imageUrl)
                    .dispatcher(Dispatchers.IO)
                    .memoryCacheKey(it.imageUrl)
                    .diskCacheKey(it.imageUrl)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .fallback(R.drawable.ic_placeholder)
                    .build()

                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.4f),
                    model = imageRequest,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.75f)
                        )
                        .align(Alignment.BottomCenter),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = it.dogName,
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
            }
        }
    }
}

@UIModePreviews
@Composable
fun DefaultPreview() {
    DoggoTheme {
        DetailScreenContent(
            uiState = State(title = "Hound/Pax"),
            onBackPress = {}
        )
    }
}