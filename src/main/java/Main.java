import ais.DumbTwoPlayerAi;
import blokus.*;
import uis.TtyUITest;

public class Main {
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    public static void main (String[] args) {
        Board board = new Board(14, 14, new MyPieceManager(2), false, 4);

        board.putOnBoard(new Move(0, 0, PieceID.PIECE_19, 0, Orientation.RIGHT, false));
        System.out.println(board);
        board.putOnBoard(2, 3, PieceID.fromStandardNotation("W"), 0, Orientation.LEFT, false);
        System.out.println(board);
        board.undo(0);
        System.out.println(board);
        board.putOnBoard(new Move(2, 3, PieceID.fromStandardNotation("W"), 0, Orientation.LEFT, false));
        System.out.println(board);


    }

}
