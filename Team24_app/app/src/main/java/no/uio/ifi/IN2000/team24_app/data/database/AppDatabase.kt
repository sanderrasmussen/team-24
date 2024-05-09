package no.uio.ifi.IN2000.team24_app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.mockito.internal.matchers.Null

@Database(entities = [Clothes::class, Bank::class, EquipedClothes::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clothesDao(): ClothesDao
    abstract fun bankDao() : BankDao

}
