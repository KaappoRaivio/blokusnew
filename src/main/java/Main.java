import ais.TwoPlayerAi;
import blokus.*;
import uis.TtyUITest;

import java.util.stream.Collectors;

public class Main {
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    public static void main (String[] args) {
        Board board = new Board(14, 14, new MyPieceManager(2));
//        Move move = new Move(7, 10, PieceID.fromStandardNotation("I5"), 0, Orientation.UP, false);
//        System.out.println(board.fits(move));
//        board.putOnBoard(move);
//        System.out.println(board);

//        board.putOnBoard(move);
//        System.out.println(board);

        Player[] players = {new TwoPlayerAi(board.deepCopy(), 0, "Blue ai"), new TwoPlayerAi(board.deepCopy(), 1, "Red ai")};

        Runner runner = new Runner(board.deepCopy(), players, new TtyUITest(board.deepCopy()));
        runner.play();

//        while (board.hasMoves(0)) {
//            Move move = board.getAllFittingMoves(0).get(0);
////            System.out.println(board.fits(move));
//            board.putOnBoard(move);
////            System.out.println(move);
//            System.out.println(board);
//
////            System.out.println(board.getPieceManager().getPiecesNotOnBoard(0).stream().map(Enum::toString).collect(Collectors.joining(", ")));
//        }


//        CapableOfPlaying[] players = {new Player(board, 0, "Player 0"), new TwoPlayerAi(board, 1, "Player 1")};
//
//        Runner runner = new Runner(board, players, new TtyUITest(board));
//        runner.play();
    }

}
