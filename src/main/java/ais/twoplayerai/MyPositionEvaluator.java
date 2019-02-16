package ais.twoplayerai;

import blokus.Board;
import blokus.PieceID;
import blokus.Position;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import uis.UI;
import uis.fancyttyui.FancyTtyUI;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyPositionEvaluator implements PositionEvaluator {
    public static final MyPositionEvaluator EVALUATOR_0 = new MyPositionEvaluator(1, 0.1, 0.2, 0.3,1);
    public static final MyPositionEvaluator EVALUATOR_1 = new MyPositionEvaluator(1, 0.1, 0.2, 0.3, 10);

    private double squaresOnBoardWeight;
    private double cornersFreeWeight;
    private double spreadWeight;
    private double centerDistanceWeight;
    private double aggression;


    public MyPositionEvaluator (double squaresOnBoardWeight, double cornersFreeWeight, double spreadWeight, double centerDistanceWeight, double aggression) {

        this.squaresOnBoardWeight = squaresOnBoardWeight;
        this.cornersFreeWeight = cornersFreeWeight;
        this.spreadWeight = spreadWeight;
        this.aggression = aggression;

        this.centerDistanceWeight = centerDistanceWeight;
    }

    @Override
    public double evaluatePosition(Board position, int color) {
        return evaluatePosition(position, color,false);
    }

    private int howManySquaresOnBoard (Board position, int color) {
        return position.getPieceManager().getPiecesOnBoard(color).stream().mapToInt(PieceID::getAmountOfSquares).sum();
    }

    private int howManyCornersFree (Board position, int color) {
        List<Integer> values = new Vector<>();

        for (var corner : position.getEligibleCorners(color)) {
            values.add(roomSpace(corner.x, corner.y, position, color));
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

    private boolean sameAmountOfPiecesOnBoard(Board node) {
        return node.getPieceManager().getPiecesOnBoard(0).size() == node.getPieceManager().getPiecesOnBoard(1).size();
    }

    private int roomSpace (int x, int y, Board node, int color) {
        return dfs(x, y, node.getDimX(), node.getDimY(), boolify(node, color), 0);
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

    private boolean[][] boolify (Board board, int color) {
        boolean[][] toReturn = new boolean[board.getDimY()][board.getDimX()];

        for (int y = 0; y < board.getDimY(); y++) {
            for (int x = 0; x < board.getDimX(); x++) {
                toReturn[y][x] = !board.adjacentsFree(x, y, color) || !board.isEmpty(x, y);
//                toReturn[y][x] = board.getBoard()[y][x] != -1;
            }
        }


        return toReturn;
    }

    private List<Position> adjacents (int x, int y) {
        return Arrays.asList(
                new Position(x + 1, y),
                new Position(x - 1, y),
                new Position(x, y + 1),
                new Position(x, y - 1)
        );
    }

    private double distanceFromCenter (Board position, int color) {
        int avgX = getAverage(position, color).x;
        int avgY = getAverage(position, color).y;



        double dX = Math.abs(avgX - position.getDimX() / 2.0);
        double dY = Math.abs(avgY - position.getDimY() / 2.0);


        return Math.hypot(dX, dY);
    }

    private static int invert (int color) {
        return 1 - color;
    }

    @Override
    public double evaluatePosition(Board position, int color, boolean verbose) {
//        return  0.0;
        double[] parameters = new double[]{
                ((double) howManySquaresOnBoard(position, color) - (double) howManySquaresOnBoard(position, invert(color))) * squaresOnBoardWeight,
                ((double) howManyCornersFree(position, color) - (double) howManyCornersFree(position, invert(color)) * aggression) * cornersFreeWeight,
                ((double) howMuchSpread(position, color) - (double) howMuchSpread(position, invert(color))) * spreadWeight,
//                -(Math.abs(getAverage(position, color).getAverage() - getAverage(position, invert(color)).getAverage())) * centerDistanceWeight
                (-distanceFromCenter(position, color) + distanceFromCenter(position, invert(color))) * centerDistanceWeight,
        };

        if (verbose) {
            System.out.println("\t" + Stream.of((double) howManySquaresOnBoard(position, color), (double) howManyCornersFree(position, color), (double) howMuchSpread(position, color)).map(String::valueOf).collect(Collectors.joining(" ")));
            System.out.println(" -\t" + Stream.of(howManySquaresOnBoard(position, invert(color)), howManyCornersFree(position, invert(color)), howMuchSpread(position, invert(color))).map(String::valueOf).collect(Collectors.joining(" ")));
//            Arrays.stream(parameters).mapToObj(Double::valueOf).forEach((item) -> System.out.println(item + " "));
            System.out.println(" =\t" + StringUtils.join(ArrayUtils.toObject(parameters), " "));


        }

        double average = 0.0f;

        for (double f : parameters) {
            average += f;
        }

        return average / parameters.length;
    }

    public static void main(String[] args) {
        Board board = Board.fromFile("/home/kaappo/git/blokusnew/src/main/resources/boards/Thu Feb 14 15:17:47 EET 2019.ser", false);
        UI ui = new FancyTtyUI(board, 1, 1);
        ui.commit();

        EVALUATOR_0.distanceFromCenter(board, 1);
        EVALUATOR_0.distanceFromCenter(board, 0);
    }



}
