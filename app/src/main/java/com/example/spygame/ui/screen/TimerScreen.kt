package com.example.spygame.ui.screen

import android.media.MediaPlayer
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.spygame.R
import com.example.spygame.navigation.SpyScreens
import com.example.spygame.ui.theme.green
import com.example.spygame.ui.theme.lightRed
import com.example.spygame.ui.theme.red
import com.example.spygame.ui.theme.white
import com.example.spygame.ui.viewmodel.LanguageViewModel
import com.example.spygame.ui.viewmodel.SettingsViewModel
import kotlinx.coroutines.delay
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel,
    languageViewModel: LanguageViewModel
) {
    val time = settingsViewModel.gameTime
    val currentLanguage by languageViewModel.currentLanguage.collectAsState()
    var remainingTime by rememberSaveable { mutableIntStateOf(time * 60) }
    val progress by animateFloatAsState(
        targetValue = remainingTime / (time * 60).toFloat(),
        animationSpec = tween(durationMillis = 1000),
        label = "progress_animation"
    )
    var isShowBackDialog by rememberSaveable { mutableStateOf(false) }
    var isShowEndDialog by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.alarm_sound2) }
    var isMuted by rememberSaveable { mutableStateOf(false) }
    var isTimeOut by rememberSaveable { mutableStateOf(false) }
    var isShowAlarmDialog by rememberSaveable { mutableStateOf(false) }
    var isAlarmStopped by rememberSaveable { mutableStateOf(false) }

    BackHandler(enabled = true) {
        isShowBackDialog = true
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.timer),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            isShowBackDialog = true
                        },
                        modifier = Modifier.clip(RoundedCornerShape(8.dp))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_to_main_screen),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }

                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LaunchedEffect(remainingTime) {
                while (remainingTime > 0) {
                    delay(1000L)
                    remainingTime--
                }
                if (!isAlarmStopped) {
                    mediaPlayer.start()
                    isTimeOut = true
                    isShowAlarmDialog = true

                    while (!isAlarmStopped) {
                        mediaPlayer.start()
                        delay(2000L)
                    }
                }
            }

            val minutes = remainingTime / 60
            val seconds = remainingTime % 60

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.size(200.dp),
                    color = green,
                    strokeWidth = 8.dp,
                    trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
                )
                Text(
                    text = String.format(Locale(currentLanguage), "%02d:%02d", minutes, seconds),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(Modifier.height(16.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            isShowEndDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                    ) {
                        Text(
                            text = stringResource(R.string.end_game),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.sound),
                            style = MaterialTheme.typography.labelLarge,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(
                            checked = !isMuted,
                            onCheckedChange = {
                                isMuted = !isMuted
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
        }
    }
    if (isShowEndDialog)
        TimerScreenDialog(
            navController,
            confirmButton = stringResource(R.string.yes_end_game),
            cancelButton = stringResource(R.string.no_keep_playing),
            message = stringResource(R.string.are_you_sure_you_want_to_end_the_game)
        ) {
            isShowEndDialog = it
        }
    if (isShowBackDialog)
        TimerScreenDialog(
            navController,
            confirmButton = stringResource(R.string.yes_go_back),
            message = stringResource(R.string.do_you_want_to_go_back)
        ) {
            isShowBackDialog = it
        }
    if (isShowAlarmDialog)
        AlarmDialog(
            message = stringResource(R.string.time_s_up),
            stopAlarm = {
                isAlarmStopped = true
                mediaPlayer.stop()
            },
            isShowDialog = {
                isShowAlarmDialog = it
            }
        )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TimerScreenDialog(
    navController: NavController,
    message: String,
    confirmButton: String = stringResource(R.string.confirm),
    cancelButton: String = stringResource(R.string.cancel),
    isShowDialog: (Boolean) -> Unit,
) {
    BasicAlertDialog(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color = MaterialTheme.colorScheme.onPrimary),
        onDismissRequest = {
            isShowDialog(false)
        },
        properties = DialogProperties(),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.height(24.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(24.dp))
                Button(
                    modifier = Modifier
                        .weight(0.8f),
                    onClick = {
                        isShowDialog(false)
                        navController.navigate(
                            route = SpyScreens.MainScreen.name
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = lightRed,
                        contentColor = white
                    )
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = confirmButton,
                        maxLines = 1,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(Modifier.width(8.dp))
                Button(
                    modifier = Modifier
                        .weight(0.8f),
                    onClick = {
                        isShowDialog(false)
                    }
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = cancelButton,
                        maxLines = 1,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(Modifier.width(24.dp))
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AlarmDialog(
    stopAlarm: () -> Unit,
    message: String,
    stopButton: String = stringResource(R.string.stop_alarm),
    isShowDialog: (Boolean) -> Unit,
) {
    BasicAlertDialog(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color = red),
        onDismissRequest = {
        },
        properties = DialogProperties(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = white,
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    isShowDialog(false)
                    stopAlarm()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = lightRed,
                    contentColor = white
                )
            ) {
                Text(text = stopButton)
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}