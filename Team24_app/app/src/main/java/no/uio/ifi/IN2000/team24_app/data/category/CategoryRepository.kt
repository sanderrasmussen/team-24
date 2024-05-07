package no.uio.ifi.IN2000.team24_app.data.category

import androidx.room.Query
import no.uio.ifi.IN2000.team24_app.data.database.Category
import no.uio.ifi.IN2000.team24_app.data.database.MyDatabase
import java.util.Calendar
import java.util.Date

class CategoryRepository {

    private val database = MyDatabase.getInstance()
    private val categoryDao = database.categoryDao()

    // FUNCTIONS FOR FETCHING CATEGORIES

    // function to get category names
    suspend fun getAllCategories(): List<String> {

        return categoryDao.getAllCategories()

    }

    // function to get category from name
    suspend fun getCategory(categoryName: String): Category {

        return categoryDao.getCategory(categoryName)

    }

    // function to get category last date answered value
    suspend fun getCategoryLastDateAnswered(categoryName: String): Date {

        return categoryDao.getCategoryLastDateAnswered(categoryName)

    }

    // function to get category points value
    suspend fun getCategoryPoints(categoryName: String): Int {

        return categoryDao.getCategoryPoints(categoryName)

    }

    // function to get value if timer should be started
    suspend fun getCategoryShouldStartTimer(categoryName: String): Boolean {

        return categoryDao.getCategoryShouldStartTimer(categoryName)

    }

    // function to get locked value by comparing lastdateanswered and todays date
    suspend fun getLockedValue(categoryName: String): Boolean {

        // get today and last date answered as calendar objects
        val today = Calendar.getInstance().apply {

            time = Date()

        }

        val lastDateAnswered = Calendar.getInstance().apply {

            time = getCategoryLastDateAnswered(categoryName)

        }

        // return check of equal value of objects years, months and day of months
        return today.get(Calendar.YEAR) == lastDateAnswered.get(Calendar.YEAR) &&
                today.get(Calendar.MONTH) == lastDateAnswered.get(Calendar.MONTH) &&
                today.get(Calendar.DAY_OF_MONTH) == lastDateAnswered.get(Calendar.DAY_OF_MONTH)

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



}