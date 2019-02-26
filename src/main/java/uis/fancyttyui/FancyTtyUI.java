package uis.fancyttyui;

import blokus.Board;
import blokus.Move;


import blokus.PiecePallet;
import listener.KeyEventListener;
import listener.KeyListener;
import org.jnativehook.keyboard.NativeKeyEvent;
import uis.MessageType;
import uis.Texel;
import uis.Texelizeable;
import uis.UI;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;


public class FancyTtyUI implements UI, Serializable {
    private Screen screen;
    private Board board;
    private final int scaleX;
    private final int scaleY;
    private int turn;
    private int moveCount;

    private Sprite boardSprite;



    private List<Sprite> overlaidSprites = new Vector<>();



    public FancyTtyUI(Board board, int scaleX, int scaleY) {
        this.board = board;
        this.scaleX = scaleX;
        this.scaleY = scaleY;

        Texel[][] texelMesh = board.texelize(new DefaultPallet(), scaleX, scaleY);

        screen = new Terminal(texelMesh[0].length, texelMesh.length);

//        boardSprite = new Sprite(board.texelize(new DefaultPallet(), scaleX, scaleY), Terminal.TRANSPARENT);
//        screen.addSprite(boardSprite);
//        boardSprite.draw(0, 0);
        screen.drawTexelizeable(board, new DefaultPallet(), 0, 0, scaleX, scaleY);

    }


    @Override
    public void updateValues (Board board, int turn, int moveCount) {
        this.board = board;
        this.turn = turn;
        this.moveCount = moveCount;

        screen.drawTexelizeable(this.board, new DefaultPallet(),0, 0, scaleX, scaleY);
    }

    @Override
    public void overlay (Texelizeable board) {
        Texel[][] mesh = board.texelize(new OverlayPallet(), scaleX, scaleY);
        var compareMesh = this.board.texelize(new OverlayPallet(), scaleX, scaleY);



        for (int y = 0; y < mesh.length; y++) {
            for (int x = 0; x < mesh[y].length; x++) {
                if (mesh[y][x].equals(compareMesh[y][x])) {
                    mesh[y][x] = new Texel('$');
                }
            }

        }
        Sprite overlaySprite = new Sprite(mesh, '$', false);

        screen.addSprite(overlaySprite, false);
        overlaySprite.draw(0, 0);
        screen.update();

        overlaidSprites.add(overlaySprite);
    }

    @Override
    public void clearOverlay() {
        for (var sprite : overlaidSprites) {
            screen.removeSprite(sprite);
        }
        screen.update();

    }

    @Override
    public void showMessage (MessageType messageType, String message) {

    }

    @Override
    public void commit () {
        screen.update();
    }

    @Override
    public void close() {
        screen.close();
    }


    @Override
    public Move getMove (int color) {
        KeyListener keyListener = new KeyListener();
        PieceSpriteSymbol sprite = new PieceSpriteSymbol(turn, new PiecePallet(), board, scaleX, scaleY);
        screen.addSprite(sprite);
        sprite.draw(1, 1);
        screen.update();

        UIKeyEventListener listener = new UIKeyEventListener(sprite, screen, scaleX, scaleY, board.getDimX(), board.getDimY(), 1, 1);

        keyListener.addKeyEventListener(listener);
        keyListener.run();

        Move move = listener.waitForMove();

        keyListener.clearAllListeners();
        keyListener.close();

        sprite.unDraw();
        screen.removeSprite(sprite);

        return move;
    }

    @Override
    public int getMoveCount() {
        return moveCount;
    }
}