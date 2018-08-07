/* This file is a part of the Basiks project (see https://github.com/Juuxel/Basiks).
   See LICENSE.txt at the repository root for license information. */
@file:JvmName("Eithers")
package juuxel.basiks

/**
 * `Either` represents a value of either [L] or [R].
 *
 * `Either` is right-biased for conversions to and from [Option].
 */
sealed class Either<out L, out R> {
    abstract val value: Any?
    abstract fun toOption(): Option<R>
}

data class Left<out L>(override val value: L): Either<L, Nothing>() {
    override fun toOption() = None
}

data class Right<out R>(override val value: R): Either<Nothing, R>() {
    override fun toOption() = Some(value)
}

inline fun <L, R, Return> Either<L, R>.fold(leftFn: (L) -> Return, rightFn: (R) -> Return): Return = when (this) {
    is Left -> leftFn(value)
    is Right -> rightFn(value)
}

inline fun <L, R, L2> Either<L, R>.mapLeft(transform: (L) -> L2): Either<L2, R> = when (this) {
    is Left -> Left(transform(value))
    is Right -> this
}

inline fun <L, R, R2> Either<L, R>.mapRight(transform: (R) -> R2): Either<L, R2> = when (this) {
    is Left -> this
    is Right -> Right(transform(value))
}

inline fun <L, R, L2> Either<L, R>.flatMapLeft(transform: (L) -> Either<L2, R>): Either<L2, R> = when (this) {
    is Left -> transform(value)
    is Right -> this
}

inline fun <L, R, R2> Either<L, R>.flatMapRight(transform: (R) -> Either<L, R2>): Either<L, R2> = when (this) {
    is Left -> this
    is Right -> transform(value)
}
