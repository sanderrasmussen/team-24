package no.uio.ifi.IN2000.team24_app.data.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

interface CategoryDao {

    @Insert
    fun insertAll(vararg category: Category)

    @Delete
    fun delete(category : Category)

    @Query("SELECT * FROM Category")
    fun getAll(): List<Category>

}