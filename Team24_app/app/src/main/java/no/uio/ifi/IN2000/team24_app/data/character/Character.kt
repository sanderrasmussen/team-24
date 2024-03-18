package no.uio.ifi.IN2000.team24_app.data.character

import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable

data class Character(val head: Head, val torso: Torso, val legs: Legs) {

    @Composable
    fun Player() {
        LazyColumn {
        }
    }
}