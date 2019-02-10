package uis.fancyttyui;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.ResizeListener;
import com.googlecode.lanterna.terminal.SimpleTerminalResizeListener;
import com.googlecode.lanterna.terminal.TerminalResizeListener;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import uis.Texel;
import uis.Texelizeable;

import java.awt.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Terminal implements Screen, Serializable {
    private final transient Object lock = new Object();
    public static final char TRANSPARENT = ' ';
    public static final TextColor.RGB foregroundColor = new TextColor.RGB(255, 255, 255);
    public static final TextColor.RGB backgroundColor = new TextColor.RGB(0, 0, 0);
    private final int dimX;
    private final int dimY;


    private com.googlecode.lanterna.terminal.Terminal terminal;
//    private TerminalResizeListener resizeListener;

    private volatile Texel[][] buffer;
    private List<Sprite> sprites = new Vector<>();

    public Terminal (int dimX, int dimY) {
        this.dimX = dimX;
        this.dimY = dimY;

        System.out.println(dimX + ", " + dimY);


        try {
            this.terminal = new DefaultTerminalFactory().createTerminal();
            terminal.setCursorVisible(false);
            ((SwingTerminalFrame) terminal).setSize(new Dimension(dimX / 2 * 16, dimY * 19));
            ((SwingTerminalFrame) terminal).setAlwaysOnTop(true);

//            this.resizeListener = new SimpleTerminalResizeListener(terminal.getTerminalSize());
//            this.resizeListener = new TerminalResizeListener() {
//                @Override
//                public void onResized(com.googlecode.lanterna.terminal.Terminal terminal, TerminalSize newSize) {
//                    System.out.println("Resized! New terminal size " + newSize.getColumns() + ", " + newSize.getRows());
//                    reinitializeBuffer(newSize.getColumns(), newSize.getRows());
//                }
//            };
//            terminal.addResizeListener(resizeListener);
//            terminal.enableSGR(SGR.REVERSE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        this.buffer = new Texel[dimY][dimX];


        initializeBuffer();
    }

    private void reinitializeBuffer (int newDimX, int newDimY) {
        Texel[][] newBuffer = new Texel[newDimY][newDimX];
        for (int y = 0; y < newBuffer.length; y++) {
            for (int x = 0; x < newBuffer[y].length; x++) {
                newBuffer[y][x] = new Texel(foregroundColor, backgroundColor, TRANSPARENT);
            }
        }

        for (int y = 0; y < buffer.length; y++) {
            for (int x = 0; x < buffer[y].length; x++) {
                try {
                    newBuffer[y][x] = buffer[y][x];
                } catch (ArrayIndexOutOfBoundsException ignored) {}
            }
        }
        buffer = newBuffer;
        System.out.println("Trying to commit!");
        commit();
    }

    private void initializeBuffer () {
        for (int y = 0; y < buffer.length; y++) {
            for (int x = 0; x < buffer[y].length; x++) {
                buffer[y][x] = new Texel(foregroundColor, backgroundColor, ' ');
            }
        }
    }

    public void addSprite (Sprite sprite) {
        synchronized (lock) {
            sprites.add(sprite);
        }
    }

    @Override
    public void removeAllSprites() {
        synchronized (lock) {
            sprites = new Vector<>();
        }
    }

    @Override
    public void removeSprite(Sprite sprite) {
        synchronized (lock) {
            sprites.remove(sprite);
        }
    }

    @Override
    public void drawTexelizeable (Texelizeable texelizeable, ColorPallet colorPallet, int posX, int posY, int scaleX, int scaleY) {

        Texel[][] buffer = texelizeable.texelize(colorPallet, scaleX, scaleY);
        if (buffer.length > this.buffer.length || buffer[0].length > this.buffer[0].length) {
            reinitializeBuffer(buffer[0].length, buffer.length);
        }

        for (int y = 0; y < buffer.length; y++) {
            for (int x = 0; x < buffer[y].length; x++) {
                try {
                    this.buffer[y + posY][x + posX] = buffer[y][x];
                } catch (ArrayIndexOutOfBoundsException ignored) {}
            }
        }
    }

    private void drawSpritesToTerminal () {
//        try {
//            terminal.setCursorPosition(0,0);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        for (Sprite sprite : sprites) {
            if (sprite.isDrawn()) {
                for (int y = sprite.getPosY(); y < sprite.getPosY() + sprite.getDimY(); y++) {
                    for (int x = sprite.getPosX(); x < sprite.getPosX() + sprite.getDimX(); x++) {
                        Texel current = sprite.getChar(x - sprite.getPosX(), y - sprite.getPosY());

                        if (current.getValue() == sprite.getTransparent()) {
                            continue;
                        }


                        try {

                            terminal.setCursorPosition(x, y);
                            terminal.setForegroundColor(current.getForegroundColor());
                            terminal.setBackgroundColor(current.getBackgroundColor());
                            terminal.putCharacter(current.getValue());

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void commit() {
        synchronized (lock) {
            for (int y = 0; y < getDimY(); y++) {
                for (int x = 0; x < getDimX(); x++) {
                    Texel current;
                    try {
                        current = buffer[y][x];
                    } catch (ArrayIndexOutOfBoundsException e) {continue;}


                    try {
                        terminal.setCursorPosition(x, y);
                        terminal.setForegroundColor(current.getForegroundColor());
                        terminal.setBackgroundColor(current.getBackgroundColor());
                        terminal.putCharacter(current.getValue());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            drawSpritesToTerminal();

            try {
                terminal.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void setPixel (int x, int y, Texel newTexel) {
        buffer[y][x] = newTexel;
    }

    @Override
    public Texel getPixel(int x, int y) {
        return buffer[y][x];
    }

    @Override
    public void updateBuffer(Texel[][] newBuffer) {
        for (int y = 0; y < newBuffer.length; y++) {
            for (int x = 0; x < newBuffer[y].length; x++) {
                buffer[y][x] = newBuffer[y][x];
            }
        }
    }

    @Override
    public void close() {
        try {
            terminal.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getDimX() {
        try {
            return terminal.getTerminalSize().getColumns();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getDimY() {
        try {
            return terminal.getTerminalSize().getRows();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public com.googlecode.lanterna.terminal.Terminal getTerminal() {
        return terminal;
    }

}
