package no.uio.ifi.IN2000.team24_app.data.character


import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import no.uio.ifi.IN2000.team24_app.data.bank.BankRepository
import no.uio.ifi.IN2000.team24_app.data.locationForecast.LocationForecastRepository
import java.util.Date
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
fun writeEquippedClothesToDisk(character: Character) {
    CoroutineScope(Dispatchers.IO).launch {
        clothingRepo.writeEquipedHead(character.head.imageAsset)
        clothingRepo.writeEquipedTorso(character.torso.imageAsset)
        clothingRepo.writeEquipedLegs(character.legs.imageAsset)

        clothingRepo.updateDate()
        //todo: any better way to do this? maybe pass the temp as a parameter to this function?
        //TODO: yeah i think i'll do that, but this works for testing. I'll ask Sander, he knows this part better than me
        val locForecast = LocationForecastRepository()
        val temp = locForecast.getWeatherNow()?.air_temperature?:0.0
        clothingRepo.setTemperatureAtLastLogin(temp.toInt())

    }
}
suspend fun loadSelectedClothes(): Character = withContext(Dispatchers.IO) {
    //this code checks the value of the clothes yesterday and the actual temperature, and
    val character  = Character(
        clothingRepo.getEquipedHead(),
        clothingRepo.getEquipedTorso(),
        clothingRepo.getEquipedLegs()
    )
    val playerTemperature = character.findAppropriateTemp()
    val lastDate = clothingRepo.getLastDate()

    givePoints(lastDate = lastDate,playerTemperature =  playerTemperature)
    return@withContext character
}

fun givePoints(lastDate:Date, playerTemperature:Double){
    val today = Date()

    if(lastDate.before(today)) {
        val lastTemperature = clothingRepo.getTemperatureAtLastLogin()
        val delta = playerTemperature - lastTemperature
        Log.d("loadSelectedClothes", "Delta: $delta")
        val points = maxOf(0.0, 10-abs(delta))
        Log.d("loadSelectedClothes", "Points: $points")
        if(points>0.0){ //this means the players clothes were within 10 degrees of the actual temperature
            Log.d("loadSelectedClothes", "writing {$points} to bank")
            val bank = BankRepository()
            CoroutineScope(Dispatchers.IO).launch {
                bank.deposit(points.toInt())
            }

            //TODO find a way to pass this to the ui
            //Toast.makeText(null, "Du fikk $points mynter for å velge gode klær!", Toast.LENGTH_LONG).show()
        }
    }
}

fun getDefaultBackupCharacter(): Character {

    return Character(
            clothingRepo.backupHead(),
            clothingRepo.backupTorso(),
            clothingRepo.backupLegs()
        )

}
