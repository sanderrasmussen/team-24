package no.uio.ifi.IN2000.team24_app.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Bank (
    @PrimaryKey val id : Int,
    val balance : Int
)