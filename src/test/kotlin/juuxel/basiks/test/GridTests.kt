package juuxel.basiks.test

import juuxel.basiks.grid.Grid
import juuxel.basiks.grid.gridOf
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GridTests {
    @Test
    fun buildImmutableGrid() {
        /* [A, B,
            C, D]
         */
        val expected = Grid.Mutable<String>(2, 2)
        expected[0, 0] = "A"
        expected[1, 0] = "B"
        expected[0, 1] = "C"
        expected[1, 1] = "D"

        val built = Grid(2, 2) { x, y ->
            when (x to y) {
                0 to 0 -> "A"
                1 to 0 -> "B"
                0 to 1 -> "C"
                1 to 1 -> "D"
                else -> throw IndexOutOfBoundsException()
            }
        }

        assertEquals(expected, built)
    }

    @Test fun fillMutableGrid() {
        val grid = Grid.Mutable<String>(3, 3)
        val filled = grid.fill("Filled")

        assertSame(grid, filled)
        assertTrue(filled.all { it == "Filled" })
    }

    @Test fun gridIteratorTest() {
        val grid = Grid(5, 5, 3)
        var total = 0
        val target = 2 * 5 * (0 + 1 + 2 + 3 + 4) // 100

        for ((x, y, _) in grid.gridIterator()) {
            total += x + y
        }

        assertEquals(target, total)
    }

    @Test fun gridOfTest() {
        val expected = Grid(2, 2) { x, y -> x + y }
        val actual = gridOf(
            2, 2,
            0, 1,
            1, 2
        )

        assertEquals(expected, actual)
    }
}
