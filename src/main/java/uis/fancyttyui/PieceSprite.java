package uis.fancyttyui;

import blokus.Orientation;
import blokus.Piece;
import blokus.PieceID;
import uis.Color;
import uis.Texel;

public class PieceSprite extends Sprite {
    protected PieceID pieceID;
    private ColorPallet pallet;
    protected Orientation orientation;
    protected boolean flip;
    protected int color;
    protected final int scaleX;
    protected final int scaleY;

    public PieceSprite (PieceID pieceID, int color, int scaleX, int scaleY, ColorPallet pallet, char transparent) {
        this(pieceID, color, Orientation.UP, false, scaleX, scaleY, pallet, transparent);

    }

    PieceSprite (PieceID pieceID, int color, Orientation orientation, boolean flip, int scaleX, int scaleY, ColorPallet pallet, char transparent) {
        super(new Piece(pieceID, color).rotate(orientation, flip).texelize(pallet, scaleX, scaleY), transparent, true);

        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.color = color;
        this.pieceID = pieceID;
        this.pallet = pallet;
        this.orientation = orientation;
    }

    public void flip () {
        flip = !flip;
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
                throw new RuntimeException("Shouldn't get here!");
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

    protected void refreshData () {
        Piece piece = new Piece(pieceID, color).rotate(orientation, flip);
        Texel[][] mesh = piece.texelize(pallet, scaleX, scaleY);
        super.mesh = mesh;
        dimY = mesh.length;
        dimX = mesh[0].length;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public boolean isFlip() {
        return flip;
    }

    public PieceID getPieceID() {
        return pieceID;
    }
}
