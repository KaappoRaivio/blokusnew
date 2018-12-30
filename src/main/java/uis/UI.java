package uis;

import blokus.Board;
import blokus.Move;

public interface UI {
    void updateValues (Board board, int turn, int moveCount);
    void commit ();
}
