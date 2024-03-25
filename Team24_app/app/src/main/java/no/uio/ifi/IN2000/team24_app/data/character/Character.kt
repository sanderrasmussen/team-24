package no.uio.ifi.IN2000.team24_app.data.character

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

/**
 * Data class representing a Character in the game.
 *
 * @property head The head clothing item of the character.
 * @property torso The torso clothing item of the character.
 * @property legs The legs clothing item of the character.
 */
data class Character(var head: Head, var torso: Torso, var legs: Legs, var temperature: Int = 0) {
    init {
       findAppropriateTemp()
    }

    fun findAppropriateTemp():Int{
        return ((head.heatValue + torso.heatValue + legs.heatValue)/3).toInt()
    }
}

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
            modifier = modifier
            ,
        ) {
            //TODO change the height based on the fraction of the standard size of the image. could be done programmatically, but for now it is hardcoded
            Image(painter = painterResource(id = character.head.imageAsset), contentDescription = character.head.name, modifier = Modifier.padding(0.dp).height(100.dp).fillMaxSize())
            Image(painter = painterResource(id = character.torso.imageAsset), contentDescription = character.torso.name, modifier = Modifier.padding(0.dp).height(100.dp).fillMaxSize())
            Image(painter = painterResource(id = character.legs.imageAsset), contentDescription = character.legs.name, modifier = Modifier.padding(0.dp).height(90.dp).fillMaxSize())
        }
    }
/**
* Preview function for the Player composable.
*/
@Preview (showSystemUi = true)
@Composable
fun DefaultPreview() {
    // Create a default character and display it.
    val defaultHead = heads().first()
    val defaultTorso = torsos().first()
    val defaultLegs = legs().first()
    Player(Character(head = defaultHead, torso = defaultTorso, legs = defaultLegs))
}




