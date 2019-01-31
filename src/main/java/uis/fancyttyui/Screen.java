package uis.fancyttyui;


import java.io.IOException;

public interface Screen {
    void commit ();
    void setPixel (int x, int y, char newChar);
    char getPixel (int x, int y);
    void updateBuffer (char[][] buffer);
    void close ();

    int getDimX ();
    int getDimY ();
    com.googlecode.lanterna.terminal.Terminal getTerminal ();
}
