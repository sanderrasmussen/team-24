package no.uio.ifi.IN2000.team24_app.data.question

import no.uio.ifi.IN2000.team24_app.data.database.MyDatabase

class QuestionRepository {

    private val database = MyDatabase.getInstance()
    private val questionDao = database.questionDao()

}