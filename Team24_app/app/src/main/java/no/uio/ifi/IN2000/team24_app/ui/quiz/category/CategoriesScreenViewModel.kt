package no.uio.ifi.IN2000.team24_app.ui.quiz.category

import android.content.ContentValues.TAG
import android.util.Log
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
import java.util.Calendar
import java.util.Date

data class CategoriesUiState(

    val categories: List<Category> = emptyList()

)

class CategoriesScreenViewModel: ViewModel() {

    // category repo to fetch categories from
    private val categoryRepository: CategoryRepository = CategoryRepository()

    // ui state values for fetching categories
    private val _categoriesUiState = MutableStateFlow(CategoriesUiState())
    val categoriesUiState: StateFlow<CategoriesUiState> = _categoriesUiState.asStateFlow()

    init {

        loadCategories()

    }

    // function to fetch categories
    private fun loadCategories() {

        viewModelScope.launch(Dispatchers.IO) {

            try {

                _categoriesUiState.update { currentCategoriesUiState ->

                    val categories = categoryRepository.getAllCategories()
                    currentCategoriesUiState.copy(categories = categories)

                }

            } catch (e: Exception) {

                Log.e(TAG, "Feil ved henting av kategorier: ${e.message}", e)

            }

        }

    }

    // function to fetch category locked value
    private fun loadCategoryLockedValue(category: Category): Boolean {

        // check if category is training category
        // and last date answered is null
        if (category.category == "Øving" && category.lastDateAnswered == null) {

            // return true, since training category should be locked before doing quiz the first time
            return true

        }

        // get today and last date answered of category as calendar objects
        val today = Calendar.getInstance().apply { time = Date() }
        val lastDateAnswered = Calendar.getInstance().apply { time = category.lastDateAnswered }

        // return check of equal value of objects years, months and day of months
        return today.get(Calendar.YEAR) == lastDateAnswered.get(Calendar.YEAR) &&
                today.get(Calendar.MONTH) == lastDateAnswered.get(Calendar.MONTH) &&
                today.get(Calendar.DAY_OF_MONTH) == lastDateAnswered.get(Calendar.DAY_OF_MONTH)

    }

}