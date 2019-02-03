package ais.twoplayerai;

import blokus.*;
import uis.UI;
import uis.fancyttyui.FancyTtyUI;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Evaluator {
    private int color;
    private float squaresOnBoardDivider;
    private float cornersFreeDivider;
    private float spreadDivider;

    public Evaluator(int color, float squaresOnBoardDivider, float cornersFreeDivider, float spreadDivider) {
        this.color = color;
        this.squaresOnBoardDivider = squaresOnBoardDivider;
        this.cornersFreeDivider = cornersFreeDivider;
        this.spreadDivider = spreadDivider;
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
        List<Position> positions = new ArrayList<>();

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

        return new Position(totalX / positions.size(), totalY / positions.size());

    }

    private float evaluatePosition(Board position) {
        float[] parameters = new float[]{
                (float) howManySquaresOnBoard(position, color) / squaresOnBoardDivider - (float) howManySquaresOnBoard(position, 1 - color) / squaresOnBoardDivider,
                (float) howManyCornersFree(position, color) / cornersFreeDivider - (float) howManyCornersFree(position, 1 - color) / cornersFreeDivider,
                (float) howMuchSpread(position, color) / (float) howManySquaresOnBoard(position, color) / spreadDivider - (float) howMuchSpread(position, 1 - color) / (float) howManySquaresOnBoard(position, color) / spreadDivider
        };

        float average = 0.0f;

        for (float f : parameters) {
            average += f;
        }

        if (!position.hasMoves(color)) {
            average -= 100.0f;
        }

        return average / parameters.length;
    }

    private float decisionTree (Board position, int depth,  int turn) {
        if (depth == 0 || !position.hasMoves(turn)) {
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

    public static void main (String[] aarghs) {
        Board board = Board.fromFile("/home/kaappo/git/blokus/src/main/resources/boards/Sun Feb 03 19:51:37 EET 2019.ser", false);

        UI ui = new FancyTtyUI(board);
        ui.commit();
        Evaluator evaluator = new Evaluator(0, 10.0f, 10.0f, 8.0f);
        System.out.println(evaluator.howMuchSpread(board, 0) + ", " + evaluator.howMuchSpread(board, 1));
        System.out.println(evaluator.evaluatePosition(board));
    }
}
