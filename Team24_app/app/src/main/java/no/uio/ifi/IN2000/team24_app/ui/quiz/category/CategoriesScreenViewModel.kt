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

data class CategoriesUiState(

    val categories: List<String> = emptyList()

)

data class CategoryLockedUiState(

    val locked: Boolean = false

)

class CategoriesScreenViewModel: ViewModel() {

    // category repo to fetch categories from
    private val categoryRepository: CategoryRepository = CategoryRepository()

    // ui state values for fetching categories
    private val _categoriesUiState = MutableStateFlow(CategoriesUiState())
    val categoriesUiState: StateFlow<CategoriesUiState> = _categoriesUiState.asStateFlow()

    // ui state values for fetching category locked ui state
    private val _categoryLockedUiState = MutableStateFlow(CategoryLockedUiState())
    val categoryLockedUiState: StateFlow<CategoryLockedUiState> = _categoryLockedUiState.asStateFlow()

    init {

        getCategories()

    }

    private fun getCategories() {

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

    private fun getCategoryLockedValue(categoryName: String) {

        viewModelScope.launch(Dispatchers.IO) {

            try {

                _categoryLockedUiState.update { currentCategoryLockedUiState ->

                    val locked = categoryRepository.getLockedValue(categoryName)
                    currentCategoryLockedUiState.copy(locked = locked)

                }

            } catch (e: Exception) {

                Log.e(TAG, "Feil ved henting av kategoriverdi: ${e.message}", e)

            }

        }

    }

}