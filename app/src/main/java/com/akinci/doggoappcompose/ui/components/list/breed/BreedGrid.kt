package com.akinci.doggoappcompose.ui.components.list.breed

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.akinci.doggoappcompose.R
import com.akinci.doggoappcompose.ui.components.list.StaggeredGrid
import com.akinci.doggoappcompose.ui.feaute.dashboard.data.Breed
import kotlinx.coroutines.launch


@ExperimentalAnimationApi
@Composable
fun BreedSelector(
    content: List<Breed>,
    rowCount:Int = 4,
    headerTitle: String,
    isVisible: Boolean,
    onItemSelected: (String)->Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(
            initialAlpha = 0f
        ),
        exit = fadeOut(
            targetAlpha = 0f
        )
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
        ){
            /** Breed Header **/
            Column(
                modifier = Modifier
                    .padding(20.dp, 10.dp, 20.dp, 10.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(color = colorResource(R.color.teal_200_90)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = headerTitle,
                    modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 10.dp),
                    style = MaterialTheme.typography.body1
                )
            }

            val breedList = remember { mutableStateListOf<Breed>() }
            breedList.clear()
            breedList.addAll(content)

            /** Breed List **/
            StaggeredGrid(
                modifier = Modifier
                    .wrapContentHeight()
                    .horizontalScroll(rememberScrollState()),
                rows = rowCount
            ) {
                breedList.forEach { breed ->
                    val scope = rememberCoroutineScope()
                    BreedItem(
                        item = breed,
                        onItemClick = { selectedItemName ->
                            scope.launch {
                                onItemSelected.invoke(selectedItemName) // inform DashboardScreen for selected Item
                                breedList.clear()
                                breedList.addAll(
                                    content.map { item ->
                                        item.selected = (item.name == selectedItemName)
                                        item
                                    }
                                )
                            }
                        }
                    )
                }
            }

        }
    }
}

