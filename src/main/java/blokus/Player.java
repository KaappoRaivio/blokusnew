package blokus;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Scanner;

public class Player implements CapableOfPlaying, Serializable {

    private int color;
    private Board board;
    private int turn = -1;
    private int moveCount;
    private Board errorBoard;
    private String id;

    public Player (Board board, int color, String id) {
        this.board = board;
        this.color = color;

        this.errorBoard = this.board.deepCopy();

        if (id != null) {
            this.id = id;
        } else {
            this.id = String.valueOf(color);
        }
    }

    @Override
    public Move getMove () {
        if (turn != color) {
            throw new RuntimeException("It's wrong turn! (should be " + color + ", is actually " + turn + ")");
        }

        while (true) {
            System.out.print("Your move [<x> <y> <piece> <orientation> <flip>]: ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            input = input.replaceAll("( )+", " ");
            String[] splitted = input.split(" ");

            if (splitted.length != 5) {
                System.out.println("Invalid format!" + splitted.length);
                continue;
            }

            try {
                int x = Integer.parseInt(splitted[0]);
                int y = Integer.parseInt(splitted[1]);

                PieceID pieceID = PieceID.fromStandardNotation(splitted[2]);

                Orientation orientation = Orientation.fromString(splitted[3]);
                boolean flip = Boolean.parseBoolean(splitted[4]);

                Move move = new Move(x, y, pieceID, color, orientation, flip);

                errorBoard.putOnBoard(move);
                System.out.print("Like this? [Y/n]");
                Arrays.stream(errorBoard.toString().split("\n")).forEach((row) -> System.out.println("\t" + row));
                errorBoard = board.deepCopy();

                String in = scanner.nextLine().toLowerCase();

                if (in.equals("") || in.equals("y")) {
                    return move;
                }
            } catch (Exception ignored) {}
        }
    }

    @Override
    public void updateValues(Board board, int turn, int moveCount) {
        this.turn = turn;
        this.moveCount = moveCount;
        this.board = board;
    }
}
