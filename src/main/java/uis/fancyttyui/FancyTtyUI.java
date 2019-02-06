package uis.fancyttyui;

import blokus.Board;
import blokus.DefaultPallet;
import blokus.Move;


import blokus.PiecePallet;
import listener.KeyEventListener;
import listener.KeyListener;
import org.jnativehook.keyboard.NativeKeyEvent;
import uis.UI;

import java.io.Serializable;
import java.util.stream.Collectors;


public class FancyTtyUI implements UI, Serializable {
    private Screen screen;
    private Board board;
    private int turn = 0;
    private int moveCount = 0;

    private Sprite boardSprite;

    private final transient Object lock = new Object();



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
        screen.removeAllSprites();
        screen.addSprite(boardSprite);
        boardSprite.draw(0, 0);
    }

    @Override
    public void commit () {

        for (int i = 0; i < board.getAmountOfPlayers(); i++) {
            System.out.println("Color " + i + ": " + board.getPieceManager().getPiecesNotOnBoard(i).stream().map(Enum::toString).collect(Collectors.joining(", \t")));
        }

        screen.commit();
    }

    @Override
    public void close() {
        screen.close();
    }


    @Override
    public Move getMove (int color) {
        KeyListener keyListener = new KeyListener();
        PieceSprite sprite = new PieceSprite(turn, new PiecePallet(), board);
        screen.addSprite(sprite);
        sprite.draw(2, 1);
        screen.commit();



        final boolean[] wait = {true, false};


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