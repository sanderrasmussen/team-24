package no.uio.ifi.IN2000.team24_app.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Bank (
    val balance : Int
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}