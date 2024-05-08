package no.uio.ifi.IN2000.team24_app.ui.quiz.question

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import no.uio.ifi.IN2000.team24_app.ui.quiz.category.CategoryUiState

@Composable
fun QuestionResultScreen(

    categoryName: String,
    questions: List<String>,
    coinsWon: Int,
    questionResultScreenViewModel: QuestionResultScreenViewModel = viewModel()

) {

    // initialize viewmodel with question list, index and category name parameter
    LaunchedEffect(questionResultScreenViewModel) {

        questionResultScreenViewModel.initialize(questions, categoryName, coinsWon)

    }

    // get category and bank balance ui state from view model
    val categoryUiState: CategoryUiState by questionResultScreenViewModel.categoryUiState.collectAsState()
    val balanceUiState: BalanceUiState by questionResultScreenViewModel.balanceUiState.collectAsState()

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        if (categoryUiState != null) {

            val maxCoins = (questions.size - 1) * categoryUiState.category!!.points

            // text displaying completion of category
            Text(

                text = "${categoryUiState.category!!.category} fullført!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold

            )

            // spacer between category text and point score text
            Spacer(modifier = Modifier.height(32.dp))

            // row for displaying point score
            Row() {

                Text(

                    text = "Poengscore: ",
                    fontWeight = FontWeight.Bold

                )

                // text displaying point score
                Text(

                    text = "$coinsWon/$maxCoins"

                )

            }

        }

        if (balanceUiState != null) {

            // spacer between point score text and total coins text
            Spacer(modifier = Modifier.height(32.dp))

            Row() {

                Text(

                    text = "Totale poeng: ",
                    fontWeight = FontWeight.Bold

                )

                // text displaying point score
                Text(

                    text = "${balanceUiState.balance}"

                )

            }

            // spacer between total coins text and back button
            Spacer(modifier = Modifier.height(32.dp))

            // button for going back to categories screen
            Button(

                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)

            ) {

                Text("Tilbake")

            }

        }

    }

}