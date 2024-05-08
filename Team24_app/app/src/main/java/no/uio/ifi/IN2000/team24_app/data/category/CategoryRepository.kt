package no.uio.ifi.IN2000.team24_app.data.category

import androidx.room.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.uio.ifi.IN2000.team24_app.data.database.Category
import no.uio.ifi.IN2000.team24_app.data.database.MyDatabase
import java.util.Calendar
import java.util.Date

class CategoryRepository {

    private val database = MyDatabase.getInstance()
    private val categoryDao = database.categoryDao()

    // FUNCTIONS FOR FETCHING CATEGORIES

    // function to get all categories
    suspend fun getAllCategories(): List<Category> {

        return withContext(Dispatchers.IO) withContex@{

            return@withContex categoryDao.getAllCategories()

        }

    }

    // function to get category from name
    suspend fun getCategory(categoryName: String): Category {

        return withContext(Dispatchers.IO) withContex@{

            return@withContex categoryDao.getCategory(categoryName)

        }

    }

    // FUNCTION TO CHANGE LAST DATE ANSWERED FOR CATEGORY

    suspend fun updateCategoryLastDateAnswered(categoryName: String) {

        // check if training category is null to unlock it
        if (getCategory("Øving").lastDateAnswered == null) {

            updateCategoryLastDateAnswered("Øving")

        }

        // lock category last date answered value
        categoryDao.updateCategoryLastDateAnswered(categoryName)

    }

    // HELP METHODS TO FETCH CATEGORIES

    fun convertToCategory(categories: Category) {



    }


}