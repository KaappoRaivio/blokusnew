package ais.twoplayerai;

import blokus.*;
import misc.BoardAndMoveAndScore;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import uis.UI;
import uis.fancyttyui.FancyTtyUI;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.*;

public class Evaluator {
    private int color;
    private double squaresOnBoardWeight;
    private double cornersFreeWeight;
    private double spreadWeight;
    private double averageWeight;
    private double aggression;
    private int n;
    private UI ui;

    public List<BoardAndMoveAndScore> getBoardAndScores() {
        return boardAndMoveAndScores;
    }

    public void resetBoardsAndScore () {
        boardAndMoveAndScores = new Vector<>();
    }

    private List<BoardAndMoveAndScore> boardAndMoveAndScores;

    public Evaluator(int color, double squaresOnBoardWeight, double cornersFreeWeight, double spreadWeight, double averageWeight, double aggression, int n, UI ui) {
        this.color = color;
        this.squaresOnBoardWeight = squaresOnBoardWeight;
        this.cornersFreeWeight = cornersFreeWeight;
        this.spreadWeight = spreadWeight;
        this.aggression = aggression;

        this.n = n;
        this.averageWeight = averageWeight;
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
        List<Integer> values = new Vector<>();
//        return position.amountOfFreeCorners(color);

        var corners = position.getEligibleCorners(color);
        for (var corner : corners) {
            values.add(roomSpace(corner.x, corner.y, position));
        }

        double average = 0;

        for (var item : values) {
            average += (double) item / (position.getDimY() * position.getDimX());
        }

        return (int) (average + 0.5);
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



    public double evaluatePosition(Board position) {
        return evaluatePosition(position, false);
    }

    private int counter;

    public double evaluatePosition(Board position, boolean verbose) {
//        return  0.0;
        double[] parameters = new double[]{
                ((double) howManySquaresOnBoard(position, color) - (double) howManySquaresOnBoard(position, 1 - color)) * squaresOnBoardWeight,
                ((double) howManyCornersFree(position, color) - (double) howManyCornersFree(position, 1 - color) * aggression) * cornersFreeWeight,
                ((double) howMuchSpread(position, color) - (double) howMuchSpread(position, 1 - color)) * spreadWeight,
                -(Math.abs(getAverage(position, color).getAverage() - getAverage(position, 1 - color).getAverage())) * averageWeight
        };
//        double[] parameters = new double[]{
////                new Random().nextFloat()
////                counter++
//                ((double) howMuchSpread(position, color) - (double) howMuchSpread(position, 1 - color)) / spreadWeight,
//
//        };

        if (verbose) {
            System.out.println("\t" + Stream.of((double) howManySquaresOnBoard(position, color), (double) howManyCornersFree(position, color), (double) howMuchSpread(position, color)).map(String::valueOf).collect(Collectors.joining(" ")));
            System.out.println(" -\t" + Stream.of(howManySquaresOnBoard(position, 1 - color), howManyCornersFree(position, 1 - color), howMuchSpread(position, 1 - color)).map(String::valueOf).collect(Collectors.joining(" ")));
//            Arrays.stream(parameters).mapToObj(Double::valueOf).forEach((item) -> System.out.println(item + " "));
            System.out.println(" =\t" + StringUtils.join(ArrayUtils.toObject(parameters), " "));


        }

        double average = 0.0f;

        for (double f : parameters) {
            average += f;
        }

        return average / parameters.length;
    }


    private double decisionTree (Board node, int depth, boolean maximizingPlayer, double alpha, double beta, Move initialMove) {
//        if (depth <= 0 && sameAmountOfPiecesOnBoard(node)) {
        if (depth <= 0) {
//            System.out.println("Hei");
//            System.out.println(node);
//            ui.updateValues(node.deepCopy(), maximizingPlayer ? color : 1 - color, 0);
//            ui.commit();
//            boardAndMoveAndScores.add(new BoardAndMoveAndScore(node.deepCopy(), new MoveAndScore(initialMove, true, true, evaluatePosition(node))));

            return evaluatePosition(node);

        } else if ( !node.hasMoves(maximizingPlayer ? color : 1 - color)) {
//            boardAndMoveAndScores.add(new BoardAndMoveAndScore(node.deepCopy(), new MoveAndScore(initialMove, true, true, evaluatePosition(node))));

            return -1e10f + evaluatePosition(node);
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

//            System.out.println("Alpha is smaller! returning " + beta + " as beta!");

            return value;
        }
    }

    private boolean sameAmountOfPiecesOnBoard(Board node) {
        return node.getPieceManager().getPiecesOnBoard(0).size() == node.getPieceManager().getPiecesOnBoard(1).size();
    }

    private int roomSpace (int x, int y, Board node) {
        return dfs(x, y, node.getDimX(), node.getDimY(), boolify(node), 0);
    }

    private int dfs (int x, int y, int dimX, int dimY, boolean[][] alreadyVisited, int area) {
        if (isOufOfBounds(x, y, dimX, dimY)) {
            return 0;
        }
        if (alreadyVisited[y][x]) {
            return 0;
        }

        alreadyVisited[y][x] = true;
        int newArea = 0;

        for (var adjacent : adjacents(x, y)) {
            newArea += dfs(adjacent.x, adjacent.y, dimX, dimY, alreadyVisited, area);
        }

        if (area == 0) {
            area = 1;
        }


        return area + newArea;
    }

    private boolean isOufOfBounds(int x, int y, int dimX, int dimY) {
//        System.out.println("x = [" + x + "], y = [" + y + "], dimX = [" + dimX + "], dimY = [" + dimY + "]");

        return 0 > x || x >= dimX || 0 > y || y >= dimY;
    }

    private boolean[][] boolify (Board board) {
        boolean[][] toReturn = new boolean[board.getDimY()][board.getDimX()];

        for (int y = 0; y < board.getDimY(); y++) {
            for (int x = 0; x < board.getDimX(); x++) {
                toReturn[y][x] = !board.adjacentsFree(x, y, color) || !board.isEmpty(x, y);
//                toReturn[y][x] = board.getBoard()[y][x] != -1;
            }
        }


        return toReturn;
    }

    private static void print (boolean[][] matrix) {
        System.out.println();
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                System.out.print((matrix[y][x] ? "X" : "." )+ " ");
            }
            System.out.println();
        }
    }
    
    

    private List<Position> adjacents (int x, int y) {
        return Arrays.asList(
                new Position(x + 1, y),
                new Position(x - 1, y),
                new Position(x, y + 1),
                new Position(x, y - 1)
        );
    }

    double evaluateMove(Board position, int depth, Move initialMove) {
        return decisionTree(position, depth, false, -1e10f, 1e10f, initialMove);
    }

    public int getN() {
        return n;
//        return (int)((n + ui.getMoveCount() + 4.5) * 0.5);
    }

    public static void main (String[] aarghs) {
//        Board board = new Board(14, 14, new MyPieceManager(2));
        Board board = Board.fromFile("/home/kaappo/git/blokusnew/src/main/resources/boards/Wed Feb 13 21:03:36 EET 2019.ser", false);


        UI ui = new FancyTtyUI(board, 1, 1);
        ui.commit();
        Evaluator evaluator = new Evaluator(0, 10.0f, 10.0f, 8.0f, 8.0f, 1,0, ui);

        long aikaA = System.currentTimeMillis();
        System.out.println(evaluator.howManyCornersFree(board, 0));
        long aikaL = System.currentTimeMillis();

        System.out.println(aikaL - aikaA);;

    }
}
