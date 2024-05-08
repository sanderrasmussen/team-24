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
