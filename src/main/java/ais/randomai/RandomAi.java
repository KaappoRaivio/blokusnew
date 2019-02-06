package ais.randomai;

import blokus.Board;
import blokus.CapableOfPlaying;
import blokus.Move;
import blokus.Player;
import uis.UI;

import java.util.List;
import java.util.Random;

public class RandomAi implements CapableOfPlaying {
    private int turn;
    private Board board;
    private int color;
    private final String id;
    private final UI ui;
    private int moveCount;

    public RandomAi(Board board, int color, String id, UI ui) {
//        super(board, color, id, ui);
        this.board = board;
        this.color = color;
        this.id = id;
        this.ui = ui;
        this.turn = -1;
        this.moveCount = -1;
    }

    @Override
    public Move getMove() {
        List<Move> moves = board.getAllFittingMoves(color);
//        return moves.get(new Random().nextInt(moves.size()));
        return moves.get(moves.size() -1);
    }

    @Override
    public void updateValues(Board board, int turn, int moveCount) {
        this.board = board;
        this.turn = turn;
        this.moveCount = moveCount;
    }

    @Override
    public int getColor() {
        return color;
    }
}
