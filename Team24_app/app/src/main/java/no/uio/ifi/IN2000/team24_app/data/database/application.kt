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
    //this is a singleton object,
    // which means that there will only be one instacne in the app that is accessed by all the repoes that needs it
    private var roomDb: AppDatabase? = null

    fun initialize(context: Context) {
        roomDb = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "AppDatabase"
        )
            .addCallback(AppDatabaseCallback()) //prepopulating the database
            .build() //build the database
    }

    fun getInstance(): AppDatabase = roomDb!!
}

class AppDatabaseCallback : RoomDatabase.Callback() {
    //this callback method is used to populate the singleton database
    override fun onCreate(db: SupportSQLiteDatabase) {

        super.onCreate(db)
        //prepopulating the database/ inserting tables with default values the first time the app is started with no stored data.
        CoroutineScope(Dispatchers.IO).launch {
            val database = MyDatabase.getInstance()
            val clothesDao = database.clothesDao()
            val categoryDao = database.categoryDao()
            val questionDao = database.questionDao()

            database.bankDao().insertAll(Bank(50))
            database.bankDao().insertAll(Bank(50))

            val clothingList = listOf(

                // Heads
                Clothes(R.drawable.head_sunglasses, "Sunglasses", 25, 10, R.drawable.head_sunglasses, "head", false),
                Clothes(R.drawable.head_short_hair_glasses, "Glasses", 25, 10, R.drawable.alt_head_short_hair_glasses, "head", false),
                Clothes(R.drawable.head_warm_hat, "Grey hat", 0, 10, R.drawable.alt_head_warm_hat, "head", false),
                Clothes(R.drawable.head_warm_blue_hat, "Blue hat", 0, 20, R.drawable.alt_head_warm_blue_hat, "head", false),
                Clothes(R.drawable.head_short_hair, "Short Hair", 25, 30, R.drawable.alt_head_short_hair, "head", true),
                Clothes(R.drawable.head_short_hair_hat, "Hat", -5, 30, R.drawable.alt_head_short_hair_hat, "head", false),
                Clothes(R.drawable.head_short_hair_yellow_hat, "Short hair yellow hat", 0, 20, R.drawable.alt_head_short_hair_yellow_hat, "head", false),
                Clothes(R.drawable.head_short_hair_blue_hat, "Short hair blue hat", 0, 30, R.drawable.alt_head_short_hair_blue_hat, "head", false),
                Clothes(R.drawable.head_short_hair_sunglasses, "Other sunglasses", 25, 25, R.drawable.alt_head_short_hair_sunglasses, "head", false),

                // Torsos
                Clothes(R.drawable.torso_long_sleeves, "Long sleeve top", 10,25, R.drawable.alt_torso_long_sleeve, "torso", true),
                Clothes(R.drawable.torso_short_sleeves,"Short sleeve", 20,  15, R.drawable.alt_torso_short_sleeve, "torso", false),
                Clothes(R.drawable.torso_long_warm_jacket, "Blue jacket", -15, 15, R.drawable.alt_torso_long_warm_jacket,"torso", false),
                Clothes(R.drawable.torso_short_sleeves_shirt,"Short sleeve shirt", 25,  15, R.drawable.alt_torso_short_sleeves_shirt, "torso", false),
                Clothes(R.drawable.torso_short_sleeves_leaves,"Short sleeve shirt leaves", 25,  15, R.drawable.alt_torso_short_sleeves_leaves, "torso", false),
                Clothes(R.drawable.torso_long_sleeves_hoodie,"Long sleeve hoodie", 10, 20, R.drawable.alt_torso_long_sleeves_hoodie, "torso", false),
                Clothes(R.drawable.torso_long_sleeves_jacket,"Long sleeve jacket", 5, 25, R.drawable.alt_torso_long_sleeves_jacket, "torso", false),
                Clothes(R.drawable.torso_long_sleeves_raincoat,"Long sleeve raincoat", 0, 25, R.drawable.alt_torso_long_sleeves_raincoat, "torso", false),
                Clothes(R.drawable.torso_long_sleeves_warm_coat,"Long sleeve warm coat", -5, 25, R.drawable.alt_torso_long_sleeves_warm_coat, "torso", false),
                Clothes(R.drawable.torso_long_sleeves_warm_jacket,"Long sleeve warm jacket", -10, 25, R.drawable.alt_torso_long_sleeves_warm_jacket, "torso", false),
                Clothes(R.drawable.torso_long_sleeves_pajamas, "Long sleeve pajama top", 15, 30, R.drawable.alt_torso_long_sleeves_pajamas, "torso", false),
                Clothes(R.drawable.torso_tank_top, "Tank top", 30, 30, R.drawable.alt_torso_tank_top, "torso", false),
                Clothes(R.drawable.torso_puffer_jacket, "Long sleeve puffer jacket", -5, 20, R.drawable.alt_torso_puffer_jacket, "torso", false),
                Clothes(R.drawable.torso_short_sleeves_duck, "Short sleeve duck top", 20, 20, R.drawable.alt_torso_short_sleeves_duck, "torso", false),

                // Legs
                Clothes(R.drawable.legs_warm_pants, "Blue pants", -15,20, R.drawable.legs_warm_pants, "legs" , false),
                Clothes(R.drawable.legs_shorts_pink,"Pink Shorts", 25,  15, R.drawable.alt_legs_shorts_pink, "legs", false),
                Clothes(R.drawable.legs_pants_black,"Black Pants", 10,  15, R.drawable.alt_legs_pants_black, "legs", false),
                Clothes(R.drawable.legs_pants, "Pants", 10, 25, R.drawable.alt_legs_pants, "legs", true),
                Clothes(R.drawable.legs_shorts, "Shorts", 25, 15, R.drawable.alt_legs_shorts, "legs", false),
                Clothes(R.drawable.legs_brown_pants_pockets, "Shorts", 5, 15, R.drawable.alt_legs_brown_pants_pockets, "legs", false),
                Clothes(R.drawable.legs_pants_pajamas, "Pajama pants", 15, 30, R.drawable.alt_legs_pants_pajamas, "legs", false),

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
        }
    }
}
class application : Application() {
    override fun onCreate() {
        super.onCreate()
        MyDatabase.initialize(this)
    }
}



