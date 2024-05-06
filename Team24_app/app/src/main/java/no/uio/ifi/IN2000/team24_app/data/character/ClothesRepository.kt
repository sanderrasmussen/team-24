package no.uio.ifi.IN2000.team24_app.data.character

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.uio.ifi.IN2000.team24_app.data.database.Clothes
import no.uio.ifi.IN2000.team24_app.data.database.MyDatabase
import java.util.Date

class ClothesRepository {
    private val db = MyDatabase.getInstance()
    private val clothesDao = db.clothesDao()


    suspend fun setClothingToOwned(id : Int){
        clothesDao.setClothingToOwned(id)
    }

    //owned clothes:
    suspend fun getAllOwnedHeads(): List<Head>{
        return withContext(Dispatchers.IO) withContex@{
            return@withContex clothesDao.getAllOwnedHeads()
                .map { convertToHead(it) } //casting all in list to head
        }
    }
    suspend fun getAllOwnedTorsos(): List<Torso>{
        return withContext(Dispatchers.IO) withContex@{
            return@withContex clothesDao.getAllOwnedTorsos()
                .map { convertToTorso(it) } //casting all in list to head
        }
    }

    suspend fun getAllOwnedLegs(): List<Legs>{
        return withContext(Dispatchers.IO) withContex@{
            return@withContex clothesDao.getAllOwnedLegs().map{convertToLegs(it)}
        }
    }

    //not owned:
    fun getAllNotOwnedHeads(): List<Head>{
        return clothesDao.getAllNotOwnedHeads().map{convertToHead(it)}
    }

    fun getAllNotOwnedTorsos(): List<Torso>{
        return clothesDao.getAllNotOwnedTorsos().map{convertToTorso(it)}
    }

    fun getAllNotOwnedLegs(): List<Legs>{
        return clothesDao.getAllNotOwnedLegs().map { convertToLegs(it) }
    }

    //Equipped clothes:
    fun getEquipedHead(): Head{

        return convertToHead(clothesDao.getEquipedHead().first())
    }

    fun getEquipedTorso(): Torso{
        return convertToTorso(clothesDao.getEquipedTorso().first())
    }

    fun getEquipedLegs(): Legs{
        return  convertToLegs(clothesDao.getEquipedLegs().first())
    }

    fun writeEquipedHead(immageAsset :Int){
        clothesDao.writeEquipedHead(immageAsset)
    }

    fun writeEquipedTorso(immageAsset : Int){
        clothesDao.writeEquipedTorso(immageAsset)
    }

    fun writeEquipedLegs(immageAsset: Int){
        clothesDao.writeEquipedLegs(immageAsset)
    }

    fun updateDate(){
        clothesDao.updateDate()
    }
    fun getLastDate() : Date {
        return clothesDao.getLastDate()
    }
    //helping methods:
    fun convertToHead(clothes: Clothes): Head {
        return Head(
            name = clothes.name,
            heatValue = clothes.heatValue,
            imageAsset = clothes.imageAsset,
            price = clothes.price,
            altAsset = clothes.altAsset,
        )
    }
    fun convertToTorso(clothes: Clothes): Torso {
        return Torso(
            name = clothes.name,
            heatValue = clothes.heatValue,
            imageAsset = clothes.imageAsset,
            price = clothes.price,
            altAsset = clothes.altAsset,
        )
    }
    fun convertToLegs(clothes: Clothes): Legs {
        return Legs(
            name = clothes.name,
            heatValue = clothes.heatValue,
            imageAsset = clothes.imageAsset,
            price = clothes.price,
            altAsset = clothes.altAsset,
        )
    }
}