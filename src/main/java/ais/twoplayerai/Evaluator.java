package ais.twoplayerai;

import blokus.*;
import misc.BoardAndMoveAndScore;
import misc.MoveAndScore;
import uis.UI;
import uis.fancyttyui.FancyTtyUI;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import static java.lang.Math.*;

public class Evaluator {
    private int color;
    private float squaresOnBoardDivider;
    private float cornersFreeDivider;
    private float spreadDivider;
    private float averageDivider;
    private int n;
    private UI ui;

    public List<BoardAndMoveAndScore> getBoardAndScores() {
        return boardAndMoveAndScores;
    }

    public void resetBoardsAndScore () {
        boardAndMoveAndScores = new Vector<>();
    }

    private List<BoardAndMoveAndScore> boardAndMoveAndScores;

    public Evaluator(int color, float squaresOnBoardDivider, float cornersFreeDivider, float spreadDivider, float averageDivider, int n, UI ui) {
        this.color = color;
        this.squaresOnBoardDivider = squaresOnBoardDivider;
        this.cornersFreeDivider = cornersFreeDivider;
        this.spreadDivider = spreadDivider;

        this.n = n;
        this.averageDivider = averageDivider;
        this.ui = ui;
        boardAndMoveAndScores = new Vector<>() {
            @Override
            public boolean add (BoardAndMoveAndScore boardAndMoveAndScore) {
                synchronized (this) {
                    super.add(boardAndMoveAndScore);
                }
                return true;
            }
        };
    }

    private int howManySquaresOnBoard (Board position, int color) {

        final int[] result = {0};
        position.getPieceManager().getPiecesOnBoard(color).stream().forEach((entry) -> result[0] += entry.getAmountOfSquares());
        return result[0];
    }


    private int howManyCornersFree (Board position, int color) {
        return position.amountOfFreeCorners(color);
    }

    private int howMuchSpread (Board position, int color) {
        Position average = getAverage(position, color);
        int tempX = 0, tempY = 0;

        for (int y = 0; y < position.getDimY(); y++) {
            for (int x = 0; x < position.getDimX(); x++) {
                if (position.getBoard()[y][x] == color) {
                    tempX += Math.pow((x - average.x), 2);
                    tempY += Math.pow((y - average.y), 2);
                }
            }
        }


        return (tempX + tempY) / 2;
    }

    private Position getAverage (Board board, int color) {
        List<Position> positions = new Vector<>();

        for (int y = 0; y < board.getDimY(); y++) {
            for (int x = 0; x < board.getDimX(); x++) {
                if (board.getBoard()[y][x] == color) {
                    positions.add(new Position(x, y));
                }
            }
        }

        int totalX = 0;
        int totalY = 0;

        for (Position position : positions) {
            totalX += position.x;
            totalY += position.y;
        }

        return new Position(totalX / (positions.size() + 1), totalY / (positions.size() + 1));

    }



    private float evaluatePosition(Board position) {
        return evaluatePosition(position, false);
    }

    protected float evaluatePosition(Board position, boolean verbose) {
        float[] parameters = new float[]{
//                ((float) howManySquaresOnBoard(position, color) - (float) howManySquaresOnBoard(position, 1 - color)) / squaresOnBoardDivider,
                ((float) howManyCornersFree(position, color) - (float) howManyCornersFree(position, 1 - color) * 2) / cornersFreeDivider,
//                ((float) howMuchSpread(position, color) - (float) howMuchSpread(position, 1 - color)) / (float) howManySquaresOnBoard(position, color) / spreadDivider,
//                -(Math.abs(getAverage(position, color).getAverage() - getAverage(position, 1 - color).getAverage())) / averageDivider
        };

        if (verbose) {
            System.out.println(Arrays.toString(parameters));
        }

        float average = 0.0f;

        for (float f : parameters) {
            average += f;
        }

        return average / parameters.length;
    }


    private float decisionTree (Board node, int depth, int turn, float alpha, float beta, Move initialMove) {
        if (depth == 0) {
//            System.out.println(node);
//            ui.updateValues(node.deepCopy(), turn, 0);
//            ui.commit();
            boardAndMoveAndScores.add(new BoardAndMoveAndScore(node.deepCopy(), new MoveAndScore(initialMove, true, true, evaluatePosition(node))));
            return evaluatePosition(node);
        } else if ( !node.hasMoves(turn)) {
            boardAndMoveAndScores.add(new BoardAndMoveAndScore(node.deepCopy(), new MoveAndScore(initialMove, true, true, evaluatePosition(node))));
            return -1000f;
        }

        if (turn == color) { // max
            float value = -1e10f;

            for (Move move : node.getFirstNFittingMoves(getN(), turn)) {
                node.putOnBoard(move);
                value = max(value, decisionTree(node, depth - 1, 1 - turn, alpha, beta, initialMove));
                alpha = max(alpha, value);

                if (alpha >= beta) {
                    node.undo(0);
//                    return beta;
                    break;
                }

                node.undo(0);
            }

            return value;

        } else { // min
            float value = 1e10f;

            for (Move move : node.getFirstNFittingMoves(getN(), turn)) {
                node.putOnBoard(move);
                value = min(value, decisionTree(node, depth - 1, 1 - turn, alpha, beta, initialMove));
                beta = min(beta, value);

                if (alpha >= beta) {
                    node.undo(0);
//                    return beta;
                    break;
                }

                node.undo(0);
            }

//            System.out.println("Alpha is smaller! returning " + beta + " as beta!");
            return value;
        }
    }

    float evaluateMove(Board position, int depth, Move initialMove) {
        return decisionTree(position, depth, 1 - color, 1e10f, -1e10f, initialMove);
    }

    public int getN() {
        return n;
//        return (int)((n + ui.getMoveCount() + 4.5) * 0.5);
    }

    public static void main (String[] aarghs) {
        Board board = Board.fromFile("/home/kaappo/git/blokus/src/main/resources/boards/Mon Feb 04 14:40:32 EET 2019.ser", false);

        UI ui = new FancyTtyUI(board, 1, 1);
        ui.commit();
        Evaluator evaluator = new Evaluator(0, 10.0f, 10.0f, 8.0f, 8.0f, 0, ui);

        System.out.println(evaluator.howMuchSpread(board, 0) + ", " + evaluator.howMuchSpread(board, 1));
        evaluator.howManySquaresOnBoard(board, 0);
        long time = System.currentTimeMillis();
        System.out.println(evaluator.evaluatePosition(board));
        System.out.println(System.currentTimeMillis() - time);
    }
}
