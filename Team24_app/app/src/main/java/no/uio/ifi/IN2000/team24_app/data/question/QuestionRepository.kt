package no.uio.ifi.IN2000.team24_app.data.question

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.uio.ifi.IN2000.team24_app.data.database.MyDatabase
import no.uio.ifi.IN2000.team24_app.data.database.Question

class QuestionRepository {

    private val database = MyDatabase.getInstance()
    private val questionDao = database.questionDao()

    // FUNCTIONS FOR FETCHING QUESTIONS FROM CATEGORIES

    // function to get all questions
    suspend fun getAllQuestions(): List<String> {

        return withContext(Dispatchers.IO) withContex@{

            return@withContex questionDao.getAllQuestions()

        }
    }

    // function to get questions for a normal category
    suspend fun getUnansweredCategoryQuestions(categoryName: String): List<String> {

        return withContext(Dispatchers.IO) withContex@{

            return@withContex questionDao.getUnansweredCategoryQuestions(categoryName)

        }

    }

    // function to get questions for a normal category when all questions are answered
    suspend fun getAllCategoryQuestions(categoryName: String): List<String> {

        return withContext(Dispatchers.IO) withContex@{

            return@withContex questionDao.getCategoryQuestions(categoryName)

        }

    }

    // function to get training category questions
    suspend fun getTrainingCategoryQuestions(): List<String> {

        return withContext(Dispatchers.IO) withContex@{

            return@withContex questionDao.getTrainingCategoryQuestions()

        }

    }

    // function to get questions for a selected category
    suspend fun getCategoryQuestions(categoryName: String): List<String> {

        // checks if all questions are unanswered
        if (getTrainingCategoryQuestions() == getAllQuestions()) {

            // get all questions in selected category
            return getAllCategoryQuestions(categoryName)

        }

        // checks if category is training category
        else if (categoryName == "Øving") {

            // get all training category questions aka all answered questions
            return getTrainingCategoryQuestions()

        }

        // checks if category is a normal category
        else {

            // get unanswered questions in selected category
            return getUnansweredCategoryQuestions(categoryName)

        }

    }


    // function to get question for a question name
    suspend fun getQuestion(questionName: String): Question {
        val whoa= "Hvilken av disse gir indikasjon på gult farevarsel?"
        val QUESTION= questionDao.getQuestion(whoa)
        val id= questionDao.getId(whoa)
        println("questionName in getQuestion: $questionName")
        println("ID $id")
        println("QUESTIONDAO.GETQUESTION $QUESTION")
        return questionDao.getQuestion(questionName)

    }

    suspend fun getAllQuestions1(): List<String> {
        val QUESTION= questionDao.getAllQuestions()

        println("QUESTIONDAO.GETQUESTION $QUESTION")
        return QUESTION

    }

    // FUNCTION TO CHANGE ANSWERED STATUS FOR QUESTION

    suspend fun updateQuestionAnsweredValue(questionName: String) {

        return questionDao.updateQuestionAnswered(questionName)

    }

}