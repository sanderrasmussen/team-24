package no.uio.ifi.IN2000.team24_app.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverter
import java.util.Date

@Dao
interface ClothesDao{
    @Insert
    fun insertAll(vararg clothing: Clothes)

    @Insert
    fun insertEquipedClothesTable(vararg equipedClothes: EquipedClothes)

    @Delete
    fun delete(clothing : Clothes)

    @Query("UPDATE Clothes SET unlocked=true WHERE imageAsset= :clothingId")
    fun setClothingToOwned(clothingId: Int)

    @Query("SELECT * FROM Clothes WHERE unlocked=true AND bodyPart='head'")
    fun getAllOwnedHeads(): List<Clothes>

    @Query("SELECT * FROM Clothes WHERE unlocked=true AND bodyPart='torso'")
    fun getAllOwnedTorsos(): List<Clothes>

    @Query("SELECT * FROM Clothes WHERE unlocked=true AND bodyPart='legs'")
    fun getAllOwnedLegs(): List<Clothes>

    @Query("SELECT * FROM Clothes WHERE unlocked=false AND bodyPart='head' ORDER BY price ASC ")
    fun getAllNotOwnedHeads(): List<Clothes>

    @Query("SELECT * FROM Clothes WHERE unlocked=false AND bodyPart='torso' ORDER BY price ASC ")
    fun getAllNotOwnedTorsos(): List<Clothes>

    @Query("SELECT * FROM Clothes WHERE unlocked=false AND bodyPart='legs' ORDER BY price ASC ")
    fun getAllNotOwnedLegs(): List<Clothes>

    @Query("SELECT * FROM Clothes WHERE imageAsset=(SELECT EquipedHead FROM EquipedClothes )")
    fun getEquipedHead(): List<Clothes>

    @Query("SELECT * FROM Clothes WHERE imageAsset=(SELECT equipedTorso FROM EquipedClothes )")
    fun getEquipedTorso(): List<Clothes>

    @Query("SELECT * FROM Clothes WHERE imageAsset=(SELECT equipedLegs FROM EquipedClothes )")
    fun getEquipedLegs(): List<Clothes>

    @Query("UPDATE EquipedClothes SET equipedHead= :clothingId")
    fun writeEquipedHead(clothingId : Int)

    @Query("UPDATE EquipedClothes SET equipedTorso= :clothingId")
    fun writeEquipedTorso(clothingId: Int)

    @Query("UPDATE EquipedClothes SET equipedLegs= :clothingId")
    fun writeEquipedLegs(clothingId: Int)

    @Query("SELECT lastLoginDate FROM EquipedClothes")
    fun getLastDate(): Long

    @Query("UPDATE EquipedClothes SET lastLoginDate= :timestamp") //i still don't get the WHERE-part
    fun updateDate(timestamp: Long = System.currentTimeMillis())

    @Query("SELECT temperatureAtLastLogin FROM EquipedClothes") //WHERE id=0??
    fun getTemperatureAtLastLogin(): Int

    @Query("UPDATE EquipedClothes SET temperatureAtLastLogin= :temperature")
    fun setTemperatureAtLastLogin(temperature: Int)

}


