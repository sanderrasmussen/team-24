package no.uio.ifi.IN2000.team24_app.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Category(

    @PrimaryKey val category: String,
    var lastDateAnswered: Date? = null,
    var points: Int = 15

)
