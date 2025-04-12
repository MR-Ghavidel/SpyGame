package com.example.spygame.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.spygame.data.Words.animalsEn
import com.example.spygame.data.Words.animalsFa
import com.example.spygame.data.Words.foodsEn
import com.example.spygame.data.Words.foodsFa
import com.example.spygame.data.Words.jobsEn
import com.example.spygame.data.Words.jobsFa
import com.example.spygame.data.Words.placesEn
import com.example.spygame.data.Words.placesFa
import com.example.spygame.model.Category
import com.example.spygame.model.WordEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Database(entities = [WordEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "word_db"
                )
                    .addCallback(DatabaseCallback())
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                INSTANCE?.let { database ->
                    populateDatabase(database.wordDao())
                }
            }
        }
    }
}

// حالا می‌توانید این لیست‌ها را در `populateDatabase` استفاده کنید:
private suspend fun populateDatabase(wordDao: WordDao) {
    val words = mutableListOf<WordEntity>()

    words.addAll(placesEn.zip(placesFa) { en, fa ->
        WordEntity(wordEn = en, wordFa = fa, category = Category.PLACE)
    })

    words.addAll(foodsEn.zip(foodsFa) { en, fa ->
        WordEntity(wordEn = en, wordFa = fa, category = Category.FOOD)
    })

    words.addAll(animalsEn.zip(animalsFa) { en, fa ->
        WordEntity(wordEn = en, wordFa = fa, category = Category.ANIMAL)
    })

    words.addAll(jobsEn.zip(jobsFa) { en, fa ->
        WordEntity(wordEn = en, wordFa = fa, category = Category.JOB)
    })

    val existingWords = wordDao.getAllWords().first()
    if (existingWords.isEmpty()) {
        wordDao.insertAll(words)
    }
}


/*private suspend fun populateDatabase(wordDao: WordDao) {
    val context = MyApplication.applicationContext()

    val defaultPlaces = context.getString(R.string.places).split(", ")
    val defaultFoods = context.getString(R.string.foods).split(", ")
    val defaultAnimals = context.getString(R.string.animals).split(", ")
    val defaultPersons = context.getString(R.string.persons).split(", ")

    val words = mutableListOf<WordEntity>()

    words.addAll(defaultPlaces.map { place ->
        WordEntity(wordEn = place, wordFa = place, category = Category.PLACE)
    })

    words.addAll(defaultFoods.map { food ->
        WordEntity(wordEn = food, wordFa = food, category = Category.FOOD)
    })

    words.addAll(defaultAnimals.map { animal ->
        WordEntity(wordEn = animal, wordFa = animal, category = Category.ANIMAL)
    })

    words.addAll(defaultPersons.map { person ->
        WordEntity(wordEn = person, wordFa = person, category = Category.JOB)
    })

    val existingWords = wordDao.getAllWords().first()
    if (existingWords.isEmpty()) {
        wordDao.insertAll(words)
    }
}*/

