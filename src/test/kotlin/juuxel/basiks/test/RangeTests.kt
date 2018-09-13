package juuxel.basiks.test

import juuxel.basiks.iterable.combine
import juuxel.basiks.iterable.flatMap
import juuxel.basiks.iterable.map
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

    @Test
    fun combinedMapTest() {
        val expected = combine(0..9, 0..4).map { (x, y) -> x + y }
        val actual = combine(0..9, 0..4).map { x, y -> x + y }

        assertEquals(expected, actual)
    }

    @Test
    fun combinedFlatMapTest() {
        val expected = combine(0..9, 0..4).flatMap { (x, y) -> x..y }
        val actual = combine(0..9, 0..4).flatMap { x, y -> x..y }

        assertEquals(expected, actual)
    }
}
