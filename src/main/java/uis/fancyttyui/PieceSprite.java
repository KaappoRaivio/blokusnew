package uis.fancyttyui;

import blokus.*;
import uis.Texel;

import java.util.List;

public class PieceSprite extends Sprite {
    private ColorPallet pallet;
    private Orientation orientation;
    private boolean flip;
    private int color;

    private List<PieceID> allPieceIDs;
    private int pieceIDPointer;

    public PieceSprite (int color, ColorPallet pallet, Board currentPosition) {
        super(texelize(new Piece(currentPosition.getPieceManager().getPiecesNotOnBoard(color).get(currentPosition.getPieceManager().getPiecesNotOnBoard(color).size() - 1), color), pallet), Terminal.TRANSPARENT);

        this.pallet = pallet;
        this.color = color;
        orientation = Orientation.UP;
        flip = false;

        allPieceIDs = currentPosition.getPieceManager().getPiecesNotOnBoard(color);
        pieceIDPointer = allPieceIDs.size() - 1;
    }

    public Move getCurrentMove () {
        return new Move((super.getPosX() - 1) / 2, (super.getPosY() - 1), getCurrentPieceID(), color, orientation, flip);
    }

    private PieceID getCurrentPieceID() {
        if (pieceIDPointer < 0) {
            pieceIDPointer += allPieceIDs.size();
            return getCurrentPieceID();
        }

        return allPieceIDs.get(pieceIDPointer % allPieceIDs.size());
    }

    private void refreshData () {
        super.mesh = texelize(new Piece(getCurrentPieceID(), color).rotate(orientation, flip), pallet);
    }

    public void changePieceIDPointer (int change) {
        pieceIDPointer += change;
        refreshData();
    }

    public void flip () {
        this.flip = !flip;
        refreshData();
    }

    public void rotateClockwise () {
        Orientation newOrientation;
        switch (orientation) {
            case UP:
                newOrientation = Orientation.RIGHT;
                break;
            case RIGHT:
                newOrientation = Orientation.DOWN;
                break;
            case DOWN:
                newOrientation = Orientation.LEFT;
                break;
            case LEFT:
                newOrientation = Orientation.UP;
                break;
            default:
                throw new RuntimeException("Shouln't get here!");
        }

        orientation = newOrientation;
        refreshData();
    }

    public void rotateAntiClockwise () {
        Orientation newOrientation;
        switch (orientation) {
            case UP:
                newOrientation = Orientation.LEFT;
                break;
            case RIGHT:
                newOrientation = Orientation.UP;
                break;
            case DOWN:
                newOrientation = Orientation.RIGHT;
                break;
            case LEFT:
                newOrientation = Orientation.DOWN;
                break;
            default:
                throw new RuntimeException("Shouln't get here!");
        }

        orientation = newOrientation;
        refreshData();

    }

    private static Texel[][] texelize (Piece piece, ColorPallet pallet) {
        Texel[][] mesh = new Texel[5][10];
        char[][] pieceMesh = piece.getMesh();

        for (int y = 0; y < mesh.length; y++) {
            for (int x = 0; x < mesh[y].length; x++) {
                mesh[y][x] = new Texel(Terminal.TRANSPARENT);
            }
        }

        for (int y = 0; y < pieceMesh.length; y++) {
            for (int x = 0; x < pieceMesh[y].length; x++) {
                if (pieceMesh[y][x] == Piece.OPAQUE) {
                    mesh[y][2 * x] = pallet.getTexel(piece.getColor());
                    mesh[y][2 * x + 1] = mesh[y][2 * x];
                }

            }
        }

        return mesh;
    }

}
