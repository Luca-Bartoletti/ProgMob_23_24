import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.betterpath.data.PathHistoryDao
import com.example.betterpath.database.MyAppDatabase
import com.example.betterpath.repository.HistoryRepository
import com.example.betterpath.viewModel.HistoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class HistoryViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockDatabase: MyAppDatabase

    @Mock
    private lateinit var mockPathHistoryDao: PathHistoryDao

    private lateinit var historyRepository: HistoryRepository
    private lateinit var historyViewModel: HistoryViewModel

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        `when`(mockDatabase.pathHistoryDao()).thenReturn(mockPathHistoryDao)
        Dispatchers.setMain(testDispatcher)

        historyViewModel = HistoryViewModel(mockDatabase)
        historyRepository = historyViewModel.repository
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun testUpdateCheckedBox_AddCheck() {
        // Inizialmente nessuna checkBox è selezionata
        Assert.assertArrayEquals(arrayOf(-1, -1), historyViewModel.checkedBox.value)
        Assert.assertEquals(0, historyViewModel.numberOfChecked.intValue)
        Assert.assertFalse(historyViewModel.enableCompareButton.value)

        // Seleziona la prima checkBox
        var result = historyViewModel.updateCheckedBox(0, true)

        Assert.assertTrue(result)
        Assert.assertArrayEquals(arrayOf(0, -1), historyViewModel.checkedBox.value)
        Assert.assertEquals(1, historyViewModel.numberOfChecked.intValue)
        Assert.assertFalse(historyViewModel.enableCompareButton.value)

        // Seleziona la seconda checkBox
        result = historyViewModel.updateCheckedBox(2, true)

        Assert.assertTrue(result)
        Assert.assertArrayEquals(arrayOf(0, 2), historyViewModel.checkedBox.value)
        Assert.assertEquals(2, historyViewModel.numberOfChecked.intValue)
        Assert.assertTrue(historyViewModel.enableCompareButton.value)
    }

    @Test
    fun testUpdateCheckedBox_RemoveCheck() {
        // Seleziona due checkBox
        historyViewModel.updateCheckedBox(0, true)
        historyViewModel.updateCheckedBox(1, true)

        // Verifica che due checkBox siano selezionate
        Assert.assertArrayEquals(arrayOf(0, 1), historyViewModel.checkedBox.value)
        Assert.assertEquals(2, historyViewModel.numberOfChecked.intValue)
        Assert.assertTrue(historyViewModel.enableCompareButton.value)

        // Deseleziona una checkBox
        var result = historyViewModel.updateCheckedBox(0, false)

        Assert.assertFalse(result)
        Assert.assertArrayEquals(arrayOf(1, -1), historyViewModel.checkedBox.value)
        Assert.assertEquals(1, historyViewModel.numberOfChecked.intValue)
        Assert.assertFalse(historyViewModel.enableCompareButton.value)

        // Deseleziona la seconda checkBox
        result = historyViewModel.updateCheckedBox(0, false)

        Assert.assertFalse(result)
        Assert.assertArrayEquals(arrayOf(-1, -1), historyViewModel.checkedBox.value)
        Assert.assertEquals(0, historyViewModel.numberOfChecked.intValue)
        Assert.assertFalse(historyViewModel.enableCompareButton.value)
    }

    @Test
    fun testResetCheckedBox(){
        // Seleziona due checkBox
        historyViewModel.updateCheckedBox(0, true)
        historyViewModel.updateCheckedBox(1, true)

        // Verifica che due checkBox siano selezionate
        Assert.assertArrayEquals(arrayOf(0, 1), historyViewModel.checkedBox.value)
        Assert.assertEquals(2, historyViewModel.numberOfChecked.intValue)
        Assert.assertTrue(historyViewModel.enableCompareButton.value)

        historyViewModel.resetCheckBoxValue()
        Assert.assertArrayEquals(arrayOf(-1, -1), historyViewModel.checkedBox.value)
        Assert.assertEquals(0, historyViewModel.numberOfChecked.intValue)
        Assert.assertFalse(historyViewModel.enableCompareButton.value)

    }
}
