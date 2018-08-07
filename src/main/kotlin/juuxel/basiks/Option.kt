/* This file is a part of the Basiks project (see https://github.com/Juuxel/Basiks).
   See LICENSE.txt at the repository root for license information. */
@file:JvmName("Options")
package juuxel.basiks

/**
 * An optional value of type [T].
 */
sealed class Option<out T> {
    open operator fun component1(): T? = null
    fun toNullable(): T? = component1()
    abstract fun toEither(): Either<None, T>
}

/**
 * A present optional value containing [value].
 */
data class Some<out T>(val value: T) : Option<T>() {
    override fun toEither(): Either<None, T> = Right(value)
}

/**
 * An optional value that is not present.
 */
object None : Option<Nothing>() {
    override fun toEither(): Either<None, Nothing> = Left(this)
    override fun toString() = "None"
}

/**
 * Converts `this` value to an [Option].
 */
@Suppress("NOTHING_TO_INLINE")
fun <T : Any> T?.toOption(): Option<T>
    = if (this != null) Some(this)
      else None

/**
 * Converts all values in `this` `Iterable` to [Option]s.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <T : Any> Iterable<T?>.toOptions(): List<Option<T>> = map { it.toOption() }

@Suppress("NOTHING_TO_INLINE")
inline fun <R, T : R> Option<T>.orElse(elseValue: R): R = orElse { elseValue }

inline fun <R, T : R> Option<T>.orElse(supplier: () -> R): R = when (this) {
    is Some -> value
    is None -> supplier()
}

inline fun <T, R> Option<T>.map(transform: (T) -> R): Option<R> = when (this) {
    is Some -> Some(transform(value))
    is None -> this
}

inline fun <T, R> Option<T>.flatMap(transform: (T) -> Option<R>): Option<R> = when (this) {
    is Some -> transform(value)
    is None -> this
}

inline fun <T> Option<T>.orThrow(supplier: () -> Exception): T = when (this) {
    is Some -> value
    is None -> throw supplier()
}
