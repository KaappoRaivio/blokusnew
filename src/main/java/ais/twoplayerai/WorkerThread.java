package ais.twoplayerai;

import blokus.Board;
import blokus.Move;

import java.util.List;
import java.util.function.Consumer;

public class WorkerThread extends Thread {
    private List<Move> movesToTest;
    private Board board;
    private TwoPlayerAi callback;
    private Move result;

        public WorkerThread(List<Move> movesToTest, Board board, TwoPlayerAi callback) {
            super();

            this.movesToTest = movesToTest;
            this.board = board;
            this.callback = callback;
            Pair<Float, Integer> a;
        }

        @Override
        public void run() {
            result = callback.getMoveCallBack(movesToTest, board);
        }

        public Move getResult () {
            if (result == null) {
                throw new RuntimeException("Result is null");
            } else {
                return result;
            }
        }
}
