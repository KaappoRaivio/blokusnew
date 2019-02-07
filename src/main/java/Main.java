import ais.randomai.RandomAi;
import ais.twoplayerai.Evaluator;
import ais.twoplayerai.TwoPlayerAi;
import blokus.*;
import uis.UI;
import uis.fancyttyui.FancyTtyUI;

public class Main {
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {


//        Board board = new Board(14, 14, new MyPieceManager(2));
        Board board = Board.fromFile("/home/kaappo/git/blokus/src/main/resources/boards/Thu Feb 07 10:21:42 EET 2019.ser", false);

        UI ui = new FancyTtyUI(board.deepCopy(), 1, 1);
        ui.commit();
//        System.exit(0);
        try {
//            Thread.sleep(10000);
            System.out.println(board.putOnBoard(new Move(8, 8, PieceID.fromStandardNotation("L4"), 0, Orientation.DOWN, false)));
            System.out.println(board);
//            System.out.println(board.getFirstNFittingMoves(400, 0));
//            System.out.println(board.getAllFittingMoves(0));
//            System.out.println(board.getPieceManager().getPiecesNotOnBoard(0));
//            System.out.println(board.canPlay());
            synchronized (Thread.currentThread()) {
                Thread.currentThread().wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int depth = 7;


//        CapableOfPlaying[] players = new CapableOfPlaying[]{
//                new Player(board.deepCopy(), 0, "human", ui),
//                new TwoPlayerAi(board.deepCopy(), 1, "ais", ui, depth, new Evaluator(1, 0.5f, 1.0f, 5.0f, 10.0f, 50, ui), false)
//        };

//        CapableOfPlaying[] players = new CapableOfPlaying[]{
//                new TwoPlayerAi(board.deepCopy(), 1, "color 1", ui, depth + 3, new Evaluator(1, 0.5f, 1.0f, 500.0f, 800.0f, 400, ui), false),
//                new TwoPlayerAi(board.deepCopy(), 0, "color 0", ui, depth, new Evaluator(0, 0.5f, 1.0f, 500.0f, 800.0f, 400, ui), false),
//                new RandomAi(board.deepCopy(), 0, "bad", ui),
//                new TwoPlayerAi(board.deepCopy(), 1, "good (hopefully)", ui, depth, new Evaluator(1, 0.5f, 1.0f, 5.0f, 10.0f, 400, ui), true)
//                new RandomAi(board.deepCopy(), 1, null, ui)
//        };
//
//        CapableOfPlaying[] players = new CapableOfPlaying[]{
//                new TwoPlayerAi(board.deepCopy(), 0, "Player 0", ui, depth, new Evaluator(0, 10.0f, 10.0f, 5.0f, 6, ui)),
//                new TwoPlayerAi(board.deepCopy(), 1, "PLayer 1", ui, depth, new Evaluator(1, 10.0f, 10.0f, 5.0f, 6, ui))
//        };
        CapableOfPlaying[] players = new CapableOfPlaying[]{new Player(board.deepCopy(), 0, null, ui), new Player(board.deepCopy(), 1, null, ui)};
//        CapableOfPlaying[] players = new CapableOfPlaying[]{}

        Runner runner = new Runner(board, players, ui);
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

    }
}
