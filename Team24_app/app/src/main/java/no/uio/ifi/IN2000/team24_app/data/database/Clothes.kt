package no.uio.ifi.IN2000.team24_app.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity//database table
data class Clothes(
    @PrimaryKey val imageAsset: Int,
    val name:String,
    val heatValue: Int,
    val price: Int,
    val altAsset: Int,
    val bodyPart : String, //should be "legs", "torso" or "head"
    val unlocked: Boolean = false
)