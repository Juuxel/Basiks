/* This file is a part of the Basiks project (see https://github.com/Juuxel/Basiks).
   See LICENSE.txt at the repository root for license information. */
@file:JvmName("Options")
package juuxel.basiks

/**
 * An optional value of type [T]. An instance of `Option` can be either a [Some] with a present value
 * or [None] with no value.
 *
 * ### Use case: nullable types
 * Nullable types can be represented with `Option`s.
 * Easy conversion between the two is provided with [toOption] and [toNullable].
 *
 * ```
 * val nullableString: String? = "Hello"
 * val stringOption: Option<String> = nullableString.toOption() // A Some<String>
 *
 * when (stringOption) {
 *     is Some -> println("Value found: ${stringOption.value}")
 *     is None -> println("No value found")
 * }
 *
 * Output:
 * Value found: Hello
 * ```
 *
 * ### Easy destructuring
 * The destructing syntax can be used instead of [toNullable].
 *
 * ```
 * fun takeOption(option: Option<String>) {
 *     val (value) = option
 *
 *     value?.let {
 *         println(it)
 *     }
 * }
 * ```
 */
sealed class Option<out T> {
    /**
     * Destructures this `Option` to a nullable value.
     * See [Option] for an example of usage.
     *
     * @see toNullable
     */
    open operator fun component1(): T? = null

    /**
     * Converts this `Option` to a nullable value.
     *
     * If this `Option` is `Some`, returns [Some.value].
     * If this option is `None`, returns `null`.
     */
    fun toNullable(): T? = component1()

    /**
     * Converts this `Option` to an [Either].
     *
     * [None] is converted to a [Left] and a [Some]
     * is converted to a [Right] containing the [value][Some.value].
     *
     * @see Either.toOption
     */
    abstract fun toEither(): Either<None, T>

    /**
     * Converts this `Option` to an [Either], replacing `None` with
     * the result of [ifNone].
     *
     * See conversion rules in `toEither()`.
     */
    abstract fun <L> toEither(ifNone: () -> L): Either<L, T>
}

/**
 * A present optional value containing [value].
 *
 * @property value the contained value
 */
data class Some<out T>(val value: T) : Option<T>() {
    override fun toEither(): Either<Nothing, T> = Right(value)
    override fun <L> toEither(ifNone: () -> L) = toEither()
}

/**
 * An optional value that is not present.
 */
object None : Option<Nothing>() {
    override fun toEither(): Either<None, Nothing> = Left(this)
    override fun <L> toEither(ifNone: () -> L): Either<L, Nothing>
        = Left(ifNone())

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

/**
 * Converts all values in `this` `Iterable` to nullable values.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <T : Any> Iterable<Option<T>>.toNullables(): List<T?> = map { it.toNullable() }

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
