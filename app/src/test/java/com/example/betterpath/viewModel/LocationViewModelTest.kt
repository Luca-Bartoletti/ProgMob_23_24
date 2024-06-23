package com.example.betterpath.viewModel

import android.content.Context
import android.location.LocationManager
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.betterpath.data.PathData
import com.example.betterpath.data.PathDataDao
import com.example.betterpath.data.PathHistoryDao
import com.example.betterpath.database.MyAppDatabase
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


@ExperimentalCoroutinesApi
class LocationViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockContext: Context
    @Mock
    private lateinit var mockDatabase: MyAppDatabase
    @Mock
    private lateinit var mockLocationManager: LocationManager

    @Mock
    private lateinit var mockPathDataDao : PathDataDao
    @Mock
    private lateinit var mockPathHistoryDao: PathHistoryDao

    private lateinit var locationViewModel: LocationViewModel
    private lateinit var historyViewModel : HistoryViewModel

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        `when`(mockDatabase.pathDataDao()).thenReturn(mockPathDataDao)
        `when`(mockDatabase.pathHistoryDao()).thenReturn(mockPathHistoryDao)
        `when`(mockContext.getSystemService(Context.LOCATION_SERVICE)).thenReturn(mockLocationManager)
        Dispatchers.setMain(testDispatcher)


        historyViewModel = HistoryViewModel(mockDatabase)
        locationViewModel = LocationViewModel(mockContext, mockDatabase, historyViewModel)

    }

    @Test
    fun getClusteredListTest(){
        val startList : List<PathData?> =
            listOf(
                PathData(lat = 45.123456789, lng = 9.123456789, time = 1678886400, startStop = 0, pathHistoryId = 1),
                PathData(lat = 45.123498765, lng = 9.123456789, time = 1678890000, startStop = 1, pathHistoryId = 1),
                PathData(lat = 45.987654321, lng = 9.123456789, time = 1678893600, startStop = 0, pathHistoryId = 1),
                PathData(lat = 45.987254321, lng = 9.123456789, time = 1678893600, startStop = 0, pathHistoryId = 1),
            )
        val resultSet = locationViewModel.getClusteredList(startList)
        val checkSet : Set<LatLng> = setOf(
            LatLng(45.123, 9.123),
            LatLng(45.987, 9.123),
            LatLng(45.988, 9.123),
        )

        Assert.assertEquals("Set diversi",checkSet, resultSet)
    }

    @Test
    fun countDifferentPointsTest(){
        val set1 : Set<LatLng> = setOf(
            LatLng(45.123, 9.123),
            LatLng(45.987, 9.123),
            LatLng(45.988, 9.123),
        )
        val set2 : Set<LatLng> = setOf(
            LatLng(45.123, 9.876),
            LatLng(45.987, 9.123),
            LatLng(45.988, 9.123),
        )

        val result = locationViewModel.countDifferentPoints(set1, set2)

        Assert.assertEquals(0.5f, result)
    }

    @Test
    fun countDifferentPointsTestOneEmptyList(){
        val set1 : Set<LatLng> = setOf(

        )
        val set2 : Set<LatLng> = setOf(
            LatLng(45.123, 9.876),
            LatLng(45.987, 9.123),
            LatLng(45.988, 9.123),
        )

        val result = locationViewModel.countDifferentPoints(set1, set2)

        Assert.assertEquals(1f, result)
    }

    @Test
    fun countDifferentPointsTestEmptyLists(){
        val set1 : Set<LatLng> = setOf(

        )
        val set2 : Set<LatLng> = setOf(

        )

        val result = locationViewModel.countDifferentPoints(set1, set2)

        Assert.assertEquals(1f, result)
    }
}