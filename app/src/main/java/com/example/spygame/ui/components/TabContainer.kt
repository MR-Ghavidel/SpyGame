package com.example.spygame.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spygame.R
import com.example.spygame.model.Category
import com.example.spygame.model.Languages
import com.example.spygame.ui.theme.gold
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
                ) {
                    val parentState =
                        wordScreenViewModel.getCategorySelectionState(categoriesList[pageIndex])
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
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
                                )
                            )
                            Text(
                                text = stringResource(
                                    R.string.select_all_in_category,
                                    categoriesList[pageIndex].getDisplayName()
                                ),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
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
                            if (currentLanguages == Languages.PERSIAN.displayName)
                                Text(
                                    text = word.wordFa,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            else if (currentLanguages == Languages.ENGLISH.displayName)
                                Text(
                                    text = word.wordEn,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
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