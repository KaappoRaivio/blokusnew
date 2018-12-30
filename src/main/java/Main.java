import blokus.*;
import uis.TtyUITest;

public class Main {
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    public static void main (String[] args) {
        Board board = new Board(14, 14, new MyPieceManager(2));


        CapableOfPlaying[] players = {new Player(board, 0, "Player 0"), new Player(board, 1, "Player 1")};

        Runner runner = new Runner(board, players, new TtyUITest(board));
        runner.play();
    }

}
