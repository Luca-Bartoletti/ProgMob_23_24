package com.example.betterpath

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

class HistoryTest {

    var checkedBox  = mutableStateOf(arrayOf(-1,-1))
    var enableCompareButton = mutableStateOf(false)


    @Test
    fun testResetCheckBox(){

    }


    @Test
    fun testTest(){
        val a : Int? = 2
        assertNotNull("'a' non ha valore non nullo",a)
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


}