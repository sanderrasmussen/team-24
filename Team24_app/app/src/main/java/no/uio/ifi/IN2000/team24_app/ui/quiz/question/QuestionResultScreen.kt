package no.uio.ifi.IN2000.team24_app.ui.quiz.question

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import no.uio.ifi.IN2000.team24_app.R
import no.uio.ifi.IN2000.team24_app.ui.BackgroundImage
import no.uio.ifi.IN2000.team24_app.ui.arrow
import no.uio.ifi.IN2000.team24_app.ui.backgroundColour
import no.uio.ifi.IN2000.team24_app.ui.quiz.category.CategoryUiState
import no.uio.ifi.IN2000.team24_app.ui.skyColour
import no.uio.ifi.IN2000.team24_app.ui.textColour

/**
 * Composable function for displaying the result of a quiz category
 * @param onBackPressed: function to navigate back to categories screen
 * @param categoryName: name of the category
 * @param questions: list of questions
 * @param coinsWon: number of coins won
 * @param questionResultScreenViewModel: view model for the question result screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionResultScreen(

    onBackPressed: () -> Unit,
    categoryName: String,
    questions: String,
    coinsWon: Int,
    questionResultScreenViewModel: QuestionResultScreenViewModel = viewModel()

) {

    println("Coins won result: $coinsWon")

    // convert question list string to actual list of strings
    val questionList = questions.split(",")

    // image name variable with background image that reflects time of day
    val imageName = BackgroundImage()

    // initialize view model with question list, index and category name parameter
    LaunchedEffect(questionResultScreenViewModel) {

        questionResultScreenViewModel.initialize(questionList, categoryName, coinsWon)

    }

    // get category and bank balance ui state from view model
    val categoryUiState: CategoryUiState by questionResultScreenViewModel.categoryUiState.collectAsState()
    val balanceUiState: BalanceUiState by questionResultScreenViewModel.balanceUiState.collectAsState()

    // top app bar with back button to navigate back to categories screen
    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text(text = "Quizresultater",
                        color = textColour())

                },

                navigationIcon = {

                    // icon button that goes back to categories screen
                    Box(modifier = Modifier.clickable { onBackPressed() }) {
                        // back arrow icon
                        arrow(30)
                    }

                },
                // set color of top app bar to reflect background
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = skyColour())

            )

        }

    ) { innerPadding ->

        Box(

            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()

        ) {

            // background with background image
            Image(

                painter = (painterResource(id = imageName)),
                contentDescription = "Background Image based on time of the day",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.matchParentSize()

            )

            Column(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {

                if (categoryUiState.category != null) {

                    val maxCoins = (questionList.size) * 10 // categoryUiState.category!!.points

                    // text displaying completion of category
                    Text(

                        text = "${categoryUiState.category!!.category} fullført!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = textColour()

                    )

                    // spacer between category text and point score text
                    Spacer(modifier = Modifier.height(32.dp))

                    // row for displaying point score
                    Row() {

                        Text(

                            text = "Poengscore: ",
                            fontWeight = FontWeight.Bold,
                            color = textColour()

                        )

                        // text displaying point score
                        Text(
                            color = textColour(),
                            text = "$coinsWon/$maxCoins"

                        )

                    }

                } else {

                    // display error message on failure
                    Text("Klarte ikke å hente kategori",)

                }

                // spacer between point score text and total coins text
                Spacer(modifier = Modifier.height(32.dp))

                Row() {

                    // text displaying total coins
                    Text(

                        text = "Totale penger: ",
                        fontWeight = FontWeight.Bold,
                        color = textColour()

                    )

                    // image for weather currency
                    Image(

                        painter = painterResource(id = R.drawable.coin),
                        contentDescription = "currency",
                        modifier = Modifier.size(30.dp)

                    )

                    // text displaying point score
                    Text(

                        text = "${balanceUiState.balance}",
                        color = textColour()

                    )

                }

                // spacer between total coins text and back button
                Spacer(modifier = Modifier.height(32.dp))

                // button for going back to categories screen
                Button(

                    onClick = onBackPressed,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(

                        containerColor = backgroundColour()

                    )

                ) {

                    Text("Tilbake")

                }

            }

        }

    }

}