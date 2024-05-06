package no.uio.ifi.IN2000.team24_app.data.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverter
import java.util.Date

interface CategoryDao {

    @Insert
    fun insertAll(vararg category: Category)

    @Delete
    fun delete(category : Category)

    @Query("SELECT * FROM Category")
    fun getAllCategories(): List<Category>

    @Query("SELECT * FROM Category where category = :categoryName")
    fun getCategory(categoryName: String): Category

    @Query("SELECT lastDateAnswered FROM Category WHERE category = :categoryName")
    fun getCategoryLastDateAnswered(categoryName: String): Date

    @Query("SELECT points FROM Category WHERE category = :categoryName")
    fun getCategoryPoints(categoryName: String): Int

    @Query("UPDATE Category SET lastDateAnswered = strftime('%Y-%m-%d', 'now') WHERE category = :categoryName")
    fun updateCategoryLastDateAnswered(categoryName: String)

}

class DateConverter {

    @TypeConverter
    fun toDate(timestamp: Long): Date {

        return Date(timestamp)

    }

    @TypeConverter
    fun toTimestamp(date: Date): Long {

        return date.time

    }

}

