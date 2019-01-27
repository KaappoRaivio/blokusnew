import ais.twoplayerai.TwoPlayerAi;
import blokus.*;
import uis.fancyttyui.FancyTtyUI;
import uis.TtyUITest;
import uis.UI;

public class Main {
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    public static void main (String[] args) {


        Board board = new Board(14, 14, new MyPieceManager(2));
//        Board board = Board.fromFile("/home/kaappo/git/blokus/src/main/resources/boards/1.16234178E12.ser", false);

        UI ui = new FancyTtyUI(board.deepCopy());

        CapableOfPlaying[] players = new CapableOfPlaying[]{new Player(board.deepCopy(), 0, null, ui), new TwoPlayerAi(board.deepCopy(), 1, null, ui,3)};

        Runner runner = new Runner(board, players, ui);



        try {
            runner.play();
        } catch (Exception e) {
            runner.getBoard().save();
            throw e;
        }









    }

}
