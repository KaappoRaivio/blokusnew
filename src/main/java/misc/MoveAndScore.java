package misc;

import blokus.Move;

public class MoveAndScore {
    private Move move;
    private boolean isMovePresent;
    private boolean isScorePresent;
    private float score;

    public MoveAndScore(Move move) {
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

    public float getScore() {
        return score;
    }

    public MoveAndScore(Move move, boolean isScorePresent, boolean isMovePresent, float score) {
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
}
