package blokus;

import uis.UI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Runner {
    private Board board;
    private CapableOfPlaying[] players;
    private UI ui;

    public Runner (Board board, CapableOfPlaying[] players, UI ui) {
        if (board.getAmountOfPlayers() != players.length) {
            throw new RuntimeException("Board is initialized with " + board.getAmountOfPlayers() + " players but there are only " + players.length + " players!");
        }

        this.board = board;
        this.players = players;
        this.ui = ui;
    }

    public void play () {
        int moveCount = 0;
        int turn = 0;

        boolean[] lost = new boolean[board.getAmountOfPlayers()];

        while (true) {
            if (!board.hasMoves(turn)) {
                lost[turn] = true;
            }

            if (!canPlay(lost)) {
                break;
            }

            if (lost[turn]) {
                turn = (turn + 1) % board.getAmountOfPlayers();
                moveCount += 1;
                updateAllPlayerValues(board, turn, moveCount);
                continue;

            }

            updateAllPlayerValues(board, turn, moveCount);

            CapableOfPlaying current = players[turn];
            ui.updateValues(board.deepCopy(), turn, moveCount);
            ui.commit();




            Move move = current.getMove();
            if (!board.fits(move)) {
                System.out.println("Move " + move + " doesn't fit!");
                continue;
            }

            board.putOnBoard(move);

            turn = (turn + 1) % board.getAmountOfPlayers();
            moveCount += 1;
        }



        List<Integer> finalScores = new ArrayList<>();

        for (int color = 0; color < board.getAmountOfPlayers(); color++) {
            int total = 0;
            for (PieceID pieceID : board.getPieceManager().getPiecesNotOnBoard(color)) {
                Piece piece = new Piece(pieceID, color);

                total += piece.getAmountOfSquares();
            }

            finalScores.add(total);
        }

        int winner = finalScores.indexOf(Collections.min(finalScores));

        System.out.println("Color " + winner + " won with " + finalScores.get(winner) + " points!");



    }

    private boolean canPlay (boolean[] lost) {
        boolean onePlayerAlive = false;

        for (boolean b : lost) {
            if (!b && !onePlayerAlive) {
//                return true;
                onePlayerAlive = true;

            } else if (!b) {
                return true;
            }
        }

        return false;
    }

    public void updateAllPlayerValues (Board board, int turn, int moveCount) {
        Arrays.stream(players).forEach((item) -> item.updateValues(board.deepCopy(), turn, moveCount));
    }

    public String save (String name) {
        String path = System.getProperty("user.dir") + "/src/main/resources/games/" + name + ".ser";

        File file = new File(path);

        try {
            if (file.createNewFile()) {
                System.out.println("Creating new file " + path);

            } else {
                System.out.println("File " + path + " already exists");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            FileOutputStream fileOut = new FileOutputStream(path);

            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();

            fileOut.close();

            System.out.println("Saved board to: " + path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return path;
    }

    public Board getBoard() {
        return board;
    }
}
