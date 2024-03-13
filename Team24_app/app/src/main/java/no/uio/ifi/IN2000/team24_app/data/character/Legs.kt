package no.uio.ifi.IN2000.team24_app.data.character

import android.media.Image

abstract class Legs (
    name: String, heatValue: Double, image: Image, price: Int
):Clothing(name, heatValue, image, price)