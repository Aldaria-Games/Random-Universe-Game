import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static int activeUnitAcquisitionSpeed = 5;
    public static int[] playerLocation = new int[2];
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
        playerLocation[0] = numGen.nextInt(10);
        playerLocation[1] = numGen.nextInt(10);
        System.out.println("Your location is: " + Arrays.toString(playerLocation));


        mapNumbers = playerTurn(mapNumbers);
        System.out.println();
        System.out.println();
//        progressTurn(mapNumbers);
        printMap(mapNumbers);
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
    // TODO add movement
    public static int[][][] playerTurn (int[][][] map) {
        Scanner input = new Scanner(System.in);
        System.out.println("Your turn. Choose what you want to do.");
        System.out.println("1. Attack a square");
        System.out.println("2. Wait (Gain active units based on your passive unit count)");
        System.out.println("3. Move into a completely empty square");
        System.out.println("Choose by typing 1 for attack, 2 for wait, and 3 for move.");
        int playerChoice = input.nextInt();
        if (playerChoice == 1) {
            map = playerAttacksSquare(map);
        } else if (playerChoice == 3) {
            if (checkForEmpty(map,playerLocation)){

            } else {
                System.out.println("You can't move because there are no empty spaces around you.");
                map = playerTurn(map);
            }
        }
        map = progressTurn(map);
        return map;
    }

    // lets the player choose which square to attack.
    public static int[][][] playerAttacksSquare (int[][][] map) {
        Scanner input = new Scanner(System.in);
        int[] attackSquare = new int[2];
        System.out.println("Choose which square you want to attack.");
        System.out.println("Type the row that you want to attack");
        attackSquare[0] = input.nextInt();
        System.out.println("Type the column that you want to attack");
        attackSquare[1] = input.nextInt();
        if (Math.abs(attackSquare[0]-playerLocation[0]) <= 1 && Math.abs(attackSquare[1]-playerLocation[1]) <= 1) {
            map = attackSquare(map,playerLocation,attackSquare);
        } else {
            System.out.println("Too far away. Choose again.");
            map = playerAttacksSquare(map);
        }
        return map;
    }

    // TODO check to see if the square chosen is empty.
    public static int[][][] playerMovesSquare (int[][][] map){
        Scanner input = new Scanner(System.in);
        int[] playerMoveChoice = new int[2];
        System.out.println("Choose the square you would like to move to.");
        System.out.println("Type the row you would like to move to.");
        playerMoveChoice[0] = input.nextInt();
        System.out.println("Type the column you would like to move to.");
        playerMoveChoice[1] = input.nextInt();
        return map;
    }

    // Progresses all of the passive actions that happens after the player turn.
    // TODO add AI attacks
    // TODO change so that it works with individual squares instead of the whole map.
    public static int[][][] progressTurn (int[][][] map) {
        // Increases active unit count based on passive unit count
        for (int row = 0; row < map.length; row++) {
            for (int column = 0; column < map[row].length; column++) {
                map[row][column][0] += map[row][column][1]/activeUnitAcquisitionSpeed;
                if (map[row][column][0] > 99) {
                    map[row][column][0] = 99;
                }
                if (map[row][column][1] > 99) {
                    map[row][column][1] = 99;
                }
            }
        }
        return map;
    }

    // decides who wins an attack and by how much.
    // Positive = attacker gains units
    // Negative = defender gains units
    // TODO change so that the captured active units are made passive
    public static int[][][] attackSquare (int[][][] map, int[] attackerLocation, int[] defenderLocation) {
        int attackerActiveUnits = map[attackerLocation[0]][attackerLocation[1]][0];
        int defenderActiveUnits = map[defenderLocation[0]][defenderLocation[1]][0];
        int attackerPassiveUnits = map[attackerLocation[0]][attackerLocation[1]][1];
        int defenderPassiveUnits = map[defenderLocation[0]][defenderLocation[1]][1];
        Random numGen = new Random();
        int result = attackerActiveUnits - numGen.nextInt(attackerActiveUnits + defenderActiveUnits) + 1;
        System.out.println("result = attacker gained " + result + " units at most");
        attackerActiveUnits += result;
        defenderActiveUnits -= result;

        // Accounts for cases where either army takes more active units than what is actually available.
        if (attackerActiveUnits < 0){
            defenderActiveUnits += attackerActiveUnits;
            attackerActiveUnits = 0;
            defenderPassiveUnits += attackerPassiveUnits;
            attackerPassiveUnits = 0;
        }
        if (defenderActiveUnits < 0){
            attackerActiveUnits += defenderActiveUnits;
            defenderActiveUnits = 0;
            attackerPassiveUnits += defenderPassiveUnits;
            defenderPassiveUnits = 0;
        }

        map[attackerLocation[0]][attackerLocation[1]][0] = attackerActiveUnits;
        map[defenderLocation[0]][defenderLocation[1]][0] = defenderActiveUnits;
        map[attackerLocation[0]][attackerLocation[1]][1] = attackerPassiveUnits;
        map[defenderLocation[0]][defenderLocation[1]][1] = defenderPassiveUnits;
        return map;
    }

    // Checks the area around "location" for empty spaces.
    public static boolean checkForEmpty (int[][][] map, int[] location) {
        for (int row = -1; row < 2; row++) {
            for (int column = -1; column < 2; column++) {
                if (map[location[0] + row][location[1] + column][0] == 0 &&
                        map[location[0] + row][location[1] + column][1] == 0){
                    return true;
                }
            }
        }
        return false;
    }
}
