package no.uio.ifi.IN2000.team24_app.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Clothes(
    @PrimaryKey val imageAsset: Int,
    val name:String,
    val heatValue: Int,

    val price: Int,
    val altAsset: Int,
    val unlocked: Boolean = false
)