package no.uio.ifi.IN2000.team24_app.data.character

import android.media.Image

abstract class Torso (
    name: String, heatValue: Double, image: Image, price: Int
):Clothing(name, heatValue, image, price)