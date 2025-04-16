package com.example.spygame.repository

import com.example.spygame.data.WordDao
import com.example.spygame.model.WordEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repository @Inject constructor(private val wordDao: WordDao) {
    suspend fun addWord(word: WordEntity) =
        withContext(Dispatchers.IO) { wordDao.insertWord(word = word) }

    suspend fun updateWord(word: WordEntity) =
        withContext(Dispatchers.IO) { wordDao.updateWord(word = word) }

    suspend fun deleteWord(word: WordEntity) =
        withContext(Dispatchers.IO) { wordDao.deleteWord(word = word) }

    suspend fun addAllWords(words: List<WordEntity>) =
        withContext(Dispatchers.IO) { wordDao.insertAll(words) }

    fun getAllWords(): Flow<List<WordEntity>> = wordDao.getAllWords().flowOn(
        Dispatchers.IO
    ).conflate()

}