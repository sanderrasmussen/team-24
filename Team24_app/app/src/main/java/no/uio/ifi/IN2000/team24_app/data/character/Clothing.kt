package no.uio.ifi.IN2000.team24_app.data.character

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun ClothingMenuCard(modifier: Modifier = Modifier){
   ElevatedCard(
       elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
       modifier = modifier
   ) {
    ClothingMenu()


   }
}
fun writeClothesToDisk(character: Character){

}

@Composable
fun ClothingMenu(modifier: Modifier = Modifier){
    Column(
        modifier = modifier.width(100.dp)

    ) {
        Text(text = "Heads:")
        LazyVerticalGrid(columns = GridCells.Fixed(2),){
            items(heads()) { head ->
                Image(painter = painterResource(id = head.altAsset, ), contentDescription = head.name)
            }
        }
            Text(text = "Tops:")
        LazyVerticalGrid(columns = GridCells.Fixed(2),){
        items(torsos()) { torso ->
            Image(painter = painterResource(id = torso.altAsset, ), contentDescription = torso.name)
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
@Preview(showSystemUi = true)

@Composable
fun ClothingMenuCardPreview() {
    ClothingMenuCard()
}


@Preview(showBackground = true)
@Composable
fun ClothingMenuPreview() {
    ClothingMenu()
}