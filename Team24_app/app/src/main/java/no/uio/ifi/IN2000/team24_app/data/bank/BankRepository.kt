package no.uio.ifi.IN2000.team24_app.data.bank

import android.app.Application
import android.content.Context
import androidx.annotation.WorkerThread
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.Flow
import no.uio.ifi.IN2000.team24_app.data.database.AppDatabase
import no.uio.ifi.IN2000.team24_app.data.database.Bank
import no.uio.ifi.IN2000.team24_app.data.database.BankDao

class BankRepository(private val bankDao : BankDao)  {
    val bankAccount : Flow<Bank> = bankDao.get()
    fun buildDatabase(context: Context): AppDatabase {
        val database = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "Database"
        ).build()

        return database
    }




    @WorkerThread
    suspend fun withdraw(context: Context, sum : Int) {
        //var balance = Bank(0,1)
        //val database = buildDatabase(context)
        bankDao.withdraw(sum)

    }
    @WorkerThread
    suspend fun deposit(context: Context, sum : Int)  {
        //var balance = Bank(0,1)
        //val database = buildDatabase(context)
        bankDao.deposit(sum)

    }
    @WorkerThread
    suspend fun getBankBalance(context: Context) : Flow<Bank> {
        return bankAccount
    }
}
