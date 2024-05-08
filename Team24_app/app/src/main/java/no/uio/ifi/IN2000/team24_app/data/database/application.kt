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
            val farevarsler = Category("Farevarsler")
            val oeving = Category("Farevarsler", 10, false)
            MyDatabase.getInstance().categoryDao().insertAll(omVaeret)
            MyDatabase.getInstance().categoryDao().insertAll(farevarsler)
            MyDatabase.getInstance().categoryDao().insertAll(oeving)

        }
    }
}


    //private val db = MyDatabase.getInstance()
