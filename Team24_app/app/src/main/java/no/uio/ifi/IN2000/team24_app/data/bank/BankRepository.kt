package no.uio.ifi.IN2000.team24_app.data.bank

import android.app.Application
import android.content.Context
import androidx.annotation.WorkerThread
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import no.uio.ifi.IN2000.team24_app.data.database.AppDatabase
import no.uio.ifi.IN2000.team24_app.data.database.Bank
import no.uio.ifi.IN2000.team24_app.data.database.BankDao
import no.uio.ifi.IN2000.team24_app.data.database.MyDatabase

class BankRepository()  {
    private val db = MyDatabase.getInstance()
    private val bankDao = db.bankDao()



    suspend fun withdraw( sum : Int) {
        //var balance = Bank(0,1)
        //val database = buildDatabase(context)
        bankDao.withdraw(sum)

    }

    suspend fun deposit( sum : Int)  {
        //var balance = Bank(0,1)
        //val database = buildDatabase(context)
        bankDao.deposit(sum)

    }

    fun getBankBalance() : Int { // this: CoroutineScope

        val balance = bankDao.get().get(0).balance

        return balance
    }



}
