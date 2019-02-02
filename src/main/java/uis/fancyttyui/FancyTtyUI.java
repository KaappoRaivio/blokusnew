package uis.fancyttyui;

import blokus.Board;
import blokus.DefaultPallet;
import blokus.Move;


import blokus.PiecePallet;
import listener.KeyEventListener;
import listener.KeyListener;
import org.jnativehook.keyboard.NativeKeyEvent;
import uis.UI;


public class FancyTtyUI implements UI {
    private Screen screen;
    private Board board;
    private int turn = 0;
    private int moveCount = 0;

    private Sprite boardSprite;

    private final Object lock = new Object();



    public FancyTtyUI(Board board) {
        this.board = board;
        this.screen = new Terminal();

        boardSprite = new Sprite(board.texelize(new DefaultPallet()), Terminal.TRANSPARENT);
        screen.addSprite(boardSprite);
        boardSprite.draw(0, 0);

    }


    @Override
    public void updateValues (Board board, int turn, int moveCount) {
        this.board = board;
        this.turn = turn;
        this.moveCount = moveCount;

        boardSprite = new Sprite(board.texelize(new DefaultPallet()), Terminal.TRANSPARENT);
        screen.addSprite(boardSprite);
        boardSprite.draw(0, 0);
    }

    @Override
    public void commit () {

        screen.commit();
    }


    @Override
    public Move getMove (int color) {
        KeyListener keyListener = new KeyListener();
        PieceSprite sprite = new PieceSprite(turn, new PiecePallet(), board);
        screen.addSprite(sprite);
        sprite.draw(1, 1);
        screen.commit();



        final boolean[] wait = {true};

        keyListener.addKeyEventListener(new KeyEventListener() {
            @Override
            public void reportKey(NativeKeyEvent event) {

                switch (event.getKeyCode()) {
                    case NativeKeyEvent.VC_LEFT:
                        sprite.jump(-2, 0);
                        break;
                    case NativeKeyEvent.VC_RIGHT:
                        sprite.jump(2, 0);
                        break;
                    case NativeKeyEvent.VC_DOWN:
                        sprite.jump(0, 1);
                        break;
                    case NativeKeyEvent.VC_UP:
                        sprite.jump(0, -1);
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
                }
                screen.commit();
            }

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
        keyListener.close();
        Move move = sprite.getCurrentMove();
        sprite.unDraw();
        screen.commit();

        return move;


    }
}
