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
    /**
     * The value contained in this `Either`.
     * Either an [L] or an [R].
     *
     * @see Left.value
     * @see Right.value
     */
    abstract val value: Any?

    /**
     * Converts this `Either` to an [Option].
     *
     * If this `Either` is a [Left], returns [None].
     * If this `Either` is a [Right], returns a [Some] containing the [value][Right.value].
     *
     * @see Option.toEither
     */
    abstract fun toOption(): Option<R>
}

/**
 * The left (L) value of an `Either<L, R>`.
 *
 * @property value the contained value
 */
data class Left<out L>(override val value: L): Either<L, Nothing>() {
    override fun toOption() = None
}

/**
 * The right (R) value of an `Either<L, R>`.
 *
 * @property value the contained value
 */
data class Right<out R>(override val value: R): Either<Nothing, R>() {
    override fun toOption() = Some(value)
}

/**
 * Converts this `Either` to [Return].
 *
 * Runs [leftFn] on [Left.value] if this is a `Left`, and
 * [rightFn] on [Right.value] if this is a `Right`.
 */
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
