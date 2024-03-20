package no.uio.ifi.IN2000.team24_app.data.character

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog

abstract class Clothing (
    open val name:String,
    open val heatValue:Double,
    open val imageAsset: Int,
    open val altAsset: Int,
    open val price: Int,
    val unlocked: Boolean = false,
 )

@Composable
fun ClothingMenuCard(){
    /*
    Dialog(
        onDismissRequest = { writeClothesToDisk()}

    )
     */
}
fun writeClothesToDisk(character: Character){

}

@Composable
fun ClothingMenu(){
    Column(

    ) {
            Text(text = "Heads:")
        LazyVerticalGrid(columns = GridCells.Fixed(2),){
            items(heads()) { head ->
                Image(painter = painterResource(id = head.imageAsset, ), contentDescription = head.name)
            }
        }
            Text(text = "Tops:")
        LazyVerticalGrid(columns = GridCells.Fixed(2),){
        items(torsos()) { torso ->
            Image(painter = painterResource(id = torso.imageAsset, ), contentDescription = torso.name)
        }
    }
            Text(text = "Bottoms:")

        LazyVerticalGrid(columns = GridCells.Fixed(2),) {
            items(legs()) { leg ->
                Image(
                    painter = painterResource(id = leg.imageAsset,),
                    contentDescription = leg.name
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun clothingMenuPreview() {
    ClothingMenu()
}