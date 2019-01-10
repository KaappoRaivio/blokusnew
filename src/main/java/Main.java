import ais.DumbTwoPlayerAi;
import blokus.*;
import uis.TtyUITest;

public class Main {
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    public static void main (String[] args) {
        Board board = new Board(14, 14, new MyPieceManager(2), false, 4);

        

    }

}
