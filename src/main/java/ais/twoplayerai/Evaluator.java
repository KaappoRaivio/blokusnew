package ais.twoplayerai;

import blokus.Board;
import blokus.Move;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Evaluator {
    private int color;
    private PositionEvaluator positionEvaluator;

    private int n;


    public PositionEvaluator getPositionEvaluator() {
        return positionEvaluator;
    }

    public Evaluator(int color, PositionEvaluator positionEvaluator, int n) {
        this.color = color;
        this.positionEvaluator = positionEvaluator;
        this.n = n;
    }


    private double decisionTree (Board node, int depth, boolean maximizingPlayer, double alpha, double beta, Move initialMove) {
        if (depth <= 0) {
            return positionEvaluator.evaluatePosition(node, color);
        } else if ( !node.hasMoves(maximizingPlayer ? color : 1 - color)) {
            return -1e10f + positionEvaluator.evaluatePosition(node, maximizingPlayer ? color : 1 - color);
        }

        if (maximizingPlayer) {
            double value = -1e10f;

            for (Move move : node.getFirstNFittingMoves(getN(), color)) {
                node.putOnBoard(move);

                value = max(value, decisionTree(node, depth - 1, false, alpha, beta, initialMove));
                alpha = max(alpha, value);

                if (alpha >= beta) {
                    node.undo(0);
                    break;
                }

                node.undo(0);
            }


            return value;

        } else {
            double value = 1e10f;

            for (Move move : node.getFirstNFittingMoves(getN(), 1 - color)) {
                node.putOnBoard(move);

                value = min(value, decisionTree(node, depth - 1, true, alpha, beta, initialMove));
                beta = min(beta, value);

                if (alpha >= beta) {
                    node.undo(0);
                    break;
                }

                node.undo(0);
            }


            return value;
        }
    }


    double evaluateMove(Board position, int depth, Move initialMove) {
        return decisionTree(position, depth, false, -1e10f, 1e10f, initialMove);
    }

    public int getN() {
        return n;
    }

}
