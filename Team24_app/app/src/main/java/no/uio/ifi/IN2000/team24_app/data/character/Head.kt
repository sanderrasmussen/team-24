package no.uio.ifi.IN2000.team24_app.data.character

import kotlinx.coroutines.runBlocking
import no.uio.ifi.IN2000.team24_app.R

/**
 * Head class that extends the Clothing class
 * @see Clothing
 */
data class Head(
    override val name: String, override val heatValue: Int, override val imageAsset: Int, override val price: Int,
    override val altAsset: Int,
):Clothing(name, heatValue, imageAsset, price, altAsset){

}
private val clothesRepo = ClothesRepository()
//val paintHead = Head("Paint", 0, R.drawable.paint_head, 10000, R.drawable.paint_head_alt)
val short_hair = Head("Short Hair", 25, R.drawable.head_short_hair, 30, R.drawable.alt_head_short_hair)

//if someone really has anything against run blocking then they can change the arcitecture themselves. The queries are short and fast and wont be blocking UI for long
/**
 * Function that returns a list of all the unlocked heads
 * @return List<Head> list of all the unlocked heads
 */
//this is a rewrite to fit with previous architecture - before the database, all clothes were fetched from a list in the code to allow testing.
//this allows the rest of the code to function as before.

fun heads(): List<Head> = runBlocking{
    clothesRepo.getAllOwnedHeads()
}

