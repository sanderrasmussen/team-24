package no.uio.ifi.IN2000.team24_app.data.database

import android.app.Application
import android.content.Context
import androidx.room.Room
import no.uio.ifi.IN2000.team24_app.data.bank.BankRepository


object MyDatabase {
    private var roomDb: AppDatabase? = null

    fun initialize(context: Context) {
        roomDb = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "AppDatabase"
        ).build()
    }

    fun getInstance(): AppDatabase = roomDb!!
}
open class application: Application() {
    override fun onCreate() {
        super.onCreate()
        MyDatabase.initialize(this.applicationContext)
    }
}


    //private val db = MyDatabase.getInstance()
