package uis;

import blokus.Board;
import blokus.Move;

public interface UI {
    void updateValues (Board board, int turn, int moveCount);
    Move getMove (int color);

    void commit ();
    void close ();

    void overlay (Texelizeable board);
    void clearOverlay ();
    void showMessage (MessageType messageType, String message);

    int getMoveCount ();
}
