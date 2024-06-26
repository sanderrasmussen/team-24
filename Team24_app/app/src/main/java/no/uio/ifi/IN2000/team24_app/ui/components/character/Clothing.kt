package no.uio.ifi.IN2000.team24_app.ui.components.character

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import no.uio.ifi.IN2000.team24_app.data.character.Character
import no.uio.ifi.IN2000.team24_app.data.character.Clothing
import no.uio.ifi.IN2000.team24_app.data.character.Head
import no.uio.ifi.IN2000.team24_app.data.character.Legs
import no.uio.ifi.IN2000.team24_app.data.character.Torso
import no.uio.ifi.IN2000.team24_app.data.character.heads
import no.uio.ifi.IN2000.team24_app.data.character.legs
import no.uio.ifi.IN2000.team24_app.data.character.torsos
import no.uio.ifi.IN2000.team24_app.data.character.writeEquippedClothesToDisk

/**
 * Composable function to display the inventory.
 * @param characterState The character to update when an item is selected.
 * @param temperature The current temperature. used to give points.
 * @param modifier The modifier to apply to the layout.
 * @see Character
 * @see Clothing
 */
@Composable
fun Inventory(characterState: MutableStateFlow<Character>,temperature: Double, modifier: Modifier = Modifier){
    var showInventory by remember { mutableStateOf(false) }
    val character = characterState.collectAsState()

    //this function has to be declared at this level so it can close the Dialog
    // Advantages of this is that 1. it is tied to the state and 2. it is passed through all levels, where closing is needed. By this i mean that ...
    //... closing is necessary on the bottom of the hierarchy when an item is clicked, but also on dialog-level when ondismiss() is called.
    // this function at the top handles all that.
    fun selectedClothing(clothing: Clothing){
        Log.d("Inventory", "Selected clothing: ${clothing.name}")
        when(clothing){ //first, change the character.
            is Head -> {
                characterState.update{
                    it.copy(head = clothing)
                }
            }
            is Torso -> {
                characterState.update{
                    it.copy(torso = clothing)
                }
            }
            is Legs -> {
                characterState.update{
                    it.copy(legs = clothing)
                }

            }
        }
        characterState.update {
            it.copy(temperature = it.findAppropriateTemp())
        }
        writeEquippedClothesToDisk(characterState.value,temperature)
        showInventory = false // then, close the dialog.

    }

    Column() {
        if (showInventory) {
            ClothingMenuCard(
                character = character,
                closeFunction = ::selectedClothing, //thats right, we doing function invocation in the parameter list
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(0.6f)
                    .padding(5.dp)
            )
        }
        Button(onClick = { showInventory = !showInventory }) {
            Icon(imageVector = Icons.Default.Face, contentDescription = "Inventory")
        }
    }
}

/**
 * Composable function to display a popup card when the inventory button is clicked.
 * @param character The character to update when an item is selected.
 * @param closeFunction The function to call when an item is selected or the dialog needs to be closed.
 * @param modifier The modifier to apply to the layout.
 * @see Character
 */
@Composable
fun ClothingMenuCard(character: State<Character>, closeFunction: (clothing: Clothing)->Unit, modifier: Modifier = Modifier) {
    Dialog(
        onDismissRequest = {
            closeFunction(character.value.head)//if the dialog is closed, the character should not change. we call it with an unchanged clothing item.
        },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),

        ) {
        Card(
            modifier = modifier,
            onClick = { },
            shape = RoundedCornerShape(16.dp),
        )
        {
            ClothingMenu(closeFunction = closeFunction, modifier = Modifier.fillMaxSize())
        }
        //TODO DISMISSBUTTON
    }
}

/**
 * Composable function to display the clothing menu grid itself.
 * @param closeFunction The function to call when an item is selected.
 * @param modifier The modifier to apply to the layout.
 */
@Composable
fun ClothingMenu(closeFunction: (clothing: Clothing) -> Unit, modifier: Modifier = Modifier){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)

    ) {
        LazyVerticalGrid(columns = GridCells.Fixed(2),) {
            item { Text(text = "Hoder:") }
            item{ Spacer(modifier = Modifier) }

            items(heads()) { head ->
                ClothingCard(clothing = head, closeFunction = closeFunction)
            }
            if(heads().size % 2 == 1) item { Spacer(modifier = Modifier) }

            item { Text(text = "Overdel:") }
            item{ Spacer(modifier = Modifier) }
            items(torsos()) { torso ->
                ClothingCard(clothing = torso, closeFunction = closeFunction)
            }
            if(torsos().size % 2 == 1) item { Spacer(modifier = Modifier) }

            item { Text(text = "Underdel:") }
            item{ Spacer(modifier = Modifier) }
            items(legs()) { leg ->
                ClothingCard(clothing = leg, closeFunction = closeFunction)
            }
            if(legs().size % 2 == 1) item { Spacer(modifier = Modifier) }
        }
    }
}
/**
 * Composable function to display a card for a clothing item.
 * @param clothing The clothing item to display.
 * @param closeFunction The function to call when the item is selected.
 * @param modifier The modifier to apply to the layout.
 */
@Composable
fun ClothingCard(
    clothing: Clothing,
    closeFunction: (clothing: Clothing) -> Unit,
    modifier: Modifier = Modifier
){
    Card(
        border = BorderStroke(1.dp, Color.Black),
        onClick = { closeFunction(clothing) },
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    )
    {

        Image(
            painter = painterResource(id = clothing.altAsset),
            contentDescription = clothing.name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        )
    }
}
