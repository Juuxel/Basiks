package juuxel.basiks.test

import juuxel.basiks.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EitherTests {
    private val left: Either<String, Int> = Left("Hello, world!")
    private val right: Either<String, Int> = Right(123)
    private val leftResult = Left("Hello, world!".length)
    private val rightResult = Right(123 * 2)
    private val foldResultLeft = leftResult.value
    private val foldResultRight = rightResult.value

    @Test fun mapLeftWithLeft() {
        val result = left.mapLeft { it.length }
        assertEquals(leftResult, result)
    }

    @Test fun mapLeftWithRight() {
        val result = right.mapLeft { it.length }
        assertEquals(right, result)
    }

    @Test fun mapRightWithLeft() {
        val result = left.mapRight { it * 2 }
        assertEquals(left, result)
    }

    @Test fun mapRightWithRight() {
        val result = right.mapRight { it * 2 }
        assertEquals(rightResult, result)
    }

    @Test fun flatMapLeftWithLeft() {
        val result = left.flatMapLeft { Left(it.length) }
        assertEquals(leftResult, result)
    }

    @Test fun flatMapLeftWithRight() {
        val result = right.flatMapLeft { Left(it.length) }
        assertEquals(right, result)
    }

    @Test fun flatMapRightWithLeft() {
        val result = left.flatMapRight { Right(it * 2) }
        assertEquals(left, result)
    }

    @Test fun flatMapRightWithRight() {
        val result = right.flatMapRight { Right(it * 2) }
        assertEquals(rightResult, result)
    }

    @Test fun foldLeft() {
        val result = left.fold(leftFn = { it.length }, rightFn = { it * 2 })
        assertEquals(foldResultLeft, result)
    }

    @Test fun foldRight() {
        val result = right.fold(leftFn = { it.length }, rightFn = { it * 2 })
        assertEquals(foldResultRight, result)
    }
}