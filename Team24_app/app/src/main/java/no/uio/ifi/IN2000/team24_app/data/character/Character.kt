package no.uio.ifi.IN2000.team24_app.data.character

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.uio.ifi.IN2000.team24_app.R

data class Character(val head: Head, val torso: Torso, val legs: Legs) {

}

    @Composable
    fun Player(character: Character) {
        Column(
            modifier = Modifier
                .padding(0.dp)
                .fillMaxSize()
                .border(0.dp, color = androidx.compose.ui.graphics.Color.Black)

            ,
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {

            Image(painter = painterResource(id = character.head.imageAsset), contentDescription = "!", modifier = Modifier.padding(0.dp))
            Image(painter = painterResource(id = character.torso.imageAsset), contentDescription = "!", modifier = Modifier.padding(0.dp))
            Image(painter = painterResource(id = character.legs.imageAsset), contentDescription = "!", modifier = Modifier.padding(0.dp))
        }
    }

@Preview (showSystemUi = true)
@Composable
fun DefaultPreview() {
    val defaultHead = heads().first()
    val defaultTorso = torsos().first()
    val defaultLegs = legs().first()
    Player(Character(head = defaultHead, torso = defaultTorso, legs = defaultLegs))
}




