package uis.fancyttyui;

import blokus.*;


import com.googlecode.lanterna.TextColor;
import uis.Texel;
import uis.Texelizeable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RemainingPieceViewer implements Texelizeable {
    private Map<PieceID, Boolean> highlighted = new HashMap<>();
    private int color;

    private Map<PieceID, Move> positions;

    RemainingPieceViewer (int color, Set<PieceID> highlighted) {
        this.color = color;
        initializePositions(color);
        highlighted.forEach(item -> this.highlighted.put(item, false));
    }

    public void placePiece (PieceID pieceID) {
        highlighted.remove(pieceID);
    }

    private boolean isHighlighted (PieceID pieceID) {
        Boolean result = highlighted.get(pieceID);

        if (result != null) {
            return result;
        } else {
            throw new RuntimeException("Piece " + pieceID + " of color " + color + " is already on board!");
        }
    }

    private void initializePositions (int color) {
         positions = Map.ofEntries(
            Map.entry(PieceID.PIECE_1, new Move(12, 3, PieceID.PIECE_1, color, Orientation.UP, false)),
            Map.entry(PieceID.PIECE_2, new Move(9, 6, PieceID.PIECE_2, color, Orientation.RIGHT, false)),
            Map.entry(PieceID.PIECE_3, new Move(8, 0, PieceID.PIECE_3, color, Orientation.LEFT, false)),
            Map.entry(PieceID.PIECE_4, new Move(4, 16, PieceID.PIECE_4, color, Orientation.UP, false)),
            Map.entry(PieceID.PIECE_5, new Move(6, 6, PieceID.PIECE_5, color, Orientation.UP, false)),
            Map.entry(PieceID.PIECE_6, new Move(11, 0, PieceID.PIECE_6, color, Orientation.DOWN, false)),
            Map.entry(PieceID.PIECE_7, new Move(8, 15, PieceID.PIECE_7, color, Orientation.UP, false)),
            Map.entry(PieceID.PIECE_8, new Move(10, 9, PieceID.PIECE_8, color, Orientation.DOWN, true)),
            Map.entry(PieceID.PIECE_9, new Move(11, 5, PieceID.PIECE_9, color, Orientation.LEFT, true)),
            Map.entry(PieceID.PIECE_10, new Move(0, 4, PieceID.PIECE_10, color, Orientation.LEFT, true)),
            Map.entry(PieceID.PIECE_11, new Move(7, 9, PieceID.PIECE_11, color, Orientation.UP, false)),
            Map.entry(PieceID.PIECE_12, new Move(0, 0, PieceID.PIECE_12, color, Orientation.RIGHT, false)),
            Map.entry(PieceID.PIECE_13, new Move(5, 0, PieceID.PIECE_13, color, Orientation.RIGHT, false)),
            Map.entry(PieceID.PIECE_14, new Move(8, 2, PieceID.PIECE_14, color, Orientation.DOWN, false)),
            Map.entry(PieceID.PIECE_15, new Move(8, 13, PieceID.PIECE_15, color, Orientation.UP, false)),
            Map.entry(PieceID.PIECE_16, new Move(2, 2, PieceID.PIECE_16, color, Orientation.LEFT, true)),
            Map.entry(PieceID.PIECE_17, new Move(3, 9, PieceID.PIECE_17, color, Orientation.UP, false)),
            Map.entry(PieceID.PIECE_18, new Move(0, 14, PieceID.PIECE_18, color, Orientation.RIGHT, false)),
            Map.entry(PieceID.PIECE_19, new Move(2, 5, PieceID.PIECE_19, color, Orientation.UP, true)),
            Map.entry(PieceID.PIECE_20, new Move(4, 12, PieceID.PIECE_20, color, Orientation.UP, false)),
            Map.entry(PieceID.PIECE_21, new Move(0, 9, PieceID.PIECE_21, color, Orientation.RIGHT, false))
        );
    }



    @Override
    public Texel[][] texelize (ColorPallet colorPallet, int scaleX, int scaleY) {
        System.out.println("colorPallet = [" + colorPallet + "], scaleX = [" + scaleX + "], scaleY = [" + scaleY + "]");
        Texel[][] blank = Texel.getBlankTexelMatrix(scaleX * 50, scaleY * 25, new Texel(new TextColor.RGB(0, 0, 0), new TextColor.RGB(0, 0,0), Terminal.TRANSPARENT));

        for (PieceID pieceID : positions.keySet()) {
            Move move = positions.get(pieceID);
            System.out.println(move);

            PieceSprite pieceSprite = new PieceSprite(pieceID, color, move.getOrientation(), move.isFlip(), scaleX, scaleY, new DefaultPallet(), Terminal.TRANSPARENT);
            pieceSprite.draw(move.getX() * 2 * scaleX, move.getY()  * scaleY);

            Texel[][] texelized = pieceSprite.texelize(new DefaultPallet(), scaleX, scaleY);
            for (int y = 0; y < texelized.length; y++) {
                for (int x = 0; x < texelized[y].length; x++) {
                    blank[y + pieceSprite.getPosY()][x + pieceSprite.getPosX()] = texelized[y][x];
                }
            }

        }

        return blank;
    }

    @Override
    public boolean isStretched() {
        return false;
    }

    public static void main(String[] args) {
        Screen screen = new Terminal(50, 25);

        RemainingPieceViewer remainingPieceViewer = new RemainingPieceViewer(0, new HashSet<>(Piece.getAllPieces(0)));
        screen.drawTexelizeable(remainingPieceViewer, new DefaultPallet(), 0, 0, 1, 1);
        screen.update();

        try {
            synchronized (Thread.currentThread()) {
                Thread.currentThread().wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            screen.close();
        }

    }
}
