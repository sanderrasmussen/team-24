package no.uio.ifi.IN2000.team24_app.data.character

import kotlinx.coroutines.runBlocking
import no.uio.ifi.IN2000.team24_app.R

/**
 * Legs class that extends the clothing class
 * @see Clothing
 */
data class Legs (
    override val name: String, override val heatValue: Int, override val imageAsset: Int, override val price: Int,
    override val altAsset: Int,
):Clothing(name, heatValue, imageAsset, price, altAsset){

}
private val clothesRepo = ClothesRepository()
//val paintLegs = Legs("Paint", 0, R.drawable.paint_legs, 10000, R.drawable.paint_legs_alt)
val pants = Legs("Pants", 5, R.drawable.legs_pants, 25, R.drawable.alt_legs_pants)

/**
 * Function that returns a list of all the unlocked legs
 * @return List<Legs> list of all the unlocked legs
 */
fun legs(): List<Legs> = runBlocking{
    clothesRepo.getAllOwnedLegs()
}