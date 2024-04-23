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
    fun getAllOwnedHeads(): List<Clothes>

    @Query("SELECT * FROM Clothes")
    fun getAllOwnedTorsos(): List<Clothes>

    @Query("SELECT * FROM Clothes")
    fun getAllOwnedLegs(): List<Clothes>

    @Query("SELECT * FROM Clothes")
    fun getAllNotOwnedHeads(): List<Clothes>

    @Query("SELECT * FROM Clothes")
    fun getAllNotOwnedTorsos(): List<Clothes>

    @Query("SELECT * FROM Clothes")
    fun getAllNotOwnedLegs(): List<Clothes>

    @Query("SELECT * FROM Clothes")
    fun getEquipedHead(): List<Clothes>

    @Query("SELECT * FROM Clothes")
    fun getEquipedTorso(): List<Clothes>

    @Query("SELECT * FROM Clothes")
    fun getEquipedLegs(): List<Clothes>
}
