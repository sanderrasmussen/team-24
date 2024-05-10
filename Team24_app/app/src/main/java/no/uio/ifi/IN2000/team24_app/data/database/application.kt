package no.uio.ifi.IN2000.team24_app.data.database

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.uio.ifi.IN2000.team24_app.R
import no.uio.ifi.IN2000.team24_app.data.character.long_sleeve
import no.uio.ifi.IN2000.team24_app.data.character.pants
import no.uio.ifi.IN2000.team24_app.data.character.short_hair

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

class AppDatabaseCallback : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {

        super.onCreate(db)
        CoroutineScope(Dispatchers.IO).launch {
            val database = MyDatabase.getInstance()
            val clothesDao = database.clothesDao()


            database.bankDao().insertAll(Bank(50))


            val clothingList = listOf(
                // Heads
                Clothes(R.drawable.head_sunglasses, "Sunglasses", 25, 10, R.drawable.head_sunglasses, "head", false),
                Clothes(R.drawable.head_warm_hat, "Grey hat", 5, 10, R.drawable.alt_head_warm_hat, "head", false),
                Clothes(R.drawable.head_warm_blue_hat, "Blue hat", 5, 20, R.drawable.alt_head_warm_blue_hat, "head", false),
                Clothes(R.drawable.head_short_hair, "Short Hair", 25, 30, R.drawable.alt_head_short_hair, "head", true),
                Clothes(R.drawable.head_short_hair_hat, "Hat", 0, 30, R.drawable.alt_head_short_hair_hat, "head", false),

                // Torsos
                Clothes(R.drawable.torso_long_sleeves, "Long Sleeve", 15,25, R.drawable.alt_torso_long_sleeve, "torso", true),
                Clothes(R.drawable.torso_short_sleeves,"Short Sleeve", 25,  15, R.drawable.alt_torso_short_sleeve, "torso", false),
                Clothes(R.drawable.torso_long_warm_jacket, "Blue jacket", 0, 15, R.drawable.alt_torso_long_warm_jacket,"torso", false),
                Clothes(R.drawable.torso_short_sleeves_shirt,"Short Sleeve Shirt", 25,  15, R.drawable.alt_torso_short_sleeves_shirt, "torso", false),
                Clothes(R.drawable.torso_short_sleeves_leaves,"Short Sleeve Shirt Leaves", 25,  15, R.drawable.alt_torso_short_sleeves_leaves, "torso", false),
                Clothes(R.drawable.torso_long_sleeves_hoodie,"Long Sleeve Hoodie", 5, 15, R.drawable.alt_torso_long_sleeves_hoodie, "torso", false),
                Clothes(R.drawable.torso_long_sleeves_jacket,"Long Sleeve Jacket", 0, 25, R.drawable.alt_torso_long_sleeves_jacket, "torso", false),
                Clothes(R.drawable.torso_long_sleeves_raincoat,"Long Sleeve Raincoat", 5, 25, R.drawable.alt_torso_long_sleeves_raincoat, "torso", false),
                Clothes(R.drawable.torso_long_sleeves_warm_coat,"Long Sleeve Warm Coat", -10, 25, R.drawable.alt_torso_long_sleeves_warm_coat, "torso", false),
                Clothes(R.drawable.torso_long_sleeves_warm_jacket,"Long Sleeve Warm Jacket", -10, 25, R.drawable.alt_torso_long_sleeves_warm_jacket, "torso", false),

                // Legs
                Clothes(R.drawable.legs_warm_pants, "Blue pants", -10,20, R.drawable.legs_warm_pants, "legs" , false),
                Clothes(R.drawable.legs_warm_green_pants, "Green pants", -10, 20, R.drawable.legs_warm_green_pants, "legs", false ),
                Clothes(R.drawable.legs_shorts_pink,"Pink Shorts", 25,  15, R.drawable.alt_legs_shorts_pink, "legs", false),
                Clothes(R.drawable.legs_pants_black,"Black Pants", 5,  15, R.drawable.alt_legs_pants_black, "legs", false),
                Clothes(R.drawable.legs_pants, "Pants", 5, 25, R.drawable.alt_legs_pants, "legs", true),
                Clothes(R.drawable.legs_shorts, "Shorts", 25, 15, R.drawable.alt_legs_shorts, "legs", false),

                )
            clothesDao.insertAll(*clothingList.toTypedArray())


            val equippedClothes = EquipedClothes(
                0,
                short_hair.imageAsset,
                long_sleeve.imageAsset,
                pants.imageAsset,
                System.currentTimeMillis(),
                15 //initial value of 15 at first login
            )
            clothesDao.insertEquipedClothesTable(equippedClothes)
            clothesDao.updateDate()

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

class application : Application() {
    override fun onCreate() {
        super.onCreate()
        MyDatabase.initialize(this)
    }
}