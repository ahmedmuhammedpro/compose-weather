package com.ahmedpro.weathercompose.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun SettingsDialog(
    visible: Boolean = false,
    tempTypeName: String,
    onTakePicClick: () -> Unit,
    onRestBackground: () -> Unit,
    onChangeTempTypeClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (visible.not()) return

    Dialog(
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
        onDismissRequest = { onDismiss.invoke() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(24.dp))
                .padding(16.dp),
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clickable { onDismiss.invoke() },
                imageVector = Icons.Rounded.Close,
                tint = Color.Black,
                contentDescription = null
            )

            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF81D4FA)),
                    onClick = {
                        onDismiss()
                        onTakePicClick.invoke()
                    },
                ) {
                    Text("Take Picture", color = Color.Black)
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF81D4FA)),
                    onClick = {
                        onDismiss()
                        onRestBackground.invoke()
                    },
                ) {
                    Text("Rest Background", color = Color.Black)
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF81D4FA)),
                    onClick = {
                        onDismiss()
                        onChangeTempTypeClick.invoke()
                    },
                ) {
                    Text("Change To $tempTypeName", color = Color.Black)
                }
            }
        }
    }
}