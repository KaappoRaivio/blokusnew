import ais.TwoPlayerAi;
import blokus.*;
import uis.TtyUITest;

import java.util.stream.Collectors;

public class Main {
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    public static void main (String[] args) {
        Board board = new Board(14, 14, new MyPieceManager(2));

        while (board.hasMoves(0)) {
            Move move = board.getAllFittingMoves(0).get(0);
            board.putOnBoard(move);
            System.out.println(board);
            System.out.println(move);
            System.out.println(board.getPieceManager().getPiecesNotOnBoard(0).stream().map(Enum::toString).collect(Collectors.joining(", ")));
        }


//        CapableOfPlaying[] players = {new Player(board, 0, "Player 0"), new TwoPlayerAi(board, 1, "Player 1")};
//
//        Runner runner = new Runner(board, players, new TtyUITest(board));
//        runner.play();
    }

}
