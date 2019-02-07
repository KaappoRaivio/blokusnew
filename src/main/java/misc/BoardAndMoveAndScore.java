package misc;

import blokus.Board;

public class BoardAndMoveAndScore {
    private Board board;
    private MoveAndScore moveAndScore;

    @Override
    public String toString() {
        return "BoardAndMoveAndScore{" +
                "board=" + board +
                ", moveAndScore=" + moveAndScore +
                '}';
    }

    public Board getBoard() {
        return board;
    }

    public MoveAndScore getMoveAndScore() {
        return moveAndScore;
    }

    public BoardAndMoveAndScore(Board board, MoveAndScore moveAndScore) {
        this.board = board;
        this.moveAndScore = moveAndScore;
    }
}
