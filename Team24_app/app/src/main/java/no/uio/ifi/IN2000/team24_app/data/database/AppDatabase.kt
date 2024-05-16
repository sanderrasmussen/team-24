package no.uio.ifi.IN2000.team24_app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

//this is the database class, it uses RoomDb and there will only be one instance of it.
@Database(entities = [Clothes::class, Bank::class, EquipedClothes::class, Category::class, Question::class], version = 1)//enteties(tables) used by database
@TypeConverters(DateConverter::class, StringListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    //these are the dao that our roomdb database uses, the function and sql queries are implemented in these dao's
    abstract fun clothesDao(): ClothesDao
    abstract fun bankDao() : BankDao
    abstract fun categoryDao() : CategoryDao
    abstract fun questionDao() : QuestionDao
}
