package juuxel.basiks.grid

// Unsafe functions to create 2D arrays of nulls
@Suppress("UNCHECKED_CAST")
internal inline fun <reified E> create2DArrayInline(width: Int, height: Int): Array<Array<E>> =
    Array(height) {
        arrayOfNulls<E>(width)
    } as Array<Array<E>>

@Suppress("UNCHECKED_CAST")
internal fun <E> create2DArray(width: Int, height: Int) =
    create2DArrayInline<Any>(width, height) as Array<Array<E>>
