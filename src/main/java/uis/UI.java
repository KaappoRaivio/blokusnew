package uis;

import blokus.Board;
import blokus.Move;

public interface UI {
    void updateBoard (Board board);
    void updateTurn (int turn);
    void updateMoveCounter ();
    void updateMoveCounter (int moveCounter);
    void update ();
    Move getMove ();


}
