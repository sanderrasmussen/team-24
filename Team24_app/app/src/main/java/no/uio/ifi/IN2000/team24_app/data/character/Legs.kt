package no.uio.ifi.IN2000.team24_app.data.character

import no.uio.ifi.IN2000.team24_app.R


data class Legs (
    override val name: String, override val heatValue: Int, override val imageAsset: Int, override val price: Int,
    override val altAsset: Int

):Clothing(name, heatValue, imageAsset, price, altAsset){

}
val pants = Legs("Pants", 0, R.drawable.legs_pants, 350, R.drawable.alt_legs_pants)
val legsShorts = Legs("Shorts", 0, R.drawable.legs_shorts, 150, R.drawable.alt_legs_shorts)
val shorts = Legs("Shorts", 0, R.drawable.legs_shorts, 150, R.drawable.alt_legs_shorts)

fun legs(): List<Legs> {
    return listOf(
        pants,
        legsShorts

    )
}