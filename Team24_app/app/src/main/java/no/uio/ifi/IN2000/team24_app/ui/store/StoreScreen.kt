package no.uio.ifi.IN2000.team24_app.ui.store

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import no.uio.ifi.IN2000.team24_app.data.character.Clothing
import no.uio.ifi.IN2000.team24_app.R
import no.uio.ifi.IN2000.team24_app.data.character.Character
import no.uio.ifi.IN2000.team24_app.data.character.Head
import no.uio.ifi.IN2000.team24_app.data.character.Legs
import no.uio.ifi.IN2000.team24_app.data.character.Player
import no.uio.ifi.IN2000.team24_app.data.character.Torso

class StoreScreen {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Preview
    @Composable
    fun StoreScreenPreview() {
        val viewModel = StoreScreenViewModel()
        Scaffold(
            topBar = { TopBar() },
            content = {
                Box(modifier = Modifier) {
                    GridView(viewModel)
                }
            }
        )
    }






    @SuppressLint("NotConstructor")
    @Composable

    fun GridView(viewModel: StoreScreenViewModel) {
        val hodeplagg by viewModel.hodePlagg.collectAsState()
        val overDeler by viewModel.overdeler.collectAsState()
        val plaggBukser by viewModel.bukser.collectAsState()
        val blue = Color(android.graphics.Color.parseColor("#ADD8E6"))
        val character by viewModel.characterStateStore.collectAsState()


        val allClothingList by remember {
            derivedStateOf {
                hodeplagg + overDeler + plaggBukser
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(blue)
                .padding(16.dp)
        ) {
            // Display cash available
            item {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 45.dp)
                        .background(Color(android.graphics.Color.parseColor("#DCF6FF")))
                ) {
                    Player(character = character, modifier = Modifier.size(100.dp))
                }

            }


            // Display all clothing items
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "MOTEARTIKLER",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                HentInfoPlagg(viewModel, allClothingList)
            }
        }
    }





    @Composable
    fun HentInfoPlagg(viewModel: StoreScreenViewModel, listePlagg:List<Clothing>) {
        val scrollState = rememberScrollState()
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(listePlagg) { plagg ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)

                        .clickable {
                            selectedClothingStore(plagg, viewModel)
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .padding(3.dp)
                            .background(Color(android.graphics.Color.parseColor("#FFFFFF"))),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(modifier = Modifier.size(150.dp)) {
                            AsyncImage(
                                plagg.altAsset,
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                //.wrapContentHeight()
                                //.height(100.dp)


                            )
                            if (!plagg.unlocked) {
                                Icon(
                                    painter = painterResource(id = R.drawable.lock_outline_filled),
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .size(30.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(9.dp))
                        Column(modifier = Modifier.fillMaxSize()) {
                            // Inneholdet av kortet (teksten)

                             val showAlertMessage = remember{ mutableStateOf(false) }

                            if(showAlertMessage.value){
                                SimpleAlertDialog(onDismissRequest = { showAlertMessage.value = false })
                            }

                            // Knappen nederst i kortet
                            Button(
                                onClick = {
                                    showAlertMessage.value = true
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Black
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                            ) {
                                Text(
                                    text = "Pris: ${plagg.price} NOK",
                                    fontSize = 24.sp,
                                    modifier = Modifier.padding(4.dp),
                                    color = Color.Black
                                )                            }
                        }
                    }
                }
            }
        }}


    @Composable
    fun SimpleAlertDialog(onDismissRequest: () -> Unit) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(onClick = {
                    /*
                      if (plagg.price <= viewModel.getCurrentSum()) {
                          viewModel.subtractMoney(plagg.price)
                          viewModel.unlockPlagg(plagg)
                      }
                      */
                })
                { Text(text = "OK") }
            },
            dismissButton = {
                TextButton(onClick = {})
                { Text(text = "Cancel") }
            },
            title = { Text(text = "Vennligst bekreft") },
            text = { Text(text = "Ønsker du å kjøpe plagget?") }
        )
    }

        fun selectedClothingStore(clothing: Clothing, viewModel:StoreScreenViewModel) {
            val price = clothing.price
            //val userBankBalance = BankBalance()

            //if(price<=userBankBalance){
            when (clothing) { //first, change the character.
                is Head -> {
                    viewModel.characterStateStore.update {
                        it.copy(head = clothing)
                    }
                }

                is Torso -> {
                    viewModel.characterStateStore.update {
                        it.copy(torso = clothing)
                    }
                }

                is Legs -> {
                    viewModel.characterStateStore.update {
                        it.copy(legs = clothing)
                    }

                }
            }
           // }
            /*
            else{
                Log.d("InventoryStore", "Insufficient funds to purchase ${clothing.name}")

            }

             */

        }




    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar() {
        TopAppBar(title = {
            Text(
                text = "Butikk",
                fontSize = 25.sp,
                textAlign = TextAlign.Center
            )
        })

    }


}