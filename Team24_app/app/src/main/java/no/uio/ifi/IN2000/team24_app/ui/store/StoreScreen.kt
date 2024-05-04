package no.uio.ifi.IN2000.team24_app.ui.store

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.IN2000.team24_app.data.character.Clothing
import no.uio.ifi.IN2000.team24_app.R
import no.uio.ifi.IN2000.team24_app.data.character.Character
import no.uio.ifi.IN2000.team24_app.data.character.Head
import no.uio.ifi.IN2000.team24_app.data.character.Legs
import no.uio.ifi.IN2000.team24_app.data.character.Player
import no.uio.ifi.IN2000.team24_app.data.character.Torso
import no.uio.ifi.IN2000.team24_app.data.locationForecast.WeatherDetails
import no.uio.ifi.IN2000.team24_app.ui.Components
import java.time.LocalTime

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun StoreScreen(navController: NavController) {
        val c = Components()
        val viewModel = StoreScreenViewModel()

        val backgroundImage = c.BackgroundImage()

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = backgroundImage),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.matchParentSize(),
            )

            Column (
                modifier = Modifier.fillMaxSize()

            ){
                    Box(modifier = Modifier.weight(1f)) {
                        GridView(viewModel = viewModel)
                    }
                    c.NavBar(navController)
            }

        }
    }





    /*

    fun StoreScreenPreview() {
        val viewModel = StoreScreenViewModel()
        Column {
            Box(modifier = Modifier.weight(1f)) {
                GridView(viewModel = viewModel)
            }
            NavBar()
        }
    }

     */




    @SuppressLint("NotConstructor")
    @Composable
    @RequiresApi(Build.VERSION_CODES.O)
    fun GridView(viewModel: StoreScreenViewModel) {
        val hodeplagg by viewModel.hodePlagg.collectAsState()
        val overDeler by viewModel.overdeler.collectAsState()
        val plaggBukser by viewModel.bukser.collectAsState()
        val character by viewModel.characterStateStore.collectAsState()

        val currentSum by viewModel.currentSum.collectAsState()

        val textColour = when (LocalTime.now().hour) {
            in 6 until 22 -> Color.Black
            else -> Color.White
        }


        LaunchedEffect(viewModel) {
          viewModel.getCurrentSum()
        }

        val allClothingList by remember(viewModel.hodePlagg, viewModel.overdeler, viewModel.bukser) {
            derivedStateOf {
                hodeplagg + overDeler + plaggBukser
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                    //.background(blue)
                .padding(16.dp)
        ) {

            item {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.coin),
                        contentDescription = "currency",
                        modifier = Modifier.size(40.dp)
                    )
                    Text(
                        text = "${currentSum}",
                        color = textColour,
                        fontSize = 30.sp
                    )
                }
            }


            // Display cash available
            item {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(400.dp)
                        //.padding(horizontal = 45.dp)
                    //.background(Color(android.g
                   //raphics.Color.parseColor("#DCF6FF")))
                ) {
                    Player(character = character, modifier = Modifier.fillMaxSize())
                }

            }


            // Display all clothing items
            item {
                Spacer(modifier = Modifier.height(35.dp))
                /*Text(
                    "MOTEARTIKLER",
                    horizontalArrangement = Arrangement.Center,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )*/
                HentInfoPlagg(viewModel, allClothingList, currentSum)
            }
        }
    }


    @Composable
    fun HentInfoPlagg(viewModel: StoreScreenViewModel,  allClothingList: List<Clothing>,
    currentSum: Int?) {
        val scrollState = rememberScrollState()
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(allClothingList) { plagg ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)

                        .clickable {
                            selectedClothingStore(plagg, viewModel, currentSum)
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

                            val showAlertMessage = remember { mutableStateOf(false) }

                            if (showAlertMessage.value) {
                                SimpleAlertDialog(plagg, viewModel, onDismissRequest = {
                                    showAlertMessage.value = false }, currentSum)
                            }

                            // Knappen nederst i kortet
                            Button(
                                onClick = {
                                    showAlertMessage.value = true
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(android.graphics.Color.parseColor("#47C947"))
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                shape = RoundedCornerShape(0.dp)

                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.coin),
                                    contentDescription = "currency",
                                    modifier = Modifier.size(40.dp)
                                )
                                Text(
                                    text = "${plagg.price}",
                                    fontSize = 26.sp,
                                    modifier = Modifier.padding(4.dp),
                                    color = Color.Black
                                )

                            }
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun SimpleAlertDialog(
        plagg: Clothing,
        viewModel: StoreScreenViewModel,
        onDismissRequest: () -> Unit,
        currentSum: Int?
    ) {
        val showAlertMessage = remember { mutableStateOf(true) }

        if (showAlertMessage.value) {
            AlertDialog(
                onDismissRequest = { onDismissRequest() }, // Call onDismissRequest when dismiss button is clicked
                confirmButton = {
                    ConfirmButton(
                        plagg = plagg,
                        viewModel = viewModel,
                        currentSum = currentSum,
                        onDismissRequest = onDismissRequest
                    )
                },
                dismissButton = {
                    TextButton(onClick = {
                        //Kaller paa onDismissRequest naar cancel knappen er trykket paa
                        onDismissRequest()
                    }) {
                        Text(text = "Cancel")
                    }
                },
                title = { Text(text = "Vennligst bekreft") },
                text = { Text(text = "Ønsker du å kjøpe plagget: ${plagg.name}?") }
            )
        }
    }


    @Composable
    private fun ConfirmButton(
        plagg: Clothing,
        viewModel: StoreScreenViewModel,
        currentSum: Int?,
        onDismissRequest: () -> Unit
    ) {
        val coroutineScope = rememberCoroutineScope()

        TextButton(onClick = {
            // Perform action when confirm button is clicked
            if (plagg.price <= currentSum!!) {
                coroutineScope.launch {
                    viewModel.subtractMoney(plagg.price)
                    viewModel.unlockPlagg(plagg)
                    onDismissRequest()
                }
            }
            onDismissRequest()
        }) {
            Text(text = "OK")
        }
    }




        fun selectedClothingStore(clothing: Clothing, viewModel:StoreScreenViewModel, currentSum: Int?) {
            val price = clothing.price

            if(price<= currentSum!!){
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
           }

            else{
                Log.d("InventoryStore", "Insufficient funds to purchase ${clothing.name}")

            }



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


