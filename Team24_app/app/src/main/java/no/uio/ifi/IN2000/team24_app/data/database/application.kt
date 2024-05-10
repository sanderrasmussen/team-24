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
import no.uio.ifi.IN2000.team24_app.data.bank.BankRepository
import no.uio.ifi.IN2000.team24_app.data.character.Head
import no.uio.ifi.IN2000.team24_app.data.character.Legs
import no.uio.ifi.IN2000.team24_app.data.character.Torso
import java.util.Date


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
            //bank table:
            MyDatabase.getInstance().bankDao().insertAll(Bank(1000))

            //clothing tables:

            //heads:
            val short_hair :Clothes= Clothes(R.drawable.head_short_hair,"Short Hair", 25,  30, R.drawable.alt_head_short_hair,"head",true)
            val paint_head : Clothes = Clothes(R.drawable.paint_head, "Paint", 0, 1000, R.drawable.paint_head_alt,"head",false)
            val sunglasses = Clothes(R.drawable.head_sunglasses, "Sunglasses", heatValue = 25, 10, R.drawable.head_sunglasses, "head", false)
            val grey_hat = Clothes(R.drawable.head_warm_hat, "Grey hat", 5, 10, R.drawable.alt_head_warm_hat, "head", false)
            val blue_hat = Clothes(R.drawable.head_warm_blue_hat, "Blue hat", 5,20,R.drawable.alt_head_warm_blue_hat, "head", false)
            val blue_hair = Clothes(R.drawable.head_short_blue_hair, "Blue Hair", 25, 20, R.drawable.head_short_blue_hair, "head", false)
            MyDatabase.getInstance().clothesDao().insertAll(short_hair)
            MyDatabase.getInstance().clothesDao().insertAll(paint_head)
            MyDatabase.getInstance().clothesDao().insertAll(sunglasses)
            MyDatabase.getInstance().clothesDao().insertAll(grey_hat)
            MyDatabase.getInstance().clothesDao().insertAll(blue_hat)
            MyDatabase.getInstance().clothesDao().insertAll(blue_hair)

            //torsos:
            val paintTorso = Clothes(R.drawable.paint_torso,"Paint", 15,  1000, R.drawable.paint_torso_alt,"torso", false )
            val long_sleeve = Clothes(R.drawable.torso_long_sleeves, "Long Sleeve", 15,25, R.drawable.alt_torso_long_sleeve, "torso", true)
            val short_sleeve = Clothes(R.drawable.torso_short_sleeves,"Short Sleeve", 25,  15, R.drawable.alt_torso_short_sleeve, "torso", false)
            val blue_jacket = Clothes(R.drawable.torso_long_warm_jacket, "Blue jacket", -5, 15, R.drawable.alt_torso_long_warm_jacket,"torso", false)
            val green_jacket = Clothes(R.drawable.torso_long_green_warm_jacket, "Green jacket", -5,15, R.drawable.alt_torso_long_green_warm_jacket, "torso", false)
            val red_jacket = Clothes(R.drawable.torso_long_red_warm_jacket, "Red jacket", -5, 15, R.drawable.alt_torso_long_warm_red_jacket, "torso", false)

            MyDatabase.getInstance().clothesDao().insertAll(paintTorso)
            MyDatabase.getInstance().clothesDao().insertAll(long_sleeve)
            MyDatabase.getInstance().clothesDao().insertAll(short_sleeve)
            MyDatabase.getInstance().clothesDao().insertAll(blue_jacket)
            MyDatabase.getInstance().clothesDao().insertAll(green_jacket)
            MyDatabase.getInstance().clothesDao().insertAll(red_jacket)

            //legs:
            val paintLegs = Clothes(R.drawable.paint_legs,"Paint", 0,  1000, R.drawable.paint_legs_alt, "legs", false)
            val pants = Clothes(R.drawable.legs_pants,"Pants", 5,  25, R.drawable.alt_legs_pants, "legs", true)
            val shorts = Clothes(R.drawable.legs_shorts,"Shorts", 25,  15, R.drawable.alt_legs_shorts, "legs", false)
            val blue_pants = Clothes(R.drawable.legs_warm_pants, "Blue pants", -10,20, R.drawable.legs_warm_pants, "legs" , false)
            val green_pants = Clothes(R.drawable.legs_warm_green_pants, "Green pants", -10, 20, R.drawable.legs_warm_green_pants, "legs", false )
            val red_pants = Clothes(R.drawable.legs_warm_red_pants, "Red pants", -10, 20, R.drawable.legs_warm_red_pants, "legs", false)
            MyDatabase.getInstance().clothesDao().insertAll(paintLegs)
            MyDatabase.getInstance().clothesDao().insertAll(pants)
            MyDatabase.getInstance().clothesDao().insertAll(shorts)
            MyDatabase.getInstance().clothesDao().insertAll(blue_pants)
            MyDatabase.getInstance().clothesDao().insertAll(green_pants)
            MyDatabase.getInstance().clothesDao().insertAll(red_pants)

            //setting inital selected clothing
            //inserting equiped clothes table
            val equipedClothes = EquipedClothes(
                0,
                short_hair.imageAsset,
                long_sleeve.imageAsset,
                pants.imageAsset,
                System.currentTimeMillis(),
                15 //initial value of 15 at first login
            )
            MyDatabase.getInstance().clothesDao().insertEquipedClothesTable(equipedClothes)
            MyDatabase.getInstance().clothesDao().updateDate()
        }
    }
}


    //private val db = MyDatabase.getInstance()
