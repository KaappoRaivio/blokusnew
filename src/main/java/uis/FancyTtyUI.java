package uis;

import blokus.Board;
import blokus.Move;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;


public class FancyTtyUI implements UI {
    public FancyTtyUI() {
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        try {
            Terminal terminal = defaultTerminalFactory.createTerminal();

            terminal.putCharacter('H');
            terminal.putCharacter('e');
            terminal.putCharacter('l');
            terminal.putCharacter('l');
            terminal.putCharacter('o');
            terminal.putCharacter('\n');
            terminal.bell();
            terminal.flush();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateValues(Board board, int turn, int moveCount) {

    }

    @Override
    public void commit() {

    }

    @Override
    public Move getMove(int color) {
        return null;
    }
}
