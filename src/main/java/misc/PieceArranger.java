package misc;

import blokus.MyPieceManager;
import blokus.PieceID;
import listener.KeyListener;
import org.jnativehook.keyboard.NativeKeyEvent;
import uis.fancyttyui.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class PieceArranger {
    private static final Object lock = new Object();
    public static void main(String[] arghs) {
        Screen screen = new Terminal(50, 25);

        boolean[] wait = {true, false};

        int scaleX = 1;
        int scaleY = 1;

        List<PieceSprite> sprites = new ArrayList<>();
        List<PieceID> pieceIDs = new MyPieceManager(1).getPiecesNotOnBoard(0);
        pieceIDs.forEach(item -> sprites.add(new PieceSprite(item, 0, scaleX, scaleY, new DefaultPallet(), Terminal.TRANSPARENT)));
        sprites.forEach(screen::addSprite);
        sprites.forEach(item -> item.draw(0, 0));
        AtomicInteger spritePointer = new AtomicInteger(21);
        AtomicReference<PieceSprite> currentSprite = new AtomicReference<>(sprites.get(spritePointer.get() % sprites.size()));

        KeyListener keyListener = new KeyListener();
        keyListener.addKeyEventListener(event -> {
            switch (event.getKeyCode()) {
                case NativeKeyEvent.VC_LEFT:
                    currentSprite.get().jump(-2 * scaleX, 0);
                    break;
                case NativeKeyEvent.VC_RIGHT:
                    currentSprite.get().jump(2 * scaleX, 0);
                    break;
                case NativeKeyEvent.VC_DOWN:
                    currentSprite.get().jump(0, scaleY);
                    break;
                case NativeKeyEvent.VC_UP:
                    currentSprite.get().jump(0, -scaleY);
                    break;
                case NativeKeyEvent.VC_A:
                    currentSprite.get().rotateAntiClockwise();
                    break;
                case NativeKeyEvent.VC_D:
                    currentSprite.get().rotateClockwise();
                    break;
                case NativeKeyEvent.VC_F:
                    currentSprite.get().flip();
                    break;
                case NativeKeyEvent.VC_W:
                    spritePointer.addAndGet(1);
                    break;
                case NativeKeyEvent.VC_S:
                    spritePointer.addAndGet(-1);
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
            currentSprite.set(sprites.get(spritePointer.get() % sprites.size()));
            screen.commit();
        });

        try {
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
        } finally {
            screen.close();
            keyListener.close();

            sprites.forEach(item -> System.out.println(item.getPosX() / 2 + ", " + item.getPosY() + " " + item.getOrientation() + " " + item.isFlip() + " " + item.getPieceID()));
        }


    }
}
