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
            .addCallback(AppDatabaseCallback())
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
            val categoryDao = database.categoryDao()
            val questionDao = database.questionDao()


            database.bankDao().insertAll(Bank(50))

            val clothingList = listOf(

                // Heads
                Clothes(R.drawable.head_sunglasses, "Sunglasses", 25, 10, R.drawable.head_sunglasses, "head", false),
                Clothes(R.drawable.head_short_hair_glasses, "Glasses", 25, 10, R.drawable.alt_head_short_hair_glasses, "head", false),
                Clothes(R.drawable.head_warm_hat, "Grey hat", 5, 10, R.drawable.alt_head_warm_hat, "head", false),
                Clothes(R.drawable.head_warm_blue_hat, "Blue hat", 5, 20, R.drawable.alt_head_warm_blue_hat, "head", false),
                Clothes(R.drawable.head_short_hair, "Short Hair", 25, 30, R.drawable.alt_head_short_hair, "head", true),
                Clothes(R.drawable.head_short_hair_hat, "Hat", 0, 30, R.drawable.alt_head_short_hair_hat, "head", false),
                Clothes(R.drawable.head_short_hair_yellow_hat, "Short hair yellow hat", 5, 20, R.drawable.alt_head_short_hair_yellow_hat, "head", false),
                Clothes(R.drawable.head_short_hair_blue_hat, "Short hair blue hat", 5, 30, R.drawable.alt_head_short_hair_blue_hat, "head", false),
                Clothes(R.drawable.head_short_hair_sunglasses, "Other sunglasses", 25, 25, R.drawable.alt_head_short_hair_sunglasses, "head", false),

                // Torsos
                Clothes(R.drawable.torso_long_sleeves, "Long sleeve top", 5,25, R.drawable.alt_torso_long_sleeve, "torso", true),
                Clothes(R.drawable.torso_short_sleeves,"Short sleeve", 20,  15, R.drawable.alt_torso_short_sleeve, "torso", false),
                Clothes(R.drawable.torso_long_warm_jacket, "Blue jacket", 0, 15, R.drawable.alt_torso_long_warm_jacket,"torso", false),
                Clothes(R.drawable.torso_short_sleeves_shirt,"Short sleeve shirt", 20,  15, R.drawable.alt_torso_short_sleeves_shirt, "torso", false),
                Clothes(R.drawable.torso_short_sleeves_leaves,"Short sleeve shirt leaves", 25,  15, R.drawable.alt_torso_short_sleeves_leaves, "torso", false),
                Clothes(R.drawable.torso_long_sleeves_hoodie,"Long sleeve hoodie", 5, 20, R.drawable.alt_torso_long_sleeves_hoodie, "torso", false),
                Clothes(R.drawable.torso_long_sleeves_jacket,"Long sleeve jacket", 0, 25, R.drawable.alt_torso_long_sleeves_jacket, "torso", false),
                Clothes(R.drawable.torso_long_sleeves_raincoat,"Long sleeve raincoat", 5, 25, R.drawable.alt_torso_long_sleeves_raincoat, "torso", false),
                Clothes(R.drawable.torso_long_sleeves_warm_coat,"Long sleeve warm coat", -10, 25, R.drawable.alt_torso_long_sleeves_warm_coat, "torso", false),
                Clothes(R.drawable.torso_long_sleeves_warm_jacket,"Long sleeve warm jacket", -10, 25, R.drawable.alt_torso_long_sleeves_warm_jacket, "torso", false),
                Clothes(R.drawable.torso_long_sleeves_pajamas, "Long sleeve pajama top", 5, 30, R.drawable.alt_torso_long_sleeves_pajamas, "torso", false),
                Clothes(R.drawable.torso_tank_top, "Tank top", 30, 30, R.drawable.alt_torso_tank_top, "torso", false),
                Clothes(R.drawable.torso_puffer_jacket, "Long sleeve puffer jacket", -5, 20, R.drawable.alt_torso_puffer_jacket, "torso", false),
                Clothes(R.drawable.torso_short_sleeves_duck, "Short sleeve duck top", 20, 20, R.drawable.alt_torso_short_sleeves_duck, "torso", false),


                // Legs
                Clothes(R.drawable.legs_warm_pants, "Blue pants", -10,20, R.drawable.legs_warm_pants, "legs" , false),
                Clothes(R.drawable.legs_warm_green_pants, "Green pants", -10, 20, R.drawable.legs_warm_green_pants, "legs", false ),
                Clothes(R.drawable.legs_shorts_pink,"Pink Shorts", 25,  15, R.drawable.alt_legs_shorts_pink, "legs", false),
                Clothes(R.drawable.legs_pants_black,"Black Pants", 5,  15, R.drawable.alt_legs_pants_black, "legs", false),
                Clothes(R.drawable.legs_pants, "Pants", 5, 25, R.drawable.alt_legs_pants, "legs", true),
                Clothes(R.drawable.legs_shorts, "Shorts", 25, 15, R.drawable.alt_legs_shorts, "legs", false),
                Clothes(R.drawable.legs_brown_pants_pockets, "Shorts", 0, 15, R.drawable.alt_legs_brown_pants_pockets, "legs", false),
                Clothes(R.drawable.legs_pants_pajamas, "Pajama pants", 5, 30, R.drawable.alt_legs_pants_pajamas, "legs", false),

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


            val weatherCategory = Category("Om været")
            val warningCategory = Category("Farevarsler")
            categoryDao.insertAll(weatherCategory, warningCategory)

            //val oeving = Category("Farevarsler", 5, false)
            //MyDatabase.getInstance().categoryDao().insertAll(oeving)

            // questions:

            // weather questions:
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

            questionDao.insertAll(*weatherQuestions.toTypedArray())
            println("WeatherQuestions: $weatherQuestions")

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

                   // questionDao.insertAll(*warningQuestions.toTypedArray())
                println("Warning: $warningQuestions")

        }

    }

}


class application : Application() {
    override fun onCreate() {
        super.onCreate()
        MyDatabase.initialize(this)
    }
}
