package ais.twoplayerai;

import blokus.Board;
import blokus.Move;
import misc.Pair;

import java.util.List;

public class WorkerThread extends Thread {
    private List<Move> movesToTest;
    private Board board;
    private TwoPlayerAi callback;
    private int depth;
    private List<Pair<Move, Double>> result;

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

        public List<Pair<Move, Double>> getResult () {
            return result;
        }
}
