package juuxel.basiks.grid;

// This code is simpler in Java
final class ArrayCreator {
    @SuppressWarnings("unchecked")
    static <E> E[][] createArray(int rows, int cols) {
        E[][] array = (E[][]) new Object[cols][];

        for (int i = 0; i < array.length; i++) {
            array[i] = (E[]) new Object[rows];
        }

        return array;
    }
}
