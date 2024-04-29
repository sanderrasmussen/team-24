package no.uio.ifi.IN2000.team24_app.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ClothesDao{
    @Insert
    fun insertAll(vararg clothing: Clothes)

    @Delete
    fun delete(clothing : Clothes)

    @Query("SELECT * FROM Clothes")
    fun getAll(): List<Clothes>
}
