package uis.fancyttyui;


import java.io.IOException;

public interface Screen {
    void commit () throws IOException;
    void setPixel (int x, int y, char newChar);
    void updateBuffer (char[][] buffer);
    void close ();

    int getDimX ();
    int getDimY ();
    com.googlecode.lanterna.terminal.Terminal getTerminal ();
}
