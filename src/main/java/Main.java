import ais.randomai.RandomAi;
import ais.twoplayerai.Evaluator;
import ais.twoplayerai.TwoPlayerAi;
import blokus.*;
import uis.UI;
import uis.fancyttyui.FancyTtyUI;

public class Main {
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {


        Board board = new Board(14, 14, new MyPieceManager(2));
//        Board board = Board.fromFile("/home/kaappo/git/blokus/src/main/resources/boards/Fri Feb 08 10:33:17 EET 2019.ser", false);

        UI ui = new FancyTtyUI(board.deepCopy(), 2, 2);
        ui.commit();
//
//        synchronized (Thread.currentThread()) {
//            try {
////                System.out.println(board.hasMoves( 0));
//                Evaluator evaluator1 = new Evaluator(0, 0.5f, 1.0f, 5.0f, 2.0f, 400, ui);
//                Evaluator evaluator2 = new Evaluator(1, 0.5f, 1.0f, 5.0f, 2.0f, 400, ui);
//                System.out.println(evaluator1.evaluatePosition(board));
//                System.out.println(evaluator2.evaluatePosition(board));
//                Thread.currentThread().wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        int depth = 2;


//        CapableOfPlaying[] players = new CapableOfPlaying[]{
//                new Player(board.deepCopy(), 0, "human", ui),
//                new TwoPlayerAi(board.deepCopy(), 1, "ais", ui, depth, new Evaluator(1, 0.5f, 1.0f, 5.0f, 10.0f, 50, ui), false)
//        };

//        CapableOfPlaying[] players = new CapableOfPlaying[]{
//
//                new Player(board.deepCopy(), 0, null, ui),
//                new Player(board.deepCopy(), 1, null, ui),
////                new TwoPlayerAi(board.deepCopy(), 1, "color 1", ui, depth, new Evaluator(1, 3f, 1.0f, 50000.0f, 80000.0f, 400, ui), false),
////                new RandomAi(board.deepCopy(), 0, "bad", ui),
////                new TwoPlayerAi(board.deepCopy(), 1, "good (hopefully)", ui, depth, new Evaluator(1, 0.5f, 1.0f, 5.0f, 10.0f, 400, ui), true)
////                new RandomAi(board.deepCopy(), 1, null, ui)
//        };

        TwoPlayerAi twoPlayerAi0 = new TwoPlayerAi(board.deepCopy(), 0, "color 0", ui, depth, new Evaluator(0, 1, 1.0, 0.2, 0.1,10,  10, ui), false);
        TwoPlayerAi twoPlayerAi1 = new TwoPlayerAi(board.deepCopy(), 1, "color 1", ui, depth, new Evaluator(1, 1, 1.0, 0.2, 0.1, 20, 40, ui), false);

        Spectator[] spectators = new Spectator[]{
                new MoveAnalyzer(twoPlayerAi0, twoPlayerAi1)
        };

        CapableOfPlaying[] players = new CapableOfPlaying[]{
//                new Player(board , 0, null, ui),
//                new RandomAi(board, 0, null, ui),
//                new Player(board, 0, null, ui),
                twoPlayerAi1,
                twoPlayerAi0,
//                new Player(board, 1, null, ui)
        };
//
//        CapableOfPlaying[] players = new CapableOfPlaying[]{
//                new TwoPlayerAi(board.deepCopy(), 0, "Player 0", ui, depth, new Evaluator(0, 10.0f, 10.0f, 5.0f, 6, ), false),
//                new TwoPlayerAi(board.deepCopy(), 1, "PLayer 1", ui, depth, new Evaluator(1, 10.0f, 10.0f, 5.0f, 6), false)
//        };
//        CapableOfPlaying[] players = new CapableOfPlaying[]{new Player(board.deepCopy(), 0, null, ui), new Player(board.deepCopy(), 1, null, ui)};
//        CapableOfPlaying[] players = new CapableOfPlaying[]{}

        Runner runner = new Runner(board, players, spectators, ui);
//        runner.getGameHistory().save();
//        GameHistory gameHistory = new Saver<GameHistory>().fromFile("/home/kaappo/git/blokus/src/main/resources/games/Wed Feb 06 12:34:50 EET 2019.ser", false);
//        System.out.println(gameHistory);
//        System.exit(0);
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
//        ui.close();

    }
}
