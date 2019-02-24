package uis.fancyttyui;

import blokus.*;

import java.util.List;

public class PieceSpriteSymbol extends PieceSprite {
    private Orientation orientation;
    private boolean flip;
    private int color;
    private final int scaleX;
    private final int scaleY;

    private List<PieceID> allPieceIDs;
    private int pieceIDPointer;

    private static PieceID getLargestPieceID(Board position, int color) {
        return position.getPieceManager().getPiecesNotOnBoard(color).get(position.getPieceManager().getPiecesNotOnBoard(color).size() - 1);
    }

    public PieceSpriteSymbol (int color, ColorPallet pallet, Board currentPosition, int scaleX, int scaleY) {
        super(getLargestPieceID(currentPosition, color), color, scaleX, scaleY, pallet, Terminal.TRANSPARENT);

        this.color      = color;
        this.scaleX     = scaleX;
        this.scaleY     = scaleY;
        orientation     = Orientation.UP;
        flip            = false;
        allPieceIDs     = currentPosition.getPieceManager().getPiecesNotOnBoard(color);
        pieceIDPointer  = allPieceIDs.size() - 1;
    }

    public Move getCurrentMove () {
        System.out.println(orientation);
        return new Move((super.getPosX() - 1) / 2 / scaleX, (super.getPosY() - 1) / scaleY, getCurrentPieceID(), color, orientation, flip);
    }

    private PieceID getCurrentPieceID() {
        if (pieceIDPointer < 0) {
            pieceIDPointer += allPieceIDs.size();
            return getCurrentPieceID();
        }

        return allPieceIDs.get(pieceIDPointer % allPieceIDs.size());
    }



    public void changePieceIDPointer (int change) {
        pieceIDPointer += change;
        refreshData();
    }



}
