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

    @Query("SELECT * FROM Question WHERE question.category = :category")
    fun getCategoryQuestions(category: Category): List<Question>

    @Query("SELECT * FROM Question WHERE question.category = :category AND question.answered = false")
    fun getUnansweredCategoryQuestions(category: Category): List<Question>

}