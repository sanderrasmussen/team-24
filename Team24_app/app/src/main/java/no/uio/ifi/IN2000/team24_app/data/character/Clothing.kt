package no.uio.ifi.IN2000.team24_app.data.character

import android.media.Image

abstract class Clothing (
    val name:String,
    val heatValue:Double,
    val image: Image,
    val price: Int,
    val unlocked: Boolean = false
 )