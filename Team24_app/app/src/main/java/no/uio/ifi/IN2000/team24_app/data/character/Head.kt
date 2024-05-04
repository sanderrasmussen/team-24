package no.uio.ifi.IN2000.team24_app.data.character

import no.uio.ifi.IN2000.team24_app.R

data class Head(
    override val name: String, override val heatValue: Int, override val imageAsset: Int, override val price: Int,
    override val altAsset: Int,
):Clothing(name, heatValue, imageAsset, price, altAsset){

}
val clothesRepo = ClothesRepository()
val paintHead = Head("Paint", 0, R.drawable.paint_head, 10000, R.drawable.paint_head_alt)
val short_hair = Head("Short Hair", 25, R.drawable.head_short_hair, 30, R.drawable.alt_head_short_hair)
suspend fun heads(): List<Head> {

    return clothesRepo.getAllOwnedHeads()

}
