import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static int activeUnitAcquisitionSpeed = 5;
    public static int[] playerLocation = new int[2];
    public static boolean playerAlive = true;
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

        //Prints the entire map
        printMap(mapNumbers);

        // Creates the player's starting location
        playerLocation[0] = numGen.nextInt(10);
        playerLocation[1] = numGen.nextInt(10);
        System.out.println("Your location is: " + Arrays.toString(playerLocation));

        // Erasing a square on the board for movement testing.
//        mapNumbers[playerLocation[0]+1][playerLocation[1]+1][0] = 0;
//        mapNumbers[playerLocation[0]+1][playerLocation[1]+1][1] = 0;
//        printMap(mapNumbers);

        while (playerAlive) {
            mapNumbers = playerTurn(mapNumbers);
            System.out.println();
            System.out.println();
            printMap(mapNumbers);
            if (mapNumbers[playerLocation[0]][playerLocation[1]][0] == 0 && mapNumbers[playerLocation[0]][playerLocation[1]][1] == 0) {
                playerAlive = false;
            }
        }
        System.out.println("Game Over");
//        progressTurn(mapNumbers);
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
        System.out.println("3. Move into a completely empty square");
        System.out.println("Choose by typing 1 for attack, 2 for wait, and 3 for move.");
        int playerChoice = input.nextInt();
        if (playerChoice == 1) {
            map = playerAttacksSquare(map);
        } else if (playerChoice == 3) {
            if (checkForEmpty(map,playerLocation)){
                map = playerMovesSquare(map);
            } else {
                System.out.println("You can't move because there are no empty spaces around you.");
                map = playerTurn(map);
            }
        } else {
            map[playerLocation[0]][playerLocation[1]][0] += map[playerLocation[0]][playerLocation[1]][1]/activeUnitAcquisitionSpeed;
        }
        map = progressTurn(map);
        return map;
    }

    // lets the player choose which square to attack, then attacks that square with the method attackSquare and
    // user parameters.
    public static int[][][] playerAttacksSquare (int[][][] map) {
        Scanner input = new Scanner(System.in);
        int[] attackSquare = new int[2];
        System.out.println("Choose which square you want to attack.");
        System.out.println("Type the row that you want to attack");
        attackSquare[0] = input.nextInt();
        System.out.println("Type the column that you want to attack");
        attackSquare[1] = input.nextInt();
        if (Math.abs(attackSquare[0]-playerLocation[0]) <= 1 && Math.abs(attackSquare[1]-playerLocation[1]) <= 1) {
            if (attackSquare[0] > 9 || attackSquare[1] > 9 || attackSquare[0] < 0 || attackSquare[1] < 0) {
                System.out.println("Out of bounds. Choose again.");
                map = playerAttacksSquare(map);
            } else {
                map = attackSquare(map, playerLocation, attackSquare);
            }
        } else {
            System.out.println("Too far away. Choose again.");
            map = playerAttacksSquare(map);
        }
        return map;
    }

    // Moves the player to a square
    // TODO Fix bug where the player moves into the wrong square.
    // TODO Find a way to replicate said bug that I found at some point in time.
    public static int[][][] playerMovesSquare (int[][][] map){
        Scanner input = new Scanner(System.in);
        int[] playerMoveChoice = new int[2];
        System.out.println("Choose the square you would like to move to.");
        System.out.println("Type the row you would like to move to.");
        playerMoveChoice[0] = input.nextInt();
        System.out.println("Type the column you would like to move to.");
        playerMoveChoice[1] = input.nextInt();

        // Checks for user area and asks for correct info if necessary.
        if (map[playerMoveChoice[0]][playerMoveChoice[1]][0] != 0 ||
                map[playerMoveChoice[0]][playerMoveChoice[1]][1] != 0) {
            System.out.println("Square was not empty. Choose another square.");
            map = playerMovesSquare(map);
        }

        // Moves the player's units to the chosen square, erases the duplicates,
        // then set's the player's location to the new spot.
        map[playerMoveChoice[0]][playerMoveChoice[1]][0] = map[playerLocation[0]][playerLocation[1]][0];
        map[playerMoveChoice[0]][playerMoveChoice[1]][1] = map[playerLocation[0]][playerLocation[1]][1];
        map[playerLocation[0]][playerLocation[1]][0] = 0;
        map[playerLocation[0]][playerLocation[1]][1] = 0;
        playerLocation[0] = playerMoveChoice[0];
        playerLocation[1] = playerMoveChoice[1];
        System.out.println("New playerLocation: " + playerLocation[0] + ", " + playerLocation[1]);
        return map;
    }

    // Progresses all of the passive actions that happens after the player turn.
    public static int[][][] progressTurn (int[][][] map) {
        Random numGen = new Random();
        for (int row = 0; row < map.length; row++) {
            for (int column = 0; column < map[row].length; column++) {
                int[] currLocation = {row,column};
                if (!(playerLocation[0] == row && playerLocation[1] == column) && map[row][column][1] != 0) {
                    int choice = numGen.nextInt(5);
                    if (choice == 4) {
                        // Attempts to attack a square
                        int attackRowOffset = numGen.nextInt(3) - 1;
                        int attackColumnOffset = numGen.nextInt(3) - 1;
                        if ((attackRowOffset != 0 || attackColumnOffset != 0) &&
                                (attackRowOffset+row < 10) && (attackColumnOffset+column < 10) &&
                                (attackRowOffset+row >= 0) && (attackColumnOffset+column >= 0)) {
//                            int[] attackerArray = {row,column};
                            int[] defenderArray = {row + attackRowOffset,column + attackColumnOffset};
                            map = attackSquare(map,currLocation,defenderArray);
                        } else {
                            // If the attack fails, it does the normal active unit increase.
                            map[row][column][0] += map[row][column][1]/activeUnitAcquisitionSpeed;
                        }
                    } else if (choice == 3) {
                        if (checkForEmpty(map,currLocation)) {
                            int moveRowOffset = numGen.nextInt(3) - 1;
                            int moveColumnOffset = numGen.nextInt(3) - 1;
                            if ((moveRowOffset != 0 || moveColumnOffset != 0) &&
                                    (moveRowOffset+row < 10) && (moveColumnOffset+column < 10) &&
                                    (moveRowOffset+row >= 0) && (moveColumnOffset+column >= 0)) {
                                map[moveRowOffset+row][moveColumnOffset+column][0] = map[row][column][0];
                                map[moveRowOffset+row][moveColumnOffset+column][1] = map[row][column][1];
                                map[row][column][0] = 0;
                                map[row][column][1] = 0;
//                                System.out.println("Moved untis at: " + row + ", " + column);
                            }
                        }
                    } else {
                        // Increases active unit count based on passive unit count
                        map[row][column][0] += map[row][column][1]/activeUnitAcquisitionSpeed;
                    }
                }
            }
        }


        for (int row = 0; row < map.length; row++) {
            for (int column = 0; column < map[row].length; column++) {
                // Checks if either the active or passive units are over the max cap and correct if necessary.
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
    public static int[][][] attackSquare (int[][][] map, int[] attackerLocation, int[] defenderLocation) {
        int attackerActiveUnits = map[attackerLocation[0]][attackerLocation[1]][0];
        int defenderActiveUnits = map[defenderLocation[0]][defenderLocation[1]][0];
        int attackerPassiveUnits = map[attackerLocation[0]][attackerLocation[1]][1];
        int defenderPassiveUnits = map[defenderLocation[0]][defenderLocation[1]][1];
        Random numGen = new Random();
        int result = attackerActiveUnits - numGen.nextInt(attackerActiveUnits + defenderActiveUnits) + 1;
        if (result > 0) {
            System.out.println("result = attacker gained " + result + " units at most");
        } else {
            System.out.println("result = attacker lost " + Math.abs(result) + " units at most");
        }
//        attackerActiveUnits += result;
//        defenderActiveUnits -= result;

        if (result > 0) {
            defenderActiveUnits -= result;
            attackerPassiveUnits += result;
        } else if (result < 0) {
            attackerActiveUnits += result;
            defenderPassiveUnits -= result;
        }

        // Accounts for cases where either army takes more active units than what is actually available.
        if (attackerActiveUnits < 0){
            defenderPassiveUnits += attackerActiveUnits;
            attackerActiveUnits = 0;
            defenderPassiveUnits += attackerPassiveUnits;
            attackerPassiveUnits = 0;
        }
        if (defenderActiveUnits < 0){
            attackerPassiveUnits += defenderActiveUnits;
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
                if (location[0] + row < 10 && location[0] + row > -1 &&
                        location[1] + column < 10 && location[1] + column > -1) {
                    if (map[location[0] + row][location[1] + column][0] == 0 &&
                            map[location[0] + row][location[1] + column][1] == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}