package no.uio.ifi.IN2000.team24_app.data.character


import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.test.core.app.ActivityScenario.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import no.uio.ifi.IN2000.team24_app.R
import no.uio.ifi.IN2000.team24_app.data.database.Clothes
import no.uio.ifi.IN2000.team24_app.data.database.MyDatabase
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
fun writeEquipedClothesToDisk(character: Character) {
    CoroutineScope(Dispatchers.IO).launch {
        clothingRepo.writeEquipedHead(character.head.imageAsset)
        clothingRepo.writeEquipedTorso(character.torso.imageAsset)
        clothingRepo.writeEquipedLegs(character.legs.imageAsset)
        //todo: write last login date?
    }
}
suspend fun loadSelectedClothes(): Character = withContext(Dispatchers.IO) {
    //this code checks the value of the clothes yesterday and the actual temperature, and
    val character  = Character(
        clothingRepo.getEquipedHead(),
        clothingRepo.getEquipedTorso(),
        clothingRepo.getEquipedLegs()
    )
    val lastDate = clothingRepo.getLastDate()
    val today = Date()
    if(lastDate != today) {
        val lastTemperature = clothingRepo.getTemperatureAtLastLogin()
        val playerTemperature = character.findAppropriateTemp()
        val delta = playerTemperature - lastTemperature
        val points = maxOf(0.0, 10-abs(delta))
        Log.d("loadSelectedClothes", "Points: $points")
        if(points>0){
            //todo: write to bank, and update the last login date in write
        }
    }
    return@withContext character
}
fun getDefaultBackupCharacter(): Character {

    return Character(
            clothingRepo.backupHead(),
            clothingRepo.backupTorso(),
            clothingRepo.backupLegs()
        )

}
