import ais.twoplayerai.TwoPlayerAi;
import blokus.*;
import uis.TtyUITest;

public class Main {
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    public static void main (String[] args) {
        Board board = new Board(14, 14, new MyPieceManager(2), false, 4);

        CapableOfPlaying[] players = new CapableOfPlaying[]{new Player(board.deepCopy(), 0, null), new TwoPlayerAi(board.deepCopy(), 1, null)};

        Runner runner = new Runner(board, players, new TtyUITest(board.deepCopy()));

        try {
            runner.play();
        } catch (Exception e) {
            runner.getBoard().save();
            throw e;
        }



//        long aikaAlussa = System.currentTimeMillis();
//        List<Move> moves = board.getAllFittingMoves(0);
//        long aikaLopussa = System.currentTimeMillis();
//
//        System.out.println(aikaLopussa - aikaAlussa);


//        for (Move move : moves) {
//            board.putOnBoard(move);
//            System.out.println(board);
//            board.undo(0);
//        }

    }

}
