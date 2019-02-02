package uis.fancyttyui;

import blokus.*;
import com.googlecode.lanterna.TextColor;
import listener.KeyEventListener;
import listener.KeyListener;
import misc.ConvertToList;
import org.apache.commons.lang3.ArrayUtils;
import org.jnativehook.keyboard.NativeKeyEvent;
import uis.Texel;

import java.util.*;
import java.util.stream.Collectors;

public class Sprite {

    private static int spriteID = 0;

    protected Texel[][] mesh;

    private volatile int posX = -1;  // These are "volatile"
    private volatile int posY = -1;  // to ensure
    private volatile boolean drawn;  // multi-thread safety.

    private int dimX;
    private int dimY;

    private int ID;

    private char transparent;

    public int getID() {
        return ID;
    }

    public Sprite(Texel[][] mesh, char transparent) {
        this.mesh = mesh;

        this.dimX = mesh[0].length;
        this.dimY = mesh.length;

        this.ID = Sprite.spriteID++;
        this.transparent = transparent;
    }

    public char getTransparent() {
        return transparent;
    }

    public static Sprite fromString (String string, String verticalDelimiter, String horizontalDelimiter, char transparent) {
        Texel[][] mesh = misc.ConvertToList.convertToList(string, verticalDelimiter, horizontalDelimiter, transparent);

        return new Sprite(mesh, transparent);
    }

    public void draw (int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.drawn = true;
    }



    public void unDraw () {
        drawn = false;
        posX = -1;
        posY = -1;
    }
    
    

    public void jump (int deltaX, int deltaY) {
        synchronized (this) {
            this.posX = this.posX + deltaX;
            this.posY = this.posY + deltaY;
        }
    }

    @Override
    public String toString() {
        return "Sprite{" +
                "mesh=" + Arrays.toString(mesh) +
                ", posX=" + posX +
                ", posY=" + posY +
                ", drawn=" + drawn +
                ", dimX=" + dimX +
                ", dimY=" + dimY +
                '}';
    }

    public static void main (String[] args) {
        Screen screen = new Terminal();

        Board board = Board.fromFile("/home/kaappo/git/blokus/src/main/resources/boards/Sat Feb 02 20:04:00 EET 2019.ser", false);
        Sprite boardSprite = new Sprite(board.texelize(new DefaultPallet()), '$');
        screen.addSprite(boardSprite);
        boardSprite.draw(0, 0);

        PieceSprite sprite = new PieceSprite(1, new DefaultPallet(), board);
        screen.addSprite(sprite);
        sprite.draw(1, 1);

        screen.commit();


        KeyListener keyListener = new KeyListener();
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
                }

                screen.commit();
            }
        });
        keyListener.run();
        try {
            keyListener.wait();
        } catch (InterruptedException e) {

        }
            System.out.println("asdasd");
    }

    public boolean isDrawn() {
        return drawn;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getDimX() {
        return dimX;
    }

    public int getDimY() {
        return dimY;
    }

    public Texel getChar (int x, int y) {
        try {
            return mesh[y][x];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(this);
            throw new RuntimeException(e);
        }
    }

}
