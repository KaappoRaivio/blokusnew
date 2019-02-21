import ais.twoplayerai.MyPositionEvaluator;
import ais.twoplayerai.TwoPlayerAi;
import blokus.*;
import uis.UI;
import uis.fancyttyui.FancyTtyUI;

public class Main {
    public static void main(String[] args) {


//        Board board = new Board(14, 14, new MyPieceManager(4), true);
        Board board = Board.ORIGINAL_BOARD;
//        Board board = Board.fromFile("/home/kaappo/git/blokusnew/src/main/resources/boards/Wed Feb 13 21:52:36 EET 2019.ser", false);

        UI ui = new FancyTtyUI(board.deepCopy(), 2, 2);
        ui.commit();

        int depth = 2;


        TwoPlayerAi twoPlayerAi0 = new TwoPlayerAi(board.deepCopy(), 0, "color 0", ui, depth, MyPositionEvaluator.EVALUATOR_0, 30, false);
        TwoPlayerAi twoPlayerAi1 = new TwoPlayerAi(board.deepCopy(), 1, "color 1", ui, depth, MyPositionEvaluator.EVALUATOR_1, 30, false);

        Spectator[] spectators = new Spectator[]{
                new MoveAnalyzer(twoPlayerAi0, twoPlayerAi1)
        };

        CapableOfPlaying[] players = new CapableOfPlaying[]{
//                new Player(board , 0, null, ui),
//                new RandomAi(board, 0, null, ui),
//                new Player(board, 0, null, ui),
//                twoPlayerAi0,
//                twoPlayerAi1,
//                new Player(board, 1, null, ui)
                new Player(board, 0, null, ui),
                new Player(board, 1, null, ui),
                new Player(board, 2, null, ui),
                new Player(board, 3, null, ui),
        };

        Runner runner = new Runner(board, players, spectators, ui);

        try {
            runner.play();
        } catch (Exception e) {
            runner.getGameHistory().save();
            runner.getBoard().save();
            System.out.println(runner.getGameHistory());
            ui.close();
            throw e;
        }
        runner.getGameHistory().save();
        runner.getBoard().save();

    }
}
