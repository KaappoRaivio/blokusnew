package ais.twoplayerai;

import blokus.*;
import misc.OnedSpan;
import misc.Splitter;

import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;


public class TwoPlayerAi extends Player {

    private Evaluator evaluator;

    public TwoPlayerAi(Board initialPosition, int color, String id) {
        super(initialPosition, color, id);
        this.evaluator = new Evaluator(color);
    }

    @Override
    public Move getMove() {
        List<Move> moves = board.getAllFittingMoves(color);

        List<Move> moveScores = new ArrayList<>();

        List<WorkerThread> threads = new ArrayList<>();

        List<OnedSpan> spans = Splitter.splitListInto(moves, Runtime.getRuntime().availableProcessors());
        for (OnedSpan span : spans) {
            WorkerThread thread = new WorkerThread(moves.subList(span.getStart(), span.getEnd()), board.deepCopy(), this);
            thread.start();
            threads.add(thread);
        }

        for (WorkerThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            moveScores.add(thread.getResult());

        }

//        float maxScore = -1.0f;
//
//        for (float f : moveScores.keySet()) {
//            maxScore = max(maxScore, f);
//        }

//        return moveScores.get(maxScore);
        return moveScores.get(new Random().nextInt(moveScores.size()));

    }

    public Evaluator getEvaluator() {
        return evaluator;
    }

    protected Move getMoveCallBack(List<Move> possibleMoves, Board board) {
        Map<Float, Move> moveScores = new HashMap<>();

        for (Move move : possibleMoves) {
            board.putOnBoard(move);
            float score = evaluator.evaluateMove(board, 3);
            board.undo(0);
            moveScores.put(score, move);
        }

        float maxScore = -1.0f;

        for (float f : moveScores.keySet()) {
            maxScore = max(maxScore, f);
        }

        return moveScores.get(maxScore);
    }

    public Board getBoard () {
        return board;
    }

    public static void main (String[] args) {
        Board board = new Board(14, 14, new MyPieceManager(2), true, 4);



        TwoPlayerAi twoPlayerAi = new TwoPlayerAi(board, 0, "asd");

        System.out.println(timeit(new Runnable() {
            @Override
            public void run() {
                System.out.println(twoPlayerAi.getEvaluator().evaluateMove(twoPlayerAi.getBoard(), 1));
            }
        }, 100));
    }

    private static long timeit (Runnable runnable, int iterations) {
        List<Long> times = new ArrayList<>();
        long sum = 0;

        for (int i = 0; i < iterations; i++) {
            long alku = System.currentTimeMillis();
            runnable.run();
            long loppu = System.currentTimeMillis();
            times.add(loppu - alku);
            sum += loppu - alku;
        }


        return sum / times.size();
    }

    private class MyThread extends Thread {

    }

}
