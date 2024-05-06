package no.uio.ifi.IN2000.team24_app.data.category

import no.uio.ifi.IN2000.team24_app.data.database.MyDatabase

class CategoryRepository {

    private val database = MyDatabase.getInstance()
    private val categoryDao = database.categoryDao()

}