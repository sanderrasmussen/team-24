package no.uio.ifi.IN2000.team24_app.data.bankTest

import kotlinx.coroutines.runBlocking
import no.uio.ifi.IN2000.team24_app.data.database.Bank
import no.uio.ifi.IN2000.team24_app.data.database.BankDao
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class bankRepoTest {

    @Mock
    private lateinit var mockBankDao: BankDao

    private lateinit var mockBankRepository: MockBankRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        mockBankRepository = MockBankRepository(mockBankDao)
    }
    @Test
    fun getBankBalance(){
        runBlocking {
            val balance = mockBankRepository.getBankBalance()
            assertNotNull(balance)
            assertEquals(balance,0)
        }
    }
    @Test
    fun depositAndWithdrawMoney(){
        runBlocking {
            val sum = 100
            mockBankRepository.deposit(sum)
            var balance = mockBankRepository.getBankBalance()
            assertEquals(balance , sum)

            mockBankRepository.withdraw(sum)
            balance= mockBankRepository.getBankBalance()
            assertEquals(balance, 0)
        }
    }


}