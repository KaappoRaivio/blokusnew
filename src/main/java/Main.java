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
        Board board = new Board(14, 14, new MyPieceManager(2));

        UI ui = new FancyTtyUI(board.deepCopy());


        CapableOfPlaying[] players = new CapableOfPlaying[]{new Player(board.deepCopy(), 0, null, ui), new TwoPlayerAi(board.deepCopy(), 1, null, ui,2)};

        Runner runner = new Runner(board, players, ui);

        try {
            runner.play();
        } catch (Exception e) {
            runner.getBoard().save();
            throw e;
        }
    }
}
