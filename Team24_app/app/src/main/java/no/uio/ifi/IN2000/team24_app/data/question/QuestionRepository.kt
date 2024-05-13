package no.uio.ifi.IN2000.team24_app.data.question

import no.uio.ifi.IN2000.team24_app.data.database.Question

class QuestionRepository {


    suspend fun getAllQuestions(): List<String> {
        return allQuestions.map { it.question }
    }
    suspend fun getQuestionById(questionId: Long): Question {
        val question = allQuestions.find { it.id == questionId }
        return question ?: throw NoSuchElementException("Question not found")
    }


    suspend fun getCategoryQuestions(categoryName: String): List<Long> {
        return when (categoryName) {
            "Om været" -> weatherQuestions.map { it.id }
            "Farevarsler" -> warningQuestions.map { it.id }
            else -> emptyList()
        }
    }


    suspend fun getQuestion(questionName: String): Question {
        return allQuestions.firstOrNull { it.question == questionName } ?: throw NoSuchElementException("Question not found")
    }


    suspend fun updateQuestionAnsweredValue(questionName: String) {
        allQuestions.firstOrNull { it.question == questionName }?.answered = true
    }


    val weatherQuestions = listOf(
        Question(0,
            "Hva er den kaldeste temperaturen som noensinne har blitt registrert?",
            "Om været",
            listOf("-89.2°C", "-61.8°C", "-100°C", "-78.9°C"),
            0),
        Question(1,
            "Hvilken landsdel får mest regn i løpet av ett år?",
            "Om været",
            listOf("Nord-Norge", "Østlandet", "Vestlandet", "Sørlandet"),
            2),
        Question(2,
            "Hva heter skalaen som norske meteorologer bruker for å bestemme vindstyrke?",
            "Om været",
            listOf("Fahrenheit-skalaen", "Richterskalaen", "Beaufortskalaen", "Saffir-Simpson-skalaen"),
            2),
        Question(3,
            "Hva er den høyeste vindstyrken på vindskalaen?",
            "Om været",
            listOf("Storm", "Tornado", "Kuling", "Orkan"),
            3),
        Question(4,
            "Hvilket vær forbindes ofte med høytrykk?",
            "Om været",
            listOf("Stille", "Storm", "Regn", "Orkan"),
            0),
        Question(5,
            "Hvor mange grader Celsius tilsvarer 0 grader Fahrenheit?",
            "Om været",
            listOf("32°C", "27.4°C", "-17.8°C", "-24°C"),
            2),
        Question(6,
            "Hvor mange grader Fahrenheit tilsvarer 0 grader Celsius?",
            "Om været",
            listOf("32°C", "27.4°C", "-17.8°C", "-24°C"),
            0),
        Question(7,
            "Hva er den vanligste typen skyer i Norge?",
            "Om været",
            listOf("Cumulonimbus-skyer", "Cirrus-skyer", "Altostratus-skyer", "Stratus-skyer"),
            3))

    val warningQuestions= listOf(
        Question(8,
            "Hvilken av disse gir indikasjon på rødt farevarsel?",
            "Farevarsler",
            listOf("Lett snø", "Orkan", "2 mm regn", "Grå skyer"),
            1),
        Question(9,
            "Hvilken av disse gir indikasjon på rødt farevarsel?",
            "Farevarsler",
            listOf("Tordenstorm", "Lett tåke", "Alvorlig jordskjelv", "Sterk vind"),
            2),
        Question(10,
            "Hvilken av disse gir indikasjon på rødt farevarsel?",
            "Farevarsler",
            listOf("Kraftig tåke", "Eksplosjoner", "Kraftig regn", "Alvorlig luftforurensning"),
            3),
        Question(11,
            "Hvilken av disse gir indikasjon på gult farevarsel?",
            "Farevarsler",
            listOf("Kraftig vind", "Orkan", "Moderat mengde snø", "Solskinn"),
            2),
        Question(12,
            "Hvilken av disse gir indikasjon på gult farevarsel?",
            "Farevarsler",
            listOf("Lett bris", "Tsunami", "Kraftig tordenvær", "Snøstorm"),
            0),
        Question(13,
            "Hvilken av disse gir indikasjon på gult farevarsel?",
            "Farevarsler",
            listOf("Ekstrem hete", "Moderat regn", "Alvorlig luftforurensning", "Kraftig jordskjelv"),
            2),
        Question(14,
            "Hvilken av disse er en av de vanligste årsakene til at meterologiske tjenester utsender farevarsel for kraftige vindkast?",
            "Farevarsler",
            listOf("Sterke stormsystemer", "Vind fra fjell- eller delstrøk", "Økt solaktivitet", "Tørkeperioder"),
            0))


    private val allQuestions: List<Question> = weatherQuestions + warningQuestions
}
