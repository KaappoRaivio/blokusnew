import ais.twoplayerai.Evaluator;
import ais.twoplayerai.TwoPlayerAi;
import blokus.*;
import listener.KeyEventListener;
import org.jnativehook.keyboard.NativeKeyEvent;
import uis.TtyUITest;
import uis.UI;
import uis.fancyttyui.FancyTtyUI;

public class Main {
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
        Board board = new Board(14, 14, new MyPieceManager(2));
//        Board board = Board.fromFile("/home/kaappo/git/blokus/src/main/resources/boards/Mon Feb 04 13:20:45 EET 2019.ser", false);

        UI ui = new FancyTtyUI(board.deepCopy());

        int depth = 2;
//        CapableOfPlaying[] players = new CapableOfPlaying[]{new Player(board.deepCopy(), 0, null, ui), new TwoPlayerAi(board.deepCopy(), 1, null, ui,2)};
        CapableOfPlaying[] players = new CapableOfPlaying[]{
                new TwoPlayerAi(board.deepCopy(), 0, "Player 0", ui, depth, new Evaluator(0, 10.0f, 10.0f, 5.0f, 4)),
                new TwoPlayerAi(board.deepCopy(), 1, "PLayer 1", ui, depth, new Evaluator(1, 10.0f, 10.0f, 5.0f, 4))
        };
//        CapableOfPlaying[] players = new CapableOfPlaying[]{new Player(board.deepCopy(), 0, null, ui), new Player(board.deepCopy(), 1, null, ui)};

        Runner runner = new Runner(board, players, ui);


        try {
            runner.play();
        } catch (Exception e) {
            runner.getBoard().save();
            throw e;
        }
        runner.getBoard().save();

    }
}
