package no.uio.ifi.IN2000.team24_app.data.bank

import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
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

    /**
     * Withdraws a sum from the bank account if the balance is sufficient.
     * @param sum The sum to withdraw.
     */
    suspend fun withdraw( sum : Int) {
        var balance = getBankBalance()
        if (balance-sum >= 0 ){
            bankDao.withdraw(sum)
        }
    }

    /**
     * Deposits a sum to the bank account.
     * @param sum The sum to deposit.
     */
    fun deposit( sum : Int)  {
        bankDao.deposit(sum)
    }

    //this method has to be called within a corutine
    /**
     * Gets the bank balance.
     * @return The bank balance.
     */
    suspend fun getBankBalance(): Int {
        try {
            return withContext(Dispatchers.IO) {
                val bankBalance = bankDao.get()
                if (bankBalance.isNotEmpty()) {
                    return@withContext bankBalance[0].balance
                } else {
                    Log.e(TAG, "No bank records found")
                    return@withContext -1
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "An error occurred while getting bank balance: ${e.message}", e)
            return -1
        }
    }

}
