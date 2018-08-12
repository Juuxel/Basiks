package juuxel.basiks.test

import juuxel.basiks.iterable.combine
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

        val result = combine(0..10, 0..5).map { (x, y) -> x + y }

        assertEquals(excepted, result)
    }
}
