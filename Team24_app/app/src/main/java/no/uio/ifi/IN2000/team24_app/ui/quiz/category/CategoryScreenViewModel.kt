package no.uio.ifi.IN2000.team24_app.ui.quiz.category

import android.content.ContentValues
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

data class CategoryUiState(

    val category: Category? = null

)

class CategoryScreenViewModel: ViewModel() {

    // category repo to fetch category from
    private val categoryRepository: CategoryRepository = CategoryRepository()

    // ui state values for fetching category
    private val _categoryUiState = MutableStateFlow(CategoryUiState())
    val categoryUiState: StateFlow<CategoryUiState> = _categoryUiState.asStateFlow()

    private var initialization = false

    @MainThread
    fun initialize(categoryName: String) {

        if (!initialization) {

            initialization = true
            viewModelScope.launch {

                loadCategoryInfo(categoryName)

            }

        }

    }

    // function to fetch categories
    private fun loadCategoryInfo(categoryName: String) {

        viewModelScope.launch(Dispatchers.IO) {

            try {

                _categoryUiState.update { currentCategoriesUiState ->

                    val category = categoryRepository.getCategory(categoryName)
                    currentCategoriesUiState.copy(category = category)

                }

            } catch (e: Exception) {

                Log.e(TAG, "Feil ved henting av kategori: ${e.message}", e)

            }

        }

    }

}