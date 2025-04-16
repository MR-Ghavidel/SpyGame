package com.example.spygame.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spygame.R
import com.example.spygame.model.Category
import com.example.spygame.model.Languages
import com.example.spygame.model.WordEntity
import com.example.spygame.ui.components.CustomSnackbar
import com.example.spygame.ui.components.ScrollingFancyIndicatorContainerTabs
import com.example.spygame.ui.components.SnackbarType
import com.example.spygame.ui.theme.lightRed
import com.example.spygame.ui.theme.red
import com.example.spygame.ui.viewmodel.LanguageViewModel
import com.example.spygame.ui.viewmodel.WordScreenViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordScreen(
    navController: NavController,
    wordScreenViewModel: WordScreenViewModel,
    languageViewModel: LanguageViewModel,
) {

    val snackbarHostState = remember { SnackbarHostState() }
    var pagerIndex by rememberSaveable { mutableIntStateOf(0) }
    var isAddBottomSheet by rememberSaveable { mutableStateOf(false) }
    var isInfoDialog by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val lazyListState = wordScreenViewModel.getLazyListStateForPage(pagerIndex)

    val isScrolling by remember(lazyListState) {
        snapshotFlow { lazyListState.isScrollInProgress }
    }.collectAsState(initial = false)
    val currentLanguage by languageViewModel.currentLanguage.collectAsState()
    BackHandler {
/*        if (currentLanguage == Languages.PERSIAN.displayName) {
            wordScreenViewModel.updateRandomWordFa()
        }
        if (currentLanguage == Languages.ENGLISH.displayName) {
            wordScreenViewModel.updateRandomWordEn()
        }*/
        navController.navigateUp()
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.words_list),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            /*if (currentLanguage == Languages.PERSIAN.displayName) {
                                wordScreenViewModel.updateRandomWordFa()
                            }
                            if (currentLanguage == Languages.ENGLISH.displayName) {
                                wordScreenViewModel.updateRandomWordEn()
                            }*/
                            navController.navigateUp()
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
                actions = {
                    IconButton(
                        onClick = {
                            isInfoDialog = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = stringResource(R.string.word_list_info),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    if (isInfoDialog)
                        BasicAlertDialog(
                            onDismissRequest = { isInfoDialog = false },
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(color = MaterialTheme.colorScheme.onPrimary)
                        ) {
                            Text(
                                text = stringResource(R.string.note_a_word_will_be_randomly_selected_from_the_words_that_are_checked_ticked_at_the_start_of_the_game),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                onClick = {
                    isAddBottomSheet = true
                },
                shape = RectangleShape,
                containerColor = MaterialTheme.colorScheme.onPrimary,
                contentColor = MaterialTheme.colorScheme.primary,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp),
                text = {
                    Text(
                        text = stringResource(R.string.add_new_word),
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_new_word)
                    )
                },
                expanded = !isScrolling
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
        ) {
            ScrollingFancyIndicatorContainerTabs(wordScreenViewModel, languageViewModel) {
                pagerIndex = it
            }
            //val focusManager = LocalFocusManager.current
            //if (isAddBottomSheet)
            AnimatedVisibility(
                visible = isAddBottomSheet
            ) {
                AddWordBottomSheet(
                    onDismissRequest = {
                        //focusManager.clearFocus(true)
                        isAddBottomSheet = false
                    }
                ) { fa, en ->
                    isAddBottomSheet = false
                    wordScreenViewModel.addWord(
                        WordEntity(
                            wordEn = en,
                            wordFa = fa,
                            category = Category.USER_DEFINED
                        )
                    )
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        if (currentLanguage == Languages.PERSIAN.displayName) {
                            snackbarHostState.showSnackbar(
                                message = context.getString(
                                    R.string.the_word_was_added_to_the_word_list,
                                    fa
                                ),
                                actionLabel = SnackbarType.SUCCESS.display,
                                duration = SnackbarDuration.Short
                            )
                        } else if (currentLanguage == Languages.ENGLISH.displayName) {
                            snackbarHostState.showSnackbar(
                                message = "The word \"$en\" was added to the word list.",
                                actionLabel = SnackbarType.SUCCESS.display,
                                duration = SnackbarDuration.Short
                            )
                        }

                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWordBottomSheet(
    onDismissRequest: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false // اجازه دهید BottomSheet به حالت نیمه‌باز و کامل بزرگ شود
    )
    var wordTextFaValue by rememberSaveable { mutableStateOf("") }
    var wordTextEnValue by rememberSaveable { mutableStateOf("") }
    var isErrorFa by remember { mutableStateOf(false) }
    var isErrorEn by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        sheetState = sheetState,
        modifier = Modifier.fillMaxHeight(),
        sheetMaxWidth = BottomSheetDefaults.SheetMaxWidth,
        onDismissRequest = {
            scope.launch { sheetState.hide() }
            onDismissRequest()
        },
        containerColor = MaterialTheme.colorScheme.onSecondary,
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.add_new_word),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(Modifier.height(8.dp))
            //word text field fa
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = wordTextFaValue,
                shape = RoundedCornerShape(12.dp),
                label = { Text(text = stringResource(R.string.enter_the_word_in_persian)) },
                placeholder = { Text(text = "فارسی") },
                onValueChange = {
                    if (it.all { char -> char.isLetterOrDigit() || char.isWhitespace() }) {
                        wordTextFaValue = it
                    }
                },
                singleLine = true,
                isError = isErrorFa,
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.primary,
                    errorTextColor = MaterialTheme.colorScheme.primary,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    errorBorderColor = red,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = Color.DarkGray,
                    errorLabelColor = MaterialTheme.colorScheme.primary,
                    focusedPlaceholderColor = Color.LightGray,
                    unfocusedPlaceholderColor = Color.LightGray,
                )
            )
            Spacer(Modifier.height(4.dp))

            //word text field en
            /*OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = wordTextEnValue,
                shape = RoundedCornerShape(12.dp),
                label = { Text(text = stringResource(R.string.enter_the_word_in_english)) },
                placeholder = { Text(text = "English") },
                onValueChange = {
                    if (it.all { char -> char.isLetterOrDigit() || char.isWhitespace() }) {
                        wordTextEnValue = it
                    }
                },
                singleLine = true,
                isError = isErrorEn,
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.primary,
                    errorTextColor = MaterialTheme.colorScheme.primary,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    errorBorderColor = red,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = Color.DarkGray,
                    errorLabelColor = MaterialTheme.colorScheme.primary,
                    focusedPlaceholderColor = Color.LightGray,
                    unfocusedPlaceholderColor = Color.LightGray,
                )
            )
            Spacer(Modifier.height(4.dp))*/

            //category
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = Category.USER_DEFINED.getDisplayName(),
                shape = RoundedCornerShape(12.dp),
                label = { Text(text = stringResource(R.string.category)) },
                onValueChange = {},
                singleLine = true,
                readOnly = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.primary,
                    errorTextColor = MaterialTheme.colorScheme.primary,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    errorBorderColor = red,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = Color.DarkGray,
                    errorLabelColor = MaterialTheme.colorScheme.primary,
                    focusedPlaceholderColor = Color.LightGray,
                    unfocusedPlaceholderColor = Color.LightGray,
                )
            )
            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.weight(0.1f))
                Button(
                    modifier = Modifier.weight(0.5f),
                    onClick = {
                        isErrorFa =
                            !(wordTextFaValue.isNotBlank() && wordTextFaValue.isNotEmpty())
                        isErrorEn = false
                            //!(wordTextEnValue.isNotBlank() && wordTextEnValue.isNotEmpty())
                        if (!isErrorEn && !isErrorFa) {
                            scope.launch { sheetState.hide() }
                            onConfirm(wordTextFaValue, wordTextEnValue)
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 4.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                    ),
                ) {
                    Text(text = stringResource(R.string.save))
                }
                Spacer(Modifier.width(8.dp))
                Button(
                    modifier = Modifier.weight(0.5f),
                    onClick = {
                        scope.launch { sheetState.hide() }
                        onDismissRequest()
                    },
                    shape = RoundedCornerShape(8.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    border = BorderStroke(
                        color = MaterialTheme.colorScheme.secondary,
                        width = 1.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onSecondary,
                        contentColor = MaterialTheme.colorScheme.secondary,
                    ),
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
                Spacer(Modifier.weight(0.1f))
            }
            //Spacer(Modifier.height(24.dp))
            /*Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, bottom = 4.dp),
                textAlign = TextAlign.Start,
                color = red,
                style = MaterialTheme.typography.bodyMedium,
                text = stringResource(R.string.a_few_notes),
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp),
                textAlign = TextAlign.Start,
                color = lightRed,
                style = MaterialTheme.typography.bodyMedium,
                text = stringResource(R.string.please_note_that_to_add_a_word_you_must_enter_the_word_in_both_english_and_persian_in_the_fields_above_after_saving_the_word_you_can_view_it_in_the_selected_category),
            )*/
            //Spacer(Modifier.height(12.dp))
        }
    }
}





