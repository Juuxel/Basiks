package juuxel.basiks.iterable

/**
 * A combined iterable of types [A] and [B].
 *
 * @param outer the outer iterable of type [A]
 * @param inner the inner iterable of type [B]
 */
private class CombinedIterable<A, B>(
    private val outer: Iterable<A>,
    private val inner: Iterable<B>
): Iterable<Pair<A, B>> {
    override fun iterator() = outer.flatMap { i ->
        inner.map { j ->
            i to j
        }
    }.iterator()
}

/**
 * Combines the two iterables.
 *
 * ### Example
 * ```
 * val letters = listOf('A', 'B', 'C')
 * val numbers = listOf(1, 2, 3)
 *
 * for ((letter, number) in combine(letters, numbers))
 *     println("$letter + $number")
 *
 * Output:
 * A: 1
 * A: 2
 * A: 3
 * B: 1
 * B: 2
 * B: 3
 * C: 1
 * C: 2
 * C: 3
 * ```
 */
fun <A, B> combine(outer: Iterable<A>, inner: Iterable<B>): Iterable<Pair<A, B>> =
    CombinedIterable(outer, inner)

inline fun <A, B, R> Iterable<Pair<A, B>>.map(transform: (A, B) -> R): List<R> =
    map { (a, b) -> transform(a, b) }

inline fun <A, B, R> Iterable<Pair<A, B>>.flatMap(transform: (A, B) -> Iterable<R>): List<R> =
    flatMap { (a, b) -> transform(a, b) }
