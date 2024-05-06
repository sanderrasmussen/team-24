package no.uio.ifi.IN2000.team24_app.data.question

import no.uio.ifi.IN2000.team24_app.data.database.Category
import no.uio.ifi.IN2000.team24_app.data.database.MyDatabase
import no.uio.ifi.IN2000.team24_app.data.database.Question

class QuestionRepository {

    private val database = MyDatabase.getInstance()
    private val questionDao = database.questionDao()

    // FUNCTIONS FOR FETCHING QUESTIONS FROM CATEGORIES

    // function to get all questions
    suspend fun getAllQuestions(): List<Question> {

        return questionDao.getAllQuestions()

    }

    // function to get questions for a normal category
    suspend fun getUnansweredCategoryQuestions(categoryName: String): List<Question> {

        return questionDao.getUnansweredCategoryQuestions(categoryName)

    }

    // function to get questions for a normal category when all questions are answered
    suspend fun getAllCategoryQuestions(categoryName: String): List<Question> {

        return questionDao.getCategoryQuestions(categoryName)

    }

    // function to get training category questions
    suspend fun getTrainingCategoryQuestions(): List<Question> {

        return questionDao.getTrainingCategoryQuestions()

    }

    // function to get questions for a selected category
    suspend fun getCategoryQuestions(categoryName: String): List<Question> {

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

    // FUNCTION TO CHANGE ANSWERED STATUS FOR QUESTION

    suspend fun updateQuestionAnsweredValue(questionName: String) {

        return questionDao.updateQuestionAnswered(questionName)

    }

}