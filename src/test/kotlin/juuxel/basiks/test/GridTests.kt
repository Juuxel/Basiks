package juuxel.basiks.test

import juuxel.basiks.grid.Grid
import org.junit.jupiter.api.Assertions.assertEquals
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
}
