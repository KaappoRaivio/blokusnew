package uis.fancyttyui;

import java.util.Arrays;

public class Layer {
    public static final char TRANSPARENT = '$';


    private int dimX, dimY;
    private char[][] data;
    private FillMode fillMode;
    private String description;

    public static Layer fromString (String string, String rowSep, String colSep) {


        String[] splitted = string.split(rowSep);

        int dimY = splitted.length;
        int dimX = splitted[0].split(colSep).length;

        char[][] mesh = new char[dimY][dimX];



        for (int y = 0; y < splitted.length; y++) {
            String[] row = splitted[y].split(colSep);
            for (int x = 0; x < row.length; x++) {
                mesh[y][x]
            }
        }


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

    public Layer (int dimX, int dimY, char[][] data, FillMode fillMode, String description) {
        this.dimX = dimX;
        this.dimY = dimY;
        this.data = data;
        this.fillMode = fillMode;
        this.description = description;
    }
}
