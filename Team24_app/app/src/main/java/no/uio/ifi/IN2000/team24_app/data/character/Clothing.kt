package no.uio.ifi.IN2000.team24_app.data.character

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

abstract class Clothing (
    open val name:String,
    open val heatValue:Double,
    open val imageAsset: Int,
    open val price: Int,
    open val altAsset: Int,
    val unlocked: Boolean = false
 )

@Composable
fun Inventory(character:Character, modifier:Modifier=Modifier){
    var showInventory by remember { mutableStateOf(false) }
    Column() {
        Button(onClick = { showInventory = !showInventory }) {
            Icon(imageVector = Icons.Default.Face, contentDescription = "Inventory")

        }
        if (showInventory) {
            ClothingMenuCard(character = character, modifier = Modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.6f)
                .padding(5.dp))
        }
        Button(onClick = { showInventory = !showInventory }) {
            Icon(imageVector = Icons.Default.Face, contentDescription = "Inventory")
        }
    }
}


fun writeClothesToDisk(character: Character){

}

@Composable
fun ClothingMenuCard(character:Character, modifier: Modifier = Modifier) {
    Dialog(
        onDismissRequest = {writeClothesToDisk(character)}
    ) {
        Card(
            modifier = modifier,
            onClick = { },
            shape = RoundedCornerShape(16.dp),
        )
        {
            ClothingMenu(character =character)
        }
    }
}

@Composable
fun ClothingMenu(character: Character, modifier: Modifier = Modifier){
        Column(
            modifier = modifier.width(100.dp)

        ) {
            Text(text = "Heads:")
            LazyVerticalGrid(columns = GridCells.Fixed(2),) {
                items(heads()) { head ->
                    Image(
                        painter = painterResource(id = head.altAsset,),
                        contentDescription = head.name
                    )
                }
            }
            Text(text = "Tops:")
            LazyVerticalGrid(columns = GridCells.Fixed(2),) {
                items(torsos()) { torso ->
                    Image(
                        painter = painterResource(id = torso.altAsset,),
                        contentDescription = torso.name
                    )
                }
            }
            Text(text = "Bottoms:")

            LazyVerticalGrid(columns = GridCells.Fixed(2),) {
                items(legs()) { leg ->
                    Image(
                        painter = painterResource(id = leg.altAsset,),
                        contentDescription = leg.name
                    )
                }
            }
        }
    }

val defaultCharacter = Character(heads().first(), torsos().first(), legs().first())
@Preview(showSystemUi = true)
@Composable
fun InventoryPreview() {
    Inventory(defaultCharacter)}



@Preview(showBackground = true)
@Composable
fun ClothingMenuPreview() {
    ClothingMenu(defaultCharacter)
}