package no.uio.ifi.IN2000.team24_app.ui.quiz.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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


@Composable
fun CategoryScreen(

    categoryName: String,
    categoryScreenViewModel: CategoryScreenViewModel = viewModel(),
    onNavigateToQuestionScreen: (String?) -> Unit

    ) {

    // initialize viewmodel with category name parameter
    LaunchedEffect(categoryScreenViewModel) {

        categoryScreenViewModel.initialize(categoryName)

    }

    // get categories and questions ui state from view model
    val categoryUiState: CategoryUiState by categoryScreenViewModel.categoryUiState.collectAsState()
    val questionsUiState: QuestionsUiState by categoryScreenViewModel.questionsUiState.collectAsState()

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Row() {

            // text displaying category
            Text(

                text = "Kategori: ",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold

            )

            // display category info if category is found
            if (categoryUiState.category != null) {

                Text(

                    text = categoryUiState.category!!.category,
                    style = MaterialTheme.typography.titleLarge

                )

            }

        }

        // spacer between category text and start button
        Spacer(modifier = Modifier.height(32.dp))

        // start button navigating to question screen
        Button(

            // navigate to question screen by loading 3 random questions
            onClick = { onNavigateToQuestionScreen(questionsUiState.questions) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)

        ) {

            // start text
            Text(

                text = "Start quiz"

            )

            // forward arrow icon
            Icon(

                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Pil fram"

            )

        }

    }

}
