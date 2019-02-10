package blokus;

import ais.twoplayerai.TwoPlayerAi;
import uis.UI;

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
        ai1.getEvaluator().evaluatePosition(board, true);
        ai2.getEvaluator().evaluatePosition(board, true);
    }
}
