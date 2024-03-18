package no.uio.ifi.IN2000.team24_app.data.character

import no.uio.ifi.IN2000.team24_app.R

data class Legs (
    override val name: String, override val heatValue: Double, override val imageAsset: Int, override val price: Int
):Clothing(name, heatValue, imageAsset, price){

}
val paintLegs = Legs("Paint", 0.5, R.drawable.paint_legs, 0)
fun legs(): List<Legs> {
    return listOf(
        paintLegs
    )
}