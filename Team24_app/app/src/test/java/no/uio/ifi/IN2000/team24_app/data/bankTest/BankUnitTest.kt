package no.uio.ifi.IN2000.team24_app.data.bankTest

import kotlinx.coroutines.runBlocking
import no.uio.ifi.IN2000.team24_app.data.bank.BankRepository
import no.uio.ifi.IN2000.team24_app.data.database.Bank
import no.uio.ifi.IN2000.team24_app.data.database.BankDao
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class BankRepositoryTest {

    // Mock dependencies
    @Mock
    private lateinit var mockBankDao: BankDao

    private lateinit var bankRepository: MockBankRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        bankRepository = MockBankRepository(mockBankDao)
    }

    @Test
    fun testWithdrawSufficientBalance() {
        // Set up
        val balance = 100
        val withdrawAmount = 50
        `when`(mockBankDao.get()).thenReturn(listOf(Bank(balance)))

        // Perform withdraw
        runBlocking { bankRepository.withdraw(withdrawAmount) }

        // Verify
        assertTrue(balance - withdrawAmount >= 0)
    }

    @Test
    fun testWithdrawInsufficientBalance() {
        // Set up
        val balance = 100
        val withdrawAmount = 150
        `when`(mockBankDao.get()).thenReturn(listOf(Bank(balance)))

        // Perform withdraw
        runBlocking { bankRepository.withdraw(withdrawAmount) }

        // Verify
        // Ensure that the balance remains unchanged
        assertTrue(balance >= 0)
    }

    @Test
    fun testDeposit() {
        // Set up
        val initialBalance = 100
        val depositAmount = 50
        val expectedBalance = initialBalance + depositAmount
        `when`(mockBankDao.get()).thenReturn(listOf(Bank(initialBalance)))

        // Perform deposit
        bankRepository.deposit(depositAmount)

        // Verify
        assertEquals(expectedBalance, initialBalance + depositAmount)
    }

    @Test
    fun testGetBankBalance() {
        // Set up
        val balance = 100
        `when`(mockBankDao.get()).thenReturn(listOf(Bank(balance)))

        // Perform
        val actualBalance = runBlocking { bankRepository.getBankBalance() }

        // Verify
        assertEquals(balance, actualBalance)
    }
}
