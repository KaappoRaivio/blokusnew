package ais;

import blokus.*;

import javax.print.attribute.standard.RequestingUserName;
import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class TwoPlayerAi extends Player {

    public TwoPlayerAi(Board initialPosition, int color, String id) {
        super(initialPosition, color, id);
    }

    @Override
    public Move getMove() {
        List<Move> moves = board.getAllFittingMoves(color);

        Map<Float, Move> moveScores = new HashMap<>();
        for (Move move : moves) {
            board.putOnBoard(move);
            float score = decisionTree(board, 1);
            board.undo(0);
            moveScores.put(score, move);
        }
        float maxScore = -1.0f;

        for (float f : moveScores.keySet()) {
            maxScore = max(maxScore, f);
        }

//        Move move = moves.get(new Random().nextInt(moves.size()));
//        Move move = moves.get(0);
//        System.out.println("Color " + color + ", move: " + move.toString());
        return moveScores.get(maxScore);
    }

    public Board getBoard () {
        return board;
    }

    private float decisionTree (Board position, int depth,  int turn) {
        if (depth == 0 || !position.canPlay()) {
//            System.out.println(evaluate(position));
            return evaluate(position);
        }

        if (turn == color) { // max
            float value = -1;

            for (Move move : position.getFirstNFittingMoves(10, turn)) {
                position.putOnBoard(move);
                value = max(value, decisionTree(position, depth - 1, 1 - turn));
                position.undo(0);
            }

            return value;
        } else { // min
            float value = 1;

            for (Move move : position.getFirstNFittingMoves(10, turn)) {
                position.putOnBoard(move);
                value = min(value, decisionTree(position, depth - 1, 1 - turn));
                position.undo(0);
            }

            return value;
        }
    }

    private float decisionTree(Board position, int depth) {
        return decisionTree(position, depth, color);
    }

    private float decisionTreeBad(Board position, int depth,  int turn) {
        if (depth == 0 || !position.canPlay()) {
            return evaluate(position);
        }

        if (turn == color) { // max
            float value = -1;
            for (Move move : position.getAllFittingMoves(turn)) {
                Board newBoard = position.deepCopy();
                newBoard.putOnBoard(move);
                value = max(value, decisionTreeBad(newBoard, depth - 1, 1 - turn));

            }

            return value;
        } else { // min
            float value = 1;
            for (Move move : position.getAllFittingMoves(turn)) {
                Board newBoard = position.deepCopy();
                newBoard.putOnBoard(move);
                value = min(value, decisionTreeBad(newBoard, depth - 1, 1 - turn));
            }

            return value;
        }
    }

    private float decisionTreeBad (Board position, int depth) {
        return decisionTreeBad(position, depth, color);
    }


    public static void main (String[] args) {
        Board board = new Board(14, 14, new MyPieceManager(2), true, 4);



        TwoPlayerAi twoPlayerAi = new TwoPlayerAi(board, 0, "asd");

        System.out.println(timeit(new Runnable() {
            @Override
            public void run() {
                System.out.println(twoPlayerAi.decisionTree(twoPlayerAi.getBoard(), 1));
            }
        }, 100));
    }


    private static long timeit (Runnable runnable, int iterations) {
        List<Long> times = new ArrayList<>();
        long sum = 0;

        for (int i = 0; i < iterations; i++) {
            long alku = System.currentTimeMillis();
            runnable.run();
            long loppu = System.currentTimeMillis();
            times.add(loppu - alku);
            sum += loppu - alku;
        }


        return sum / times.size();
    }


    private float evaluate (Board position) {
//        System.out.println((float) howManySquaresOnBoard(position, color) / 89.0f);

        float[] parameters = new float[]{(float) howManySquaresOnBoard(position, color) / 89.0f - (float) howManySquaresOnBoard(position, 1 - color) / 89.0f, (float) howManyCornersFree(position, color) / 10.0f - (float) howManyCornersFree(position, 1 - color) / 10.0f};

        float average = 0.0f;

        for (float f : parameters) {
            average += f;
        }

        return average / parameters.length;
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


    private int howManyCornersFree (Board position, int color) {
        int counter = 0;
        PieceID pieceID = PieceID.fromStandardNotation("I1");

        for (int y = 0; y < position.getDimY(); y++) {
            for (int x = 0; x < position.getDimX(); x++) {
                if (position.fits(x, y, pieceID, color, Orientation.UP, false, true)) {
                    counter += 1;
                }
            }
        }

        return counter;
    }

}
