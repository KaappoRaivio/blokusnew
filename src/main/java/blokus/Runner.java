package blokus;

import misc.Saver;
import uis.UI;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Runner {
    private Board board;
    private CapableOfPlaying[] players;
    private UI ui;
    private GameHistory gameHistory;
    private Spectator[] spectators;

    public Runner (Board board, CapableOfPlaying[] players, Spectator[] spectators, UI ui) {
        this.spectators = spectators;
        if (board.getAmountOfPlayers() != players.length) {
            throw new RuntimeException("Board is initialized with " + board.getAmountOfPlayers() + " players but there are only " + players.length + " players!");
        }

        this.board = board;
        this.players = players;
        this.ui = ui;
        this.gameHistory = new GameHistory(board, 0);
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
                ui.commit();
                break;
            }

            if (lost[turn]) {
                turn = (turn + 1) % board.getAmountOfPlayers();
                moveCount += 1;
                updateAllPlayerValues(board, turn, moveCount);
                continue;
            }

            for (int i = 0; i < board.getAmountOfPlayers(); i++) {
                System.out.println("Color " + i + ": " + board.getPieceManager().getPiecesNotOnBoard(i).stream().map(Enum::toString).collect(Collectors.joining(", \t")));
            }

            updateAllPlayerValues(board, turn, moveCount);

            CapableOfPlaying current = players[turn];
            System.out.println(Arrays.toString(lost));
//            System.out.println("N is currently "0,0 + current.getEvaluator().getN());
            ui.updateValues(board.deepCopy(), turn, moveCount);
            ui.commit();




            Move move = current.getMove();
            if (!board.fits(move)) {
                System.out.println("Move " + move + " doesn't fit!");
                continue;
            }

            board.putOnBoard(move);
            gameHistory.addMove(move);



            turn = (turn + 1) % board.getAmountOfPlayers();
            moveCount += 1;
        }

        ui.commit();



        List<Integer> finalScores = new Vector<>();

        for (int color = 0; color < board.getAmountOfPlayers(); color++) {
            int total = board.getPieceManager().getPiecesNotOnBoard(color).stream().mapToInt(PieceID::getAmountOfSquares).sum();

            finalScores.add(total);
        }

        int winner = finalScores.indexOf(Collections.min(finalScores));

        System.out.println("Color " + winner + " won with " + finalScores.get(winner) + " points!");
        System.out.println(finalScores);

    }

    private boolean canPlay (boolean[] lost) {
        for (boolean b : lost) {
            if (!b) {
                return true;
            }
        }

        return false;
    }

    private void updateAllPlayerValues (Board board, int turn, int moveCount) {
        Arrays.stream(players).forEach((item) -> item.updateValues(board.deepCopy(), turn, moveCount));
        Arrays.stream(spectators).forEach((item) -> item.updateValues(board.deepCopy(), turn, moveCount));

    }

    public GameHistory getGameHistory() {
        return gameHistory;
    }

    public Board getBoard() {
        return board;
    }
}
