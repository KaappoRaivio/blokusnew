package uis.fancyttyui;

import blokus.*;
import uis.Texel;

import java.util.List;

public class PieceSprite extends Sprite {
    private ColorPallet pallet;
    private Orientation orientation;
    private boolean flip;
    private int color;
    private final int scaleX;
    private final int scaleY;

    private List<PieceID> allPieceIDs;
    private int pieceIDPointer;

    public PieceSprite (int color, ColorPallet pallet, Board currentPosition, int scaleX, int scaleY) {
        super(texelize(new Piece(currentPosition.getPieceManager().getPiecesNotOnBoard(color).get(currentPosition.getPieceManager().getPiecesNotOnBoard(color).size() - 1), color), pallet, scaleX, scaleY), Terminal.TRANSPARENT);

        this.pallet = pallet;
        this.color = color;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        orientation = Orientation.UP;
        flip = false;
        allPieceIDs = currentPosition.getPieceManager().getPiecesNotOnBoard(color);
        pieceIDPointer = allPieceIDs.size() - 1;
    }

    public Move getCurrentMove () {
        return new Move((super.getPosX() - 1) / 2 / scaleX, (super.getPosY() - 1) / scaleY, getCurrentPieceID(), color, orientation, flip);
    }

    private PieceID getCurrentPieceID() {
        if (pieceIDPointer < 0) {
            pieceIDPointer += allPieceIDs.size();
            return getCurrentPieceID();
        }

        return allPieceIDs.get(pieceIDPointer % allPieceIDs.size());
    }

    private void refreshData () {
        super.mesh = texelize(new Piece(getCurrentPieceID(), color).rotate(orientation, flip), pallet, scaleX, scaleY);
    }

    public void changePieceIDPointer (int change) {
        pieceIDPointer += change;
        refreshData();
    }

    public void flip () {
        this.flip = !flip;
        refreshData();
    }

    public void rotateClockwise() {
        rotateClockwise(true);
    }

    private void rotateClockwise (boolean recursive) {
        if (flip && recursive) {
            rotateAntiClockwise(false);
            return;
        }
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

    public void rotateAntiClockwise() {
        rotateAntiClockwise(true);
    }

    private void rotateAntiClockwise (boolean recursive) {
        if (flip && recursive) {
            rotateClockwise(false);
            return;
        }
        
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

    private static Texel[][] texelize (Piece piece, ColorPallet pallet, int scaleX, int scaleY) {
        char[][] pieceMesh = piece.getMesh();
        Texel[][] mesh = new Texel[piece.getMesh().length * scaleY][2 * scaleX * piece.getMesh()[0].length];

        for (int y = 0; y < mesh.length; y++) {
            for (int x = 0; x < mesh[y].length; x++) {
                mesh[y][x] = new Texel(Terminal.TRANSPARENT);
            }
        }

        for (int y = 0; y < mesh.length; y++) {
            for (int x = 0; x < mesh[y].length; x++) {
                if (pieceMesh[y / scaleY][x / scaleX / 2] == Piece.OPAQUE) {
                    mesh[y][x] = pallet.getTexel(piece.getColor());
                }

            }
        }

        return mesh;
    }

//    @Override
//    public Texel[][] texelize (ColorPallet pallet, int scaleX, int scaleY) {
//
//    }

}
