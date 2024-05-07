package no.uio.ifi.IN2000.team24_app.ui.quiz.question

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import no.uio.ifi.IN2000.team24_app.data.database.Question
import no.uio.ifi.IN2000.team24_app.ui.quiz.category.CategoryScreenViewModel
import no.uio.ifi.IN2000.team24_app.ui.quiz.category.CategoryUiState

// quiz question screen with question
@Composable
fun QuestionScreen(

    categoryName: String,
    questions: List<String>,
    index: Int = 0,
    coinsWon: Int = 0,
    questionScreenViewModel: QuestionScreenViewModel = viewModel(),
    onNavigateToNextQuestionScreen: (Unit) -> Unit

) {

    // initialize viewmodel with question list, index and category name parameter
    LaunchedEffect(questionScreenViewModel) {

        questionScreenViewModel.initialize(questions, index, categoryName)

    }

    // get question and category ui state from view model
    val questionUiState: QuestionUiState by questionScreenViewModel.questionUiState.collectAsState()
    val categoryUiState: CategoryUiState by questionScreenViewModel.categoryUiState.collectAsState()

    // progress value for progress indicator
    val currentProgress = ((index + 1).toFloat() / questions.size)

    if (questionUiState.question != null && categoryUiState.category != null) {

        // set timer values
        val getTimer: Boolean = categoryUiState.category!!.shouldStartTimer
        var readingTime by remember { mutableIntStateOf(5) }
        var answeringTime by remember { mutableIntStateOf(categoryUiState.category!!.points) }
        // set pause timer value by default to opposite of should start timer value:
        // if timer should not be started, timer should be paused and vice versa
        var pauseTimer: Boolean = !getTimer

        // variable for storing new coins won
        var newCoinsWon = coinsWon

        // start timer until paused
        LaunchedEffect(key1 = readingTime) {

            while (readingTime > 0 && pauseTimer) {

                delay(1000L)
                readingTime--

            }

        }

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

                QuestionScreenHeader(

                    currentProgress,
                    readingTime,
                    answeringTime,
                    pauseTimer,
                    questionUiState

                )

            }

    }

}

@Composable
fun QuestionScreenHeader(

    currentProgress: Float,
    readingTime: Int,
    answeringTime: Int,
    pauseTimer: Boolean,
    questionUiState: QuestionUiState

) {

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        // row for displaying progress and time left to answer
        Row() {

            // progress indicator
            LinearProgressIndicator(
                modifier = Modifier
                    .height(16.dp)
                    .clip(RoundedCornerShape(8.dp)),
                color = Color.Green,
                trackColor = ProgressIndicatorDefaults.linearTrackColor,
                progress = currentProgress
            )

            Spacer(modifier = Modifier.width(16.dp))

            // value that displays time
            val displayTime = if (readingTime > 0 || pauseTimer) readingTime else answeringTime

            Text(

                text = "$displayTime",
                style = MaterialTheme.typography.titleSmall

            )

        }

        // spacer between progress bar and title
        Spacer(modifier = Modifier.height(32.dp))

        // question text
        Text(

            text = questionUiState.question!!.question,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold

        )

    }

}



