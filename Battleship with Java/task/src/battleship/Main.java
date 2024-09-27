package battleship;

import java.util.*;

public class Main {

    private static final int BOARD_SIZE = 11;
    private static final String[][] board = new String[BOARD_SIZE][BOARD_SIZE];
    private static final char EMPTY_CELL = '~';
    private static final char SHIP_CELL = 'O';
    private static final char HIT_CELL = 'X';
    private static final char MISS_CELL = 'M';
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

        System.out.println("The game starts!");
        printBoard();

        boolean validShot = false;
        while (!validShot) {
            System.out.println("Take a shot!");
            String shot = scanner.nextLine().toUpperCase();
            if (isValidCoordinate(shot)) {
                boolean isHit = takeShot(shot);
                printBoard();
                System.out.println(isHit ? "You hit a ship!" : "You missed!");
                validShot = true;
            } else {
                System.out.println("Error: You entered the wrong coordinates. Try again:");
            }
        }
    }

    private static boolean takeShot(String shot) {
        int row = getRowIndex(shot.charAt(0));
        int col = Integer.parseInt(shot.substring(1));

        if (board[row][col].trim().equals(String.valueOf(SHIP_CELL))) {
            board[row][col] = HIT_CELL + " ";
            return true;
        } else {
            board[row][col] = MISS_CELL + " ";
            return false;
        }
    }

    private static boolean isValidCoordinate(String coordinate) {
        if (coordinate.length() < 2 || coordinate.length() > 3) {
            return false;
        }
        char rowChar = Character.toUpperCase(coordinate.charAt(0));
        if (aToJ.indexOf(rowChar) == -1) {
            return false;
        }
        String colStr = coordinate.substring(1);
        int col;
        try {
            col = Integer.parseInt(colStr);
        } catch (NumberFormatException e) {
            return false;
        }
        return col >= 1 && col <= 10;
    }


    private static void initializeShips() {
        ships.put("Aircraft Carrier", 5);
        ships.put("Battleship", 4);
        ships.put("Submarine", 3);
        ships.put("Cruiser", 3);
        ships.put("Destroyer", 2);
    }

    private static boolean checkAndPlaceShip(String placedShip, String shipType, int shipLength) {
        String[] coordinates = placedShip.toUpperCase().split(" ");

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

        if (isOverlapping(start, end)) {
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
        char startRowChar = Character.toUpperCase(start.charAt(0));
        int startRow = getRowIndex(startRowChar);
        int startCol = Integer.parseInt(start.substring(1));

        char endRowChar = Character.toUpperCase(end.charAt(0));
        int endRow = getRowIndex(endRowChar);
        int endCol = Integer.parseInt(end.substring(1));

        if (startRow == endRow) {
            return Math.abs(endCol - startCol) + 1 == shipLength;
        } else if (startCol == endCol) {
            return Math.abs(endRow - startRow) + 1 == shipLength;
        } else {
            System.out.println("Error: Wrong ship location! Try again:");
            return false;
        }
    }

    private static boolean isOverlapping(String start, String end) {
        List<String> coordinates = getCoordinatesBetween(start, end);
        for (String coordinate : coordinates) {
            if (field.get(coordinate) == 1) {
                System.out.println("Error: You placed it on another ship. Try again:");
                return true;
            }
        }
        return false;
    }

    private static List<String> getCoordinatesBetween(String start, String end) {
        List<String> coordinates = new ArrayList<>();
        char startRowChar = Character.toUpperCase(start.charAt(0));
        int startRow = getRowIndex(startRowChar);
        int startCol = Integer.parseInt(start.substring(1));

        char endRowChar = Character.toUpperCase(end.charAt(0));
        int endRow = getRowIndex(endRowChar);
        int endCol = Integer.parseInt(end.substring(1));

        int minRow = Math.min(startRow, endRow);
        int maxRow = Math.max(startRow, endRow);
        int minCol = Math.min(startCol, endCol);
        int maxCol = Math.max(startCol, endCol);

        if (startRow == endRow) {
            // Horizontal ship
            for (int col = minCol; col <= maxCol; col++) {
                String coordinate = startRowChar + "" + col;
                coordinates.add(coordinate);
            }
        } else {
            // Vertical ship
            for (int row = minRow; row <= maxRow; row++) {
                String coordinate = aToJ.charAt(row - 1) + "" + startCol;
                coordinates.add(coordinate);
            }
        }
        return coordinates;
    }

    private static boolean isAdjacentToAnotherShip(String start, String end) {
        char startRowChar = Character.toUpperCase(start.charAt(0));
        int startRow = getRowIndex(startRowChar);
        int startCol = Integer.parseInt(start.substring(1));

        char endRowChar = Character.toUpperCase(end.charAt(0));
        int endRow = getRowIndex(endRowChar);
        int endCol = Integer.parseInt(end.substring(1));

        int minRow = Math.min(startRow, endRow);
        int maxRow = Math.max(startRow, endRow);
        int minCol = Math.min(startCol, endCol);
        int maxCol = Math.max(startCol, endCol);

        for (int row = minRow - 1; row <= maxRow + 1; row++) {
            if (row < 1 || row > 10) continue;
            for (int col = minCol - 1; col <= maxCol + 1; col++) {
                if (col < 1 || col > 10) continue;
                String coordinate = aToJ.charAt(row - 1) + "" + col;
                if (field.containsKey(coordinate) && field.get(coordinate) == 1) {
                    if (!getCoordinatesBetween(start, end).contains(coordinate)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static void placeShip(String start, String end) {
        char startRowChar = Character.toUpperCase(start.charAt(0));
        int startRow = getRowIndex(startRowChar);
        int startCol = Integer.parseInt(start.substring(1));

        char endRowChar = Character.toUpperCase(end.charAt(0));
        int endRow = getRowIndex(endRowChar);
        int endCol = Integer.parseInt(end.substring(1));

        int minRow = Math.min(startRow, endRow);
        int maxRow = Math.max(startRow, endRow);
        int minCol = Math.min(startCol, endCol);
        int maxCol = Math.max(startCol, endCol);

        if (startRow == endRow) {
            // Horizontal ship
            for (int col = minCol; col <= maxCol; col++) {
                String coordinate = startRowChar + "" + col;
                field.put(coordinate, 1);
                board[startRow][col] = SHIP_CELL + " ";
            }
        } else {
            // Vertical ship
            for (int row = minRow; row <= maxRow; row++) {
                String coordinate = aToJ.charAt(row - 1) + "" + startCol;
                field.put(coordinate, 1);
                board[row][startCol] = SHIP_CELL + " ";
            }
        }
    }

    private static boolean isInvalidCoordinate(String coordinate) {
        if (coordinate.length() < 2) {
            return true;
        }
        char rowChar = Character.toUpperCase(coordinate.charAt(0));
        if (aToJ.indexOf(rowChar) == -1) {
            return true;
        }
        String colStr = coordinate.substring(1);
        int col;
        try {
            col = Integer.parseInt(colStr);
        } catch (NumberFormatException e) {
            return true;
        }
        return col < 1 || col > 10;
    }

    private static void createField() {
        for (int i = 0; i < aToJ.length(); i++) {
            for (int j = 1; j <= 10; j++) {
                String coordinate = aToJ.charAt(i) + "" + j;
                field.put(coordinate, 0);
            }
        }
    }

    private static void createBoard() {
        board[0][0] = "  ";
        for (int j = 1; j < BOARD_SIZE; j++) {
            board[0][j] = j + " ";
        }
        for (int i = 1; i < BOARD_SIZE; i++) {
            board[i][0] = aToJ.charAt(i - 1) + " ";
            for (int j = 1; j < BOARD_SIZE; j++) {
                board[i][j] = EMPTY_CELL + " ";
            }
        }
    }

    private static void printBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    private static int getRowIndex(char rowChar) {
        return aToJ.indexOf(rowChar) + 1;
    }
}
