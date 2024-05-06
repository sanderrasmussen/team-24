package no.uio.ifi.IN2000.team24_app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.mockito.internal.matchers.Null

@Database(entities = [Clothes::class, Bank::class, Category::class, Question::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clothesDao(): ClothesDao
    abstract fun bankDao() : BankDao
    abstract fun categoryDao() : CategoryDao
    abstract fun questionDao() : QuestionDao

}
