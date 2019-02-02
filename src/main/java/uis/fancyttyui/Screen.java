package uis.fancyttyui;


import uis.Texel;

import java.io.IOException;

public interface Screen {
    void commit ();
    void setPixel (int x, int y, Texel newTexel);
    Texel getPixel (int x, int y);
    void updateBuffer (Texel[][] buffer);
    void close ();
    void addSprite (Sprite sprite);
    void removeAllSprites ();

    int getDimX ();
    int getDimY ();
    com.googlecode.lanterna.terminal.Terminal getTerminal ();
}
