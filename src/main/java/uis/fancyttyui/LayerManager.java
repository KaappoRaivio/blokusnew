package uis.fancyttyui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LayerManager {
    private List<Layer> layers = new ArrayList<>();

    public LayerManager (List<Layer> layers) {
        this.layers = layers;
    }

    public char[][] compose (int dimX, int dimY) {
        char[][] mesh = new char[dimY][dimX];

        for (Layer layer : layers) {
            drawLayer(layer, mesh);
        }

        return mesh;


    }

    private void drawLayer (Layer layer, char[][] mesh) {
        int startX;
        int startY;

        switch (layer.getFillMode()) {
            case CENTERED:
                startX = (mesh[0].length - layer.getDimX()) / 2;
                startY = (mesh.length - layer.getDimY()) / 2;
                break;
            case UP_RIGHT:
                startX = 0;
                startY = 0;
                break;
            default:
                throw new RuntimeException("Fatal error!");
        }

        for (int y = startY; y < startY + layer.getDimY(); y++) {
            for (int x = startX; x < startX + layer.getDimX(); x++) {
                try {
                    mesh[y][x] = layer.getData()[y][x];
                } catch (ArrayIndexOutOfBoundsException ignored) {}
            }
        }
    }

    public void addLayer (Layer layer) {
        layers.add(layer);
    }


}
