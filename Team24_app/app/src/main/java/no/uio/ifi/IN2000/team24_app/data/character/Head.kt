package no.uio.ifi.IN2000.team24_app.data.character

import no.uio.ifi.IN2000.team24_app.R

data class Head (
    override val name: String, override val heatValue: Double, override val imageAsset: Int, override val price: Int
):Clothing(name, heatValue, imageAsset, price){

}

val paintHead = Head("Paint", 0.5, R.drawable.paint_head, 0)


fun heads(): List<Head> {
    return listOf(
        paintHead
    )
}