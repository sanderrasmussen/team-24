package no.uio.ifi.IN2000.team24_app.data.bankRepository

class Bank : BankAccountManager {
    override var sum: Int = 5000

    override fun bankBalance(): Int {
        return sum
    }

    override fun withdrawMoney(withdrawAmount: Int) {
        sum -= withdrawAmount // Subtract withdrawAmount from sum
    }

    override fun deposit(depositSum: Int) {
        sum += depositSum // Add depositSum to sum
    }
}

interface BankAccountManager {
    var sum: Int
    fun bankBalance(): Int
    fun withdrawMoney(withdrawAmount: Int)
    fun deposit(depositSum: Int)
}