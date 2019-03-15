package ais.server;

import blokus.Board;
import blokus.Player;
import uis.UI;

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
}
