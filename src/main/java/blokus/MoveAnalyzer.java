package blokus;

import ais.twoplayerai.TwoPlayerAi;

public class MoveAnalyzer implements Spectator {
    private TwoPlayerAi ai1;
    private TwoPlayerAi ai2;

//    public MoveAnalyzer(Board board, UI ui) {
//        this(new TwoPlayerAi(board.deepCopy(), 0, null, ui), );
//    }

    public MoveAnalyzer(TwoPlayerAi ai1, TwoPlayerAi ai2) {
        this.ai1 = ai1;
        this.ai2 = ai2;
    }

    @Override
    public void updateValues(Board board, int turn, int moveCount) {
        ai1.getEvaluator().getPositionEvaluator().evaluatePosition(board, ai1.getColor(), true);
        ai2.getEvaluator().getPositionEvaluator().evaluatePosition(board, ai2.getColor(), true);
    }
}
