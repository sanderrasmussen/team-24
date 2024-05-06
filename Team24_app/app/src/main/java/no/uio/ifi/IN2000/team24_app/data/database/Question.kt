package no.uio.ifi.IN2000.team24_app.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Question(

    @PrimaryKey val question: String,
    val categoryName: String,
    val options: List<String>,
    val correctOption: Int,
    val answered: Boolean = false

)
