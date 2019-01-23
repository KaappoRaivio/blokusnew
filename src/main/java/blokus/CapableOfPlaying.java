package blokus;

import misc.MoveAndScore;

public interface CapableOfPlaying {
    MoveAndScore getMove();
    void updateValues (Board board, int turn, int moveCount);
    int getColor ();

}
