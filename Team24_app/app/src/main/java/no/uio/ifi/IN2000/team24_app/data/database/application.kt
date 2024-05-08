package no.uio.ifi.IN2000.team24_app.data.database

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.uio.ifi.IN2000.team24_app.data.bank.BankRepository


object MyDatabase {
    private var roomDb: AppDatabase? = null

    fun initialize(context: Context) {
        roomDb = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "AppDatabase"
        )
            .addCallback(AppDatabaseCallback()) // Flyttet til dette punktet
            .build()
    }

    fun getInstance(): AppDatabase = roomDb!!
}
open class application: Application() {
    override fun onCreate() {
        super.onCreate()
        MyDatabase.initialize(this.applicationContext)
    }
}

class AppDatabaseCallback : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {

        super.onCreate(db)

        // Utfør handlinger når databasen blir opprettet for første gang
        CoroutineScope(Dispatchers.IO).launch {

            MyDatabase.getInstance().bankDao().insertAll(Bank(50))

            // quiz tables:

            // categories:
            val omVaeret = Category("Om været")
            MyDatabase.getInstance().categoryDao().insertAll(omVaeret)

            val farevarsler = Category("Farevarsler")
            MyDatabase.getInstance().categoryDao().insertAll(farevarsler)

            val oeving = Category("Farevarsler", 5, false)
            MyDatabase.getInstance().categoryDao().insertAll(oeving)

            // questions:

            // weather questions:
            val vaerSpoersmaal1 = Question(

                "Hva er den kaldeste temperaturen som noensinne har blitt registrert?",
                "Om været",
                listOf("-89.2°C", "-61.8°C", "-100°C", "-78.9°C"),
                0

            )
            MyDatabase.getInstance().questionDao().insertAll(vaerSpoersmaal1)

            val vaerSpoersmaal2 = Question(

                "Hvilken landsdel får mest regn i løpet av ett år?",
                "Om været",
                listOf("Nord-Norge", "Østlandet", "Vestlandet", "Sørlandet"),
                2

            )
            MyDatabase.getInstance().questionDao().insertAll(vaerSpoersmaal2)

            val vaerSpoersmaal3 = Question(

                "Hva heter skalaen som norske meteorologer bruker for å bestemme vindstyrke?",
                "Om været",
                listOf("Fahrenheit-skalaen", "Richterskalaen", "Beaufortskalaen", "Saffir-Simpson-skalaen"),
                2

            )
            MyDatabase.getInstance().questionDao().insertAll(vaerSpoersmaal3)

            val vaerSpoersmaal4 = Question(

                "Hva er den høyeste vindstyrken på vindskalaen?",
                "Om været",
                listOf("Storm", "Tornado", "Kuling", "Orkan"),
                3

            )
            MyDatabase.getInstance().questionDao().insertAll(vaerSpoersmaal4)

            val vaerSpoersmaal5 = Question(

                "Hvilket vær forbindes ofte med høytrykk?",
                "Om været",
                listOf("Stille", "Storm", "Regn", "Orkan"),
                0

            )
            MyDatabase.getInstance().questionDao().insertAll(vaerSpoersmaal5)

            val vaerSpoersmaal6 = Question(

                "Hvor mange grader Celsius tilsvarer 0 grader Fahrenheit?",
                "Om været",
                listOf("32°C", "27.4°C", "-17.8°C", "-24°C"),
                2

            )
            MyDatabase.getInstance().questionDao().insertAll(vaerSpoersmaal6)

            val vaerSpoersmaal7 = Question(

                "Hvor mange grader Fahrenheit tilsvarer 0 grader Celsius?",
                "Om været",
                listOf("32°C", "27.4°C", "-17.8°C", "-24°C"),
                0

            )
            MyDatabase.getInstance().questionDao().insertAll(vaerSpoersmaal7)

            val vaerSpoersmaal8 = Question(

                "Hva er den vanligste typen skyer i Norge?",
                "Om været",
                listOf("Cumulonimbus-skyer", "Cirrus-skyer", "Altostratus-skyer", "Stratus-skyer"),
                3

            )
            MyDatabase.getInstance().questionDao().insertAll(vaerSpoersmaal8)

            // farevarsel questions:
            val roedtFarevarselSpoersmaal1 = Question(

                "Hvilken av disse gir indikasjon på rødt farevarsel?",
                "Farevarsler",
                listOf("Lett snø", "Orkan", "2 mm regn", "Grå skyer"),
                1

            )
            MyDatabase.getInstance().questionDao().insertAll(roedtFarevarselSpoersmaal1)

            val roedtFarevarselSpoersmaal2 = Question(

                "Hvilken av disse gir indikasjon på rødt farevarsel?",
                "Farevarsler",
                listOf("Tordenstorm", "Lett tåke", "Alvorlig jordskjelv", "Sterk vind"),
                2

            )
            MyDatabase.getInstance().questionDao().insertAll(roedtFarevarselSpoersmaal2)

            val roedtFarevarselSpoersmaal3 = Question(

                "Hvilken av disse gir indikasjon på rødt farevarsel?",
                "Farevarsler",
                listOf("Kraftig tåke", "Eksplosjoner", "Kraftig regn", "Alvorlig luftforurensning"),
                3

            )
            MyDatabase.getInstance().questionDao().insertAll(roedtFarevarselSpoersmaal3)

            val gultFarevarselSpoersmaal1 = Question(

                "Hvilken av disse gir indikasjon på gult farevarsel?",
                "Farevarsler",
                listOf("Kraftig vind", "Orkan", "Moderat mengde snø", "Solskinn"),
                2

            )
            MyDatabase.getInstance().questionDao().insertAll(gultFarevarselSpoersmaal1)

            val gultFarevarselSpoersmaal2 = Question(

                "Hvilken av disse gir indikasjon på gult farevarsel?",
                "Farevarsler",
                listOf("Lett bris", "Tsunami", "Kraftig tordenvær", "Snøstorm"),
                0

            )
            MyDatabase.getInstance().questionDao().insertAll(gultFarevarselSpoersmaal2)

            val gultFarevarselSpoersmaal3 = Question(

                "Hvilken av disse gir indikasjon på gult farevarsel?",
                "Farevarsler",
                listOf("Ekstrem hete", "Moderat regn", "Alvorlig luftforurensning", "Kraftig jordskjelv"),
                2

            )
            MyDatabase.getInstance().questionDao().insertAll(gultFarevarselSpoersmaal3)

            val vindkastFarevarselAarsakerSpoersmaal = Question(

                "Hvilken av disse er en av de vanligste årsakene til at meterologiske tjenester utsender farevarsel for kraftige vindkast?",
                "Farevarsler",
                listOf("Sterke stormsystemer", "Vind fra fjell- eller delstrøk", "Økt solaktivitet", "Tørkeperioder"),
                0

            )
            MyDatabase.getInstance().questionDao().insertAll(vindkastFarevarselAarsakerSpoersmaal)

        }

    }

}


    //private val db = MyDatabase.getInstance()
