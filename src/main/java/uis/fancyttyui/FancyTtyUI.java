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

    private final transient Object lock = new Object();

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
        Sprite overlaySprite = new Sprite(mesh, '$');

        screen.addSprite(overlaySprite, false);
        overlaySprite.draw(0, 0);
        screen.commit();

        overlaidSprites.add(overlaySprite);
    }

    @Override
    public void clearOverlay() {
        for (var sprite : overlaidSprites) {
            screen.removeSprite(sprite);
        }
        screen.commit();

    }

    @Override
    public void showMessage (MessageType messageType, String message) {

    }

    @Override
    public void commit () {
        screen.commit();
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
        sprite.draw(2, 1);
        screen.commit();

        final boolean[] wait = {true, false};

        keyListener.addKeyEventListener(event -> {
            switch (event.getKeyCode()) {
                case NativeKeyEvent.VC_LEFT:
                    sprite.jump(-2 * scaleX, 0);
                    break;
                case NativeKeyEvent.VC_RIGHT:
                    sprite.jump(2 * scaleX, 0);
                    break;
                case NativeKeyEvent.VC_DOWN:
                    sprite.jump(0, scaleY);
                    break;
                case NativeKeyEvent.VC_UP:
                    sprite.jump(0, -scaleY);
                    break;
                case NativeKeyEvent.VC_A:
                    sprite.rotateAntiClockwise();
                    break;
                case NativeKeyEvent.VC_D:
                    sprite.rotateClockwise();
                    break;
                case NativeKeyEvent.VC_F:
                    sprite.flip();
                    break;
                case NativeKeyEvent.VC_W:
                    sprite.changePieceIDPointer(1);
                    break;
                case NativeKeyEvent.VC_S:
                    sprite.changePieceIDPointer(-1);
                    break;
                case NativeKeyEvent.VC_ENTER:
                    synchronized (lock) {
                        wait[0] = false;
                        lock.notifyAll();
                    }

                    break;
                case NativeKeyEvent.VC_ESCAPE:
                    keyListener.close();
                    synchronized (lock) {
                        wait[0] = false;
                        wait[1] = true;
                        lock.notifyAll();
                    }
                    break;
            }
            screen.commit();
        });

        keyListener.run();
        synchronized (lock) {
            while (wait[0]) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (wait[1]) {
            throw new RuntimeException("Quit!");
        }

        keyListener.close();
        Move move = sprite.getCurrentMove();
        sprite.unDraw();
        screen.removeSprite(sprite);

        return move;
    }

    @Override
    public int getMoveCount() {
        return moveCount;
    }
}