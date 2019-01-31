package uis.fancyttyui;

import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Terminal implements Screen {
    private com.googlecode.lanterna.terminal.Terminal terminal;

    private char[][] buffer;

    public Terminal () {
        try {
            this.terminal = new DefaultTerminalFactory().createTerminal();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.buffer = new char[getDimY()][getDimX()];
        initializeBuffer();
    }

    private void initializeBuffer () {
        for (int y = 0; y < buffer.length; y++) {
            for (int x = 0; x < buffer[y].length; x++) {
                buffer[y][x] = ' ';
            }
        }
    }

    @Override
    public void commit() {
        for (int y = 0; y < buffer.length; y++) {
            for (int x = 0; x < buffer[y].length; x++) {
                try {
                    terminal.setCursorPosition(x, y);
                    terminal.putCharacter(buffer[y][x]);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        try {
            terminal.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setPixel(int x, int y, char newChar) {
        buffer[y][x] = newChar;
    }

    @Override
    public char getPixel(int x, int y) {
        return buffer[y][x];
    }

    @Override
    public void updateBuffer(char[][] newBuffer) {
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
        terminal.setPixel(10, 10, 'A');
        terminal.commit();
        System.out.println("done!");
        terminal.close();
    }
}
