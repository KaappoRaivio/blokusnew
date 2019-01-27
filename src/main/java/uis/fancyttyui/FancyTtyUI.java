package uis.fancyttyui;

import blokus.Board;
import blokus.Move;


import blokus.NotImplementedError;
import uis.UI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class FancyTtyUI implements UI {
    private Terminal terminal;
    private Board board;
    private int turn = 0;
    private int moveCount = 0;

    private char[][] buffer;
    private LayerManager layerManager;
    private List<Layer> layers = new ArrayList<>();

    public FancyTtyUI(Board board) {
        this.board = board;
        this.terminal = new Terminal();
    }

    @Override
    public void updateValues (Board board, int turn, int moveCount) {
        this.board = board;
        this.turn = turn;
        this.moveCount = moveCount;

        this.buffer = new char[terminal.getDimY()][terminal.getDimX()];

    }

    @Override
    public void commit () {
        terminal.updateBuffer(convertFromIntBoard(board.getBoard()));


        terminal.commit();
    }

//    private static char[][] prepareBoard (Board board) {
//        String[] rows = board.toString().split("\n");
//
//
//    }

    private static char[][] convertFromIntBoard (int[][] oldBoard) {
        char[][] newBoard = new char[oldBoard.length][oldBoard[0].length];

        for (int y = 0; y < oldBoard.length; y++) {
            for (int x = 0; x < oldBoard[y].length; x++) {
                newBoard[y][x] = getMatchingChar(oldBoard[y][x]);
                System.out.println(getMatchingChar(oldBoard[y][x]));
            }
        }

        return newBoard;
    }

    private static char getMatchingChar (int integer) {
        switch (integer) {
            case 0:
                return '0';
            case 1:
                return '1';
            default:
                return '.';
        }
    }

    @Override
    public Move getMove (int color) {
//        throw new RuntimeException(new NotImplementedError());
        while (true) {
            String inStr = new Scanner(System.in).nextLine();
            try {
                return Move.parseMove(inStr, color);
            } catch (Exception ignored) {}
        }
    }
}
