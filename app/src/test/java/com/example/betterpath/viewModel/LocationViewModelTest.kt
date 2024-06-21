package com.example.betterpath.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.betterpath.database.MyAppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.runner.manipulation.Ordering.Context
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


@ExperimentalCoroutinesApi
class LocationViewModelTest {

    @Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private val mockContext: Context? = null

    @Mock
    private val mockDatabase: MyAppDatabase? = null

    @Mock
    private val mockHistoryViewModel: HistoryViewModel? = null

    private var locationViewModel: LocationViewModel? = null

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        `when`(mockDatabase.pathDataDao()).thenReturn(mockPathDataryDao)
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        `when`(mockDatabase.pathHistoryDao()).thenReturn(mockPathHistoryDao)
        Dispatchers.setMain(testDispatcher)
        historyViewModel = HistoryViewModel(mockDatabase)
        locationViewModel = LocationViewModel(mockContext, mockDatabase, historyViewModel)

    }
}