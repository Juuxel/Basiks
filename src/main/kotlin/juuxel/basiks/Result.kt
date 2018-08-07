/* This file is a part of the Basiks project (see https://github.com/Juuxel/Basiks).
   See LICENSE.txt at the repository root for license information. */
@file:JvmName("Results")
package juuxel.basiks

/**
 * A result of a function with [T] as the return type and [E] as the error type.
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

data class Ok<out T>(val value: T): Result<T, Nothing>() {
    override fun toEither(): Either<Nothing, T> = Right(value)
}

data class Err<out E>(val error: E): Result<Nothing, E>() {
    override fun toEither(): Either<E, Nothing> = Left(error)
}

inline fun <T, E, T2> Result<T, E>.map(transform: (T) -> T2): Result<T2, E> = when (this) {
    is Ok -> Ok(transform(value))
    is Err -> this
}

inline fun <T, E, E1> Result<T, E>.mapErr(transform: (E) -> E1): Result<T, E1> = when (this) {
    is Ok -> this
    is Err -> Err(transform(error))
}
