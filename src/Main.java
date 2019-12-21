import java.sql.Array;
import java.util.Arrays;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        // The map is 10 x 10, with 2 spaces.
        // First space = active units (soldiers)
        // Second space = passive units (civilians/captives)
        int[][][] mapNumbers = new int[10][10][2];

        // Iterates through mapNumbers and adds random numbers to them
        Random numGen = new Random();
        for (int row = 0; row < mapNumbers.length; row++) {
            for (int column = 0; column < mapNumbers[row].length; column++) {
                for (int depth = 0; depth < mapNumbers[row][column].length; depth++) {
                    mapNumbers[row][column][depth] = numGen.nextInt(100) + 1;
                }
            }
        }

        // Prints rows of mapNumbers
        for (int row = 0; row < mapNumbers.length; row++) {
            System.out.println(Arrays.deepToString(mapNumbers[row]));
        }

        // Testing new print statement
        printArr(mapNumbers);
    }

    public static void printArr (int[][][] array) {
        for (int row = 0; row < array.length; row++) {
            for (int column = 0; column < array[row].length; column++) {
                for (int depth = 0; depth < array[row][column].length; depth++) {
                    System.out.print(array[row][column][depth]);
                    if (array[row][column][depth] < 10) {
                        System.out.print(" ");
                    }
                    System.out.print(" ");
                }
                System.out.println();
            }
        }
    }
}
