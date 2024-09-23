package battleship;

import java.util.*;

public class Main {

    private static final int BOARD_SIZE = 11;
    private static final String[][] board = new String[BOARD_SIZE][BOARD_SIZE];
    private static final char EMPTY_CELL = '~';
    private static final char SHIP_CELL = 'O';
    private static final String aToJ = "ABCDEFGHIJ";
    private static final Map<String, Integer> field = new LinkedHashMap<>();
    private static final Map<String, Integer> ships = new LinkedHashMap<>();
    private static final Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        createBoard();
        createField();
        initializeShips();
        printBoard();

        for (Map.Entry<String, Integer> ship : ships.entrySet()) {
            boolean isPlaced = false;
            while (!isPlaced) {
                System.out.printf("Enter the coordinates of the %s (%d cells):%n", ship.getKey(), ship.getValue());
                String input = scanner.nextLine();
                if (checkAndPlaceShip(input, ship.getKey(), ship.getValue())) {
                    isPlaced = true;
                    printBoard();
                }
            }
        }
    }

    private static void initializeShips() {
        ships.put("Aircraft Carrier", 5);
        ships.put("Battleship", 4);
        ships.put("Submarine", 3);
        ships.put("Cruiser", 3);
        ships.put("Destroyer", 2);
    }

    private static boolean checkAndPlaceShip(String placedShip, String shipType, int shipLength) {
        String[] coordinates = placedShip.split(" ");

        if (coordinates.length != 2) {
            System.out.println("Error: Invalid input. Please enter two coordinates.");
            return false;
        }

        String start = coordinates[0];
        String end = coordinates[1];

        if (isInvalidCoordinate(start) || isInvalidCoordinate(end)) {
            System.out.println("Error: Invalid coordinates. Please enter coordinates within the game field.");
            return false;
        }

        if (!isValidShipPlacement(start, end, shipLength)) {
            System.out.println("Error: Wrong length of the " + shipType + ". Try again:");
            return false;
        }

        if (isAdjacentToAnotherShip(start, end)) {
            System.out.println("Error: You placed it too close to another one. Try again:");
            return false;
        }

        placeShip(start, end);
        return true;
    }

    private static boolean isValidShipPlacement(String start, String end, int shipLength) {
        char startCol = start.charAt(0);
        int startRow = Integer.parseInt(start.substring(1));
        char endCol = end.charAt(0);
        int endRow = Integer.parseInt(end.substring(1));

        if (startCol == endCol) {
            return Math.abs(endRow - startRow) + 1 == shipLength;
        } else if (startRow == endRow) {
            return Math.abs(endCol - startCol) + 1 == shipLength;
        }

        System.out.println("Error: Wrong ship location! Try again:");
        return false;
    }

    private static boolean isAdjacentToAnotherShip(String start, String end) {
        char startCol = start.charAt(0);
        int startRow = Integer.parseInt(start.substring(1));
        char endCol = end.charAt(0);
        int endRow = Integer.parseInt(end.substring(1));

        int minRow = Math.min(startRow, endRow);
        int maxRow = Math.max(startRow, endRow);
        char minCol = (char) Math.min(startCol, endCol);
        char maxCol = (char) Math.max(startCol, endCol);

        for (int row = minRow - 1; row <= maxRow + 1; row++) {
            for (char col = (char) (minCol - 1); col <= (char) (maxCol + 1); col++) {
                String coordinate = col + "" + row;
                if (field.containsKey(coordinate) && field.get(coordinate) == 1) {
                    return true;
                }
            }
        }

        return false;
    }

    private static void placeShip(String start, String end) {
        char startCol = start.charAt(0);
        int startRow = Integer.parseInt(start.substring(1));
        char endCol = end.charAt(0);
        int endRow = Integer.parseInt(end.substring(1));

        int minRow = Math.min(startRow, endRow);
        int maxRow = Math.max(startRow, endRow);
        char minCol = (char) Math.min(startCol, endCol);
        char maxCol = (char) Math.max(startCol, endCol);

        if (startCol == endCol) {
            for (int row = minRow; row <= maxRow; row++) {
                String coordinate = startCol + "" + row;
                field.put(coordinate, 1);
                System.out.println("Row: " + row + " and Col: " + aToJ.indexOf(startCol) + 1);
                board[row][aToJ.indexOf(startCol) + 1] = SHIP_CELL + " ";
            }
        } else {
            for (char col = minCol; col <= maxCol; col++) {
                String coordinate = col + "" + startRow;
                field.put(coordinate, 1);
                board[startRow][aToJ.indexOf(col) + 1] = SHIP_CELL + " ";
            }
        }
    }
    private static boolean isInvalidCoordinate(String coordinate) {
        return !field.containsKey(coordinate);
    }

    private static void createField() {
        for (int i = 1; i < BOARD_SIZE; i++) {
            for (int j = 1; j < BOARD_SIZE; j++) {
                field.put(aToJ.toCharArray()[j-1] + "" + i, 0);
            }
        }
        System.out.println(field);
    }

    private static void createBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (i == 0) {
                board[0][i] = "  ";
            } else {
                board[0][i] = i + " ";
            }
            for (int j = 1; j < BOARD_SIZE; j++) {
                if (i == 0) {
                    board[j][0] = aToJ.toCharArray()[j-1] + " ";
                } else {
                    board[i][j] = EMPTY_CELL + " ";
                }
            }
        }
    }

    private static void printBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (i == 0 && j == 0) {
                    System.out.print("  ");
                } else if (i == 0) {
                    System.out.print(j + " ");
                } else if (j == 0) {
                    System.out.print(aToJ.charAt(i - 1) + " ");
                } else {
                    System.out.print(board[i][j]);
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
