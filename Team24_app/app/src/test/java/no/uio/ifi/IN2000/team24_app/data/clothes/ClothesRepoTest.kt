package no.uio.ifi.IN2000.team24_app.data.clothes

import kotlinx.coroutines.runBlocking
import no.uio.ifi.IN2000.team24_app.data.database.ClothesDao
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class clothesRepoTest {
    // Mock dependencies
    @Mock
    private lateinit var mockClothesDao: ClothesDao

    private lateinit var clothesRepo: MockClothesRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        clothesRepo = MockClothesRepository(mockClothesDao)
    }


    // I test that the response from the database is not null. The lists will be empty
    //as I am using mock repositories for testing. There should be noting inside the database
    @Test
    fun testGetAllOwnedHeads() = runBlocking {
        val ownedHeads = clothesRepo.getAllOwnedHeads()
        assertNotNull(ownedHeads)
    }

    @Test
    fun testGetAllOwnedTorsos() = runBlocking {
        val ownedTorsos = clothesRepo.getAllOwnedTorsos()
        assertNotNull(ownedTorsos)
    }
    @Test
    fun testGetAllOwnedLegs() = runBlocking {
        val ownedLegs = clothesRepo.getAllOwnedTorsos()
        assertNotNull(ownedLegs)
    }

}