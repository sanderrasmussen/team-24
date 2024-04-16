package no.uio.ifi.IN2000.team24_app.data.database

import android.app.Application
import no.uio.ifi.IN2000.team24_app.data.bank.BankRepository

class application : Application() {
    private val database by lazy {AppDatabase.getDatabase(this)}
    val bankRepo by lazy { BankRepository(database.bankDao()) }
}


