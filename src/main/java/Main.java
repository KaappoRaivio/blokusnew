import listener.Key;
import listener.KeyListener;

public class Main {
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    public static void main (String[] args) {
        Board board = new Board(14, 14, new MyPieceManager(2));


        board.putOnBoard(0, 0, PieceID.PIECE_13, 0, Orientation.UP, true);
        board.putOnBoard(3, 1, PieceID.PIECE_14, 0, Orientation.LEFT, false);
        System.out.println(board);

        KeyListener keyListener = new KeyListener();
        Thread thread = new Thread(keyListener);
        thread.start();

        System.out.println("as");

        while (true) {
            Key key = keyListener.getKey();
            System.out.println(key);

        }

    }

}
