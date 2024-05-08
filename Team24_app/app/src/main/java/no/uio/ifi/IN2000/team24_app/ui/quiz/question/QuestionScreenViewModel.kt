package no.uio.ifi.IN2000.team24_app.ui.quiz.question

import android.content.ContentValues.TAG
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
import no.uio.ifi.IN2000.team24_app.data.category.CategoryRepository
import no.uio.ifi.IN2000.team24_app.data.database.Question
import no.uio.ifi.IN2000.team24_app.data.question.QuestionRepository
import no.uio.ifi.IN2000.team24_app.ui.quiz.category.CategoryUiState

data class QuestionUiState(

    val question: Question? = null

)

class QuestionScreenViewModel: ViewModel() {

    // question and category and repo to fetch category and question from
    private val categoryRepository: CategoryRepository = CategoryRepository()
    private val questionRepository: QuestionRepository = QuestionRepository()

    // ui state values for fetching question
    private val _questionUiState = MutableStateFlow(QuestionUiState())
    val questionUiState: StateFlow<QuestionUiState> = _questionUiState.asStateFlow()

    // ui state values for fetching category
    private val _categoryUiState = MutableStateFlow(CategoryUiState())
    val categoryUiState: StateFlow<CategoryUiState> = _categoryUiState.asStateFlow()

    private var initialization = false

    @MainThread
    fun initialize(questionList: List<String>, index: Int, categoryName: String) {

        if (!initialization) {

            initialization = true
            viewModelScope.launch {

                loadQuestionInfo(questionList, index)
                loadCategoryInfo(categoryName)

            }

        }

    }

    // function to fetch question
    private fun loadQuestionInfo(questionList: List<String>, index: Int) {

        viewModelScope.launch(Dispatchers.IO) {

            try {

                _questionUiState.update { currentQuestionUiState ->

                    val questionName = questionList[index]

                    val question = questionRepository.getQuestion(questionName)
                    currentQuestionUiState.copy(question = question)

                }

            } catch (e: Exception) {

                Log.e(TAG, "Feil ved henting av spoersmaal: ${e.message}", e)

            }

        }

    }

    // function to fetch category
    private fun loadCategoryInfo(categoryName: String) {

        viewModelScope.launch(Dispatchers.IO) {

            try {

                _categoryUiState.update { currentCategoryUiState ->

                    val category = categoryRepository.getCategory(categoryName)
                    currentCategoryUiState.copy(category = category)

                }

            } catch (e: Exception) {

                Log.e(TAG, "Feil ved henting av kategori: ${e.message}", e)

            }

        }

    }

}