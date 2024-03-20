package no.uio.ifi.IN2000.team24_app.data.character

import no.uio.ifi.IN2000.team24_app.R

data class Head (
    override val name: String, override val heatValue: Double, override val imageAsset: Int, override val price: Int,
    override val altAsset: Int,
):Clothing(name, heatValue, imageAsset, price, altAsset){

}

val paintHead = Head("Paint", 0.5, R.drawable.paint_head, 10000, R.drawable.paint_head_alt)


fun heads(): List<Head> {
    return listOf(
        paintHead
    )
}