package uis;

import blokus.Board;
import blokus.Move;
import blokus.Orientation;
import blokus.PieceID;

import java.util.Scanner;
import java.util.stream.Collectors;

public class TtyUITest implements UI {
    private Board board;
    private int turn = -1;
    private int playerColor;
    private int amountOfPlayers;
    private int moveCounter = 0;

    public TtyUITest (Board board, int playerColor) {
        this.board = board;
        this.playerColor = playerColor;
        this.amountOfPlayers = board.getAmountOfPlayers();
    }

    @Override
    public void updateBoard (Board board) {
        this.board = board;
    }

    @Override
    public void updateTurn (int turn) {
        this.turn = turn;
    }

    @Override
    public void updateMoveCounter() {
        moveCounter++;
    }

    @Override
    public void updateMoveCounter(int moveCounter) {
        this.moveCounter = moveCounter;
    }

    @Override
    public void update () {
        System.out.println(board);

        for (int i = 0; i < amountOfPlayers; i++) {
            System.out.println("Color " + i + ": " + board.getPieceManager().getPiecesNotOnBoard(i).stream().map(Enum::toString).collect(Collectors.joining(", ")));
        }

    }

    @Override
    public Move getMove () {
        Move move;

        while (true) {
            System.out.print("Your move [<x> <y> <piece> <orientation> <flip>]: ");
            Scanner scanner = new Scanner(System. in);
            String input = scanner.nextLine();
            input = input.replace("  ", " ");
            String[] splitted = input.split(" ");

            int x, y;
            PieceID pieceID;
            Orientation orientation;
            boolean flip;


            try {
                x = Integer.parseInt(splitted[0]);
                y = Integer.parseInt(splitted[1]);

                pieceID = PieceID.fromStandardNotation(splitted[2]);

                orientation = Orientation.fromString(splitted[3]);
                flip = Boolean.parseBoolean(splitted[4]);

                move = new Move(x, y, pieceID, playerColor, orientation, flip);
//                if (board.fits(move)) {
                if (true) {
                    return new Move(x, y, pieceID, playerColor, orientation, flip);
                }

            } catch (IndexOutOfBoundsException ignored) {}

        }


    }
}
