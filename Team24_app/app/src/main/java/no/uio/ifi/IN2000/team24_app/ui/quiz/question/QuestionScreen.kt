package no.uio.ifi.IN2000.team24_app.ui.quiz.question

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import no.uio.ifi.IN2000.team24_app.ui.quiz.category.CategoryUiState

// quiz question screen with question
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(

    onBackPressed: () -> Unit,
    categoryName: String,
    questions: String,
    index: Int = 0,
    coinsWon: Int = 0,
    questionScreenViewModel: QuestionScreenViewModel = viewModel(),
    onNavigateToNextQuestionScreen: (String, String, Int?, Int?) -> Unit,
    onNavigateToResultQuestionScreen: (String, String, Int?) -> Unit

) {

    // convert question list string to actual list of strings
    val questionList = questions.split(", ")

    // initialize viewmodel with question list, index and category name parameter
    LaunchedEffect(questionScreenViewModel) {

        questionScreenViewModel.initialize(questionList, index, categoryName)

    }

    // get question and category ui state from view model
    val questionUiState: QuestionUiState by questionScreenViewModel.questionUiState.collectAsState()
    val categoryUiState: CategoryUiState by questionScreenViewModel.categoryUiState.collectAsState()

    // progress value for progress indicator
    val currentProgress = ((index + 1).toFloat() / questionList.size)

    if (questionUiState.question != null && categoryUiState.category != null) {

        // set timer values
        val getTimer: Boolean = categoryUiState.category!!.shouldStartTimer
        var readingTime by remember {

            if (categoryUiState.category!!.category == "Øving") mutableIntStateOf(0)
            else mutableIntStateOf(5)

        }
        var answeringTime by remember { mutableIntStateOf(categoryUiState.category!!.points) }
        // set pause timer value by default to opposite value as getTimer
        // timer should be paused from beginning if category shouldn't start timer
        var pauseTimer = !getTimer

        // variable for storing new coins won
        var newCoinsWon = coinsWon

        val onAnswerSelected: (String) -> Unit = { selectedOption ->

            // check if the selected option is correct
            val isCorrect = selectedOption == questionUiState.question!!.options[questionUiState.question!!.correctOptionIndex]
            // update points based on correctness of the answer
            newCoinsWon += if (isCorrect) answeringTime else 0
            // stop the timer
            pauseTimer = true

        }

        // start timer until paused
        LaunchedEffect(key1 = readingTime) {

            while (readingTime > 0) {

                delay(1000L)
                readingTime--

            }

        }

        // checks if readingtime is 0
        if (readingTime == 0) {

            // starts answering timer
            LaunchedEffect(Unit) {
                while (answeringTime > 0 && !pauseTimer) {

                    delay(1000L)
                    answeringTime--

                }
            }

        }

        // top app bar with back button to navigate back to categories screen
        Scaffold(

            topBar = {

                TopAppBar(

                    title = {

                        Text(text = "Spørsmål")

                    },

                    navigationIcon = {

                        // icon button that goes back to categories screen
                        IconButton(onClick = onBackPressed) {

                            // back arrow icon
                            Icon(

                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Tilbakeknapp"

                            )

                        }

                    }

                )

            }

        ) { innerPadding ->

            Box(

                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()

            ) {

                Column(

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {

                    // question screen header with
                    QuestionScreenHeader(

                        currentProgress,
                        readingTime,
                        answeringTime,
                        getTimer,
                        questionUiState

                    )

                    if (readingTime == 0) {

                        // spacer between header and functionality
                        Spacer(modifier = Modifier.height(16.dp))

                        QuestionScreenBody(

                            questionUiState,
                            onAnswerSelected,
                            categoryName,
                            questions,
                            questionList,
                            index,
                            coinsWon,
                            onNavigateToNextQuestionScreen,
                            onNavigateToResultQuestionScreen

                        )

                    }

                }

            }

        }

    }

}

@Composable
fun QuestionScreenHeader(

    currentProgress: Float,
    readingTime: Int,
    answeringTime: Int,
    getTimer: Boolean,
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

                progress = {
                    currentProgress
                },
                modifier = Modifier
                    .height(16.dp)
                    .clip(RoundedCornerShape(8.dp)),
                color = Color.Green,
                trackColor = ProgressIndicatorDefaults.linearTrackColor

            )

            if (getTimer) {

                Spacer(modifier = Modifier.width(16.dp))

                // value that displays time
                val displayTime = if (readingTime > 0) readingTime else answeringTime

                Text(

                    text = "$displayTime",
                    style = MaterialTheme.typography.titleSmall

                )

            }

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

@Composable
fun QuestionScreenBody(

    questionUiState: QuestionUiState,
    onAnswerSelected: (String) -> Unit,
    categoryName: String,
    questions: String,
    questionList: List<String>,
    index: Int,
    coinsWon: Int,
    onNavigateToNextQuestionScreen: (String, String, Int?, Int?) -> Unit,
    onNavigateToResultQuestionScreen: (String, String, Int?) -> Unit

) {

    // displaying text options for the question
    // selected option variable
    var selectedOption by remember { mutableStateOf<String?>(null) }

    // checks if selected option is null
    if (selectedOption != null) {

        // makes callback to parent function
        onAnswerSelected(selectedOption!!)

    }

    // lazy column containing answer alternatives
    LazyColumn(

        modifier = Modifier.heightIn(220.dp, 440.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)

    ) {

        // puts all option items in lazy column format
        items(questionUiState.question!!.options.size) { optionIndex ->

            // variables for each option
            val answerOption: String = questionUiState.question.options[optionIndex]
            val correctOption: String = questionUiState.question.options[questionUiState.question.correctOptionIndex]

            // answer option displaying button for each option based on feedback
            AnswerOption(

                answerOption,
                selectedOption,
                correctOption,
                onClick = {

                    if (selectedOption == null) {

                        selectedOption = answerOption

                    }

                }

            )

        }

    }

    // spacer between option buttons and continue button
    Spacer(modifier = Modifier.height(32.dp))

    // continue button
    Button(

        onClick = { if (index >= questionList.size) onNavigateToResultQuestionScreen(categoryName, questions, coinsWon)
            else onNavigateToNextQuestionScreen(categoryName, questions, index + 1, coinsWon) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)

    ) {

        Text("Fortsett")

    }

}

@Composable
fun AnswerOption(

    answerOption: String,
    selectedOption: String?,
    correctOption: String,
    onClick: () -> Unit

) {

    // intensity variables for intensity of button color
    val lightIntensity = 0.75f

    // makes background and outline color a muted gray by default
    var backgroundColor: Color = mutedColor(Color.Blue, lightIntensity)
    var outlineColor: Color = mutedColor(Color.Blue, lightIntensity)
    // makes text color gray by default
    var textColor: Color = Color.Blue

    // checks if a button is pressed
    if (selectedOption != null) {

        // checks if this option is the correct option
        if (correctOption == answerOption) {

            // makes background and outline color a muted green if correct
            backgroundColor = mutedColor(Color.Green, lightIntensity)
            outlineColor = mutedColor(Color.Green, lightIntensity)
            // makes text color a more intense green color if correct
            textColor = Color.Green

        }

        // checks if this option is the selected option
        if (selectedOption == answerOption) {

            // checks if this selected option is the correct option
            if (correctOption == answerOption) {

                // makes outline color a more intense green color if correct
                outlineColor = Color.Green

            }

            // checks if this selected option is not the right option
            else {

                // makes background color a muted red if selected and incorrect
                backgroundColor = mutedColor(Color.Red, lightIntensity)
                // makes outline and text color a more intense red if selected and incorrect
                outlineColor = Color.Red
                textColor = Color.Red

            }

        }

    }

    // elevated button with option text and colors based on pressed button
    ElevatedButton(

        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        // border with outline color in a rounded corner shape
        border = BorderStroke(2.dp, outlineColor),
        colors = ButtonDefaults.buttonColors(

            containerColor = backgroundColor

        )

    ) {

        // text with option text and text color
        Text(

            text = answerOption,
            color = textColor

        )

    }

}

// function for making a less intense version of a color:
fun mutedColor(color: Color, intensity: Float): Color {

    // changes value of color components to a more muted version
    val mutedRed = color.red * (1 - intensity) + intensity
    val mutedGreen = color.green * (1 - intensity) + intensity
    val mutedBlue = color.blue * (1 - intensity) + intensity

    // returns color with muted color components
    return Color(red = mutedRed, green = mutedGreen, blue = mutedBlue)

}






