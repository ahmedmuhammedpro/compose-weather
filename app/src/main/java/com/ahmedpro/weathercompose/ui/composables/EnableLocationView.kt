package com.ahmedpro.weathercompose.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

@Composable
fun EnableLocationView(
    modifier: Modifier = Modifier,
    onEnableLocationClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Red)
            .padding(horizontal = 16.dp)
            .zIndex(10f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Enable Location",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Yellow
        )

        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow),
            onClick = onEnableLocationClick,
        ) {
            Text("enable", color = Color.Black)
        }
    }
}