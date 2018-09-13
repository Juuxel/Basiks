package juuxel.basiks.test

import juuxel.basiks.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OptionTests {
    private val nullables = listOf("A", null, 10, 2.0)
    private val options = listOf(Some("A"), None, Some(10), Some(2.0))

    @Test
    fun testToOptionNonNull() {
        assertEquals(Some(5), 5.toOption())
    }

    @Test
    fun testToOptionNull() {
        assertEquals(None, null.toOption())
    }

    @Test
    fun testToEitherNone() {
        assertEquals(Left(None), None.toEither())
    }

    @Test
    fun testToEitherSome() {
        assertEquals(Right(10), Some(10).toEither())
    }

    @Test
    fun testMapSome() {
        assertEquals(Some('H'), Some("Hello").map { it.first() })
    }

    @Test
    fun testMapNone() {
        val option: Option<String> = None

        assertEquals(None, option.map { it.first() })
    }

    @Test fun testToOptions() {
        assertEquals(options, nullables.toOptions())
    }

    @Test fun testToNullables() {
        assertEquals(nullables, options.toNullables())
    }
}
