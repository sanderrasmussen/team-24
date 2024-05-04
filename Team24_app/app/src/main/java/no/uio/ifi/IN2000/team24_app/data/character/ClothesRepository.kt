package no.uio.ifi.IN2000.team24_app.data.character

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import no.uio.ifi.IN2000.team24_app.data.database.Clothes
import no.uio.ifi.IN2000.team24_app.data.database.MyDatabase

class ClothesRepository {
    private val db = MyDatabase.getInstance()
    private val clothesDao = db.clothesDao()

    suspend fun setClothingToOwned(id : Int){
        clothesDao.setClothingToOwned(id)
    }

    //owned clothes:
    suspend fun getAllOwnedHeads(): List<Clothes>{
        return clothesDao.getAllOwnedHeads()
    }
    fun getAllOwnedTorsos(): List<Clothes>{
        return clothesDao.getAllOwnedTorsos()
    }

    fun getAllOwnedLegs(): List<Clothes>{
        return clothesDao.getAllOwnedLegs()
    }

    //not owned:
    fun getAllNotOwnedHeads(): List<Clothes>{
        return clothesDao.getAllNotOwnedHeads()
    }


    fun getAllNotOwnedTorsos(): List<Clothes>{
        return clothesDao.getAllNotOwnedTorsos()
    }

    fun getAllNotOwnedLegs(): List<Clothes>{
        return clothesDao.getAllNotOwnedLegs()
    }

    //Equipped clothes:
    fun getEquipedHead(): List<Clothes>{
        return clothesDao.getEquipedHead()
    }

    fun getEquipedTorso(): List<Clothes>{
        return clothesDao.getEquipedTorso()
    }

    fun getEquipedLegs(): List<Clothes>{
        return  getEquipedLegs()
    }


}