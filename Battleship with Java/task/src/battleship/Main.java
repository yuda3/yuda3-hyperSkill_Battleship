package battleship;

import java.util.*;

class Player {
    String name;
    Board board;
    SpecificShip[] ships;

    public Player(String name) {
        this.name = name;
        this.board = new Board();
        this.ships = new SpecificShip[5];
    }

    public void placeShips() {
        System.out.println(name + ", place your ships on the game field");
        board.initializeBoard(); // Reset the board before placing ships
        board.printBoard("main");

        ships[0] = new SpecificShip("Aircraft Carrier", 5, this.board);
        ships[1] = new SpecificShip("Battleship", 4, this.board);
        ships[2] = new SpecificShip("Submarine", 3, this.board);
        ships[3] = new SpecificShip("Cruiser", 3, this.board);
        ships[4] = new SpecificShip("Destroyer", 2, this.board);

        System.out.println("Press Enter and pass the move to another player");
        new Scanner(System.in).nextLine();
    }
}
class Board {
    private static final int boardSize = 11;
    public String[][] boardVisual = new String[boardSize][boardSize];
    public String[][] boardMain = new String[boardSize][boardSize];
    public final String[][] boardCoords = new String[boardSize][boardSize];

    public Board(){
        initializeBoard();
    }

    public void initializeBoard() {
        boardVisual[0][0] = " ";
        boardMain[0][0] = " ";
        boardCoords[0][0] = " ";

        List<String> capitalAlphabet = misc.capitalAlphabet();
        for (int i = 1; i < boardSize; i++) {
            boardCoords[i][0] = capitalAlphabet.get(i-1);
            boardMain[i][0] = capitalAlphabet.get(i -1);
            boardVisual[i][0] = capitalAlphabet.get(i -1);
        }
        for (int i = 1; i < boardSize; i++) {
            boardCoords[0][i] = i + " ";
            boardMain[0][i] = Integer.toString(i);
            boardVisual[0][i] = Integer.toString(i);
        }
        for (int i = 1; i < boardSize; i++) {
            for (int j = 1; j < boardSize; j++) {
                boardMain[i][j] = "~";
                boardVisual[i][j] = "~";
                boardCoords[i][j] = boardCoords[i][0] + j;
            }
        }
    }

    public void printBoard(String layer) {
        if (Objects.equals(layer, "visual")) {
            misc.printArray(boardVisual);
        } else if (Objects.equals(layer, "main")) {
            misc.printArray(boardMain);
        } else if (Objects.equals(layer, "coords")) {
            misc.printArray(boardCoords);
        }
    }

