package misc;

import blokus.Move;

import java.util.Objects;

public class MoveAndScore {
    private Move move;
    private boolean isMovePresent;
    private boolean isScorePresent;
    private double score;

    public MoveAndScore (Move move) {
        if (move == null) {
            throw new RuntimeException("Move can't be null in this context!");
        }

        this.move = move;
        this.isMovePresent = true;
        this.isScorePresent = false;
        this.score = 0;

    }

    public Move getMove() {
        return move;
    }

    public boolean isMovePresent() {
        return isMovePresent;
    }

    public boolean isScorePresent() {
        return isScorePresent;
    }

    public double getScore() {
        return score;
    }

    public MoveAndScore(Move move, boolean isScorePresent, boolean isMovePresent, double score) {
        this.move = move;
        this.isScorePresent = isScorePresent;
        this.isMovePresent = isMovePresent;
        this.score = score;
    }

    @Override
    public String toString() {
        return "MoveAndScore{" +
                "move=" + move +
                ", score=" + score +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveAndScore that = (MoveAndScore) o;
        return isMovePresent == that.isMovePresent &&
                isScorePresent == that.isScorePresent &&
                Double.compare(that.score, score) == 0 &&
                move.equals(that.move);
    }

    @Override
    public int hashCode() {
        return Objects.hash(move, isMovePresent, isScorePresent, score);
    }
}
