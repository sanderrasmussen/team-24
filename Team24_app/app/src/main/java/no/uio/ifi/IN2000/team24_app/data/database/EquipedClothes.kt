package no.uio.ifi.IN2000.team24_app.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class EquipedClothes(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val equipedHead: Int,
    val equipedTorso: Int,
    val equipedLegs: Int,
    val lastLoginDate: Date,
    val temperatureAtFirstLogin: Int
)
//trenger alle euqiped , last date logged inn, temperature when logged inn first

