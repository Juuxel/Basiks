/* This file is a part of the Basiks project (see https://github.com/Juuxel/Basiks).
   See LICENSE.txt at the repository root for license information. */
@file:JvmName("Eithers")
package juuxel.basiks

/**
 * `Either` represents a value of either [A] or [B].
 *
 * `Either` is right-biased for conversions to and from [Option].
 */
sealed class Either<out A, out B> {
    abstract val value: Any?
    abstract fun toOption(): Option<B>
}

data class Left<out A>(override val value: A): Either<A, Nothing>() {
    override fun toOption() = None
}

data class Right<out B>(override val value: B): Either<Nothing, B>() {
    override fun toOption() = Some(value)
}

inline fun <A, B, R> Either<A, B>.fold(leftFn: (A) -> R, rightFn: (B) -> R): R = when (this) {
    is Left -> leftFn(value)
    is Right -> rightFn(value)
}

inline fun <A, B, A2> Either<A, B>.mapLeft(transform: (A) -> A2): Either<A2, B> = when (this) {
    is Left -> Left(transform(value))
    is Right -> this
}

inline fun <A, B, B2> Either<A, B>.mapRight(transform: (B) -> B2): Either<A, B2> = when (this) {
    is Left -> this
    is Right -> Right(transform(value))
}

inline fun <A, B, A2> Either<A, B>.flatMapLeft(transform: (A) -> Either<A2, B>): Either<A2, B> = when (this) {
    is Left -> transform(value)
    is Right -> this
}

inline fun <A, B, B2> Either<A, B>.flatMapRight(transform: (B) -> Either<A, B2>): Either<A, B2> = when (this) {
    is Left -> this
    is Right -> transform(value)
}
