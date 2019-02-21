package uis.fancyttyui;

import blokus.Piece;
import blokus.PieceID;



import uis.Texel;
import uis.Texelizeable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RemainingPieceViewer implements Texelizeable {
    private Map<PieceID, Boolean> pieces = new HashMap<>();
    private int color;
    private int width = 4;

    RemainingPieceViewer (int color, Set<PieceID> pieces) {
        this.color = color;
        pieces.forEach(item -> this.pieces.put(item, false));
    }

    public void placePiece (PieceID pieceID) {
        pieces.remove(pieceID);
    }

    private boolean isHighlighted (PieceID pieceID) {
        Boolean result = pieces.get(pieceID);

        if (result != null) {
            return result;
        } else {
            throw new RuntimeException("Piece " + pieceID + " of color " + color + " is already on board!");
        }
    }



    @Override
    public Texel[][] texelize (ColorPallet colorPallet, int scaleX, int scaleY) {
        int pieceDimX = 2 * scaleX * width;
        int pieceDimY = scaleY * (pieces.size() / width);

        Texel[][] blank = Texel.getBlankTexelMatrix(pieceDimX, pieceDimY, colorPallet.getBackgroundTexel());

//        for (int y = 0; y < )

        return null;
    }
}
