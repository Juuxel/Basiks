/* This file is a part of Image Filterer.
   See LICENSE.txt at the repository root for license information. */
package juuxel.basiks.grid

import juuxel.basiks.iterable.combine

/**
 * A function that generates grid elements with the coordinates
 * of the requested element.
 */
typealias GridGenerator<E> = (x: Int, y: Int) -> E

/**
 * An iterator iterating over grid elements with their positions.
 */
typealias GridIterator<E> = Iterator<Triple<Int, Int, E>>

// TODO Inspect Grid's type-safety
/**
 * A two-dimensional grid of values of type [E].
 *
 * @property width the grid width
 * @property height the grid height
 */
sealed class Grid<out E>(val width: Int, val height: Int) : Cloneable, Iterable<E> {
    internal val array: Array<Array<@UnsafeVariance E>> = create2DArray(width, height)

    /**
     * A list containing the rows of the grid.
     * @see columns
     */
    val rows: List<List<E>> get() = array.map { it.toList() }

    /**
     * A list containing the columns of the grid.
     * @see rows
     */
    val columns: List<List<E>> get() = List(width) { x ->
        List(height) { y ->
            array[y][x]
        }
    }

    /**
     * Gets the element from this grid at the [x] and [y] coordinates.
     *
     * @throws IndexOutOfBoundsException if one of the coordinates is not a valid coordinate
     */
    operator fun get(x: Int, y: Int): E = array[y][x]

    /**
     * Converts this grid to an [immutable grid][Immutable].
     */
    abstract fun toImmutableGrid(): Immutable<E>

    /**
     * Converts this grid to a [mutable grid][Mutable].
     */
    abstract fun toMutableGrid(): Mutable<@UnsafeVariance E>

    override fun equals(other: Any?) = this === other || other is Grid<*> && array.contentDeepEquals(other.array)
    override fun hashCode() = array.contentDeepHashCode()
    override fun toString(): String = array.contentDeepToString()
    @Suppress("UNCHECKED_CAST") public override fun clone(): Grid<E> = super.clone() as Grid<E>

    /**
     * Converts this grid to a `List`.
     */
    fun toList(): List<E> = rows.flatten().toList()

    /**
     * Returns an iterator iterating over the grid elements.
     */
    final override fun iterator(): Iterator<E> = rows.flatMap { it.asIterable() }.iterator()

    /**
     * Returns an iterator iterating over the grid elements with the coordinates.
     *
     * @see GridIterator
     */
    fun gridIterator(): GridIterator<E> = rows.mapIndexed { y, row ->
        row.mapIndexed { x, elem ->
            Triple(x, y, elem)
        }
    }.flatten().iterator()

    fun dropLeft(columns: Int = 1): Grid<E> =
        Grid(width - columns, height) { x, y ->
            rows[y][x + columns]
        }

    fun dropRight(columns: Int = 1): Grid<E> =
        Grid(width - columns, height) { x, y ->
            rows[y][x]
        }

    fun dropTop(rows: Int = 1): Grid<E> =
        Grid(width, height - rows) { x, y ->
            this.rows[y + rows][x]
        }

    fun dropBottom(rows: Int = 1): Grid<E> =
        Grid(width, height - rows) { x, y ->
            this.rows[y][x]
        }

    companion object {
        /**
         * Creates an immutable grid with the [generator].
         */
        inline operator fun <E> invoke(
            width: Int,
            height: Int,
            generator: GridGenerator<E>
        ): Immutable<E> {
            return Mutable(width, height, generator).toImmutableGrid()
        }

        /**
         * Creates an immutable grid filled with the [fillValue].
         */
        operator fun <E> invoke(width: Int, height: Int, fillValue: E): Immutable<E>
            = Mutable(width, height, fillValue).toImmutableGrid()

        /**
         * Creates an immutable grid of `null`s with the [width] and [height].
         */
        fun <E> ofNulls(width: Int, height: Int): Grid<E?> = Immutable(width, height)

        /**
         * Creates an immutable grid from the [rows].
         */
        fun <E> fromRows(rows: Iterable<Iterable<E>>): Immutable<E> =
            Mutable.fromRows(rows).toImmutableGrid()

        /**
         * Creates an immutable grid from the [columns].
         */
        fun <E> fromColumns(columns: Iterable<Iterable<E>>): Immutable<E> =
            Mutable.fromColumns(columns).toImmutableGrid()
    }

    /**
     * An immutable grid.
     *
     * See [Grid.Companion] for functions for creating immutable grids.
     */
    class Immutable<out E> internal constructor(width: Int, height: Int) : Grid<E>(width, height) {
        override fun toImmutableGrid(): Immutable<E> = this

        override fun toMutableGrid(): Mutable<@UnsafeVariance E> = Mutable<E>(
            width,
            height
        ).also { grid ->
            for ((y, x) in combine(0 until height, 0 until width)) {
                grid.array[y][x] = array[y][x]
            }
        }
    }

    /**
     * A mutable grid.
     *
     * Use the functions in the companion object of this class to create mutable grids.
     */
    class Mutable<E> @PublishedApi internal constructor(
        width: Int,
        height: Int
    ) : Grid<E>(width, height) {
        /**
         * Sets the value at [x] and [y] to the [value].
         */
        operator fun set(x: Int, y: Int, value: E) {
            array[y][x] = value
        }

        /**
         * Fills the grid with the [value].
         */
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
            for ((y, x) in combine(0 until height, 0 until width)) {
                grid.array[y][x] = array[y][x]
            }
        }

        override fun toMutableGrid(): Mutable<E> = this

        companion object {
            /**
             * Creates a mutable grid with the [generator].
             */
            inline operator fun <E> invoke(
                width: Int,
                height: Int,
                generator: GridGenerator<E>
            ) = Mutable<E>(width, height).apply {
                    for ((x, y) in combine(0 until width, 0 until height)) {
                        this[x, y] = generator(x, y)
                    }
                }

            /**
             * Creates a mutable grid filled with the [fillValue].
             */
            operator fun <E> invoke(width: Int, height: Int, fillValue: E) =
                Mutable<E>(width, height).apply { fill(fillValue) }

            /**
             * Creates a mutable grid of `null`s.
             */
            fun<E> ofNulls(width: Int, height: Int): Mutable<E?> = Mutable(width, height)

            /**
             * Creates a mutable grid from the [rows].
             */
            fun <E> fromRows(rows: Iterable<Iterable<E>>): Mutable<E> {
                require(rows.any()) { "rows must not be empty" }
                val list = rows.map { it.toList() }
                val width = list.first().size

                return Mutable(width, list.size) { x, y ->
                    list[y][x]
                }
            }

            /**
             * Creates a mutable grid from the [columns].
             */
            fun <E> fromColumns(columns: Iterable<Iterable<E>>): Mutable<E> {
                require(columns.any()) { "columns must not be empty" }
                val list = columns.map { it.toList() }
                val height = list.first().size

                return Mutable(list.size, height) { x, y ->
                    list[x][y]
                }
            }
        }
    }
}

/**
 * Creates a grid with the [values].
 *
 * ### Example
 *
 * `gridOf(2, 2, "A", "B", "C", "D")` results in a grid with this structure:
 * ```
 * [[A, B],
 *  [C, D]]
 * ```
 */
fun <E> gridOf(width: Int, height: Int, vararg values: E): Grid<E> {
    require(values.size % width * height == 0)

    return Grid.Mutable(width, height) { x, y ->
        values[x + y * width]
    }
}

/**
 * This typealias is provided for symmetry with the Kotlin standard library.
 */
typealias MutableGrid<E> = Grid.Mutable<E>
