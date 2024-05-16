package no.uio.ifi.IN2000.team24_app.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity //database table
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val question: String,
    val categoryName: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    var answered: Boolean = false
)
