package com.example.calculator

import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4.0, 2.0 + 2.0, 0.0001)
    }

    @Test
    fun subtraction_isCorrect() {
        assertEquals(5.0, 10.0 - 5.0, 0.0001)
    }

    @Test
    fun multiplication_isCorrect() {
        assertEquals(12.0, 3.0 * 4.0, 0.0001)
    }

    @Test
    fun division_isCorrect() {
        assertEquals(2.5, 5.0 / 2.0, 0.0001)
    }
}