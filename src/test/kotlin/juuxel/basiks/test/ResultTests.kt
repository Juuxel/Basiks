package juuxel.basiks.test

import juuxel.basiks.Err
import juuxel.basiks.Ok
import juuxel.basiks.Result
import juuxel.basiks.orElse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ResultTests {
    private val okValue = 10
    private val errValue = "Something happened. :-("
    private val orElseValue = 12

    private val ok: Result<Int, String> = Ok(okValue)
    private val err: Result<Int, String> = Err(errValue)

    @Test fun orElseWithOk() {
        assertEquals(okValue, ok.orElse { orElseValue })
    }

    @Test fun orElseWithErr() {
        assertEquals(orElseValue, err.orElse { orElseValue })
    }

    @Test fun valueToResult() {
        assertEquals(ok, Result(::getOk))
    }

    @Test fun errorToResult() {
        assertTrue(Result(::throwException) is Err)
    }

    private fun getOk() = okValue
    private fun throwException(): Int = throw Exception(errValue)
}
