package uis;

import blokus.Board;
import blokus.Move;
import blokus.Orientation;
import blokus.PieceID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class TtyUITest implements UI {
    private Board board;
    private int turn = -1;
    private int amountOfPlayers;
    private int moveCounter = 0;

    public TtyUITest (Board board) {
        this.board = board;
        this.amountOfPlayers = board.getAmountOfPlayers();
    }

    @Override
    public void updateValues(Board board, int turn, int moveCount) {
        this.board = board;
        this.turn = turn;
        this.moveCounter = moveCount;
    }

    @Override
    public void commit() {

        System.out.print("Round " + moveCounter / amountOfPlayers + ", turn " + turn + ":");

        String[] rowsRaw = board.toString().split("\n");
        Arrays.stream(rowsRaw).forEach((row) -> System.out.println("\t" + row));


        for (int i = 0; i < amountOfPlayers; i++) {
            System.out.println("Color " + i + ": " + board.getPieceManager().getPiecesNotOnBoard(i).stream().map(Enum::toString).collect(Collectors.joining(", ")));
        }

    }
}
