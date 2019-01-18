package ais.twoplayerai;

import blokus.Board;
import blokus.Move;
import blokus.Orientation;
import blokus.PieceID;

import static java.lang.Math.max;
import static java.lang.Math.min;

class Evaluator {
    private int color;

    Evaluator(int color) {
        this.color = color;
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

    private float evaluatePosition(Board position) {
        float[] parameters = new float[]{(float) howManySquaresOnBoard(position, color) / 89.0f - (float) howManySquaresOnBoard(position, 1 - color) / 89.0f, (float) howManyCornersFree(position, color) / 10.0f - (float) howManyCornersFree(position, 1 - color) / 10.0f};

        float average = 0.0f;

        for (float f : parameters) {
            average += f;
        }

        return average / parameters.length;
    }

    private float decisionTree (Board position, int depth,  int turn) {
        if (depth == 0 || !position.canPlay()) {
//            System.out.println(evaluatePosition(position));
            return evaluatePosition(position);
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

    float evaluateMove(Board position, int depth) {
        return decisionTree(position, depth, color);
    }
}
