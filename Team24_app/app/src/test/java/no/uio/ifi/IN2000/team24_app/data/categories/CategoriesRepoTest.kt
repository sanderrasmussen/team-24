package no.uio.ifi.IN2000.team24_app.data.categories

import kotlinx.coroutines.runBlocking
import no.uio.ifi.IN2000.team24_app.data.database.CategoryDao
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class categoriesRepoTest {


    @Mock
    private lateinit var mockCategoryDao: CategoryDao

    private lateinit var mockCategoryRepo: mockCategoriesRepo

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        mockCategoryRepo= mockCategoriesRepo(mockCategoryDao)
    }

    @Test
    fun testGetAllCategories(){
        runBlocking {
            var categories = mockCategoryRepo.getAllCategories()
            assertNotNull(categories)

            assertFalse(categories.isEmpty())
            assertNotNull(categories.first())
        }

    }
    @Test
    fun testGetCategory(){
        runBlocking {
            var category = mockCategoryRepo.getCategory("Øving")
            assertNotNull(category)
            assertEquals(category.category, "Øving")

        }
    }


}