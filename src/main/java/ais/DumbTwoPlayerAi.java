package ais;

import blokus.Board;
import blokus.CapableOfPlaying;
import blokus.Move;
import blokus.Player;

import java.util.List;
import java.util.Random;

public class DumbTwoPlayerAi extends Player {

    public DumbTwoPlayerAi(Board initialPosition, int color, String id) {
        super(initialPosition, color, id);
    }

    @Override
    public Move getMove() {
        List<Move> moves = board.getAllFittingMoves(color);

//        Move move = moves.get(new Random().nextInt(moves.size()));
        Move move = moves.get(0);
        System.out.println("Color " + color + ", move: " + move.toString());
        return move;
    }

//    @Override
//    public void updateValues(Board board, int turn, int moveCount) {
//        this.board = board;
//        this.turn = turn;
//        this.moveCount = moveCount;
//    }

//    @Override
//    public int getColor() {
//        return color;
//    }
}
