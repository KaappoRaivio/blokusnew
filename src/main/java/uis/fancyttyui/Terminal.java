package uis.fancyttyui;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import uis.Texel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Terminal implements Screen {
    public static final char TRANSPARENT = ' ';
    public static final TextColor.RGB foregroundColor = new TextColor.RGB(255, 255, 255);
    public static final TextColor.RGB backgroundColor = new TextColor.RGB(0, 0, 0);


    private com.googlecode.lanterna.terminal.Terminal terminal;

    private Texel[][] buffer;
    private List<Sprite> sprites = new ArrayList<>();

    public Terminal () {
        try {
            this.terminal = new DefaultTerminalFactory().createTerminal();
            terminal.setCursorVisible(false);
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
        sprites.add(sprite);
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
        for (int y = 0; y < buffer.length; y++) {
            for (int x = 0; x < buffer[y].length; x++) {
                Texel current = buffer[y][x];


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


    public static void main (String[] aarghs) {
        Terminal terminal = new Terminal();
        terminal.setPixel(10, 10, new Texel('A'));
        terminal.commit();
        System.out.println("done!");
        terminal.close();
    }
}
