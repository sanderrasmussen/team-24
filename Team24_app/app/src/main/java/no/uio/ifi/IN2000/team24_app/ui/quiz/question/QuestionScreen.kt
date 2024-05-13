package no.uio.ifi.IN2000.team24_app.ui.quiz.question

import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import no.uio.ifi.IN2000.team24_app.ui.BackgroundImage
import no.uio.ifi.IN2000.team24_app.ui.backgroundColour
import no.uio.ifi.IN2000.team24_app.ui.quiz.category.CategoryUiState
import no.uio.ifi.IN2000.team24_app.ui.skyColour

// quiz question screen with question
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    onBackPressed: () -> Unit,
    categoryName: String,
    questions: String,
    index: Int,
    coinsWon: Int,
    questionScreenViewModel: QuestionScreenViewModel = viewModel(),
    onNavigateToNextQuestionScreen: (Int, Int?) -> Unit,
    onNavigateToResultQuestionScreen: (Int?) -> Unit
) {

    // convert question list string to actual list of strings
    val questionList = questions.split(",")
    val longList: List<Long> = questionList.map { it.toLong() }

    // initialize view model with question list, index and category name parameter
    LaunchedEffect(questionScreenViewModel) {

        questionScreenViewModel.initialize(longList, index, categoryName)

    }

    // get question and category ui state from view model
    val questionUiState: QuestionUiState by questionScreenViewModel.questionUiState.collectAsState()
    val categoryUiState: CategoryUiState by questionScreenViewModel.categoryUiState.collectAsState()

    // image name variable with background image that reflects time of day
    val imageName = BackgroundImage()

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
            val options = questionUiState.question!!.options
            val correctOptionIndex = questionUiState.question!!.correctOptionIndex
            val isCorrect =
                selectedOption == options[correctOptionIndex]
            // update points based on correctness of the answer
            newCoinsWon += if (isCorrect) answeringTime else 0
            // stop the timer
            pauseTimer = true

        }

        // start reading timer
        LaunchedEffect(key1 = readingTime) {

            while (readingTime > 0) {

                delay(1000L)
                readingTime--

            }

        }

        // check if readingtime is 0
        if (readingTime == 0) {

            // start answering timer
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

                    // row for displaying progress and time left to answer
                    // NOTE: should probably be stored in a header function for cleaner code
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

                        Spacer(modifier = Modifier.width(16.dp))

                        // check if getTimer is true
                        if (getTimer) {

                            // value that displays time if the timer should be started
                            val displayTime =
                                if (readingTime > 0) readingTime else answeringTime
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

                    // displays rest of information after reading timer is done
                    // NOTE: should be stored in a separate function for cleaner code
                    if (readingTime == 0) {

                        // spacer between title and functionality
                        Spacer(modifier = Modifier.height(16.dp))

                        // displaying text options for the question
                        TextOptions(

                            questionUiState = questionUiState,
                            onAnswerSelected = onAnswerSelected

                        )

                        // spacer between option buttons and continue button
                        Spacer(modifier = Modifier.height(32.dp))

                        // continue button
                        Button(

                            onClick = {

                                // index for next question
                                val nextIndex = index + 1
                                // check if next index is higher than questions to show
                                if (nextIndex < questionList.size) {

                                    // navigate to question screen if there are more questions to show
                                    onNavigateToNextQuestionScreen(nextIndex, newCoinsWon)

                                } else {

                                    // navigate to result screen if there are no more questions to show
                                    onNavigateToResultQuestionScreen(newCoinsWon)
                                }

                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = ButtonDefaults.buttonColors(

                                containerColor = backgroundColour()

                            )

                        ) {

                            // continue button displaying continue text
                            Text("Fortsett")

                        }

                    }

                }

            }

        }

    }

}

// function for displaying all text options
@Composable
fun TextOptions(

    questionUiState: QuestionUiState,
    onAnswerSelected: (String) -> Unit

) {

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
            val correctIndex: Int = questionUiState.question.correctOptionIndex
            val correctOption: String = questionUiState.question.options[correctIndex]

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

}

// function for displaying an answer option
// with change of button color and display when clicked
@Composable
fun AnswerOption(

    answerOption: String,
    selectedOption: String?,
    correctOption: String,
    onClick: () -> Unit

) {

    // intensity variables for intensity of button color
    val lightIntensity = 0.5f

    // makes background and outline color a muted gray by default
    var backgroundColor: Color = mutedColor(Color.Gray, lightIntensity)
    var outlineColor: Color = mutedColor(Color.Gray, lightIntensity)
    // makes text color gray by default
    var textColor: Color = Color.Gray

    // green color that fits the backgrounds better
    val greenColor = Color(android.graphics.Color.parseColor("#003312"))
    // red color that fits the backgrounds better
    val redColor = Color(android.graphics.Color.parseColor("#990000"))

    // checks if a button is pressed
    if (selectedOption != null) {

        // checks if this option is the correct option
        if (correctOption == answerOption) {

            // makes background and outline color a muted green if correct
            backgroundColor = mutedColor(greenColor, lightIntensity)
            outlineColor = mutedColor(greenColor, lightIntensity)
            // makes text color a more intense green color if correct
            textColor = greenColor

        }

        // checks if this option is the selected option
        if (selectedOption == answerOption) {

            // checks if this selected option is the correct option
            if (correctOption == answerOption) {

                // makes outline color a more intense green color if correct
                outlineColor = greenColor

            }

            // checks if this selected option is not the right option
            else {

                // makes background color a muted red if selected and incorrect
                backgroundColor = mutedColor(redColor, lightIntensity)
                // makes outline and text color a more intense red if selected and incorrect
                outlineColor = redColor
                textColor = redColor

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





