package no.uio.ifi.IN2000.team24_app.data.character


import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import no.uio.ifi.IN2000.team24_app.data.bank.BankRepository
import java.time.LocalDate
import kotlin.math.abs

/**
 * This class represents a piece of clothing in the game.
 *
 * @param name The name of the clothing item.
 * @param heatValue The temperature appropriate for the clothing item. If a hat is best suited for 12 degrees, the heatValue is 12
 * @param imageAsset The resource ID of the image representing the clothing item, used for rendering the character.
 * @param price The price of the clothing item.
 * @param altAsset The resource ID of the alternate image representing the clothing item. The alternate image is used in the inventory. and store.
 * @param unlocked A boolean indicating whether the clothing item is unlocked.
 */
abstract class Clothing(
    open val name:String,
    open val heatValue: Int,
    open val imageAsset: Int,
    open val price: Int,
    open val altAsset: Int,
    var unlocked: Boolean = false
 )

private val clothingRepo = ClothesRepository()

/**
 * this function writes the character to the database, to be read on next load
 * @param character the character to be written to the database
 * @param temperature the temperature at the time of writing
 */
fun writeEquippedClothesToDisk(character: Character, temperature: Double) {
    //the temperature needs to be known, to give the player points for wearing the right clothes based on actual temperature
    CoroutineScope(Dispatchers.IO).launch {
        clothingRepo.writeEquipedHead(character.head.imageAsset)
        clothingRepo.writeEquipedTorso(character.torso.imageAsset)
        clothingRepo.writeEquipedLegs(character.legs.imageAsset)

        clothingRepo.updateDate()
        clothingRepo.setTemperatureAtLastLogin(temperature.toInt())

    }
}

/**
 * This function loads the clothes that the player has equipped.
 * It also checks if the player has logged in previously before today, and if so, gives the player points for wearing the right clothes.
 * @return The character with the equipped clothes.
 * @see Character
 */
suspend fun loadSelectedClothes(): Character = withContext(Dispatchers.IO) {
    //this code checks the value of the clothes yesterday and the actual temperature, and
    val character  = Character(
        clothingRepo.getEquipedHead(),
        clothingRepo.getEquipedTorso(),
        clothingRepo.getEquipedLegs()
    )
    //if you log in and don't change clothes, you still need to update the date

    val playerTemperature = character.findAppropriateTemp()
    val lastDate = clothingRepo.getLastDate()

    givePoints(lastDate = lastDate,playerTemperature =  playerTemperature)

    clothingRepo.updateDate()
    return@withContext character
}

/**
 * This function gives the player points based on the temperature of the clothes they wore last time they logged in.
 * The player gets 10 points if the clothes were within 10 degrees of the actual temperature.
 * @param lastDate The date of the last login.
 * @param playerTemperature The temperature of the clothes the player wore last time they logged in.
 * @see BankRepository
 * @see ClothesRepository
 */
fun givePoints(lastDate:LocalDate, playerTemperature:Double){
    val today= LocalDate.now()

    Log.d("loadSelectedClothes", "${lastDate.toString()}, ${today.toString()}")
    if(lastDate.isBefore(today)) {
        val lastTemperature = clothingRepo.getTemperatureAtLastLogin()
        val delta = abs(playerTemperature - lastTemperature)
        Log.d("loadSelectedClothes", "Delta: $delta")
        val points = maxOf(0.0, 10.0-delta)
        Log.d("loadSelectedClothes", "Points: $points")
        if(points>0.0){ //this means the players clothes were within 10 degrees of the actual temperature
            Log.d("loadSelectedClothes", "writing {$points} to bank")
            val bank = BankRepository()
            CoroutineScope(Dispatchers.IO).launch {
                bank.deposit(points.toInt())
            }

            //TODO find a way to pass a toast/snackbar to the ui. realistically, we don't have time to implement that.
        }
    }else{
        Log.d("loadSelectedClothes", "date was not before today, no points given")}
}

/**
 * fetches the default backup character from the database, to be used in case of failure
 * @return the default backup character
 */
fun getDefaultBackupCharacter(): Character {

    return Character(
            clothingRepo.backupHead(),
            clothingRepo.backupTorso(),
            clothingRepo.backupLegs()
        )

}
