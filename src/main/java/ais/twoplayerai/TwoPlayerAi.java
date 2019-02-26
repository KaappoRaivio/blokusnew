package ais.twoplayerai;

import blokus.Board;
import blokus.Move;
import blokus.Player;
import misc.OnedSpan;
import misc.Pair;
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

        evaluator = new Evaluator(color, positionEvaluator, n);
    }

    public PositionEvaluator getPositionEvaluator() {
        return evaluator.getPositionEvaluator();
    }

    @Override
    public Move getMove() {
        List<Move> moves = board.getFirstNFittingMoves(evaluator.getN(), color);
        System.out.println(moves);


        List<Pair<Move, Double>> moveScores = new Vector<>();
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
                System.out.println(Collections.max(thread.getResult(), (moveAndScore, t1) -> {
                    if (moveAndScore.getV() - t1.getV() < 0) {
                        return -1;
                    } else if (moveAndScore.getV() - t1.getV() > 0) {
                        return 1;
                    } else {
                        return 0;
                    }
                }));
                moveScores.addAll(thread.getResult());

            }
        }

        moveScores.sort((moveDoublePair, t1) -> moveDoublePair.getV() - t1.getV() < 0 ? 1 : moveDoublePair.getV() - t1.getV() == 0 ? 0 : -1
        );



        System.out.println(moveScores);



        Pair<Move, Double> bestMove;

        if (randomize) {
            try {
                bestMove = moveScores.get(new Random().nextInt(2));
            } catch (IndexOutOfBoundsException e) {
                bestMove = moveScores.get(0);
            }
        } else {
            System.out.println(moveScores.get(0).getK() + ", " + moveScores.get(0).getV());
            bestMove = moveScores.get(0);
        }



        System.out.println("Parameters: " + evaluator.getPositionEvaluator().evaluatePosition(board, color, true));

        showChainOfDeduction(bestMove.getK());
        return  bestMove.getK();
    }

    private void showChainOfDeduction (Move move) {
        Board position = evaluator.getFinalPosition(move);
//        ui.updateValues(position, (color + depth + 1) % 2, moveCount + depth + 1);
//        ui.update();
        ui.overlay(position);
//        new Scanner(System.in).nextLine();
        ui.clearOverlay();
    }

    public Evaluator getEvaluator() {
        return evaluator;
    }

    List<Pair<Move, Double>> getMoveCallBack(List<Move> possibleMoves, Board board, int depth) {
//        Map<double, Move> moveScores = new HashMap<>();
        List<Pair<Move, Double>> moveScores = new Vector<>();

        for (Move move : possibleMoves) {
            board.putOnBoard(move);
            double score = evaluator.evaluateMove(board, depth, move);
            board.undo(0);
//            moveScores.put(score, move);
            moveScores.add(new Pair<>(move, score));
        }

        return moveScores;

    }

    public Board getBoard () {
        return board;
    }

}
