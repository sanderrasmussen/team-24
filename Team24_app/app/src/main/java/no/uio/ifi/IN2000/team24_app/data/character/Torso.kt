package no.uio.ifi.IN2000.team24_app.data.character

import kotlinx.coroutines.runBlocking
import no.uio.ifi.IN2000.team24_app.R

/**
 * Torso class that extends the clothing class
 * @see Clothing
 */
data class Torso (
    override val name: String, override val heatValue: Int, override val imageAsset: Int, override val price: Int,
    override val altAsset: Int,
):Clothing(name, heatValue, imageAsset, price, altAsset){

}
private val clothesRepo = ClothesRepository()

//val paintTorso = Torso("Paint", 0, R.drawable.paint_torso, 10000, R.drawable.paint_torso_alt)
val long_sleeve = Torso("Long Sleeve", 5, R.drawable.torso_long_sleeves, 25, R.drawable.alt_torso_long_sleeve)

//run blocking is only for testing that the database works
/**
 * Function that returns a list of all the unlocked torsos
 * @return List<Torso> list of all the unlocked torsos
 */
fun torsos(): List<Torso> = runBlocking {
    clothesRepo.getAllOwnedTorsos()
}