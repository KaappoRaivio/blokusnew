package ais.twoplayerai;

import blokus.Board;
import blokus.Move;
import misc.MoveAndScore;

import java.util.List;
import java.util.function.Consumer;

public class WorkerThread extends Thread {
    private List<Move> movesToTest;
    private Board board;
    private TwoPlayerAi callback;
    private int depth;
    private MoveAndScore result;

        public WorkerThread(List<Move> movesToTest, Board board, TwoPlayerAi callback, int depth) {
            super();

            this.movesToTest = movesToTest;
            this.board = board;
            this.callback = callback;

            this.depth = depth;
        }

        @Override
        public void run() {
            result = callback.getMoveCallBack(movesToTest, board, depth);
        }

        public MoveAndScore getResult () {
            return result;
        }
}