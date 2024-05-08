package no.uio.ifi.IN2000.team24_app.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverter
import java.util.Date

@Dao
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

    @Query("SELECT shouldStartTimer FROM Category WHERE category = :categoryName")
    fun getCategoryShouldStartTimer(categoryName: String): Boolean

    @Query("UPDATE Category SET lastDateAnswered = strftime('%Y-%m-%d', 'now') WHERE category = :categoryName")
    fun updateCategoryLastDateAnswered(categoryName: String)

}

// date converter to convert dates to long date time
// because room database cannot store date objects
class DateConverter {

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {

        if (timestamp == null) {

            return null

        }

        // turn long timestamp into a date object to fetch from database
        return Date(timestamp)

    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {

        // turn date object into a long timestamp to insert to database
        if (date == null) {

            return null

        }

        return date.time

    }

}

