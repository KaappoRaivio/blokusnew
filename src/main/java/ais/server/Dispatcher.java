package ais.server;

import blokus.*;
import uis.UI;
import uis.fancyttyui.FancyTtyUI;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Dispatcher extends Player {
    private Socket socket;

    public Dispatcher(Board board, int color, String id, UI ui, String hostname, int port) {
        super(board, color, id, ui);

        try {
            this.socket = new Socket(hostname, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateValues(Board board, int turn, int moveCount) {
        super.updateValues(board, turn, moveCount);

        ObjectOutputStream outputStream;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            outputStream.writeObject(new GameValues(board, turn, moveCount));
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Board board = Board.DUO_BOARD;

        board.putOnBoard(new Move(4, 4, PieceID.PIECE_1, 0, Orientation.UP, false));

        new Dispatcher(board, 0, null, new FancyTtyUI(board, 1, 1), "localhost", 1212).updateValues(board, 3, 2);
    }
}
