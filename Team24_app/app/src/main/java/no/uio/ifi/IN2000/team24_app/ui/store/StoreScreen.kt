package no.uio.ifi.IN2000.team24_app.ui.store

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

class StoreScreen(viewModel: StoreScreenViewModel) {

    val hodeplagg by viewModel.hodePlagg.collectAsState()
    val overDeler by viewModel.overdeler.collectAsState()
    val plaggBukser by viewModel.bukser.collectAsState()




    @Composable
    fun GridView(viewModel: StoreScreenViewModel) {
        Column {
            // Display cash available
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Cash available: 500 NOK", // Replace with a variable
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Display hodePlagg (heads)
            Text("HodePlagg:")
            hentInfoPlagg(hodeplagg)

            // Display overdeler (torsos)
            Text("Overdeler:")
            hentInfoPlagg(overDeler)

            // Display bukser (legs)
            Text("Bukser:")
            hentInfoPlagg(plaggBukser)
        }
    }

        @Composable
        fun hentInfoPlagg(listePlagg:List<Clothing>) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),

                //columns = GridCells.Adaptive(minSize = 200.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                content = {
                    items(listePlagg) { plagg ->
                        Card(
                            modifier = Modifier.padding(8.dp)

                        ) {
                            Column(
                                modifier = Modifier.padding(5.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center

                            ) {
                                Box {
                                    AsyncImage(
                                        plagg.altAsset,
                                        contentScale = ContentScale.Crop,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight()
                                            .size(150.dp)
                                    )

                                }
                                Spacer(modifier = Modifier.height(9.dp))
                                Text(
                                    text = "Pris:" + plagg.price.toString(),
                                    fontSize = 24.sp,
                                    modifier = Modifier.padding(4.dp),
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            )
        }
        }





    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar() {
        TopAppBar(title = {
            Text(
                text = "Store",
                fontSize = 40.sp,
                textAlign = TextAlign.Center
            )
        })
    }
}