package no.uio.ifi.IN2000.team24_app.data.character

import no.uio.ifi.IN2000.team24_app.R

data class Torso (
    override val name: String, override val heatValue: Int, override val imageAsset: Int, override val price: Int,
    override val altAsset: Int,
):Clothing(name, heatValue, imageAsset, price, altAsset){

}
//TODO add alt assets for all paint clothing

val paintTorso = Torso("Paint", 0, R.drawable.paint_torso, 10000, R.drawable.paint_torso_alt)
//todo reimport change id from sleeves to sleeve
val long_sleeve = Torso("Long Sleeve", 12, R.drawable.torso_long_sleeves, 25, R.drawable.alt_torso_long_sleeve)
val short_sleeve = Torso("Short Sleeve", 25, R.drawable.torso_short_sleeves, 15, R.drawable.alt_torso_short_sleeve)

fun torsos(): List<Torso> {
    return listOf(
        //paintTorso,
        long_sleeve,
        short_sleeve
    )
}