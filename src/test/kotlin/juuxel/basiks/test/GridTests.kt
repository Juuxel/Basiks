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
        val expected = Grid.Mutable.ofNulls<String>(2, 2)
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
        val grid = Grid.Mutable.ofNulls<String>(3, 3)
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

    @Test fun dropLeftTest() {
        /* [1,| 2, 3
            4,| 5, 6]
         */

        val expected = Grid.Mutable.ofNulls<Int>(2, 2)
        expected[0, 0] = 2
        expected[1, 0] = 3
        expected[0, 1] = 5
        expected[1, 1] = 6

        val base = Grid(3, 2) { x, y ->
            x + 1 + y * 3
        }

        assertEquals(expected, base.dropLeft())
        assertEquals(gridOf(1, 2, 3, 6), base.dropLeft(2))
    }

    @Test fun dropRightTest() {
        /* [1, 2, |3
            4, 5, |6]
         */

        val expected = Grid.Mutable.ofNulls<Int>(2, 2)
        expected[0, 0] = 1
        expected[1, 0] = 2
        expected[0, 1] = 4
        expected[1, 1] = 5

        val base = Grid(3, 2) { x, y ->
            x + 1 + y * 3
        }

        assertEquals(expected, base.dropRight())
        assertEquals(gridOf(1, 2, 1, 4), base.dropRight(2))
    }

    @Test fun dropTopTest() {
        /* [1, 2,
            ----
            3, 4
            5, 6]
         */

        val expected = Grid.Mutable.ofNulls<Int>(2, 2)
        expected[0, 0] = 3
        expected[1, 0] = 4
        expected[0, 1] = 5
        expected[1, 1] = 6

        val base = Grid(2, 3) { x, y ->
            x + 1 + y * 2
        }

        assertEquals(expected, base.dropTop())
        assertEquals(gridOf(2, 1, 5, 6), base.dropTop(2))
    }

    @Test fun dropBottomTest() {
        /* [1, 2,
            3, 4
            ----
            5, 6]
         */

        val expected = Grid.Mutable.ofNulls<Int>(2, 2)
        expected[0, 0] = 1
        expected[1, 0] = 2
        expected[0, 1] = 3
        expected[1, 1] = 4

        val base = Grid(2, 3) { x, y ->
            x + 1 + y * 2
        }

        assertEquals(expected, base.dropBottom())
        assertEquals(gridOf(2, 1, 1, 2), base.dropBottom(2))
    }
}
