package no.uio.ifi.IN2000.team24_app.data.character

import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import coil.compose.SubcomposeAsyncImage
import no.uio.ifi.IN2000.team24_app.R

data class Character(val head: Head, val torso: Torso, val legs: Legs) {

    @Composable
    fun Player() {
        LazyColumn {

            SubcomposeAsyncImage(
                model = R.drawable.
            )


        }
    }
}