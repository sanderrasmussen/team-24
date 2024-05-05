package no.uio.ifi.IN2000.team24_app.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class EquipedClothes(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val equipedHead: Int, //id
    val equipedTorso: Int, //id
    val equipedLegs: Int, // id
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP") val lastLoginDate: Date,
    val temperatureAtFirstLogin: Int
)
//trenger alle euqiped , last date logged inn, temperature when logged inn first

