package uis.fancyttyui;

import blokus.*;

import java.util.List;

public class PieceSpriteSymbol extends PieceSprite {
    private List<PieceID> allPieceIDs;
    private int pieceIDPointer;

    private static PieceID getLargestPieceID(Board position, int color) {
        return position.getPieceManager().getPiecesNotOnBoard(color).get(position.getPieceManager().getPiecesNotOnBoard(color).size() - 1);
    }

    public PieceSpriteSymbol (int color, ColorPallet pallet, Board currentPosition, int scaleX, int scaleY) {
        super(getLargestPieceID(currentPosition, color), color, Orientation.UP, false, scaleX, scaleY, pallet, Terminal.TRANSPARENT);

        allPieceIDs     = currentPosition.getPieceManager().getPiecesNotOnBoard(color);
        pieceIDPointer  = allPieceIDs.size() - 1;
    }

    public Move getCurrentMove () {
        return new Move((getPosX() - 1) / scaleX, (getPosY() - 1) / scaleY, getCurrentPieceID(), color, orientation, flip);
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
        super.pieceID = getCurrentPieceID();
        refreshData();
    }



}
