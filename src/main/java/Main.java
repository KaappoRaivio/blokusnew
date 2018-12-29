import blokus.*;
import listener.Key;
import listener.KeyListener;
import uis.TtyUITest;

public class Main {
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    public static void main (String[] args) {
        Board board = new Board(14, 14, new MyPieceManager(2));
        TtyUITest ttyUITest = new TtyUITest(board, 0);
        ttyUITest.updateTurn(0);
        ttyUITest.update();
        var move = ttyUITest.getMove();
//        board.putOnBoard(new Move(0, 0, PieceID.PIECE_19, 0, Orientation.LEFT, false));
        board.putOnBoard(move);
        ttyUITest.updateBoard(board.deepCopy());
        ttyUITest.updateMoveCounter();
        ttyUITest.updateTurn(1);
        ttyUITest.update();

//        Piece.getAllPieces(0).forEach((a) -> System.out.println(new Piece(a, 0) + " " + a));
    }

}
