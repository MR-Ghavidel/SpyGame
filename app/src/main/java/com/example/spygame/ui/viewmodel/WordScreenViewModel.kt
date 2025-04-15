package com.example.spygame.ui.viewmodel

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.state.ToggleableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spygame.model.Category
import com.example.spygame.model.WordEntity
import com.example.spygame.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordScreenViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    private val _wordEntityList = MutableStateFlow<List<WordEntity>>(emptyList())
    val wordEntityList = _wordEntityList.asStateFlow()

    private val _randomWord = MutableStateFlow<String?>("null")
    val randomWord: StateFlow<String?> = _randomWord.asStateFlow()

    private var shuffledWordsFa: MutableList<String> = mutableListOf()
    private var shuffledWordsEn: MutableList<String> = mutableListOf()


    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllWords().distinctUntilChanged().collect { listOfWords ->
                _wordEntityList.value = listOfWords
                shuffledWordsFa.clear()
                shuffledWordsEn.clear()
            }
            Log.d("WordViewmodel", "wordEntityList: $wordEntityList")
        }
    }

    private val _listStates = MutableStateFlow<Map<Int, LazyListState>>(emptyMap())

    fun getLazyListStateForPage(pageIndex: Int): LazyListState {

        val currentMap = _listStates.value
        return currentMap[pageIndex] ?: LazyListState().also {
            _listStates.value = currentMap + (pageIndex to it)
        }
    }

    fun getCategorySelectionState(category: Category): ToggleableState {
        val wordsInCategory = _wordEntityList.value.filter { it.category == category }
        return when {
            wordsInCategory.all { it.isSelect } -> ToggleableState.On
            wordsInCategory.none { it.isSelect } -> ToggleableState.Off
            else -> ToggleableState.Indeterminate
        }
    }

    fun toggleCategorySelection(category: Category) {
        val wordsInCategory = _wordEntityList.value.filter { it.category == category }
        val newState = wordsInCategory.any { !it.isSelect }

        viewModelScope.launch {
            wordsInCategory.forEach { word ->
                updateWord(word.copy(isSelect = newState))
            }
        }
    }


    fun addWord(wordEntity: WordEntity) =
        viewModelScope.launch { repository.addWord(wordEntity.copy(category = Category.USER_DEFINED)) }

    fun updateWord(wordEntity: WordEntity) =
        viewModelScope.launch { repository.updateWord(wordEntity) }

    fun removeWord(wordEntity: WordEntity) =
        viewModelScope.launch { repository.deleteWord(wordEntity) }

/*    fun updateRandomWordFa() {
        val selectedWords = _wordEntityList.value.filter { it.isSelect }

        _randomWord.value = if (selectedWords.isNotEmpty()) {
            selectedWords.random().wordFa
        } else {
            null
        }
    }*/
    fun updateRandomWordFa() {
        val selectedWords = _wordEntityList.value.filter { it.isSelect }.map { it.wordFa }

        if (shuffledWordsFa.isEmpty()) {
            shuffledWordsFa = selectedWords.shuffled().toMutableList()
        }

        _randomWord.value = if (shuffledWordsFa.isNotEmpty()) {
            shuffledWordsFa.removeAt(0)
        } else {
            Log.d("WordViewmodel", "first: $shuffledWordsFa")
            null
        }
    Log.d("WordViewmodel", "shuffled: $shuffledWordsFa")
    }


/*    fun updateRandomWordEn() {
        val selectedWords = _wordEntityList.value.filter { it.isSelect }

        _randomWord.value = if (selectedWords.isNotEmpty()) {
            selectedWords.random().wordEn
        } else {
            null
        }
    }*/
fun updateRandomWordEn() {
    val selectedWords = _wordEntityList.value.filter { it.isSelect }.map { it.wordEn }

    if (shuffledWordsEn.isEmpty()) {
        shuffledWordsEn = selectedWords.shuffled().toMutableList()
    }

    _randomWord.value = if (shuffledWordsEn.isNotEmpty()) {
        shuffledWordsEn.removeAt(0)
    } else {
        null
    }
}

}