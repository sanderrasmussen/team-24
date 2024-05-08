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
import no.uio.ifi.IN2000.team24_app.data.bank.BankRepository
import no.uio.ifi.IN2000.team24_app.data.category.CategoryRepository
import no.uio.ifi.IN2000.team24_app.data.question.QuestionRepository
import no.uio.ifi.IN2000.team24_app.ui.quiz.category.CategoryUiState

data class BalanceUiState(

    val balance: Int = 0

)

class QuestionResultScreenViewModel: ViewModel() {

    // question and category and repo to fetch category and question from
    private val categoryRepository: CategoryRepository = CategoryRepository()
    private val questionRepository: QuestionRepository = QuestionRepository()
    // bank repo to add coins to
    val bankRepository: BankRepository = BankRepository()

    // ui state values for fetching category
    private val _categoryUiState = MutableStateFlow(CategoryUiState())
    val categoryUiState: StateFlow<CategoryUiState> = _categoryUiState.asStateFlow()

    // ui state values for fetching coins
    private val _balanceUiState = MutableStateFlow(BalanceUiState())
    val balanceUiState: StateFlow<BalanceUiState> = _balanceUiState.asStateFlow()

    private var initialization = false

    @MainThread
    fun initialize(questionList: List<String>, categoryName: String, coinsWon: Int) {

        if (!initialization) {

            initialization = true
            viewModelScope.launch {

                updateAnsweredQuestionValue(questionList)
                updateLastDateAnsweredCategoryValue(categoryName)
                updateBalance(coinsWon)
                loadCategoryInfo(categoryName)
                loadBalanceInfo()

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

    // function for updating answered value for questions
    private fun updateAnsweredQuestionValue(questionList: List<String>) {

        viewModelScope.launch(Dispatchers.IO) {

            try {

                questionList.forEach { questionName ->

                    questionRepository.updateQuestionAnsweredValue(questionName)

                }

            } catch (e: Exception) {

                Log.e(TAG, "Feil ved oppdatering av besvarte spoersmaal: ${e.message}", e)

            }

        }

    }

    // function to update last date answered category value
    private fun updateLastDateAnsweredCategoryValue(categoryName: String) {

        viewModelScope.launch(Dispatchers.IO) {

            try {

                categoryRepository.updateCategoryLastDateAnswered(categoryName)

            } catch (e: Exception) {

                Log.e(TAG, "Feil ved oppdatering av besvart kategori: ${e.message}", e)

            }

        }

    }

    // function to fetch balance from bank
    private fun loadBalanceInfo() {

        viewModelScope.launch(Dispatchers.IO) {

            try {

                val balance = bankRepository.getBankBalance()

                if (balance != null) {

                    _balanceUiState.update { currentBalanceUiState ->

                        currentBalanceUiState.copy(balance = balance)

                    }

                }

            } catch (e: Exception) {

                Log.e(TAG, "Feil ved henting av kategori: ${e.message}", e)

            }

        }

    }

    // function to update balance with coins won
    private fun updateBalance(coinsWon: Int) {

        viewModelScope.launch(Dispatchers.IO) {

            try {

                bankRepository.deposit(coinsWon)

            } catch (e: Exception) {

                Log.e(TAG, "Feil ved oppdatering av opptjente penger: ${e.message}", e)

            }

        }

    }

}