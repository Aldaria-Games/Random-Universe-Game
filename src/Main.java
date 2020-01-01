import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static int activeUnitAcquisitionSpeed = 5;
    public static void main(String[] args) {
//        System.out.println("Hello World!");

        // The map is 10 x 10, with 2 spaces.
        // First space = active units (soldiers)
        // Second space = passive units (civilians/captives)
        int[][][] mapNumbers = new int[10][10][2];

        // Iterates through mapNumbers and adds random numbers to them
        Random numGen = new Random();
        for (int row = 0; row < mapNumbers.length; row++) {
            for (int column = 0; column < mapNumbers[row].length; column++) {
                for (int depth = 0; depth < mapNumbers[row][column].length; depth++) {
                    mapNumbers[row][column][depth] = numGen.nextInt(99) + 1;
                }
            }
        }

        // Prints rows of mapNumbers
//        for (int row = 0; row < mapNumbers.length; row++) {
//            System.out.println(Arrays.deepToString(mapNumbers[row]));
//        }

        printMap(mapNumbers);

        // Creates the player's starting location
        int[] playerLocation = {numGen.nextInt(10),numGen.nextInt(10)};
        System.out.println("Your location is: " + Arrays.toString(playerLocation));


//        System.out.println();
//        System.out.println();
//        progressTurn(mapNumbers);
//        printMap(mapNumbers);
    }

    // Prints the current map state in a user-friendly format
    public static void printMap(int[][][] map) {
        for (int row = 0; row < map.length; row++) {
            for (int column = 0; column < map[row].length; column++) {
                for (int depth = 0; depth < map[row][column].length; depth++) {
                    if (map[row][column][depth] < 10) {
                        System.out.print(" ");
                    }
                    System.out.print(map[row][column][depth]);
                    System.out.print(" ");
                }
                System.out.print(" |  ");
            }
            System.out.println();
        }
    }

    // Asks the player for their move.
    public static int[][][] playerTurn (int[][][] map) {
        Scanner input = new Scanner(System.in);
        System.out.println("Your turn. Choose what you want to do.");
        System.out.println("1. Attack a square");
        System.out.println("2. Wait (Gain active units based on your passive unit count)");
        return map;
    }

    // Progresses all of the passive actions that happens after the player turn.
    public static int[][][] progressTurn (int[][][] map) {
        // Increases active unit count based on passive unit count
        for (int row = 0; row < map.length; row++) {
            for (int column = 0; column < map[row].length; column++) {
                map[row][column][0] += map[row][column][1]/activeUnitAcquisitionSpeed;
                if (map[row][column][0] > 99) {
                    map[row][column][0] = 99;
                }
            }
        }
        return map;
    }
}
