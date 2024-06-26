package no.uio.ifi.IN2000.team24_app.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverter

@Dao //methods for sql queries
interface QuestionDao {

    @Insert
    fun insertAll(vararg questions: Question)

    @Delete
    fun delete(question: Question)

    @Query("SELECT question FROM Question")
    fun getAllQuestions(): List<String>


    @Query("SELECT question FROM Question WHERE question.categoryName = :categoryName")
    fun getCategoryQuestions(categoryName: String): List<String>

    @Query("SELECT question FROM Question WHERE question.categoryName = :categoryName AND question.answered = 0")
    fun getUnansweredCategoryQuestions(categoryName: String): List<String>

    @Query("SELECT question FROM Question WHERE question.answered = 1")
    fun getTrainingCategoryQuestions(): List<String>

    @Query("SELECT * FROM Question WHERE question = :questionName")
    fun getQuestion(questionName: String): Question

    @Query ("SELECT question FROM Question WHERE question = :questionName")
    fun getId(questionName: String) : String

    @Query("UPDATE Question SET answered = 1 WHERE question = :questionName")
    fun updateQuestionAnswered(questionName: String)

}

// string list converter for converting option list into string
// because room database cannot store lists of strings
class StringListConverter {
    @TypeConverter
    fun fromStringList(value: String?): List<String>? {

        // split the comma separated string into a list of string
        return value?.split(", ")

    }

    @TypeConverter
    fun toStringList(list: List<String>?): String? {

        // join list of strings into a single comma-separated string
        return list?.joinToString(", ")

    }
}

