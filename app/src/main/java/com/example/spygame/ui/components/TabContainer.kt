package com.example.spygame.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.spygame.R
import com.example.spygame.model.Category
import com.example.spygame.model.Languages
import com.example.spygame.ui.theme.gold
import com.example.spygame.ui.theme.lightRed
import com.example.spygame.ui.theme.white
import com.example.spygame.ui.viewmodel.LanguageViewModel
import com.example.spygame.ui.viewmodel.WordScreenViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrollingFancyIndicatorContainerTabs(
    wordScreenViewModel: WordScreenViewModel,
    languageViewModel: LanguageViewModel,
    pagerIndex: (Int) -> Unit
) {
    val categoriesList: List<Category> = Category.entries
    val wordList by wordScreenViewModel.wordEntityList.collectAsState()
    val pagerState = rememberPagerState(pageCount = { categoriesList.size })
    val coroutineScope = rememberCoroutineScope()
    val currentLanguages by languageViewModel.currentLanguage.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        SecondaryScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            edgePadding = 8.dp,
            modifier = Modifier.fillMaxWidth(),
            indicator = {
                FancyAnimatedIndicatorWithModifier(index = pagerState.currentPage)
            },
            divider = { HorizontalDivider(color = MaterialTheme.colorScheme.onBackground) }
        ) {
            categoriesList.forEachIndexed { index, category ->
                Tab(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp)),
                    selected = pagerState.currentPage == index,
                    selectedContentColor = gold,
                    unselectedContentColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = category.getDisplayName(),
                            style = MaterialTheme.typography.labelLarge,
                            maxLines = 1
                        )
                    },
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) { pageIndex ->
            val filteredList = wordList.filter { it.category == categoriesList[pageIndex] }
            val lazyListState = wordScreenViewModel.getLazyListStateForPage(pageIndex)
            LaunchedEffect(pagerState.currentPage) {
                pagerIndex(pagerState.currentPage)
            }
            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_words_in_this_category),
                        color = Color.Gray,
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Italic
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState,
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val parentState =
                        wordScreenViewModel.getCategorySelectionState(categoriesList[pageIndex])
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                /*.background(
                                    color = Color.DarkGray,//MaterialTheme.colorScheme.secondary,
                                    shape = RoundedCornerShape(12.dp)
                                )*/
                                .clickable {
                                    wordScreenViewModel.toggleCategorySelection(
                                        categoriesList[pageIndex]
                                    )
                                }
                        ) {
                            TriStateCheckbox(
                                state = parentState,
                                onClick = {
                                    wordScreenViewModel.toggleCategorySelection(
                                        categoriesList[pageIndex]
                                    )
                                },
                                colors = CheckboxDefaults.colors(
                                    checkmarkColor = gold
                                ),
                                modifier = Modifier.scale(1f)
                            )
                            Text(
                                text = stringResource(
                                    R.string.select_all_in_category,
                                    categoriesList[pageIndex].getDisplayName()
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                            var isShowDialogAll by rememberSaveable {
                                mutableStateOf(false)
                            }
                            if (categoriesList[pageIndex] == Category.USER_DEFINED) {
                                Spacer(Modifier.weight(1f))
                                IconButton(
                                    onClick = {
                                        isShowDialogAll = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "delete word",
                                        tint = lightRed
                                    )
                                }
                            }

                            if (isShowDialogAll) {
                                BasicAlertDialog(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(color = MaterialTheme.colorScheme.onPrimary),
                                    onDismissRequest = {
                                        isShowDialogAll = false
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
                                            text = "همه کلمات بخش \"کلمات کاربر\" حذف شود؟",
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
                                                    isShowDialogAll = false
                                                    wordScreenViewModel.removeWordList(
                                                        category = Category.USER_DEFINED,
                                                        wordsList = wordList
                                                    )
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = lightRed,
                                                    contentColor = white
                                                )
                                            ) {
                                                Text(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    text = "بله",
                                                    maxLines = 1,
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                            Spacer(Modifier.width(8.dp))
                                            Button(
                                                modifier = Modifier
                                                    .weight(0.8f),
                                                onClick = {
                                                    isShowDialogAll = false
                                                }
                                            ) {
                                                Text(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    text = "لغو",
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
                        }
                    }
                    items(filteredList) { word ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    wordScreenViewModel.updateWord(word.copy(isSelect = !word.isSelect))
                                },
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = word.isSelect,
                                onCheckedChange = {
                                    wordScreenViewModel.updateWord(word.copy(isSelect = !word.isSelect))
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = MaterialTheme.colorScheme.primary,
                                ),
                                modifier = Modifier.scale(0.8f)
                            )
                            if (currentLanguages == Languages.PERSIAN.displayName)
                                Text(
                                    text = word.wordFa,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            else if (currentLanguages == Languages.ENGLISH.displayName)
                                Text(
                                    text = word.wordEn,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            var isShowDialog by rememberSaveable {
                                mutableStateOf(false)
                            }
                            if (word.category == Category.USER_DEFINED) {
                                Spacer(Modifier.weight(1f))
                                IconButton(
                                    onClick = {
                                        isShowDialog = true
                                    }
                                ) {
                                    Icon(Icons.Outlined.Delete, "delete word")
                                }
                            }

                            if (isShowDialog) {
                                BasicAlertDialog(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(color = MaterialTheme.colorScheme.onPrimary),
                                    onDismissRequest = {
                                        isShowDialog = false
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
                                            text = "کلمه \"${word.wordFa}\" حذف شود؟",
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
                                                    isShowDialog = false
                                                    wordScreenViewModel.removeWord(word)
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = lightRed,
                                                    contentColor = white
                                                )
                                            ) {
                                                Text(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    text = "بله",
                                                    maxLines = 1,
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                            Spacer(Modifier.width(8.dp))
                                            Button(
                                                modifier = Modifier
                                                    .weight(0.8f),
                                                onClick = {
                                                    isShowDialog = false
                                                }
                                            ) {
                                                Text(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    text = "لغو",
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
                        }
                    }
                }
            }
        }
    }
}

/*LazyColumn(state = lazyListState) {
                if (filteredList.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.no_words_in_this_category),
                                color = Color.Gray,
                                fontSize = 18.sp,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }
                } else {
                    items(filteredList) { word ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    wordScreenViewModel.updateWord(word.copy(isSelect = !word.isSelect))
                                },
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = word.isSelect,
                                onCheckedChange = {
                                    wordScreenViewModel.updateWord(word.copy(isSelect = !word.isSelect))
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = MaterialTheme.colorScheme.primary,
                                )
                            )
                            Text(
                                text = word.word,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }*/