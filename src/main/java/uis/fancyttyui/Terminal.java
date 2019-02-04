package uis.fancyttyui;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import uis.Texel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Terminal implements Screen {
    private final Object lock = new Object();
    public static final char TRANSPARENT = ' ';
    public static final TextColor.RGB foregroundColor = new TextColor.RGB(255, 255, 255);
    public static final TextColor.RGB backgroundColor = new TextColor.RGB(0, 0, 0);


    private com.googlecode.lanterna.terminal.Terminal terminal;

    private Texel[][] buffer;
    private List<Sprite> sprites = new Vector<>();

    public Terminal () {
        try {
            this.terminal = new DefaultTerminalFactory().createTerminal();
            terminal.setCursorVisible(false);
//            terminal.enableSGR(SGR.REVERSE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        this.buffer = new Texel[getDimY()][getDimX()];


        initializeBuffer();
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
