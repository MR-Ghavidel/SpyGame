package com.example.spygame.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.spygame.ui.theme.green
import com.example.spygame.ui.theme.red
import com.example.spygame.ui.theme.warningYellow

@Composable
fun CustomSnackbar(snackbarData: SnackbarData, snackbarHostState: SnackbarHostState) {
    val backgroundColor = when (snackbarData.visuals.actionLabel) {
        SnackbarType.SUCCESS.display -> green
        SnackbarType.ERROR.display -> red
        else -> warningYellow
    }

    Snackbar(
        containerColor = backgroundColor,
        contentColor = Color.White,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { snackbarHostState.currentSnackbarData?.dismiss() }
            )
            Text(snackbarData.visuals.message)
        }
    }
}

enum class SnackbarType(val display: String) {
    SUCCESS("SUCCESS"),
    ERROR("ERROR")
}