/* This file is a part of the Basiks project (see https://github.com/Juuxel/Basiks).
   See LICENSE.txt at the repository root for license information. */
@file:JvmName("Results")
package juuxel.basiks

/**
 * A result of an operation. A `Result` can be either an [Ok] or an [Err].
 *
 * ### Use case: exception replacement
 * You can use `Result` for error handling instead of exceptions and try-catch blocks.
 * Crashes can be eliminated by using the explicit `Err` type instead of exceptions.
 *
 * ```
 * // With exceptions
 * fun sumOfPositive(vararg ints: Int): Int {
 *     require(ints.all { it > 0 }) // Throws an IllegalArgumentException
 *                                  // if a number is not above 0
 *     return ints.sum()
 * }
 *
 * // With Result
 * fun sumOfPositive(vararg ints: Int): Result<Int, String> =
 *     if (ints.any { it <= 0 }) Err("An input is not positive")
 *     else Ok(ints.sum())
 * ```
 *
 * Existing functions with exceptions can be converted to `Result`s with
 * [Result.Companion.invoke].
 *
 * ```
 * val result = Result {
 *     possiblyDangerousOperation("ABC")
 * }
 * ```
 *
 * ### Handling `Result`s
 * You can match `Result`s with a `when` block:
 * ```
 * fun getInt(): Int {
 *     val result = maybeGetInt()
 *
 *     return when (result) {
 *         is Ok -> result.value
 *         is Err -> {
 *             log.error(result.error)
 *             12 // Default value
 *         }
 *     }
 * }
 * ```
 *
 * ### Transforming `Result`s
 * There are two functions for transforming results: [map] and [mapErr].
 * Additionally, `Result`s can be [converted to `Either`s][toEither] to use
 * [Either.flatMapLeft], [Either.flatMapRight] and [Either.fold].
 *
 * ```
 * val result = maybeGetInt()
 * println(result.map { it * 2 }) // Doubles the Int inside an Ok
 * ```
 *
 * @param T the successful return type
 * @param E the error type
 */
sealed class Result<out T, out E> {
    companion object {
        /**
         * Gets the result of [function] and wraps it in a [Result].
         *
         * If the function returns normally, this function returns an [Ok].
         * If the function throws an `Exception`, this function returns an [Err].
         */
        @JvmName("of")
        @JvmStatic
        inline operator fun <T> invoke(function: () -> T): Result<T, Exception> = try {
            Ok(function())
        } catch (e: Exception) {
            Err(e)
        }
    }

    /**
     * Converts this `Result` to an [Either], with [Err] mapping to [Left]
     * and [Ok] mapping to [Right].
     */
    abstract fun toEither(): Either<E, T>
}

/**
 * The successful result of an operation.
 *
 * @property value the contained value
 */
data class Ok<out T>(val value: T): Result<T, Nothing>() {
    override fun toEither(): Either<Nothing, T> = Right(value)
}

/**
 * A failure case of an operation.
 *
 * @property error the error
 */
data class Err<out E>(val error: E): Result<Nothing, E>() {
    override fun toEither(): Either<E, Nothing> = Left(error)
}

/**
 * Maps an [Ok] case with the [transform] function, leaving [Err] cases intact.
 *
 * @see mapErr
 */
inline fun <T, E, T2> Result<T, E>.map(transform: (T) -> T2): Result<T2, E> = when (this) {
    is Ok -> Ok(transform(value))
    is Err -> this
}

/**
 * Maps an [Err] case with the [transform] function, leaving [Ok] cases intact.
 *
 * @see map
 */
inline fun <T, E, E1> Result<T, E>.mapErr(transform: (E) -> E1): Result<T, E1> = when (this) {
    is Ok -> this
    is Err -> Err(transform(error))
}

/**
 * Returns [Ok.value] if this `Result` is an `Ok`, otherwise
 * returns the value returned by the [provider].
 */
inline fun <T, E> Result<T, E>.orElse(provider: (E) -> T): T =
    when (this) {
        is Ok -> value
        is Err -> provider(error)
    }
