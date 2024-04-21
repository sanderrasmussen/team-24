package no.uio.ifi.IN2000.team24_app.data.character

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

abstract class Clothing(
    open val name:String,
    open val heatValue: Int,
    open val imageAsset: Int,
    open val price: Int,
    open val altAsset: Int,
    val unlocked: Boolean = false
 )


fun writeClothesToDisk(character: Character){
    //TODO, IMPORTANT! THIS SHOULD CALL A SEPARATE ASYNC-METHOD, TO WRITE ON AN IO-THREAD
}

@Composable
fun Inventory(characterState:MutableStateFlow<Character>, modifier:Modifier=Modifier){
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
        showInventory = false // then, close the dialog.
    }

    Column() {
        if (showInventory) {
            ClothingMenuCard(
                character = character, closeFunction = ::selectedClothing , modifier = Modifier    //thats right, we doing function invocation in the parameter list
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(0.6f)
                    .padding(5.dp))
        }
        Button(onClick = { showInventory = !showInventory }) {
            Icon(imageVector = Icons.Default.Face, contentDescription = "Inventory")
        }
    }
}


@Composable
fun ClothingMenuCard(character: State<Character>, closeFunction: (clothing:Clothing)->Unit, modifier: Modifier = Modifier) {
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

@Composable
fun ClothingMenu(closeFunction: (clothing: Clothing) -> Unit, modifier: Modifier = Modifier){
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp)

        ) {
            Text(text = "Heads:")
            LazyVerticalGrid(columns = GridCells.Fixed(2),) {
                items(heads()) { head ->
                    ClothingCard(clothing = head, closeFunction = closeFunction)

                }
            }
            Text(text = "Tops:")
            LazyVerticalGrid(columns = GridCells.Fixed(2),) {
                items(torsos()) { torso ->
                    ClothingCard(clothing = torso, closeFunction = closeFunction)
                }
            }
            Text(text = "Bottoms:")

            LazyVerticalGrid(columns = GridCells.Fixed(2),) {
                items(legs()) { leg ->
                    ClothingCard(clothing = leg, closeFunction = closeFunction)
                }
            }
        }
    }

@Composable
fun ClothingCard(
    clothing: Clothing,
    closeFunction: (clothing: Clothing) -> Unit,
    modifier: Modifier = Modifier){
    Card(
        border = BorderStroke(1.dp, Color.Black),
        onClick = { closeFunction(clothing)},
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

@Preview(showSystemUi = true)
@Composable
fun InventoryPreview() {
    Inventory(MutableStateFlow(Character(heads().first(), torsos().first(), legs().first())))
}



