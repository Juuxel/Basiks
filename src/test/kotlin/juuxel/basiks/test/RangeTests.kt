package juuxel.basiks.test

import juuxel.basiks.iterable.with
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RangeTests {
    @Test
    fun combinedRangeTest() {
        val excepted = (0..10).flatMap { x ->
            (0..5).map { y ->
                x to y
            }
        }.map { (x, y) -> x + y }

        val result = ((0..10) with (0..5)).map { (x, y) -> x + y }

        assertEquals(excepted, result)
    }
}
