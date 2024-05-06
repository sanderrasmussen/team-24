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
            val paint_head : Clothes = Clothes(R.drawable.paint_head, "Paint", 0, 10000, R.drawable.paint_head_alt,"head",false)
            MyDatabase.getInstance().clothesDao().insertAll(short_hair)
            MyDatabase.getInstance().clothesDao().insertAll(paint_head)

            //torsos:
            val paintTorso = Clothes(R.drawable.paint_torso,"Paint", 0,  10000, R.drawable.paint_torso_alt,"torso", false )
            val long_sleeve = Clothes(R.drawable.torso_long_sleeves, "Long Sleeve", 5,25, R.drawable.alt_torso_long_sleeve, "torso", true)
            val short_sleeve = Clothes(R.drawable.torso_short_sleeves,"Short Sleeve", 25,  15, R.drawable.alt_torso_short_sleeve, "torso", true)
            MyDatabase.getInstance().clothesDao().insertAll(paintTorso)
            MyDatabase.getInstance().clothesDao().insertAll(long_sleeve)
            MyDatabase.getInstance().clothesDao().insertAll(short_sleeve)

            //legs:
            val paintLegs = Clothes(R.drawable.paint_legs,"Paint", 0,  10000, R.drawable.paint_legs_alt, "legs", false)
            val pants = Clothes(R.drawable.legs_pants,"Pants", 5,  25, R.drawable.alt_legs_pants, "legs", true)
            val shorts = Clothes(R.drawable.legs_shorts,"Shorts", 25,  15, R.drawable.alt_legs_shorts, "legs", true)
            MyDatabase.getInstance().clothesDao().insertAll(paintLegs)
            MyDatabase.getInstance().clothesDao().insertAll(pants)
            MyDatabase.getInstance().clothesDao().insertAll(shorts)

            //setting inital selected clothing
            //inserting equiped clothes table
            val equipedClothes = EquipedClothes(
                0,
                short_hair.imageAsset,
                long_sleeve.imageAsset,
                pants.imageAsset,
                Date() ,
                15 //initial value of 15 at first login
            )
            MyDatabase.getInstance().clothesDao().insertEquipedClothesTable(equipedClothes)
            MyDatabase.getInstance().clothesDao().updateDate()
        }
    }
}


    //private val db = MyDatabase.getInstance()
