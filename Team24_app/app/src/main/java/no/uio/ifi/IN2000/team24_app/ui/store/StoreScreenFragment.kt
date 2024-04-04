package no.uio.ifi.IN2000.team24_app.ui.store

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import io.ktor.websocket.Frame
import no.uio.ifi.IN2000.team24_app.R
import no.uio.ifi.IN2000.team24_app.data.character.Clothing

class StoreScreenFragment: Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            return ComposeView(requireContext()).apply {
                setContent {
                    StoreScreenContent()
                }
            }
        }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun StoreScreenContent() {
        val sharedViewModel = remember {
            ViewModelProvider(requireActivity()).get(SharedBankViewModel::class.java)
        }
        val viewModel = remember { StoreScreenViewModel() }

        Scaffold(
            topBar = { TopBar() },
            content = {
                Box(modifier = Modifier.padding(top = 60.dp)) {
                    GridView(viewModel, sharedBankViewModel)
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


        Column {
            // Display cash available
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                // Image of money
                Image(
                    painter = painterResource(id = R.drawable.coin),
                    contentDescription = "Money",
                    modifier = Modifier.size(100.dp)
                )

                Text(
                    text = "Cash available: 500 NOK", // Byttes ut med en variabel.
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .shadow(10.dp, shape = CircleShape, clip = true)
                        .background(brush = Brush.horizontalGradient(listOf(Color.Black, Color.LightGray)), shape = CircleShape)
                        .clip(shape = CircleShape)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Display hodePlagg (heads)
            Text("HODEPLAGG:", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            HentInfoPlagg(viewModel,hodeplagg)

            // Display overdeler (torsos)
            Text("OVERDELER:",  fontSize = 20.sp, fontWeight = FontWeight.Bold)
            HentInfoPlagg(viewModel,overDeler)

            // Display bukser (legs)
            Text("BUKSER:",  fontSize = 20.sp, fontWeight = FontWeight.Bold)
            HentInfoPlagg(viewModel,plaggBukser)
        }
    }



    @Composable
    fun HentInfoPlagg(viewModel: StoreScreenViewModel, listePlagg:List<Clothing>) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),

            horizontalArrangement = Arrangement.spacedBy(8.dp),
            content = {
                items(listePlagg) { plagg ->
                    Card(
                        modifier = Modifier.padding(8.dp)
                            .clickable {
                                /*
                                if (plagg.price <= viewModel.getCurrentSum()) {
                                    viewModel.subtractMoney(plagg.price)
                                    viewModel.unlockPlagg(plagg)
                                }

                                 */

                            }
                    )
                    {
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