package com.example.spygame.ui.components

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp

/*
@Composable
fun TopAppBarMenu(
    isMuteValue: Boolean,
    isMutedChange: (Boolean) -> Unit,
    mediaPlayer: MediaPlayer
) {
    val isExpanded by rememberSaveable { mutableStateOf(false) }
    var isMuted by rememberSaveable { mutableStateOf(isMuteValue) }
    DropdownMenu(
        modifier = Modifier,
        expanded = isExpanded,
        onDismissRequest = { },
        shape = RoundedCornerShape(8.dp),
        containerColor = MaterialTheme.colorScheme.onBackground,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sound",
                    style = MaterialTheme.typography.labelLarge,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = !isMuted,
                    onCheckedChange = {
                        isMuted = !isMuted
                        isMutedChange(isMuted)
                        if (isMuted) {
                            mediaPlayer.setVolume(0f, 0f)
                        } else {
                            mediaPlayer.setVolume(1f, 1f)
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.onPrimary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.onPrimary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.primary,
                    ),
                    modifier = Modifier.scale(0.7f)
                )
            }
        }
    }
}*/
