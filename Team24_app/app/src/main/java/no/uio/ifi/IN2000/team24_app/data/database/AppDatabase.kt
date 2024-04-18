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
/*
    companion object
    {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{

            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "AppDatabase"
                ).build()
                INSTANCE = instance
                instance
            }
        }

    }@*/
}
