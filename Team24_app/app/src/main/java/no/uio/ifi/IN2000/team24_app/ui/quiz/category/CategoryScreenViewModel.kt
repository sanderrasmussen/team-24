package no.uio.ifi.IN2000.team24_app.ui.quiz.category

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
import no.uio.ifi.IN2000.team24_app.data.database.Category
import no.uio.ifi.IN2000.team24_app.data.question.QuestionRepository

/**
 * Ui state for the category screen.
 *
 * This class holds the ui state for the category screen, specifically the category
 */
data class CategoryUiState(

    val category: Category? = null

)

data class QuestionsUiState(

    val questions: String? = null

)

/**
 * ViewModel for the category screen.
 *
 * This ViewModel is responsible for fetching the category and questions for the category.
 * It uses the [CategoryRepository] and [QuestionRepository] to fetch the data.
 */
class CategoryScreenViewModel: ViewModel() {

    // category and question repo to fetch category and question from
    private val categoryRepository: CategoryRepository = CategoryRepository()
    private val questionRepository: QuestionRepository = QuestionRepository()

    // ui state values for fetching category
    private val _categoryUiState = MutableStateFlow(CategoryUiState())
    val categoryUiState: StateFlow<CategoryUiState> = _categoryUiState.asStateFlow()

    // ui state values for fetching category questions
    private val _questionsUiState = MutableStateFlow(QuestionsUiState())
    val questionsUiState: StateFlow<QuestionsUiState> = _questionsUiState.asStateFlow()

    private var initialization = false

    @MainThread
    fun initialize(categoryName: String) {

        if (!initialization) {

            initialization = true
            viewModelScope.launch {

                loadCategoryInfo(categoryName)
                loadQuestionsForCategory(categoryName)

            }

        }

    }

    // function to fetch category
    /**
     * Function to fetch the category from the database.
     *
     * This function fetches the category from the database using the [CategoryRepository].
     * It updates the ui state with the fetched category.
     * @param categoryName the name of the category to fetch
     */
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


    /**
     * Function to fetch 3 random questions for the category, to pass to the question screen
     * @param categoryName the name of the category to fetch questions for
     * @see QuestionRepository
     * @see no.uio.ifi.IN2000.team24_app.ui.quiz.question.QuestionScreen
     */
    fun loadQuestionsForCategory(categoryName: String) {

        viewModelScope.launch(Dispatchers.IO) {

            try {
                _questionsUiState.update { currentQuestionsUiState ->

                    val questionList = questionRepository.getCategoryQuestions(categoryName)
                    println("QuestionList: $questionList")
                    val shuffledQuestionList = questionList.shuffled().take(3)
                    val questions = shuffledQuestionList.joinToString(",")
                    currentQuestionsUiState.copy(questions = questions)

                }

            } catch (e: Exception) {

                Log.e(TAG, "Feil ved henting av spoersmaal til kategori: ${e.message}", e)

            }

        }

    }

}