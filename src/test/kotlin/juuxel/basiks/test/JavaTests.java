/* This file is a part of the Basiks project (see https://github.com/Juuxel/Basiks).
   See LICENSE.txt at the repository root for license information. */
package juuxel.basiks.test;

import juuxel.basiks.Either;
import juuxel.basiks.Result;
import juuxel.basiks.Results;

public class JavaTests {
    public static void main(String[] args) {
        Result<String, Exception> result = Result.of(() -> "Hello, world!");
        Either<Exception, Character> either
                = Results.map(result, (value) -> value.charAt(0))
                         .toEither();

        System.out.println(either);
    }
}
