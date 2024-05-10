package no.uio.ifi.IN2000.team24_app.data.character


import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import no.uio.ifi.IN2000.team24_app.data.bank.BankRepository
import no.uio.ifi.IN2000.team24_app.data.locationForecast.LocationForecastRepository
import java.time.LocalDate
import java.util.Calendar
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
fun writeEquippedClothesToDisk(character: Character, temperature: Double) {
    CoroutineScope(Dispatchers.IO).launch {
        clothingRepo.writeEquipedHead(character.head.imageAsset)
        clothingRepo.writeEquipedTorso(character.torso.imageAsset)
        clothingRepo.writeEquipedLegs(character.legs.imageAsset)

        clothingRepo.updateDate()
        //todo: any better way to do this? maybe pass the temp as a parameter to this function?
        //TODO: yeah i think i'll do that, but this works for testing. I'll ask Sander, he knows this part better than me

        clothingRepo.setTemperatureAtLastLogin(temperature.toInt())

    }
}
@RequiresApi(Build.VERSION_CODES.O)
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

@RequiresApi(Build.VERSION_CODES.O)
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

fun getDefaultBackupCharacter(): Character {

    return Character(
            clothingRepo.backupHead(),
            clothingRepo.backupTorso(),
            clothingRepo.backupLegs()
        )

}
