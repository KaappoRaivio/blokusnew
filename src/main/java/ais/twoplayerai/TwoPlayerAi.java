package ais.twoplayerai;

import blokus.Board;
import blokus.Move;
import blokus.Player;
import misc.MoveAndScore;
import misc.OnedSpan;
import misc.Splitter;
import uis.UI;

import java.util.*;

import static java.lang.Math.min;


public class TwoPlayerAi extends Player {

    private Evaluator evaluator;
    private int depth;
    private boolean randomize;

    public TwoPlayerAi(Board initialPosition, int color, String id, UI ui, int depth, PositionEvaluator positionEvaluator, int n, boolean randomize) {
        super(initialPosition, color, id, ui);
        this.depth = depth;
        this.randomize = randomize;

        this.evaluator = new Evaluator(color, positionEvaluator, n);
    }

    public PositionEvaluator getPositionEvaluator() {
        return evaluator.getPositionEvaluator();
    }

    @Override
    public Move getMove() {
        List<Move> moves = board.getFirstNFittingMoves(evaluator.getN(), color);
        System.out.println(moves);


        List<MoveAndScore> moveScores = new Vector<>();
        System.out.println("Found " + moves.size() + " moves as " + id);

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
                    return 0;
                } else {
                    return moveAndScore.getScore() - t1.getScore() < 0 ? 1 : moveAndScore.getScore() - t1.getScore() == 0 ? 0 : -1;
                }
            }
        });



        System.out.println(moveScores);



        MoveAndScore bestMove;

        if (randomize) {
            try {
                bestMove = moveScores.get(new Random().nextInt(2));
            } catch (IndexOutOfBoundsException e) {
                bestMove = moveScores.get(0);
            }
        } else {
            System.out.println(moveScores.get(0).getMove() + ", " + moveScores.get(0).getScore());
            bestMove = moveScores.get(0);
        }



        System.out.println("Parameters: " + evaluator.getPositionEvaluator().evaluatePosition(board, color, true));

        showChainOfDeduction(bestMove.getMove());
        return  bestMove.getMove();

    }

    private void showChainOfDeduction (Move move) {
        Board position = evaluator.getFinalPosition(move);
//        ui.updateValues(position, (color + depth + 1) % 2, moveCount + depth + 1);
//        ui.commit();
        ui.overlay(position);
        new Scanner(System.in).nextLine();
        ui.clearOverlay();
    }

    public Evaluator getEvaluator() {
        return evaluator;
    }

    protected List<MoveAndScore> getMoveCallBack(List<Move> possibleMoves, Board board, int depth) {
//        Map<double, Move> moveScores = new HashMap<>();
        List<MoveAndScore> moveScores = new Vector<>();

        for (Move move : possibleMoves) {
            board.putOnBoard(move);
            double score = evaluator.evaluateMove(board, depth, move);
            board.undo(0);
//            moveScores.put(score, move);
            moveScores.add(new MoveAndScore(move, true, true, score));
        }

        return moveScores;

    }

    public Board getBoard () {
        return board;
    }

}
