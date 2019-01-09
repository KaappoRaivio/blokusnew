package ais;

import blokus.*;

import javax.print.attribute.standard.RequestingUserName;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class TwoPlayerAi extends Player {

    public TwoPlayerAi(Board initialPosition, int color, String id) {
        super(initialPosition, color, id);
    }

    @Override
    public Move getMove() {
        List<Move> moves = board.getAllFittingMoves(color);

//        Move move = moves.get(new Random().nextInt(moves.size()));
        Move move = moves.get(0);
        System.out.println("Color " + color + ", move: " + move.toString());
        return move;
    }

    public Board getBoard () {
        return board;
    }

    private float decisionTree(Board position, int depth,  int turn) {
        if (depth == 0 || !position.canPlay()) {
            return evaluate(position);
        }

        if (turn == color) { // max
            float value = -1;
            for (Move move : position.getAllFittingMoves(turn)) {
                Board newBoard = position.deepCopy();
                newBoard.putOnBoard(move);
                value = max(value, decisionTree(newBoard, depth - 1, 1 - turn));
            }

            return value;
        } else { // min
            float value = 1;
            for (Move move : position.getAllFittingMoves(turn)) {
                Board newBoard = position.deepCopy();
                newBoard.putOnBoard(move);
                value = min(value, decisionTree(newBoard, depth - 1, 1 - turn));
            }

            return value;
        }
    }



    private float decisionTree(Board position, int depth) {
        return decisionTree(position, depth, color);
    }

    public static void main (String[] args) {
        Board board = new Board(14, 14, new MyPieceManager(2), true, 4);



        TwoPlayerAi twoPlayerAi = new TwoPlayerAi(board, 1, "asd");

        System.out.println(timeit(new Runnable() {
            @Override
            public void run() {
                System.out.println(twoPlayerAi.decisionTree(twoPlayerAi.getBoard(), 2));
            }
        }));
    }


    public static long timeit (Runnable runnable) {
        long alku = System.currentTimeMillis();
        runnable.run();
        long loppu = System.currentTimeMillis();
        return loppu - alku;
    }


    private float evaluate (Board position) {
//        System.out.println((float) howManySquaresOnBoard(position, color) / 89.0f);
        return (float) howManySquaresOnBoard(position, color) / 89.0f;
    }

    private int howManySquaresOnBoard (Board position, int color) {
        int counter = 0;

        for (int y = 0; y < position.getDimY(); y++) {
            for (int x = 0; x < position.getDimX(); x++) {
                if (position.getBoard()[y][x] == color) {
                    counter += 1;
                }
            }
        }

        return counter;
    }

}
