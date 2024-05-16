package no.uio.ifi.IN2000.team24_app.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity //database table
data class Category(

    @PrimaryKey val category: String,
    var points: Int = 15,
    var shouldStartTimer: Boolean = true,
    var lastDateAnswered: Date? = null

)
