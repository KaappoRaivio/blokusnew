package ais.twoplayerai;

import blokus.*;
import misc.MoveAndScore;
import misc.OnedSpan;
import misc.Splitter;
import uis.TtyUITest;
import uis.UI;

import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;


public class TwoPlayerAi extends Player {

    private Evaluator evaluator;
    private int depth;

    public TwoPlayerAi(Board initialPosition, int color, String id, UI ui, int depth) {
        super(initialPosition, color, id, ui);
        this.evaluator = new Evaluator(color, depth);
        this.depth = depth;
    }

    @Override
    public Move getMove() {
        List<Move> moves = board.getAllFittingMoves(color);
        System.out.println("Found " + moves.size() + " moves as " + id);

        List<MoveAndScore> moveScores = new ArrayList<>();

        List<WorkerThread> threads = new ArrayList<>();

        List<OnedSpan> spans = Splitter.splitListInto(moves, Runtime.getRuntime().availableProcessors());
        for (OnedSpan span : spans) {
            WorkerThread thread = new WorkerThread(moves.subList(span.getStart(), span.getEnd()), board.deepCopy(), this, depth);
            thread.start();
            threads.add(thread);
        }

        for (WorkerThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                moveScores.add(thread.getResult());
            } catch (NullPointerException e) {
                System.out.println("result is null!");
            }

        }

        return Collections.max(moveScores, new Comparator<MoveAndScore>() {
            @Override
            public int compare(MoveAndScore moveAndScore, MoveAndScore t1) {
                if (!(moveAndScore.isScorePresent() && t1.isScorePresent())) {
                    return -1;
                } else {
                    return moveAndScore.getScore() - t1.getScore() > 0 ? 1 : 0;
                }
            }
        }).getMove();

    }

    public Evaluator getEvaluator() {
        return evaluator;
    }

    protected MoveAndScore getMoveCallBack(List<Move> possibleMoves, Board board) {
        return getMoveCallBack(possibleMoves, board, depth);
    }

    protected MoveAndScore getMoveCallBack(List<Move> possibleMoves, Board board, int depth) {
        Map<Float, Move> moveScores = new HashMap<>();

        for (Move move : possibleMoves) {
            board.putOnBoard(move);
            float score = evaluator.evaluateMove(board, depth);
            board.undo(0);
            moveScores.put(score, move);
        }

        float maxScore = -10000.0f;

        for (float f : moveScores.keySet()) {
            maxScore = max(maxScore, f);
        }
//        System.out.println(moveScores.toString());
        return new MoveAndScore(moveScores.get(maxScore), true, true, maxScore);
    }

    public Board getBoard () {
        return board;
    }

}
