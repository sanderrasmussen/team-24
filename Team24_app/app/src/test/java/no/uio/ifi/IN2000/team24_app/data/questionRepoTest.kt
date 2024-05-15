package no.uio.ifi.IN2000.team24_app.data.questions

import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import no.uio.ifi.IN2000.team24_app.data.question.QuestionRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class questionRepoTest {


    private lateinit var questionRepository: QuestionRepository

    @Before
    fun setUp() {
        questionRepository = QuestionRepository()
    }

    @Test
    fun testGetAllQuestions() = runBlocking {
        val allQuestions = questionRepository.getAllQuestions()
        assertEquals(15, allQuestions.size)
    }

    @Test
    fun testGetCategoryQuestions() = runBlocking {
        val weatherQuestions = questionRepository.getCategoryQuestions("Om været")
        assertEquals(8, weatherQuestions.size)
        assertTrue(weatherQuestions.all { it in 0..7 })

        val warningQuestions = questionRepository.getCategoryQuestions("Farevarsler")
        assertEquals(7, warningQuestions.size)
        assertTrue(warningQuestions.all { it in 8..14 })
    }

    @Test
    fun testGetQuestion() = runBlocking {
        val questionName = "Hva er den høyeste vindstyrken på vindskalaen?"
        val question = questionRepository.getQuestion(questionName)
        assertEquals(3L, question.id)
    }

    @Test
    fun testUpdateQuestionAnsweredValue() = runBlocking {
        val questionName = "Hva er den høyeste vindstyrken på vindskalaen?"
        val initialQuestion = questionRepository.getQuestion(questionName)
        assertFalse(initialQuestion.answered)

        questionRepository.updateQuestionAnsweredValue(questionName)

        val updatedQuestion = questionRepository.getQuestion(questionName)
        assertTrue(updatedQuestion.answered)
    }
}