    public boolean searchBoardCoords(String input) {
        for (int i = 1; i < boardSize; i++) {
            for (int j = 1; j < boardSize; j++) {
                if (input.equals(boardCoords[i][j])) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean outOfBounds(int coord) {
        return !(coord < 11);
    }
}

class Ship {
    protected String start;
    protected String end;
    protected String[] placement;
    protected int[][] placementCoord;
    protected int fields;
    protected int maxLength;
    protected String shipName;
    protected String textInit;
    protected Board playerBoard;

    protected Ship(String shipName, int maxLength, Board playerBoard) {
        this.shipName = shipName;
        this.maxLength = maxLength;
        this.fields = maxLength;
        this.textInit = String.format("%s (%d cells)", shipName, maxLength);
        this.playerBoard = playerBoard; // Add this line
        System.out.printf("Enter the coordinates of the %s:%n", textInit);
        boolean idx = false;
        while (!idx) {
            Scanner sc = new Scanner(System.in);
            String[] input = sc.nextLine().split(" ");
            start = input[0];
            end = input[1];
            idx = shipPlacement(start, end);
        }
    }
    protected boolean shipPlacement(String start, String end) {
        if (!(shipOnBoard(start, end))) {
            System.out.println("Error!");
            return false;
        } else if (
                shipValidHorizontal(start, end) && shipValidHorizontalLength(start, end)
        ) {
            String[] placement = new String[Math.abs(misc.realCoords(start)[1] - misc.realCoords(end)[1]) + 1];
            if ((Math.abs(misc.realCoords(start)[1] - misc.realCoords(end)[1]) + 1) != maxLength){
                System.out.printf("Error! Wrong length of the %s! Try again:%n", textInit);
            }

            String[] splitStart = start.split("");
            String[] splitEnd = end.split("");
            if (misc.realCoords(start)[1] < misc.realCoords(end)[1]){
                for (int i = 0; i < placement.length; i++) {
                    placement[i] = splitStart[0] + (misc.realCoords(start)[1] + i);
                }
            } else if (misc.realCoords(start)[1] > misc.realCoords(end)[1]){
                for (int i = placement.length; i > 0; i--) {
                    placement[i-1] = splitEnd[0] + (misc.realCoords(end)[1] + i - 1);
                }
            }
            setPlacement(placement);
            setFields(Math.abs(misc.realCoords(start)[1] - misc.realCoords(end)[1]) + 1);
            setStart(start);
            setEnd(end);
            placementCoord = new int[this.getLength()][2];
            for (int i = 0; i < placement.length; i++) {
                placementCoord[i] = misc.realCoords(placement[i]);
            }
            if (shipClose(placementCoord, start, end)) {
                System.out.println("Length: " + (Math.abs(misc.realCoords(start)[1] - misc.realCoords(end)[1]) + 1));
                System.out.print("Parts:");
                if (misc.realCoords(start)[1] < misc.realCoords(end)[1]){
                    for (String s : placement) {
                        System.out.print(" " + s);
                    }
                    System.out.printf("%n");
                } else if (misc.realCoords(start)[1] > misc.realCoords(end)[1]){
                    for (int i = placement.length; i > 0; i--) {
                        System.out.print(" " + placement[i-1]);
                    }
                }
            } else {
                System.out.println("Error! You placed it too close to another one. Try again:");
                return false;
            }
        } else if (
                shipValidVertical(start, end) && shipValidVerticalLength(start, end)
        ) {
            String[] placement = new String[Math.abs(misc.realCoords(start)[0] - misc.realCoords(end)[0]) + 1];
            String[] splitStart = start.split("");
            String[] splitEnd = end.split("");
            if (misc.realCoords(start)[0] < misc.realCoords(end)[0]){
                for (int i = 0; i < placement.length; i++) {
                    placement[i] = misc.capitalAlphabet().get(misc.realCoords(start)[0] - 1 + i) + misc.realCoords(start)[1];
                }
            } else if (misc.realCoords(start)[0] > misc.realCoords(end)[0]){
                for (int i = placement.length; i > 0; i--) {
                    placement[i - 1] = misc.capitalAlphabet().get(misc.realCoords(end)[0] - 2 + i) + misc.realCoords(start)[1];
                }
            }
            setPlacement(placement);
            setFields(Math.abs(misc.realCoords(start)[0] - misc.realCoords(end)[0]) + 1);
            setStart(start);
            setEnd(end);

            placementCoord = new int[placement.length][2];
            for (int i = 0; i < placement.length; i++) {
                placementCoord[i] = misc.realCoords(placement[i]);
            }
            if (shipClose(placementCoord, start, end)) {
                System.out.println("Length: " + (Math.abs(misc.realCoords(start)[0] - misc.realCoords(end)[0]) + 1));
                System.out.print("Parts:");
                if (misc.realCoords(start)[0] < misc.realCoords(end)[0]){
                    for (String s : placement) {
                        System.out.print(" " + s);
                    }
                } else if (misc.realCoords(start)[0] > misc.realCoords(end)[0]){
                    for (int i = placement.length; i > 0; i--) {
                        System.out.print(" " + placement[i - 1]);
                    }
                }
            } else {
                System.out.println("Error! You placed it too close to another one. Try again:");
                return false;
            }

        } else if (!(shipValidVerticalLength(start, end) || shipValidHorizontalLength(start, end))) {
            System.out.printf("Error! Wrong length of %s! Try again:%n", textInit);
            return false;
        } else {
            System.out.println("Error! Wrong ship location! Try again:");
            System.out.println("Something in shipPlacement went wrong :(");
            return false;
        }
        for (int i = 0; i < placement.length; i++) {
            playerBoard.boardMain[placementCoord[i][0]][placementCoord[i][1]] = "O";
        }
        System.out.println();
        playerBoard.printBoard("main");
        return true;
    }
    public boolean isSunk(Board board) {
        for (int[] coord : this.getPlacementCoord()) {
            if (!board.boardMain[coord[0]][coord[1]].equals("X")) {
                return false; // Found an unhit part of the ship
            }
        }
        return true; // All parts are hit
    }
    public void setPlacementCoord(int[][] placementCoord) {
        this.placementCoord = placementCoord;
    }

    public int[][] getPlacementCoord() {
        return placementCoord;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setPlacement(String[] placement) {
        this.placement = placement;
    }

    public void setFields(int fields) {
        this.fields = fields;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String[] getPlacement() {
        return placement;
    }

    public int getLength() {
        return fields;
    }

    protected boolean checkBoard () {
        return Objects.equals(playerBoard.boardMain[1][1], "O");
    }

    protected boolean shipClose(int[][] placementCoord, String start, String end) {
        int blocked = 0;
        if (shipValidHorizontal(start, end)){
            for (int[] ints : placementCoord) {
                if (!(Board.outOfBounds(ints[0] - 1) || Board.outOfBounds(ints[1]))) {
                    blocked += Objects.equals(playerBoard.boardMain[ints[0] - 1][ints[1]], "O") ? 1 : 0;
                }
                if (!(Board.outOfBounds(ints[0] + 1) || Board.outOfBounds(ints[1]))) {
                    blocked += Objects.equals(playerBoard.boardMain[ints[0] + 1][ints[1]], "O") ? 1 : 0;
                }
                blocked += Objects.equals(playerBoard.boardMain[ints[0]][ints[1]], "O") ? 1 : 0;
            }
            if (
                    placementCoord[0][1] > placementCoord[placementCoord.length-1][1]
            ) {
                if (!(Board.outOfBounds(placementCoord[0][0]) || Board.outOfBounds(placementCoord[0][1] + 1))){
                    blocked += Objects.equals(playerBoard.boardMain[placementCoord[0][0]][placementCoord[0][1] + 1], "O") ? 1 : 0;
                }
                if (!(Board.outOfBounds(placementCoord[0][0] - 1) || Board.outOfBounds(placementCoord[0][1] + 1))){
                    blocked += Objects.equals(playerBoard.boardMain[placementCoord[0][0] - 1][placementCoord[0][1] + 1], "O") ? 1 : 0;
                }
                if (!(Board.outOfBounds(placementCoord[0][0] + 1) || Board.outOfBounds(placementCoord[0][1] + 1))){
                    blocked += Objects.equals(playerBoard.boardMain[placementCoord[0][0] + 1][placementCoord[0][1] + 1], "O") ? 1 : 0;
                }
                if (!(Board.outOfBounds(placementCoord[0][0]) || Board.outOfBounds(placementCoord[0][1] - 1))){
                    blocked += Objects.equals(playerBoard.boardMain[placementCoord[0][0]][placementCoord[0][1] - 1], "O") ? 1 : 0;
                }
                if (!(Board.outOfBounds(placementCoord[0][0] - 1) || Board.outOfBounds(placementCoord[0][1] - 1))){
                    blocked += Objects.equals(playerBoard.boardMain[placementCoord[0][0] - 1][placementCoord[0][1] - 1], "O") ? 1 : 0;
                }
                if (!(Board.outOfBounds(placementCoord[0][0] + 1) || Board.outOfBounds(placementCoord[0][1] - 1))){
                    blocked += Objects.equals(playerBoard.boardMain[placementCoord[0][0] + 1][placementCoord[0][1] - 1], "O") ? 1 : 0;
                }
            } else if (
                    placementCoord[0][1] < placementCoord[placementCoord.length-1][1]
            ) {
                if (!(Board.outOfBounds(placementCoord[0][0]) || Board.outOfBounds(placementCoord[0][1] - 1))){
                    blocked += Objects.equals(playerBoard.boardMain[placementCoord[0][0]][placementCoord[0][1] - 1], "O") ? 1 : 0;
                }
                if (!(Board.outOfBounds(placementCoord[0][0] - 1) || Board.outOfBounds(placementCoord[0][1] - 1))){
                    blocked += Objects.equals(playerBoard.boardMain[placementCoord[0][0] - 1][placementCoord[0][1] - 1], "O") ? 1 : 0;
                }
                if (!(Board.outOfBounds(placementCoord[0][0] + 1) || Board.outOfBounds(placementCoord[0][1] - 1))){
                    blocked += Objects.equals(playerBoard.boardMain[placementCoord[0][0] + 1][placementCoord[0][1] - 1], "O") ? 1 : 0;
                }
                if (!(Board.outOfBounds(placementCoord[placementCoord.length-1][0]) || Board.outOfBounds(placementCoord[placementCoord.length-1][1] + 1))){
                    blocked += Objects.equals(playerBoard.boardMain[placementCoord[placementCoord.length-1][0]][placementCoord[placementCoord.length-1][1] + 1], "O") ? 1 : 0;
                }
                if (!(Board.outOfBounds(placementCoord[placementCoord.length-1][0] - 1) || Board.outOfBounds(placementCoord[placementCoord.length-1][1] - 1))){
                    blocked += Objects.equals(playerBoard.boardMain[placementCoord[placementCoord.length-1][0] - 1][placementCoord[placementCoord.length-1][1] - 1], "O") ? 1 : 0;
                }
                if (!(Board.outOfBounds(placementCoord[placementCoord.length-1][0] + 1) || Board.outOfBounds(placementCoord[placementCoord.length-1][1] - 1))){
                    blocked += Objects.equals(playerBoard.boardMain[placementCoord[placementCoord.length-1][0] + 1][placementCoord[placementCoord.length-1][1] - 1], "O") ? 1 : 0;
                }
            }
        } else if (shipValidVertical(start, end)) {
            for (int[] ints : placementCoord) {
                blocked += Objects.equals(playerBoard.boardMain[ints[0]][ints[1]], "O") ? 1 : 0;
                if (!(Board.outOfBounds(ints[0]) || Board.outOfBounds(ints[1] - 1))) {
                    blocked += Objects.equals(playerBoard.boardMain[ints[0]][ints[1] - 1], "O") ? 1 : 0;
                }
                if (!(Board.outOfBounds(ints[0]) || Board.outOfBounds(ints[1] + 1))) {
                    blocked += Objects.equals(playerBoard.boardMain[ints[0]][ints[1] + 1], "O") ? 1 : 0;
                }
            }
            if (
                    placementCoord[0][0] > placementCoord[placementCoord.length-1][0]
            ) {
                if (!(Board.outOfBounds(placementCoord[0][0] + 1) || Board.outOfBounds(placementCoord[0][1]))){
                    blocked += Objects.equals(playerBoard.boardMain[placementCoord[0][0] + 1][placementCoord[0][1]], "O") ? 1 : 0;
                }
                if (!(Board.outOfBounds(placementCoord[0][0] + 1) || Board.outOfBounds(placementCoord[0][1] + 1))){
                    blocked += Objects.equals(playerBoard.boardMain[placementCoord[0][0] + 1][placementCoord[0][1] + 1], "O") ? 1 : 0;
                }
                if (!(Board.outOfBounds(placementCoord[0][0] + 1) || Board.outOfBounds(placementCoord[0][1] - 1))){
                    blocked += Objects.equals(playerBoard.boardMain[placementCoord[0][0] + 1][placementCoord[0][1] - 1], "O") ? 1 : 0;
                }
                if (!(Board.outOfBounds(placementCoord[placementCoord.length - 1][0] - 1) || Board.outOfBounds(placementCoord[placementCoord.length - 1][1]))){
                    blocked += Objects.equals(playerBoard.boardMain[placementCoord[placementCoord.length - 1][0] - 1][placementCoord[placementCoord.length - 1][1]], "O") ? 1 : 0;
                }
                if (!(Board.outOfBounds(placementCoord[placementCoord.length - 1][0] - 1) || Board.outOfBounds(placementCoord[placementCoord.length - 1][1] - 1))){
                    blocked += Objects.equals(playerBoard.boardMain[placementCoord[placementCoord.length - 1][0] - 1][placementCoord[placementCoord.length - 1][1] - 1], "O") ? 1 : 0;
                }
                if (!(Board.outOfBounds(placementCoord[placementCoord.length - 1][0] - 1) || Board.outOfBounds(placementCoord[placementCoord.length - 1][1] + 1))){
                    blocked += Objects.equals(playerBoard.boardMain[placementCoord[placementCoord.length - 1][0] - 1][placementCoord[placementCoord.length - 1][1] + 1], "O") ? 1 : 0;
                }
            } else if (
                    placementCoord[0][0] < placementCoord[placementCoord.length-1][0]
            ) {
                if (!Board.outOfBounds(placementCoord[0][0] - 1)){
                    blocked += Objects.equals(playerBoard.boardMain[placementCoord[0][0] - 1][placementCoord[0][1]], "O") ? 1 : 0;
                }

                if (!(Board.outOfBounds(placementCoord[0][0] - 1) || Board.outOfBounds(placementCoord[0][1] + 1))) {
                    blocked += Objects.equals(playerBoard.boardMain[placementCoord[0][0] - 1][placementCoord[0][1] + 1], "O") ? 1 : 0;
                }
                if (!(Board.outOfBounds(placementCoord[0][0] - 1) || Board.outOfBounds(placementCoord[0][1] - 1))) {
                    blocked += Objects.equals(playerBoard.boardMain[placementCoord[0][0] - 1][placementCoord[0][1] - 1], "O") ? 1 : 0;
                }
                if (!(Board.outOfBounds(placementCoord[placementCoord.length-1][0] + 1))) {
                    blocked += Objects.equals(playerBoard.boardMain[placementCoord[placementCoord.length-1][0] + 1][placementCoord[0][1]], "O") ? 1 : 0;
                }
                if (!(Board.outOfBounds(placementCoord[placementCoord.length-1][0] + 1) || Board.outOfBounds(placementCoord[0][1] + 1))) {
                    blocked += Objects.equals(playerBoard.boardMain[placementCoord[placementCoord.length-1][0] + 1][placementCoord[0][1] + 1], "O") ? 1 : 0;
                }
                if (!(Board.outOfBounds(placementCoord[placementCoord.length - 1][0] + 1) || Board.outOfBounds(placementCoord[0][1] - 1))) {
                    blocked += Objects.equals(playerBoard.boardMain[placementCoord[placementCoord.length - 1][0] + 1][placementCoord[0][1] - 1], "O") ? 1 : 0;
                }
            }
        }
        return blocked <= 0;
    }
    protected boolean shipOnBoard(String start, String end) {
        return playerBoard.searchBoardCoords(start) && playerBoard.searchBoardCoords(end);
    }
    protected boolean shipValidHorizontalLength (String start, String end) {
        return (Math.abs(misc.realCoords(start)[0] - misc.realCoords(end)[0]) < 5)
                && (Math.abs(misc.realCoords(start)[0] - misc.realCoords(end)[0]) > 0);
    }
    protected boolean shipValidVerticalLength (String start, String end) {
        return (Math.abs(misc.realCoords(start)[1] - misc.realCoords(end)[1]) < 5)
                && (Math.abs(misc.realCoords(start)[1] - misc.realCoords(end)[1]) > 0);
    }
    protected boolean shipValidHorizontal (String start, String end) {
        return (misc.realCoords(start)[0] == misc.realCoords(end)[0]);
    }
    protected boolean shipValidVertical (String start, String end) {
        return (misc.realCoords(start)[1] == misc.realCoords(end)[1]);
    }
}

class SpecificShip extends Ship {

    protected SpecificShip(String shipName, int maxLength, Board playerBoard){
        super(shipName, maxLength, playerBoard);
    }
    @Override
    protected boolean shipValidHorizontalLength (String start, String end) {
//        System.out.println("@Override shipValidHorizontalLength");
//        System.out.println(Math.abs(misc.realCoords(start)[1] - misc.realCoords(end)[1]));
//        System.out.println("maxLength = " + maxLength);
        return (Math.abs(misc.realCoords(start)[1] - misc.realCoords(end)[1]) == maxLength - 1);
    }

    @Override
    protected boolean shipValidVerticalLength (String start, String end) {
//        System.out.println("@Override shipValidVerticalLength");
//        System.out.println(Math.abs(misc.realCoords(start)[0] - misc.realCoords(end)[0]));
//        System.out.println("maxLength = " + maxLength);
        return (Math.abs(misc.realCoords(start)[0] - misc.realCoords(end)[0]) == maxLength - 1);
    }

    @Override
    protected boolean shipValidHorizontal (String start, String end) {
//        System.out.println("@Override shipValidHorizontal");
        return (misc.realCoords(start)[0] == misc.realCoords(end)[0]);
    }

    @Override
    protected boolean shipValidVertical (String start, String end) {
//        System.out.println("@Override shipValidVertical");
        return (misc.realCoords(start)[1] == misc.realCoords(end)[1]);
    }
}

class Shooting {
    public static int shoot(Board board, String coord, SpecificShip[] ships) {
        int[] realCoord = misc.realCoords(coord);
        String currentCell = board.boardMain[realCoord[0]][realCoord[1]];

        if (currentCell.equals("O") || currentCell.equals("X")) {
            board.boardMain[realCoord[0]][realCoord[1]] = "X";
            board.boardVisual[realCoord[0]][realCoord[1]] = "X";

            // Now check if the ship is sunk
            for (SpecificShip ship : ships) {
                for (int[] cell : ship.getPlacementCoord()) {
                    if (cell[0] == realCoord[0] && cell[1] == realCoord[1]) {
                        // Found the ship that was hit
                        if (ship.isSunk(board)) {
                            return 2; // Ship is sunk
                        } else {
                            return 1; // Ship is hit but not sunk
                        }
                    }
                }
            }
            return 1; // Default to hit if ship not found (shouldn't happen)
        } else {
            board.boardMain[realCoord[0]][realCoord[1]] = "M";
            board.boardVisual[realCoord[0]][realCoord[1]] = "M";
            return 0; // Miss
        }
    }

    public static boolean gameOver(Board board) {
        for (int i = 1; i < board.boardMain.length; i++) {
            for (int j = 1; j < board.boardMain.length; j++) {
                if (board.boardMain[i][j].equals("O")) {
                    return false;
                }
            }
        }
        return true;
    }
}

class misc {
    public static void printArray(String[][] array){
        for (String[] strings : array) {
            for (String string : strings) {
                System.out.print(string + " ");
            }
            System.out.printf("%n");
        }
    }

    public static List<String> capitalAlphabet() {
        List<String> capitalAlphabet = new ArrayList<>();

        for (char ch = 'A'; ch <= 'Z'; ch++) {
            capitalAlphabet.add("" + ch);
        }
        return capitalAlphabet;
    }

    public static int[] realCoords(String coords){
        List<String> capitalAlphabet = capitalAlphabet();
        String[] split = coords.split("");
        if (split.length == 3) {
            return new int[]{
                    capitalAlphabet.indexOf(split[0]) + 1,
                    Integer.parseInt(split[1]+split[2])
            };
        } else if (split.length == 2) {
            return new int[]{
                    capitalAlphabet.indexOf(split[0]) + 1,
                    Integer.parseInt(split[1])
            };
        } else { return new int[]{0,0};}
    }
}


public class Main {
    public static void main(String[] args) {
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");

        player1.placeShips();
        player2.placeShips();

        boolean gameOver = false;
        Player currentPlayer = player1;
        Player otherPlayer = player2;

        while (true) {
            printGameState(currentPlayer, otherPlayer);
            System.out.println(currentPlayer.name + ", it's your turn:");

            String shotCoord = new Scanner(System.in).nextLine();
            int result = Shooting.shoot(otherPlayer.board, shotCoord, otherPlayer.ships);

            gameOver = Shooting.gameOver(otherPlayer.board);

            if (gameOver) {
                System.out.println("You sank the last ship. You won. Congratulations!");
                break;
            } else {
                if (result == 2) {
                    System.out.println("You sank a ship!");
                } else if (result == 1) {
                    System.out.println("You hit a ship!");
                } else {
                    System.out.println("You missed!");
                }

                System.out.println("Press Enter and pass the move to another player");
                new Scanner(System.in).nextLine();

                // Swap players
                Player temp = currentPlayer;
                currentPlayer = otherPlayer;
                otherPlayer = temp;
            }
        }

        System.out.println(currentPlayer.name + " won. Congratulations!");
    }

    private static void printGameState(Player currentPlayer, Player otherPlayer) {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (int i = 1; i < 11; i++) {
            System.out.print((char)('A' + i - 1) + " ");
            for (int j = 1; j < 11; j++) {
                System.out.print(otherPlayer.board.boardVisual[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("---------------------");
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (int i = 1; i < 11; i++) {
            System.out.print((char)('A' + i - 1) + " ");
            for (int j = 1; j < 11; j++) {
                System.out.print(currentPlayer.board.boardMain[i][j] + " ");
            }
            System.out.println();
        }
    }
}

