package uis.fancyttyui;

import blokus.Board;
import blokus.MyPieceManager;

import java.util.Arrays;

public class Layer {
    public static final char TRANSPARENT = '$';


    private int dimX, dimY;
    private char[][] data;
    private FillMode fillMode;
    private String description;

    public static Layer fromString (String string, String rowSep, String colSep, FillMode fillMode, String description) {
        String[] splitted = string.split(rowSep);

        int dimY = splitted.length;
        int dimX = splitted[0].split(colSep).length;

        char[][] mesh = new char[dimY][dimX];

        for (int y = 0; y < splitted.length; y++) {
            String[] row = splitted[y].split(colSep);
            for (int x = 0; x < row.length; x++) {
                try {
                    System.out.println(row[x]);
                    mesh[y][x] = row[x].charAt(0);
                } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException ignored) {
                    mesh[y][x] = TRANSPARENT;
                }
            }
        }

        return new Layer(dimX, dimY, mesh, fillMode, description);
    }

    public int getDimX() {
        return dimX;
    }

    public int getDimY() {
        return dimY;
    }

    public char[][] getData() {
        return data;
    }

    public FillMode getFillMode() {
        return fillMode;
    }

    public String getDescription() {
        return description;
    }

    Layer (int dimX, int dimY, char[][] data, FillMode fillMode, String description) {
        this.dimX = dimX;
        this.dimY = dimY;
        this.data = data;
        this.fillMode = fillMode;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Layer{" +
                "dimX=" + dimX +
                ", dimY=" + dimY +
                ", data=" + Arrays.deepToString(data) +
                ", fillMode=" + fillMode +
                ", description='" + description + '\'' +
                '}';
    }

    public static void main (String[] adsa) {
        Board board = new Board(14, 14, new MyPieceManager(2));

        Layer layer = Layer.fromString(board.toString(), "\n", " ", FillMode.UP_RIGHT, "");
        System.out.println(layer);
        System.out.println(board.toString());
    }
}
