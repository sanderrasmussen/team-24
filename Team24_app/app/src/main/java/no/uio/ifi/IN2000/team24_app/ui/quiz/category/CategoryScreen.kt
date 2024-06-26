package no.uio.ifi.IN2000.team24_app.ui.quiz.category


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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import no.uio.ifi.IN2000.team24_app.ui.BackgroundImage
import no.uio.ifi.IN2000.team24_app.ui.arrow
import no.uio.ifi.IN2000.team24_app.ui.backgroundColour
import no.uio.ifi.IN2000.team24_app.ui.skyColour
import no.uio.ifi.IN2000.team24_app.ui.textColour


/**
 * Composable function for the category screen for a single category
 * @param onBackPressed: function to navigate back to categories screen
 * @param categoryName: name of the category to display
 * @param categoryScreenViewModel: view model for the category screen
 * @param onNavigateToQuestionScreen: function to navigate to question screen
 * @see CategoryScreenViewModel
 * @see no.uio.ifi.IN2000.team24_app.ui.quiz.question.QuestionScreen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(

    onBackPressed: () -> Unit,
    categoryName: String,
    categoryScreenViewModel: CategoryScreenViewModel = viewModel(),
    onNavigateToQuestionScreen: (String?) -> Unit

) {

    // initialize view model with category name parameter
    LaunchedEffect(categoryScreenViewModel) {

        categoryScreenViewModel.initialize(categoryName)

    }

    // get categories and questions ui state from view model
    val categoryUiState: CategoryUiState by categoryScreenViewModel.categoryUiState.collectAsState()
    val questionsUiState: QuestionsUiState by categoryScreenViewModel.questionsUiState.collectAsState()

    // image name variable with background image that reflects time of day
    val imageName = BackgroundImage()

    // top app bar with back button to navigate back to categories screen
    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        text = "Kategori",
                        color = textColour()
                    )

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

            // content in column format
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
                        color = textColour(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold

                    )

                    Text(

                        // display category info if category is found
                        text = if (categoryUiState.category != null) categoryUiState.category!!.category
                        else "Klarte ikke å hente kategori",
                        color = textColour(),
                        style = MaterialTheme.typography.titleLarge

                    )

                }

                // spacer between category text and start button
                Spacer(modifier = Modifier.height(32.dp))

                // start button navigating to question screen
                Button(

                    // navigate to question screen by loading 3 random questions
                    onClick = { onNavigateToQuestionScreen(questionsUiState.questions) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(

                        containerColor = backgroundColour()

                    )

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

    }

}