/* This file is a part of Image Filterer.
   See LICENSE.txt at the repository root for license information. */
package juuxel.basiks.grid

import juuxel.basiks.iterable.with

typealias GridGenerator<E> = (x: Int, y: Int) -> E
typealias GridIterator<E> = Iterator<Triple<Int, Int, E>>

/**
 * A two-dimensional grid of values of type [E].
 *
 * @property width the grid width
 * @property height the grid height
 */
sealed class Grid<out E>(val width: Int, val height: Int) : Cloneable, Iterable<E> {
    internal val array: Array<Array<@UnsafeVariance E>> = ArrayCreator.createArray(width, height)

    val rows: Array<Array<@UnsafeVariance E>> get() = array.clone()

    operator fun get(x: Int, y: Int): E = array[y][x]

    abstract fun toImmutableGrid(): Immutable<E>
    abstract fun toMutableGrid(): Mutable<@UnsafeVariance E>

    override fun equals(other: Any?) = this === other || other is Grid<*> && array.contentDeepEquals(other.array)
    override fun hashCode() = array.contentDeepHashCode()
    override fun toString(): String = array.contentDeepToString()
    fun toList(): List<E> = rows.flatten().toList()

    final override fun iterator(): Iterator<E> = rows.flatMap { it.toList() }.iterator()

    fun gridIterator(): GridIterator<E> = rows.mapIndexed { y, row ->
        row.mapIndexed { x, elem ->
            Triple(x, y, elem)
        }
    }.flatten().iterator()

    companion object {
        @Suppress("NOTHING_TO_INLINE")
        inline operator fun <E> invoke(width: Int, height: Int): Grid<E>
                = Immutable(width, height)

        inline operator fun <E> invoke(
            width: Int,
            height: Int,
            generator: GridGenerator<E>
        ): Grid<E> {
            return Mutable(width, height, generator).toImmutableGrid()
        }

        operator fun <E> invoke(width: Int, height: Int, fillValue: E): Grid<E>
            = Mutable(width, height, fillValue).toImmutableGrid()
    }

    class Immutable<out E>(width: Int, height: Int) : Grid<E>(width, height) {
        override fun toImmutableGrid(): Immutable<E> = this

        override fun toMutableGrid(): Mutable<@UnsafeVariance E> = Mutable<E>(
            width,
            height
        ).also { grid ->
            for (y in 0 until height) {
                for (x in 0 until width) {
                    grid.array[y][x] = array[y][x]
                }
            }
        }
    }

    class Mutable<E>(width: Int, height: Int) : Grid<E>(width, height) {
        operator fun set(x: Int, y: Int, value: E) {
            array[y][x] = value
        }

        fun fill(value: E): Mutable<E> {
            for (y in 0 until height) {
                for (x in 0 until width) {
                    array[y][x] = value
                }
            }

            return this
        }

        override fun toImmutableGrid(): Immutable<E> = Immutable<E>(
            width,
            height
        ).also { grid ->
            for (y in 0 until height) {
                for (x in 0 until width) {
                    grid.array[y][x] = array[y][x]
                }
            }
        }

        override fun toMutableGrid(): Mutable<E> = this

        companion object {
            inline operator fun <E> invoke(width: Int, height: Int, generator: GridGenerator<E>) =
                Mutable<E>(width, height).apply {
                    for ((x, y) in (0 until width) with (0 until height)) {
                        this[x, y] = generator(x, y)
                    }
                }

            operator fun <E> invoke(width: Int, height: Int, fillValue: E) =
                Mutable<E>(width, height).apply { fill(fillValue) }
        }
    }
}

fun <E> gridOf(width: Int, height: Int, vararg values: E): Grid<E> {
    require(values.size % width * height == 0)

    return Grid.Mutable(width, height) { x, y ->
        values[x + y * width]
    }
}
