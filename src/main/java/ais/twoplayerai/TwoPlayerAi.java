package ais.twoplayerai;

import blokus.*;
import misc.MoveAndScore;
import misc.OnedSpan;
import misc.Splitter;
import uis.UI;

import java.util.*;

import static java.lang.Math.*;


public class TwoPlayerAi extends Player {

    private Evaluator evaluator;
    private int depth;
    private boolean randomize;

    public TwoPlayerAi(Board initialPosition, int color, String id, UI ui, int depth, Evaluator evaluator, boolean randomize) {
        super(initialPosition, color, id, ui);
        this.evaluator = evaluator;
        this.depth = depth;
        this.randomize = randomize;
    }

    @Override
    public Move getMove() {
        long time = System.currentTimeMillis();
//        List<Move> moves = board.getAllFittingMoves(color);
        List<Move> moves = board.getFirstNFittingMoves(evaluator.getN(), color);
        System.out.println(moves);


        List<MoveAndScore> moveScores = new Vector<>();
        System.out.println("Found " + moves.size() + " moves as " + id);

//        moves.parallelStream().reduce((item, result) -> {
//            synchronized (moveScores) {
//                moveScores.add(new MoveAndScore(item, true, true, evaluator.evaluateMove(board.deepCopy(), depth)));
//            }
//        });

        List<WorkerThread> threads = new Vector<>();

        List<OnedSpan> spans = Splitter.splitListInto(moves, min(Runtime.getRuntime().availableProcessors(), moves.size()));
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

            if (thread.getResult() == null) {
                System.out.println("BADDD");
                throw new NullPointerException();

            } else {
//                System.out.println(thread.getResult().getMove() + ", " + thread.getResult().getScore());
                System.out.println(Collections.max(thread.getResult(), new Comparator<MoveAndScore>() {
                    @Override
                    public int compare(MoveAndScore moveAndScore, MoveAndScore t1) {
                        if (moveAndScore.getScore() - t1.getScore() < 0) {
                            return -1;
                        } else if (moveAndScore.getScore() - t1.getScore() > 0) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                }));
                moveScores.addAll(thread.getResult());

            }
        }

        moveScores.sort(new Comparator<MoveAndScore>() {
            @Override
            public int compare (MoveAndScore moveAndScore, MoveAndScore t1) {
                if (!(moveAndScore.isScorePresent() && t1.isScorePresent())) {
                    return -1;
                } else {
                    return moveAndScore.getScore() - t1.getScore() < 0 ? 1 : moveAndScore.getScore() - t1.getScore() == 0 ? 0 : -1;
                }
            }
        });
        System.out.println(moveScores);

        long timeEnd = System.currentTimeMillis();
        System.out.println("Took " + (timeEnd - time) + " milliseconds as " + id);

        Move bestMove;

        if (randomize) {
            try {
                bestMove = moveScores.get(new Random().nextInt(2)).getMove();
            } catch (IndexOutOfBoundsException e) {
                bestMove = moveScores.get(0).getMove();
            }
        } else {
            System.out.println(moveScores.get(0).getMove() + ", " + moveScores.get(0).getScore());
//            return moveScores.get(0).getMove();
            bestMove = moveScores.get(0).getMove();
        }

        board.putOnBoard(bestMove);
        System.out.println("Parameters: " + evaluator.evaluatePosition(board, true));

        return  bestMove;

    }

    public Evaluator getEvaluator() {
        return evaluator;
    }

    protected List<MoveAndScore> getMoveCallBack(List<Move> possibleMoves, Board board) {
        return getMoveCallBack(possibleMoves, board, depth);
    }

    protected List<MoveAndScore> getMoveCallBack(List<Move> possibleMoves, Board board, int depth) {
//        Map<Float, Move> moveScores = new HashMap<>();
        List<MoveAndScore> moveScores = new Vector<>();

        for (Move move : possibleMoves) {
            board.putOnBoard(move);
            float score = evaluator.evaluateMove(board, depth);
            board.undo(0);
//            moveScores.put(score, move);
            moveScores.add(new MoveAndScore(move, true, true, score));
        }

        return moveScores;

//        moveScores.sort(new Comparator<MoveAndScore>() {
//            @Override
//            public int compare(MoveAndScore moveAndScore, MoveAndScore t1) {
//                if (moveAndScore.getScore() - t1.getScore() < 0) {
//                    return -1;
//                } else if (t1.getScore() - moveAndScore.getScore() > 0) {
//                    return 1;
//                } else {
//                    return 0;
//                }
//            }
//        });

//        List<Move> bestMoves = new Vector<>();
//
//        float maxScore = -1000000000.0f;
//
//        moveScores.v
//
//        for (float f : moveScores.keySet()) {
//            maxScore = max(maxScore, f);
//        }
//        return new MoveAndScore(moveScores.get(maxScore), true, true, maxScore);
    }

    public Board getBoard () {
        return board;
    }

}
