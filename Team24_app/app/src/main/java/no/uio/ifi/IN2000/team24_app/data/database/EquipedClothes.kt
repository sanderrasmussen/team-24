package no.uio.ifi.IN2000.team24_app.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EquipedClothes(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val equipedHead: Int, //id
    val equipedTorso: Int, //id
    val equipedLegs: Int, // id
    val lastLoginDate: Long,    //lastLoginDate is now a millisecond timestamp since 1970, used to create a java.util.Date
    val temperatureAtLastLogin: Int
)
//need all equipped, last date logged inn, temperature when logged inn first

