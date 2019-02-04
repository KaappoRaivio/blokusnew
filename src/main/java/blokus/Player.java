package blokus;

import misc.MoveAndScore;
import uis.UI;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Scanner;

public class Player implements CapableOfPlaying, Serializable {

    protected int color;
    protected UI ui;
    protected Board board;
    protected int turn = -1;
    protected int moveCount;
    private Board errorBoard;
    protected String id;

    public Player (Board board, int color, String id, UI ui) {
        this.board = board;
        this.color = color;
        this.ui = ui;

        this.errorBoard = this.board.deepCopy();

        if (id != null) {
            this.id = id;
        } else {
            this.id = String.valueOf(color);
        }
    }

    @Override
    public Move getMove () {
//        if (turn != color) {
//            throw new RuntimeException("It's wrong turn! (should be " + color + ", is actually " + turn + ")");
//        }
//
//        while (true) {
//            System.out.print("Your move [<x> <y> <piece> <orientation> <flip>]: ");
//            Scanner scanner = new Scanner(System.in);
//            String input = scanner.nextLine();
//            input = input.replaceAll("( )+", " "); // Remove multiple spaces
//            String[] splitted = input.split(" ");
//
//            if (splitted.length != 5) {
//                System.out.println("Invalid format! " + splitted.length);
//                continue;
//            }
//
//            try {
//                int x = Integer.parseInt(splitted[0]);
//                int y = Integer.parseInt(splitted[1]);
//
//                PieceID pieceID = PieceID.fromStandardNotation(splitted[2]);
//
//                Orientation orientation = Orientation.fromString(splitted[3]);
//                boolean flip = Boolean.parseBoolean(splitted[4]);
//
//                Move move = new Move(x, y, pieceID, color, orientation, flip);
//
//                errorBoard = board.deepCopy();
//                errorBoard.putOnBoard(move);
//                System.out.print("Like this? [Y/n]");
//                Arrays.stream(errorBoard.toString().split("\n")).forEach((row) -> System.out.println("\t" + row));
//
//                String in = scanner.nextLine().toLowerCase();
//
//                if (in.equals("") || in.equals("y")) {
//                    return move;
//                }
//            } catch (Exception ignored) {}
//        }
        return ui.getMove(color);
    }

    @Override
    public void updateValues(Board board, int turn, int moveCount) {
        this.turn = turn;
        this.moveCount = moveCount;
        this.board = board;
    }

    @Override
    public int getColor() {
        return color;
    }
}
