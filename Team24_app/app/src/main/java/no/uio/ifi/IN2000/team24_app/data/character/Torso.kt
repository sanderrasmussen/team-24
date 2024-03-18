package no.uio.ifi.IN2000.team24_app.data.character

import no.uio.ifi.IN2000.team24_app.R

data class Torso (
    override val name: String, override val heatValue: Double, override val imageAsset: Int, override val price: Int
):Clothing(name, heatValue, imageAsset, price){

}
val paintTorso = Torso("Paint", 0.5, R.drawable.paint_torso, 0)
fun torsos(): List<Torso> {
    return listOf(
        paintTorso
    )
}