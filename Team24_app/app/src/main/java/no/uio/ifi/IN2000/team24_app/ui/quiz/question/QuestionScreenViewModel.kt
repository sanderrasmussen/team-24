package no.uio.ifi.IN2000.team24_app.ui.quiz.question

import android.content.ContentValues
import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.IN2000.team24_app.data.database.Question
import no.uio.ifi.IN2000.team24_app.data.question.QuestionRepository

data class QuestionUiState(

    val question: Question? = null

)

class QuestionScreen: ViewModel() {

    // question repo to fetch question from
    private val questionRepository: QuestionRepository = QuestionRepository()

    // ui state values for fetching question
    private val _questionUiState = MutableStateFlow(QuestionUiState())
    val questionUiState: StateFlow<QuestionUiState> = _questionUiState.asStateFlow()

    private var initialization = false

    @MainThread
    fun initialize(questionName: String) {

        if (!initialization) {

            initialization = true
            viewModelScope.launch {

                loadQuestionInfo(questionName)

            }

        }

    }

    // function to fetch categories
    private fun loadQuestionInfo(questionName: String) {

        viewModelScope.launch(Dispatchers.IO) {

            try {

                _questionUiState.update { currentQuestionUiState ->

                    val question = questionRepository.getQuestion(questionName)
                    currentQuestionUiState.copy(question = question)

                }

            } catch (e: Exception) {

                Log.e(ContentValues.TAG, "Feil ved henting av kategori: ${e.message}", e)

            }

        }

    }

}