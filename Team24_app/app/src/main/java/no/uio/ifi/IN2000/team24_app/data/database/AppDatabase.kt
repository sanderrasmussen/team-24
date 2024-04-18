package no.uio.ifi.IN2000.team24_app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.mockito.internal.matchers.Null

@Database(entities = [Clothes::class, Bank::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clothesDao(): ClothesDao
    abstract fun bankDao() : BankDao

}
