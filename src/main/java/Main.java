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

    public static void main (String[] args) {
//        Board board = new Board(14, 14, new MyPieceManager(2));
        Board board = Board.fromFile("/home/kaappo/git/blokus/src/main/resources/boards/Sun Feb 03 22:12:25 EET 2019.ser", false);

        UI ui = new FancyTtyUI(board.deepCopy());


//        CapableOfPlaying[] players = new CapableOfPlaying[]{new Player(board.deepCopy(), 0, null, ui), new TwoPlayerAi(board.deepCopy(), 1, null, ui,2)};
        CapableOfPlaying[] players = new CapableOfPlaying[]{new TwoPlayerAi(board.deepCopy(), 0, null, ui, 2, new Evaluator(0, 10.0f, 10.0f, 4.0f)), new TwoPlayerAi(board.deepCopy(), 1, null, ui, 2, new Evaluator(1, 10.0f, 10.0f, 8.0f))};
//        CapableOfPlaying[] players = new CapableOfPlaying[]{new Player(board.deepCopy(), 0, null, ui), new Player(board.deepCopy(), 1, null, ui)};

        Runner runner = new Runner(board, players, ui);

        try {
            runner.play();
        }
        catch (Exception e) {
            runner.getBoard().save();
            throw e;
        }
        runner.getBoard().save();

    }
}
