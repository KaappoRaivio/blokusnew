package uis;

import blokus.Board;
import blokus.Move;
import blokus.Orientation;
import blokus.PieceID;
import misc.MoveAndScore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class TtyUITest implements UI {
    private Board board;
    private int turn = -1;
    private int amountOfPlayers;
    private int moveCounter = 0;
    private Board errorBoard;

    public TtyUITest (Board board) {
        this.board = board;
        this.amountOfPlayers = board.getAmountOfPlayers();
    }

    @Override
    public void updateValues(Board board, int turn, int moveCount) {
        this.board = board;
        this.turn = turn;
        this.moveCounter = moveCount;
    }

    @Override
    public void commit() {

        System.out.print("Round " + moveCounter / amountOfPlayers + ", turn " + turn + ":");

        String[] rowsRaw = board.toString().split("\n");
        Arrays.stream(rowsRaw).forEach((row) -> System.out.println("\t" + row));


        for (int i = 0; i < amountOfPlayers; i++) {
            System.out.println("Color " + i + ": " + board.getPieceManager().getPiecesNotOnBoard(i).stream().map(Enum::toString).collect(Collectors.joining(", ")));
        }

    }

    @Override
    public Move getMove(int color) {
        if (turn != color) {
            throw new RuntimeException("It's wrong turn! (should be " + color + ", is actually " + turn + ")");
        }

        while (true) {
            System.out.print("Your move [<x> <y> <piece> <orientation> <flip>]: ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            input = input.replaceAll("( )+", " "); // Remove multiple spaces
            String[] splitted = input.split(" ");

            if (splitted.length != 5) {
                System.out.println("Invalid format! " + splitted.length);
                continue;
            }

            try {
                int x = Integer.parseInt(splitted[0]);
                int y = Integer.parseInt(splitted[1]);

                PieceID pieceID = PieceID.fromStandardNotation(splitted[2]);

                Orientation orientation = Orientation.fromString(splitted[3]);
                boolean flip = Boolean.parseBoolean(splitted[4]);

                Move move = new Move(x, y, pieceID, color, orientation, flip);

                errorBoard = board.deepCopy();
                errorBoard.putOnBoard(move);
                System.out.print("Like this? [Y/n]");
                Arrays.stream(errorBoard.toString().split("\n")).forEach((row) -> System.out.println("\t" + row));

                String in = scanner.nextLine().toLowerCase();

                if (in.equals("") || in.equals("y")) {
                    return move;
                }
            } catch (Exception ignored) {}
        }
    }
}
