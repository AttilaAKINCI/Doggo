package com.akinci.doggo.ui.ds.components

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers

/**
 *  CachedImage is a variation of [AsyncImage] which automatically cache it's content.
 *
 *  @property [modifier] compose modifier
 *  @property [imageUrl] image url to load
 *  @property [placeHolderId] placeholder resource Id in case of any error, fallback or etc.
 *
 * **/
@Composable
fun CachedImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    @DrawableRes placeHolderId: Int,
) {
    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(imageUrl)
        .dispatcher(Dispatchers.IO)
        .memoryCacheKey(imageUrl)
        .diskCacheKey(imageUrl)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .placeholder(placeHolderId)
        .error(placeHolderId)
        .fallback(placeHolderId)
        .build()

    AsyncImage(
        modifier = modifier,
        model = imageRequest,
        contentScale = ContentScale.Crop,
        contentDescription = null,
    )
}
