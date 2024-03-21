package no.uio.ifi.IN2000.team24_app.data.character

import no.uio.ifi.IN2000.team24_app.R

data class Torso (
    override val name: String, override val heatValue: Double, override val imageAsset: Int, override val price: Int,
    override val altAsset: Int
):Clothing(name, heatValue, imageAsset, price, altAsset){

}
val paintTorso = Torso("Paint", 0.5, R.drawable.paint_torso, 10000, R.drawable.paint_torso_alt)
fun torsos(): List<Torso> {
    return listOf(
        paintTorso
    )
}