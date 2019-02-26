package uis.fancyttyui;

import blokus.Move;
import listener.KeyEventListener;
import org.jnativehook.keyboard.NativeKeyEvent;

public class UIKeyEventListener implements KeyEventListener {
    private final transient Object lock = new Object();
    private PieceSpriteSymbol sprite;
    private Screen screen;
    private final int scaleX;
    private final int scaleY;
    private final int boardDimX;
    private final int boardDimY;
    private final int offsetX;
    private final int offsetY;
    private volatile boolean wait = true;
    private volatile boolean quit = false;


    public UIKeyEventListener(PieceSpriteSymbol sprite, Screen screen, int scaleX, int scaleY, int boardDimX, int boardDimY, int offsetX, int offsetY) {
        sprite.draw(offsetX, offsetY);
        this.sprite = sprite;
        this.screen = screen;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.boardDimX = boardDimX;
        this.boardDimY = boardDimY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;

    }

    private void comeBackIfOverTheEdge () {
        if (isOverRightBorder()) {
            sprite.move(boardDimX * scaleX - sprite.getDimX() + offsetX, sprite.getPosY());
        }
        if (isOverLeftBorder()) {
            sprite.move(offsetX, sprite.getPosY());
        }
        if (isOverDownBorder()) {
            sprite.move(sprite.getPosX(), boardDimY * scaleY - sprite.getDimY() + offsetY);
        }
        if (isOverUpBorder()) {
            sprite.move(sprite.getPosX(), offsetY);
        }
    }

    @Override
    public void reportKey (NativeKeyEvent event) {
        switch (event.getKeyCode()) {
            case NativeKeyEvent.VC_LEFT:
                sprite.jump(-scaleX, 0);
                break;
            case NativeKeyEvent.VC_RIGHT:
                sprite.jump(scaleX, 0);
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
                    wait = false;
                    lock.notifyAll();
                }

                return;
            case NativeKeyEvent.VC_ESCAPE:
                synchronized (lock) {
                    wait = false;
                    quit = true;
                    lock.notifyAll();
                }
                break;
        }
        comeBackIfOverTheEdge();
        screen.update();
    }

    private boolean isOverLeftBorder () {
        return sprite.getPosX() / scaleX < offsetX;
    }

    private boolean isOverRightBorder () {
        return (sprite.getPosX() + sprite.getDimX() - offsetX) > boardDimX * scaleX;
//        return false;
    }

    private boolean isOverUpBorder () {
        return sprite.getPosY() / scaleY < offsetY;
    }

    private boolean isOverDownBorder () {
        return (sprite.getPosY() + sprite.getDimY()) >= offsetY + boardDimY * scaleY;
    }



    public Move waitForMove () {
        synchronized (lock) {
            while (wait) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        if (quit) {
            throw new RuntimeException("Quit");
        }

        return sprite.getCurrentMove();
    }
}
