package com.example.spygame.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.spygame.R
import com.example.spygame.navigation.SpyScreens
import com.example.spygame.ui.theme.green
import com.example.spygame.ui.viewmodel.WordScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RolesScreen(
    navController: NavController,
    wordScreenViewModel: WordScreenViewModel,
    players: Int,
    spies: Int
) {
    var playerCount by rememberSaveable { mutableIntStateOf(1) }
    val playerSpies = rememberSaveable { selectSpies(players, spies) }
    val role by wordScreenViewModel.randomWord.collectAsState()
    var counter by rememberSaveable { mutableIntStateOf(0) }
    var isAnimating by rememberSaveable { mutableStateOf(false) }


    var isFlipped by rememberSaveable { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "Card Flip Animation"
    )
    val coroutineScope = rememberCoroutineScope()
    var showText by rememberSaveable { mutableStateOf(false) }

    var finishedShowRoles by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.start_game),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() },
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
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.2f))
            AnimatedContent(
                targetState = finishedShowRoles,
                transitionSpec = {
                    fadeIn(animationSpec = tween(1500)) togetherWith fadeOut(
                        animationSpec = tween(
                            1500
                        )
                    )
                },
                label = "PlayerTextSwitch"
            ) { isFinished ->
                val text = if (isFinished) "" else if (playerCount <= players) stringResource(
                    R.string.player_counter,
                    playerCount
                ) else ""

                Text(
                    text = text,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.weight(0.1f))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .graphicsLayer {
                        rotationY = rotation
                        cameraDistance = 12f * density
                    },
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    if (!isAnimating && !finishedShowRoles) {
                        coroutineScope.launch {
                            isAnimating = true
                            showText = false
                            isFlipped = !isFlipped
                            delay(600)
                            counter++
                            if (counter == 2) {
                                delay(200)
                                playerCount++
                                counter = 0
                                if (playerCount == players + 1) {
                                    finishedShowRoles = true
                                }
                            }
                            isAnimating = false
                        }
                    }
                },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                ),
                border = CardDefaults.outlinedCardBorder()
            ) {
                if (rotation < 90f) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            modifier = Modifier.size(60.dp),
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(R.string.checked)
                        )
                        AnimatedContent(
                            targetState = finishedShowRoles,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(1500)) togetherWith fadeOut(
                                    animationSpec = tween(1500)
                                )
                            },
                            label = "TextSwitch"
                        ) { isFinished ->
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 4.dp, end = 4.dp),
                                text = if (isFinished) stringResource(R.string.done) else stringResource(
                                    R.string.after_clicking_on_this_card_you_will_see_your_role
                                ),
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }

                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer { rotationY = 180f },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        LaunchedEffect(playerCount) {
                            showText = true
                        }
                        AnimatedVisibility(
                            visible = showText,
                            enter = fadeIn(animationSpec = tween(durationMillis = 800)),
                        ) {
                            if (playerCount in playerSpies) {
                                Text(
                                    text = buildAnnotatedString {
                                        append(stringResource(R.string.you_are) + " ")
                                        withStyle(
                                            style = SpanStyle(
                                                color = MaterialTheme.colorScheme.onBackground,
                                                fontWeight = FontWeight.Bold,
                                                textDecoration = TextDecoration.Underline
                                            )
                                        ) {
                                            append(stringResource(R.string.spy))
                                        }
                                    },
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(8.dp)
                                )
                            } else {
                                Text(
                                    text = buildAnnotatedString {
                                        append(stringResource(R.string.the_word_is) + " ")
                                        withStyle(
                                            style = SpanStyle(
                                                color = MaterialTheme.colorScheme.onBackground,
                                                fontWeight = FontWeight.Bold,
                                                textDecoration = TextDecoration.Underline
                                            )
                                        ) {
                                            append(role)
                                        }
                                    },
                                    style = MaterialTheme.typography.titleLarge,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
            val progress = (playerCount - 1).toFloat() / (players).toFloat()
            val animatedProgress by animateFloatAsState(
                targetValue = progress,
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = LinearEasing
                )
            )
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    AnimatedVisibility(
                        visible = !finishedShowRoles,
                        enter = fadeIn(animationSpec = tween(durationMillis = 1000)),
                        exit = fadeOut(animationSpec = tween(durationMillis = 1000))
                    ) {
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                            LinearProgressIndicator(
                                progress = { animatedProgress },
                                modifier = Modifier
                                    .fillMaxWidth(0.85f)
                                    .padding(top = 16.dp/*, start = 16.dp, end = 16.dp*/)
                                    .height(4.dp),
                                color = green,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                gapSize = 1.dp
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    AnimatedVisibility(
                        visible = finishedShowRoles,
                        enter = slideInVertically(
                            initialOffsetY = { -it },
                            animationSpec = tween(durationMillis = 1400)
                        ) + fadeIn(animationSpec = tween(durationMillis = 1400)),
                    ) {
                        Button(
                            modifier = Modifier.padding(top = 16.dp),
                            onClick = {
                                navController.navigate(route = SpyScreens.TimerScreen.name)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onPrimary,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                        ) {
                            Text(text = stringResource(R.string.start_timer))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(0.7f))
        }
    }
}


fun selectSpies(playersCount: Int, spiesCount: Int): List<Int> {
    // لیست بازیکنان از 1 تا playersCount (مثلاً برای 6 بازیکن، لیست [1, 2, 3, 4, 5, 6])
    val players = (1..playersCount).toList()

    // انتخاب تصادفی spiesCount جاسوس از بین بازیکنان
    val playerSpies = players.shuffled().take(spiesCount)

    return playerSpies
}