import ais.DumbTwoPlayerAi;
import ais.TwoPlayerAi;
import blokus.*;
import uis.TtyUITest;
import uis.UI;

import java.util.List;

public class Main {
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    public static void main (String[] args) {
        Board board = new Board(14, 14, new MyPieceManager(2), false, 4);
//
//        CapableOfPlaying[] players = new CapableOfPlaying[]{new Player(board.deepCopy(), 0, null), new TwoPlayerAi(board.deepCopy(), 1, null)};
//
//        Runner runner = new Runner(board, players, new TtyUITest(board.deepCopy()));
//        runner.play();
        Piece piece = new Piece(PieceID.fromStandardNotation("P"), 0);

//        piece.getAllOrientations().forEach(System.out::println);
//        System.exit(0);


        long aikaAlussa = System.currentTimeMillis();
        List<Move> moves = board.getAllFittingMoves(0);
        long aikaLopussa = System.currentTimeMillis();

        System.out.println(aikaLopussa - aikaAlussa);


//        for (Move move : moves) {
//            board.putOnBoard(move);
//            System.out.println(board);
//            board.undo(0);
//        }

    }

}
