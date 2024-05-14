package no.uio.ifi.IN2000.team24_app.data.bankTest

import no.uio.ifi.IN2000.team24_app.data.database.BankDao

class MockBankRepository(private val bankDao: BankDao) {

    /**
     * Withdraws a sum from the bank account if the balance is sufficient.
     * @param sum The sum to withdraw.
     */
    suspend fun withdraw(sum: Int) {
        var balance = getBankBalance()
        if (balance - sum >= 0) {
            bankDao.withdraw(sum)
        }
    }

    /**
     * Deposits a sum to the bank account.
     * @param sum The sum to deposit.
     */
    fun deposit(sum: Int) {
        bankDao.deposit(sum)
    }

    //this method has to be called within a coroutine
    /**
     * Gets the bank balance.
     * @return The bank balance.
     */
    suspend fun getBankBalance(): Int {
        try {
            return bankDao.get().first().balance
        } catch (e: Exception) {
            e.printStackTrace()
            return -1
        }
    }
}
