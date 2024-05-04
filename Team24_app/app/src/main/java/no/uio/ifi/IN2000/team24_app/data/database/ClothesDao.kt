package no.uio.ifi.IN2000.team24_app.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.util.Date

@Dao
interface ClothesDao{
    @Insert
    fun insertAll(vararg clothing: Clothes)

    @Delete
    fun delete(clothing : Clothes)

    @Query("UPDATE Clothes SET unlocked=true WHERE imageAsset= :clothingId")
    fun setClothingToOwned(clothingId: Int): List<Clothes>

    @Query("SELECT * FROM Clothes")
    fun getAllOwnedHeads(): List<Clothes>

    @Query("SELECT * FROM Clothes")
    fun getAllOwnedTorsos(): List<Clothes>

    @Query("SELECT * FROM Clothes WHERE unlocked=true AND bodyPart='legs'")
    fun getAllOwnedLegs(): List<Clothes>

    @Query("SELECT * FROM Clothes WHERE unlocked=false AND bodyPart='head'")
    fun getAllNotOwnedHeads(): List<Clothes>

    @Query("SELECT * FROM Clothes WHERE unlocked=false AND bodyPart='torso'")
    fun getAllNotOwnedTorsos(): List<Clothes>

    @Query("SELECT * FROM Clothes WHERE unlocked=false AND bodyPart='legs'")
    fun getAllNotOwnedLegs(): List<Clothes>

    @Query("SELECT * FROM Clothes WHERE imageAsset=(SELECT equipedHead FROM equipedclothes WHERE id=0)")
    fun getEquipedHead(): List<Clothes>

    @Query("SELECT * FROM Clothes WHERE imageAsset=(SELECT equipedTorso FROM equipedclothes WHERE id=0)")
    fun getEquipedTorso(): List<Clothes>

    @Query("SELECT * FROM Clothes WHERE imageAsset=(SELECT equipedLegs FROM equipedclothes WHERE id=0)")
    fun getEquipedLegs(): List<Clothes>

    @Query("SELECT lastLoginDate FROM EquipedClothes WHERE id=0")
    fun getLastDate(): Date

    @Query("UPDATE EquipedClothes SET lastLoginDate=strftime('%Y-%m-%d', 'now') WHERE id=0")
    fun updateDate()
}
