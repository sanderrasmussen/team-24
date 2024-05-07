package no.uio.ifi.IN2000.team24_app.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface QuestionDao {

    @Insert
    fun insertAll(vararg question: Question)

    @Delete
    fun delete(question : Question)

    @Query("SELECT question FROM Question")
    fun getAllQuestions(): List<String>

    @Query("SELECT question FROM Question WHERE question.categoryName = :categoryName")
    fun getCategoryQuestions(categoryName: String): List<String>

    @Query("SELECT question FROM Question WHERE question.categoryName = :categoryName AND question.answered = false")
    fun getUnansweredCategoryQuestions(categoryName: String): List<String>

    @Query("SELECT question FROM Question WHERE question.answered = true")
    fun getTrainingCategoryQuestions(): List<String>

    @Query("SELECT * FROM Question WHERE question = :questionName")
    fun getQuestion(questionName: String): Question

    @Query("SELECT options FROM Question WHERE question = :questionName")
    fun getQuestionOptions(questionName: String): Question

    @Query("SELECT correctOptionIndex FROM Question WHERE question = :questionName")
    fun getCorrectOptionIndex(questionName: String): Question

    @Query("UPDATE Question SET answered = true WHERE question = :questionName")
    fun updateQuestionAnswered(questionName: String)

}