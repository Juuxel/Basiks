package juuxel.basiks.iterable

/**
 * A combined iterable of types [A] and [B].
 *
 * @param outer the outer iterable of type [A]
 * @param inner the inner iterable of type [B]
 */
class CombinedIterable<A, B>(
    private val outer: Iterable<A>,
    private val inner: Iterable<B>
): Iterable<Pair<A, B>> {
    override fun iterator() = outer.flatMap { i ->
        inner.map { j ->
            i to j
        }
    }.iterator()
}

infix fun <A, B> Iterable<A>.with(other: Iterable<B>)
        = CombinedIterable(this, other)
