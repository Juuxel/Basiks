/* This file is a part of the Basiks project (see https://github.com/Juuxel/Basiks).
   See LICENSE.txt at the repository root for license information. */
package juuxel.basiks.test

import juuxel.basiks.*

fun main(args: Array<String>) {
    for (fn in listOf(::stringOrNull1, ::stringOrNull2)) {
        val option = fn().toOption()
        val either = option.toEither()
        val noneOrInt = either.mapRight(String::toInt).toOption()

        println(noneOrInt)

        // Can be simplified to
        println(fn().toOption().map(String::toInt))
    }

    // The functional way™
    listOf(::stringOrNull1, ::stringOrNull2)
        .map { it() }
        .map(String?::toOption)
        .map { it.map(String::toInt) }
        .forEach(::println)

    // The map() calls can be merged to
    listOf(::stringOrNull1, ::stringOrNull2)
        .map { it()
            .toOption()
            .map(String::toInt) }
        .forEach(::println)

    val result1 = Result {
        throw Exception("Boo")
    }

    val result2 = run {
        Err("Boo")
    }

    println(result1)
    println(result2)
    println(result1.toEither())
    println(result2.toEither())
}

fun stringOrNull1(): String? = "10"
fun stringOrNull2(): String? = null
