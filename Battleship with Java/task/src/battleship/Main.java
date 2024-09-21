package battleship;

import java.util.*;

public class Main {

    private static final int BOARD_SIZE = 11;
    private static final String[][] board = new String[BOARD_SIZE][BOARD_SIZE];
    private static final char EMPTY_CELL = '~';
    private static final char SHIP_CELL = 'O';
    private static final String aToJ = "ABCDEFGHIJ";
    private static final Map<String, Integer> field = new LinkedHashMap<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        createBoard();
        createField();
        printBoard();
        while (true) {
            System.out.println("Enter the coordinates of the ship:");
            String input = scanner.nextLine();
            String[] coordinates = input.split(" ");

            if (coordinates.length != 2) {
                System.out.println("Error: Invalid input. Please enter two coordinates.");
                continue;
            }
            String start = coordinates[0];
            String end = coordinates[1];
            if (isInvalidCoordinate(start) || isInvalidCoordinate(end)) {
                System.out.println("Error: Invalid coordinates. Please enter coordinates within the game field.");
                continue;
            }

            if (!isValidShipPlacement(start, end)) {
                System.out.println("Error: Invalid ship placement. Ships must be placed horizontally or vertically.");
                continue;
            }

            placeShip(start, end);
            break;
        }
    }

    private static void placeShip(String start, String end) {
        char startCol = start.charAt(0);
        int startRow = Integer.parseInt(start.substring(1));
        char endCol = end.charAt(0);
        int endRow = Integer.parseInt(end.substring(1));

        int length = Math.max(Math.abs(endCol - startCol), Math.abs(endRow - startRow)) + 1;
        List<String> parts = new ArrayList<>();

        if (startCol == endCol) {
            int minRow = Math.min(startRow, endRow);
            int maxRow = Math.max(startRow, endRow);
            for (int row = minRow; row <= maxRow; row++) {
                String coordinate = startCol + "" + row;
                parts.add(coordinate);
                field.put(coordinate, 1);
                board[row][aToJ.indexOf(startCol) + 1] = SHIP_CELL + " ";
            }
        } else {
            char minCol = (char) Math.min(startCol, endCol);
            char maxCol = (char) Math.max(startCol, endCol);
            for (char col = minCol; col <= maxCol; col++) {
                String coordinate = col + "" + startRow;
                parts.add(coordinate);
                field.put(coordinate, 1);
                board[startRow][aToJ.indexOf(col) + 1] = SHIP_CELL + " ";
            }
        }

        System.out.println("Length: " + length + " and Parts: " + String.join(" ", parts));
    }

    private static boolean isValidShipPlacement(String start, String end) {
        char starCol = start.charAt(0);
        int startRow = Integer.parseInt(start.substring(1));
        char endCol = end.charAt(0);
        int endRow = Integer.parseInt(end.substring(1));

        return starCol == endCol || startRow == endRow;
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
        for (String[] row : Main.board) {
            for (String cell : row) {
                System.out.print(cell);
            }
            System.out.println();
        }
    }
}
