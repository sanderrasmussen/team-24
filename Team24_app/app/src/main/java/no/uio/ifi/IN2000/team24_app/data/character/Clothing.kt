package no.uio.ifi.IN2000.team24_app.data.character

abstract class Clothing (
    open val name:String,
    open val heatValue:Double,
    open val imageAsset: Int,
    open val price: Int,
    val unlocked: Boolean = false
 )