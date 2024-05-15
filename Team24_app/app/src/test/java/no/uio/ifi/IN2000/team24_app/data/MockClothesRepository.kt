package no.uio.ifi.IN2000.team24_app.data.clothesTest

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.uio.ifi.IN2000.team24_app.R
import no.uio.ifi.IN2000.team24_app.data.character.Head
import no.uio.ifi.IN2000.team24_app.data.character.Legs
import no.uio.ifi.IN2000.team24_app.data.character.Torso
import no.uio.ifi.IN2000.team24_app.data.database.Clothes
import no.uio.ifi.IN2000.team24_app.data.database.ClothesDao
import no.uio.ifi.IN2000.team24_app.data.database.MyDatabase
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


    class MockClothesRepository(val clothesDao: ClothesDao) {




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
        suspend fun getAllNotOwnedHeads(): List<Head>{
            return withContext(Dispatchers.IO) withContex@{
                return@withContex clothesDao.getAllNotOwnedHeads().map{convertToHead(it)} //casting all in list to head
            }

        }

        suspend fun getAllNotOwnedTorsos(): List<Torso>{
            return withContext(Dispatchers.IO) withContex@{
                return@withContex clothesDao.getAllNotOwnedTorsos().map{convertToTorso(it)} //casting all in list to head
            }
        }

        suspend fun getAllNotOwnedLegs(): List<Legs>{
            return withContext(Dispatchers.IO) withContex@{
                return@withContex clothesDao.getAllNotOwnedLegs().map{convertToLegs(it)} //casting all in list to head
            }
        }

        //Equipped clothes:
        //I had a bug where the first time the app started without any cahce, the app would crash because
        //equiped clothes list was empty, therefore i check if it is empty and if null or empty load a hardcoded backup character
        fun getEquipedHead(): Head {
            var head = clothesDao.getEquipedHead()
            if (head.isNullOrEmpty()){
                return backupHead()
            }
            return convertToHead(head.first())
        }

        fun getEquipedTorso(): Torso {
            var torso = clothesDao.getEquipedTorso()
            if (torso.isNullOrEmpty()){
                return backupTorso()
            }
            return convertToTorso(torso.first())
        }

        fun getEquipedLegs(): Legs {
            var legs = clothesDao.getEquipedLegs()
            if (legs.isNullOrEmpty()){
                return backupLegs()
            }
            return convertToLegs(legs.first())
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
            clothesDao.updateDate(timestamp = System.currentTimeMillis())
        }
        @RequiresApi(Build.VERSION_CODES.O)
        fun getLastDate() : LocalDate {
            //this is ugly, but allows me to get the DATE in a non-deprecated format (looking at you, java.util.Date), to compare with current date later
            return Instant.ofEpochMilli(clothesDao.getLastDate()).atZone(ZoneId.systemDefault()).toLocalDate()
        }
        fun backupHead(): Head {
            val short_hair : Clothes = Clothes(R.drawable.head_short_hair,"Short Hair", 25,  30, R.drawable.alt_head_short_hair,"head",true)
            return convertToHead(short_hair)
        }
        fun backupTorso(): Torso {
            val long_sleeve = Clothes(R.drawable.torso_long_sleeves, "Long Sleeve", 5,25, R.drawable.alt_torso_long_sleeve, "torso", true)
            return convertToTorso(long_sleeve)
        }
        fun backupLegs(): Legs {
            val pants = Clothes(R.drawable.legs_pants,"Pants", 5,  25, R.drawable.alt_legs_pants, "legs", true)
            return convertToLegs(pants)
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

        fun getTemperatureAtLastLogin(): Int {
            return clothesDao.getTemperatureAtLastLogin()
        }
        fun setTemperatureAtLastLogin(temperature: Int){
            clothesDao.setTemperatureAtLastLogin(temperature)
        }
    }

