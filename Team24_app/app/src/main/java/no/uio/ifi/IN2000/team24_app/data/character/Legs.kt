package no.uio.ifi.IN2000.team24_app.data.character

import no.uio.ifi.IN2000.team24_app.R

data class Legs (
    override val name: String, override val heatValue: Int, override val imageAsset: Int, override val price: Int,
    override val altAsset: Int,
):Clothing(name, heatValue, imageAsset, price, altAsset){

}
//TODO add alt assets for all paint clothing

val paintLegs = Legs("Paint", 0, R.drawable.paint_legs, 10000, R.drawable.paint_legs_alt)
val pants = Legs("Pants", 12, R.drawable.legs_pants, 25, R.drawable.alt_legs_pants)
val shorts = Legs("Shorts", 25, R.drawable.legs_shorts, 15, R.drawable.alt_legs_shorts)

fun legs(): List<Legs> {
    return listOf(
        //paintLegs,
        pants,
        shorts,
    )
}