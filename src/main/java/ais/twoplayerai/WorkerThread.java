package ais.twoplayerai;

import blokus.Board;
import blokus.Move;
import misc.MoveAndScore;

import java.util.List;

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
//            board.putOnBoard(result.getMove());
//            System.out.println(board);
            System.out.println(result.getMove() + ", " + result.getScore());
            return result;
        }
}
