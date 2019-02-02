import ais.twoplayerai.TwoPlayerAi;
import blokus.*;
import listener.KeyEventListener;
import org.jnativehook.keyboard.NativeKeyEvent;
import uis.TtyUITest;
import uis.UI;
import uis.fancyttyui.FancyTtyUI;

public class Main {
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    public static void main (String[] args) {

//
        Board board = new Board(14, 14, new MyPieceManager(2));
        board.putOnBoard(new Move(0, 0, PieceID.fromStandardNotation("V5"), 0, Orientation.UP, false));
        board.putOnBoard(new Move(11, 11, PieceID.fromStandardNotation("V5"), 1, Orientation.UP, false));
        board.save();
        System.out.println(board);
        System.exit(0);


//        Board board = Board.fromFile("/home/kaappo/git/blokus/src/main/resources/boards/1.16234178E12.ser", false);

        UI ui = new TtyUITest(board.deepCopy());

        CapableOfPlaying[] players = new CapableOfPlaying[]{new Player(board.deepCopy(), 0, null, ui), new TwoPlayerAi(board.deepCopy(), 1, null, ui,3)};

        Runner runner = new Runner(board, players, ui);



        try {
            runner.play();
        } catch (Exception e) {
            runner.getBoard().save();
            throw e;
        }
        System.out.println(new KeyEventListener() {
            @Override
            public void reportKey(NativeKeyEvent event) {
//                switch (key) {
//                    case NO_KEY:
//                        break;
//                    case LEFT:
//                        sprite.jump(2, 0);
//                        break;
//                    case RIGHT:
//                        sprite.jump(-2, 0);
//                        break;
//                    case DOWN:
//                        sprite.jump(0, 1);
//                        break;
//                    case UP:
//                        sprite.jump(0, -1);
//                        break;
//                    case ENTER:
//                        break;
//                    case CTRL:
//                        break;
//                    case SHIFT:
//                        break;
//                }
//                screen.commit();
            }
        });









    }

}
