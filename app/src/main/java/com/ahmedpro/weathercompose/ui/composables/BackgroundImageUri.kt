package com.ahmedpro.weathercompose.ui.composables

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun BackgroundImageUri(uri: Uri? = null, date: String? = null) {
    if (uri == null) {
        DefaultBackground()
    } else {
        LoadImageFromUri(uri = uri, date)
    }
}

@Composable
fun LoadImageFromUri(uri: Uri, date: String?) {
    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = uri,
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )

        if (date.isNullOrBlank().not())
            Text(
                modifier = Modifier.align(Alignment.BottomEnd),
                text = date!!,
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
    }
}