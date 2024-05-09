package no.uio.ifi.IN2000.team24_app.ui.components.character

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.uio.ifi.IN2000.team24_app.data.character.Character
import no.uio.ifi.IN2000.team24_app.data.character.heads
import no.uio.ifi.IN2000.team24_app.data.character.legs
import no.uio.ifi.IN2000.team24_app.data.character.torsos

/**
 * Composable function to display the character.
 *
 * @param character The character to display.
 * @param modifier The modifier to apply to the layout.
 */
    @Composable
    fun Player(character: Character, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        //TODO change the height based on the fraction of the standard size of the image. could be done programmatically, but for now it is hardcoded
        Image(
            painter = painterResource(id = character.head.imageAsset),
            contentDescription = character.head.name,
            modifier = Modifier
                .padding(0.dp)
                .height(100.dp)
                .fillMaxSize()
        )
        Image(
            painter = painterResource(id = character.torso.imageAsset),
            contentDescription = character.torso.name,
            modifier = Modifier
                .padding(0.dp)
                .height(100.dp)
                .fillMaxSize()
        )
        Image(
            painter = painterResource(id = character.legs.imageAsset),
            contentDescription = character.legs.name,
            modifier = Modifier
                .padding(0.dp)
                .height(88.dp)
                .fillMaxSize()
        )
    }
    }

/**
* Preview function for the Player composable.
*/
@Preview(showSystemUi = true)
@Composable
fun DefaultPreview() {
    // Create a default character and display it.
    val defaultHead = heads().first()
    val defaultTorso = torsos().first()
    val defaultLegs = legs().first()
    Player(Character(head = defaultHead, torso = defaultTorso, legs = defaultLegs))
}