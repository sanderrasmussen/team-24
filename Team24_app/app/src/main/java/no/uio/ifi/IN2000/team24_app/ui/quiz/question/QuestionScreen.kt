package no.uio.ifi.IN2000.team24_app.ui.quiz.question

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

        // start timer until paused
        LaunchedEffect(key1 = readingTime) {

            while (readingTime > 0 && pauseTimer) {

                delay(1000L)
                readingTime--

            }

        }

    }

}

