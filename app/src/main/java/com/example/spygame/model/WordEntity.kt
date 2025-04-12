package com.example.spygame.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class WordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val wordEn: String,
    val wordFa: String,
    val category: Category,
    var isSelect: Boolean = true
)

