package uis;

import blokus.Board;
import blokus.Move;

public interface UI {
    void updateValues (Board board, int turn, int moveCount);
    void showMessage (MessageType messageType, String message);
    void commit ();
    void close ();
    Move getMove (int color);
    int getMoveCount ();

}
