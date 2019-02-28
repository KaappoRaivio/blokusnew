package uis.fancyttyui;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
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
    private static final TextColor.RGB foregroundColor = new TextColor.RGB(255, 255, 255);
    private static final TextColor.RGB backgroundColor = new TextColor.RGB(0, 0, 0);

    private com.googlecode.lanterna.terminal.Terminal terminal;

    private volatile Texel[][] buffer;
    private List<Sprite> sprites = new ArrayList<>();

    public Terminal (int dimX, int dimY) {
        try {
            terminal = new DefaultTerminalFactory().createTerminal();
            terminal.setCursorVisible(false);

            int pixDimX = dimX / 2 * 16;
            int pixDimY = dimY * 16;


            try {
                Insets insets = ((SwingTerminalFrame) terminal).getInsets();

                ((SwingTerminalFrame) terminal).setSize(new Dimension(insets.left + insets.right + pixDimX + 1, insets.top + insets.bottom + pixDimY + 1));
                ((SwingTerminalFrame) terminal).setAlwaysOnTop(true);
            } catch (ClassCastException e) {
                System.out.println("Couldn't set terminal dimensions for " + terminal.getClass());
            }



        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        buffer = new Texel[dimY][dimX];


        initializeBuffer();
    }

    private void reinitializeBuffer (int newDimX, int newDimY) {
        Texel[][] newBuffer = new Texel[newDimY][newDimX];

        for (int y = 0; y < newBuffer.length; y++) {
            for (int x = 0; x < newBuffer[y].length; x++) {
                newBuffer[y][x] = new Texel(foregroundColor, backgroundColor, TRANSPARENT);
            }
        }

        for (int y = 0; y < newBuffer.length; y++) {
            for (int x = 0; x < newBuffer[y].length; x++) {
                try {
                    newBuffer[y][x] = buffer[y][x];
                } catch (ArrayIndexOutOfBoundsException ignored) {}
            }
        }

        buffer = newBuffer;
        update();
    }

    private void initializeBuffer () {
        for (int y = 0; y < buffer.length; y++) {
            for (int x = 0; x < buffer[y].length; x++) {
                buffer[y][x] = new Texel(foregroundColor, backgroundColor, ' ');
            }
        }
    }

    public void addSprite (Sprite sprite) {
        addSprite(sprite, false);
    }

    @Override
    public void addSprite (Sprite sprite, boolean tuck) {
        synchronized (lock) {
            if (tuck) {
                sprites.add(0, sprite);
            } else {
                sprites.add(sprite);
            }
        }
    }

    @Override
    public void removeAllSprites () {
        synchronized (lock) {
            sprites = new Vector<>();
        }
    }

    @Override
    public void removeSprite (Sprite sprite) {
        synchronized (lock) {
            sprites.remove(sprite);
        }
    }

    @Override
    public void drawTexelizeable (Texelizeable texelizeable, ColorPallet colorPallet, int posX, int posY, int scaleX, int scaleY) {

        Texel[][] buffer = texelizeable.texelize(colorPallet, scaleX, scaleY);
        if (buffer.length + posY > this.buffer.length || buffer[0].length + posX > this.buffer[0].length) {
            reinitializeBuffer(buffer[0].length, buffer.length);
            System.out.println("moi");
        }

        for (int y = 0; y < buffer.length; y++) {
            for (int x = 0; x < buffer[y].length; x++) {
                try {
                    Texel current = buffer[y][x];
                    setBuffer(x + posX, y + posY, current, texelizeable.isStretched());
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();

                }
            }
        }
    }

    private void setBuffer (int posX, int posY, Texel texel, boolean stretch) {
        try {
            if (stretch) {
                buffer[posY][2 * posX] = texel;
                buffer[posY][2 * posX + 1] = texel;
            } else {
                buffer[posY][posX] = texel;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private void drawSpritesToTerminal () {
        for (Sprite sprite : sprites) {
            if (sprite.isDrawn()) {
                for (int y = sprite.getPosY(); y < sprite.getPosY() + sprite.getDimY(); y++) {
                    for (int x = sprite.getPosX(); x < sprite.getPosX() + sprite.getDimX(); x++) {
                        Texel current = sprite.getChar(x - sprite.getPosX(), y - sprite.getPosY());

                        if (current.getValue() == sprite.getTransparent()) {
                            continue;
                        }

                        setCharacter(x, y, current, sprite.isStretched());
                    }
                }
            }
        }
    }

    private synchronized void setCharacter (int posX, int posY, Texel texel, boolean stretch) {
        if (stretch) {
            setCharacter(2 * posX, posY, texel);
            setCharacter(2 * posX + 1, posY, texel);
        } else {
            setCharacter(posX, posY, texel);
        }
    }

    private synchronized void setCharacter (int posX, int posY, Texel texel) {
        try {
            terminal.setCursorPosition(posX, posY);
            terminal.setForegroundColor(texel.getForegroundColor());
            terminal.setBackgroundColor(texel.getBackgroundColor());
            terminal.putCharacter(texel.getValue());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update() {
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
            if (newBuffer[y].length >= 0) System.arraycopy(newBuffer[y], 0, buffer[y], 0, newBuffer[y].length);
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
