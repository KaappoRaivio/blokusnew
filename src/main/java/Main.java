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

        UI ui = new FancyTtyUI(board.deepCopy(), 3, 3);

        int depth = 2;


//        CapableOfPlaying[] players = new CapableOfPlaying[]{
//                new Player(board.deepCopy(), 0, "human", ui),
//                new TwoPlayerAi(board.deepCopy(), 1, "ais", ui, depth, new Evaluator(1, 0.5f, 1.0f, 5.0f, 10.0f, 50, ui), false)
//        };

        CapableOfPlaying[] players = new CapableOfPlaying[]{
                new TwoPlayerAi(board.deepCopy(), 0, "good (hopefully)", ui, depth, new Evaluator(0, 0.5f, 1.0f, 5.0f, 10.0f, 70, ui), false),
//                new RandomAi(board.deepCopy(), 0, "bad", ui),
                new TwoPlayerAi(board.deepCopy(), 1, "good (hopefully)", ui, depth, new Evaluator(1, 0.5f, 1.0f, 5.0f, 10.0f, 70, ui), false)
        };
//
//        CapableOfPlaying[] players = new CapableOfPlaying[]{
//                new TwoPlayerAi(board.deepCopy(), 0, "Player 0", ui, depth, new Evaluator(0, 10.0f, 10.0f, 5.0f, 6, ui)),
//                new TwoPlayerAi(board.deepCopy(), 1, "PLayer 1", ui, depth, new Evaluator(1, 10.0f, 10.0f, 5.0f, 6, ui))
//        };
//        CapableOfPlaying[] players = new CapableOfPlaying[]{new Player(board.deepCopy(), 0, null, ui), new Player(board.deepCopy(), 1, null, ui)};

        Runner runner = new Runner(board, players, ui);
//        runner.getGameHistory().save();
//        GameHistory gameHistory = new Saver<GameHistory>().fromFile("/home/kaappo/git/blokus/src/main/resources/games/Wed Feb 06 12:34:50 EET 2019.ser", false);
//        System.out.println(gameHistory);
//        System.exit(0);
        try {
            runner.play();
        } catch (Exception e) {
            runner.getGameHistory().save();
            System.out.println(runner.getGameHistory());
            ui.close();
            throw e;
        }
        runner.getGameHistory().save();

    }
}
