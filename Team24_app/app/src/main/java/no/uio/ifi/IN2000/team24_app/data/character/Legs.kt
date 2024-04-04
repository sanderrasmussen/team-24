package no.uio.ifi.IN2000.team24_app.data.character

import no.uio.ifi.IN2000.team24_app.R


data class Legs (
    override val name: String, override val heatValue: Int, override val imageAsset: Int, override val price: Int,
    override val altAsset: Int

):Clothing(name, heatValue, imageAsset, price, altAsset){

}
val paintLegs = Legs("Paint", 0, R.drawable.paint_legs, 10000, R.drawable.paint_legs_alt)
fun legs(): List<Legs> {
    return listOf(
        paintLegs
    )
}