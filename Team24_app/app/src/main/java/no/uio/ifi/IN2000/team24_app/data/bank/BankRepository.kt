package no.uio.ifi.IN2000.team24_app.data.bank

import android.app.Application
import android.content.Context
import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import no.uio.ifi.IN2000.team24_app.data.database.AppDatabase
import no.uio.ifi.IN2000.team24_app.data.database.Bank
import no.uio.ifi.IN2000.team24_app.data.database.BankDao
import no.uio.ifi.IN2000.team24_app.data.database.MyDatabase

class BankRepository()  {
    private val db = MyDatabase.getInstance()
    private val bankDao = db.bankDao()

    suspend fun withdraw( sum : Int) {
        var balance = getBankBalance()
        if (balance-sum >= 0 ){
            bankDao.withdraw(sum)
        }
    }

    suspend fun deposit( sum : Int)  {
        bankDao.deposit(sum)
    }

    //this method has to be called within a corutine
    suspend fun getBankBalance(): Int {
        return withContext(Dispatchers.IO) {
            return@withContext bankDao.get().get(0).balance
        }
    }

}
