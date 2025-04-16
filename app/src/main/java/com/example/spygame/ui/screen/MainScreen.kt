package com.example.spygame.ui.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.spygame.R
import com.example.spygame.model.BottomSheetType
import com.example.spygame.model.Languages
import com.example.spygame.navigation.SpyScreens
import com.example.spygame.ui.components.CustomSnackbar
import com.example.spygame.ui.components.InvertibleImage
import com.example.spygame.ui.components.OptionRow
import com.example.spygame.ui.components.SnackbarType
import com.example.spygame.ui.components.bottomSheet.SpyPickerBottomSheet
import com.example.spygame.ui.theme.gold
import com.example.spygame.ui.viewmodel.LanguageViewModel
import com.example.spygame.ui.viewmodel.SettingsViewModel
import com.example.spygame.ui.viewmodel.WordScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("StringFormatMatches")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    wordScreenViewModel: WordScreenViewModel,
    settingsViewModel: SettingsViewModel,
    languageViewModel: LanguageViewModel
) {
    val context = LocalContext.current
    var lastBackPressedTime = 0L
    val toastDuration = 2000L // مدت زمان برای تایید دوبار زدن دکمه برگشت (2 ثانیه)

    // استفاده از BackHandler برای کنترل دکمه برگشت
    BackHandler {
        val currentBackPressedTime = SystemClock.elapsedRealtime()

        if (currentBackPressedTime - lastBackPressedTime < toastDuration) {
            // بستن اپلیکیشن بعد از دوبار زدن دکمه برگشت
            // برنامه با استفاده از متد finish() بسته می‌شود
            (context as? Activity)?.finish()
        } else {
            // نمایش پیام "برای خروج دوباره دکمه برگشت را بزنید"
            Toast.makeText(
                context,
                context.getString(R.string.press_the_back_button_again_to_exit), Toast.LENGTH_SHORT
            ).show()
        }

        lastBackPressedTime = currentBackPressedTime
    }

    val minPlayer = 3
    val minSpy = 1
    val minTime = 1

    var players by rememberSaveable { mutableIntStateOf(settingsViewModel.numPlayers) }
    var spies by rememberSaveable { mutableIntStateOf(settingsViewModel.numSpies) }
    var time by rememberSaveable { mutableIntStateOf(settingsViewModel.gameTime) }

    val maxSpy = players - 1
    val maxTime = 100
    val maxPlayer = 25

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var activeSheet by rememberSaveable { mutableStateOf(BottomSheetType.NONE) }
    var isShowMenu by remember { mutableStateOf(false) }
    val currentLanguage by languageViewModel.currentLanguage.collectAsState()
    val randomWord by wordScreenViewModel.randomWord.collectAsState()
    LaunchedEffect(randomWord) {
        Log.d("WordViewmodel", "randomWord: $randomWord")
    }
    // بروزرسانی کلمه رندوم بر اساس زبان
    /*    if (currentLanguage == Languages.PERSIAN.displayName) {
            wordScreenViewModel.updateRandomWordFa()
        }
        if (currentLanguage == Languages.ENGLISH.displayName) {
            wordScreenViewModel.updateRandomWordEn()
        }*/

    LaunchedEffect(currentLanguage) {
        delay(500)
        isShowMenu = false
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                /*actions = {
                    IconButton(
                        onClick = {
                            isShowMenu = !isShowMenu
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = stringResource(R.string.drop_down_menu),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    AnimatedVisibility(
                        visible = isShowMenu,
                        enter = slideInVertically(
                            initialOffsetY = { -it }, // ورود از بالا
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = FastOutSlowInEasing
                            )
                        ) + fadeIn(animationSpec = tween(500)),
                        exit = slideOutVertically(
                            targetOffsetY = { it }, // خروج به پایین
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = FastOutSlowInEasing
                            )
                        ) + fadeOut(animationSpec = tween(500))
                    ) {
                        DropdownMenu(
                            expanded = isShowMenu,
                            onDismissRequest = { isShowMenu = false },
                            properties = PopupProperties(),
                            shape = RoundedCornerShape(12.dp),
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.persian)) },
                                onClick = {
                                    languageViewModel.setLanguage(Languages.PERSIAN.displayName)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.english)) },
                                onClick = {
                                    languageViewModel.setLanguage(Languages.ENGLISH.displayName)
                                }
                            )
                        }
                    }
                },*/
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(8.dp)
            ) { data ->
                CustomSnackbar(snackbarData = data, snackbarHostState = snackbarHostState)
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(0.5f))
                InvertibleImage(
                    imageRes = R.drawable.spy_icon,// R.drawable.spy_icon3,
                    Modifier.size(180.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Player row
                    OptionRow(
                        title = stringResource(R.string.number_of_players),
                        value = players.toString()
                    ) {
                        activeSheet = BottomSheetType.PLAYER
                    }
                    if (activeSheet == BottomSheetType.PLAYER) {
                        SpyPickerBottomSheet(
                            onDismissRequest = {
                                scope.launch {
                                    sheetState.hide()
                                    activeSheet = BottomSheetType.NONE
                                }
                            },
                            onConfirmed = {
                                settingsViewModel.updateNumPlayers(it)
                                players = settingsViewModel.numPlayers
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar(
                                        message = context.getString(
                                            R.string.the_number_of_players_was_set_to,
                                            players
                                        ),
                                        actionLabel = SnackbarType.SUCCESS.display,
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            },
                            sheetState = sheetState,
                            startRange = minPlayer,
                            endRange = maxPlayer,
                            startIndex = players,
                            topText = stringResource(R.string.number_of_players),
                        )
                    }
                    Spacer(
                        modifier = Modifier.padding(4.dp)
                    )
                    OptionRow(
                        title = stringResource(R.string.number_of_spies),
                        value = spies.toString()
                    ) {
                        activeSheet = BottomSheetType.SPY
                    }

                    if (activeSheet == BottomSheetType.SPY) {
                        SpyPickerBottomSheet(
                            onDismissRequest = {
                                scope.launch {
                                    sheetState.hide()
                                    activeSheet = BottomSheetType.NONE
                                }
                            },
                            onConfirmed = {
                                settingsViewModel.updateNumSpies(it)
                                spies = settingsViewModel.numSpies
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar(
                                        message = context.getString(
                                            R.string.the_number_of_spies_was_set_to,
                                            spies
                                        ),
                                        actionLabel = SnackbarType.SUCCESS.display,
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            },
                            sheetState = sheetState,
                            startRange = minSpy,
                            endRange = maxSpy,
                            startIndex = spies,
                            topText = stringResource(R.string.number_of_spies),
                        )
                    }
                    Spacer(
                        modifier = Modifier.padding(4.dp)
                    )
                    OptionRow(
                        title = stringResource(R.string.timer),
                        value = stringResource(R.string.time_minutes, time)
                    ) {
                        activeSheet = BottomSheetType.TIME
                    }

                    if (activeSheet == BottomSheetType.TIME) {
                        SpyPickerBottomSheet(
                            onDismissRequest = {
                                scope.launch {
                                    sheetState.hide()
                                    activeSheet = BottomSheetType.NONE
                                }
                            },
                            onConfirmed = {
                                settingsViewModel.updateGameTime(it)
                                time = settingsViewModel.gameTime
                                scope.launch {
                                    //isSnackbarVisible = true
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar(
                                        message = context.getString(
                                            R.string.the_timer_was_set_for_minutes,
                                            time
                                        ),
                                        actionLabel = SnackbarType.SUCCESS.display,
                                        duration = SnackbarDuration.Short
                                    )
                                    //isSnackbarVisible = false
                                }
                            },
                            sheetState = sheetState,
                            startRange = minTime,
                            endRange = maxTime,
                            startIndex = time,
                            topText = stringResource(R.string.timer),
                        )
                    }
                    Spacer(
                        modifier = Modifier.padding(4.dp)
                    )
                    OptionRow(
                        title = stringResource(R.string.words_list),
                        value = null
                    ) {
                        navController.navigate(route = SpyScreens.WordScreen.name)
                    }

                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        scope.launch {
                            if (players > spies) {
                                settingsViewModel.updateGameTime(time)
                                //isSnackError = false

                                // بروزرسانی کلمه رندوم بر اساس زبان
                                if (currentLanguage == Languages.PERSIAN.displayName) {
                                    wordScreenViewModel.updateRandomWordFa()
                                }
                                if (currentLanguage == Languages.ENGLISH.displayName) {
                                    wordScreenViewModel.updateRandomWordEn()
                                }

                                // چک کردن اگر کلمه رندوم موجود است
                                if (randomWord != null) {
                                    navController.navigate(
                                        route = "${SpyScreens.RolesScreen.name}?players=$players&spies=$spies"
                                    )
                                } else {
                                    // نمایش اسنک بار در صورت نبود کلمه رندوم
                                    //if (!isSnackbarVisible) {
                                    //isSnackError = true
                                    //isSnackbarVisible = true
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar(
                                        message = context.getString(R.string.you_should_select_at_least_one_word_from_the_list),
                                        actionLabel = SnackbarType.ERROR.display,
                                        duration = SnackbarDuration.Short
                                    )

                                    //isSnackbarVisible = false
                                    //delay(150)
                                    //isSnackError = false
                                    //}
                                }
                            } else {
                                // نمایش اسنک بار در صورت تعداد بازیکن کمتر از جاسوس‌ها
                                //if (!isSnackbarVisible) {
                                //isSnackError = true
                                //isSnackbarVisible = true
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar(
                                    message = context.getString(R.string.the_number_of_players_must_be_greater_than_the_number_of_spies),
                                    actionLabel = SnackbarType.ERROR.display,
                                    duration = SnackbarDuration.Short
                                )

                                //isSnackbarVisible = false
                                //delay(150)
                                //isSnackError = false
                                //}
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = gold,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                ) {
                    Text(
                        text = stringResource(R.string.start_game),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                Button(
                    onClick = {
                        navController.navigate(route = SpyScreens.RulesScreen.name)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    ),
                ) {
                    Text(
                        text = stringResource(R.string.rules),
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}




