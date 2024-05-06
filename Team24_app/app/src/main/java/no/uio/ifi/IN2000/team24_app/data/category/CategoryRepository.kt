package no.uio.ifi.IN2000.team24_app.data.category

import no.uio.ifi.IN2000.team24_app.data.database.Category
import no.uio.ifi.IN2000.team24_app.data.database.MyDatabase
import java.util.Date

class CategoryRepository {

    private val database = MyDatabase.getInstance()
    private val categoryDao = database.categoryDao()

    suspend fun getAllCategories(): List<Category> {

        return categoryDao.getAllCategories()

    }

    suspend fun getCategory(categoryName: String): Category {

        return categoryDao.getCategory(categoryName)

    }

    suspend fun getCategoryLastDateAnswered(categoryName: String): Date {

        return categoryDao.getCategoryLastDateAnswered(categoryName)

    }

    suspend fun updateCategoryLastDateAnswered(categoryName: String) {

        categoryDao.updateCategoryLastDateAnswered(categoryName)

    }

}