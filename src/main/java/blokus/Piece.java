package blokus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Piece implements java.io.Serializable {
    private static final List<String> paths = Arrays.asList(
            "../pieces/piece1.txt",
            "../pieces/piece2.txt",
            "../pieces/piece3.txt",
            "../pieces/piece4.txt",
            "../pieces/piece5.txt",
            "../pieces/piece6.txt",
            "../pieces/piece7.txt",
            "../pieces/piece8.txt",
            "../pieces/piece9.txt",
            "../pieces/piece10.txt",
            "../pieces/piece11.txt",
            "../pieces/piece12.txt",
            "../pieces/piece13.txt",
            "../pieces/piece14.txt",
            "../pieces/piece15.txt",
            "../pieces/piece16.txt",
            "../pieces/piece17.txt",
            "../pieces/piece18.txt",
            "../pieces/piece19.txt",
            "../pieces/piece20.txt",
            "../pieces/piece21.txt"
    );


    public static final char OPAQUE = '#';
    public static final char TRANSPARENT = '.';

    private char[][] mesh = new char[5][5];
    private int color;

    private int posX = -1;
    private int posY = -1;
    private boolean onBoard = false;
    private final PieceID id;
    private final Orientation orientation;
    private final boolean flipped;
    private final int amountOfSquares;
    private List<Position> squares = new ArrayList<>();
    private int dimX;
    private int dimY;

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }


    public void placeOnBoard (int posX, int posY) {
        if (onBoard) {
            throw new RuntimeException("blokus.Piece " + this.toString() + " is already on board at coordnates " + this.posX + ", " + this.posY + "!");
        }

        onBoard = true;
        this.posX = posX;
        this.posY = posY;
    }

    public PieceID getID () {
        return id;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public boolean isFlipped() {
        return this.flipped;
    }

    public char[][] getMesh() {
        return mesh;
    }


    private static boolean isValid (int color) {
        return true;
    }

    private void initializeMesh () {

        for (int y = 0; y < mesh.length; y++) {
            for (int x = 0; x < mesh[y].length; x++) {
                this.mesh[x][y] = Piece.TRANSPARENT;
            }
        }
    }

    public List<Position> getSquares() {
        return squares;
    }

    public Piece (PieceID pieceID, int color) {
        if (isValid(color)) {
            this.color = color;
        } else {
            throw new RuntimeException("Invalid color " + color + "!");
        }

        initializeMesh();

        String text;
        try {
            text = new String(this.getClass().getResourceAsStream(paths.get(pieceID.getOrdinal())).readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[] lines = text.split("\n");
        dimY = lines.length;
        dimX = lines[0].length();

        int counter = 0;

        for (int y = 0; y < dimY; y++) {
            for (int x = 0; x < dimX; x++) {
                char current = lines[y].charAt(x);

                switch (current) {
                    case Piece.TRANSPARENT:
                        mesh[x][y] = Piece.TRANSPARENT;
                        break;
                    case Piece.OPAQUE:
                        counter++;
                        squares.add(new Position(x, y));
                        mesh[x][y] = Piece.OPAQUE;
                        break;
                    default:
                        System.out.println("Invalid data in " + pieceID + " at " + x + ", " + y + "!" );
                        mesh[x][y] = Piece.TRANSPARENT;
                        break;

                }
            }
        }

        this.amountOfSquares = counter;
        this.id = pieceID;
        this.orientation = Orientation.UP;
        this.flipped = false;

    }

    private void refreshSquares () {
        squares = new ArrayList<>();

        for (int x = 0; x < dimX; x++) {
            for (int y = 0; y < dimY; y++) {
                if (mesh[x][y] == OPAQUE) {
                    squares.add(new Position(x, y));
                }
            }
        }
    }

    public int getAmountOfSquares () {
        return amountOfSquares;
    }

    private Piece (char[][] mesh, int color, PieceID pieceID, Orientation orientation, boolean flipped, boolean isOnBoard, int amountOfSquares) {
        if (isValid(color)) {
            this.color = color;
        } else {
            throw new RuntimeException("Invalid color " + color + "!");
        }

        initializeMesh();

        this.mesh = mesh;
        refreshSquares();

        this.id = pieceID;
        this.orientation = orientation;
        this.flipped = flipped;
        this.onBoard = isOnBoard;
        this.amountOfSquares = amountOfSquares;
    }

    private void moveLeft () {
        for (int y = 0; y < 5; y++) {
            if (mesh[0][y] == Piece.OPAQUE) {
                return;
            }
        }

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                if (x + 1 >= 5) {
                    mesh[x][y] = Piece.TRANSPARENT;
                } else {
                    mesh[x][y] = mesh[x + 1][y];
                }
            }
        }

        moveLeft();
    }

    private void moveUp () {
        for (int x = 0; x < 5; x++) {
            if (mesh[x][0] == Piece.OPAQUE) {
                return;
            }
        }

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                if (y >= 4) {
                    mesh[x][y] = Piece.TRANSPARENT;
                } else {
                    mesh[x][y] = mesh[x][y + 1];
                }
            }
        }

        moveUp();
    }

    public Piece rotate (Orientation orientation, boolean flip) {
        char[][] newlist = new char[5][5];


        switch (orientation) {
            case UP:
                newlist = this.mesh;
                break;
            case DOWN:
                for (int y = 0; y < this.mesh.length; y++) {
                    for (int x = 0; x < this.mesh[y].length; x++) {
                        newlist[4 - y][4 - x] = this.mesh[y][x];
                    }
                }
                break;
            case LEFT:
                for (int y = 0; y < this.mesh.length; y++) {
                    for (int x = 0; x < this.mesh[y].length; x++) {
                        newlist[4 - x][y] = this.mesh[y][x];
                    }
                }
                break;
            case RIGHT:
                for (int y = 0; y < this.mesh.length; y++) {
                    for (int x = 0; x < this.mesh[y].length; x++) {
                        newlist[x][4 - y] = this.mesh[y][x];
                    }
                }
                break;
        }

        char[][] afterFlip = new char[5][5];

        if (flip) {
            for (int y = 0; y < this.mesh.length; y++) {
                for (int x = 0; x < this.mesh[y].length; x++) {
                    afterFlip[y][4 - x] = newlist[y][x];
                }
            }
        } else {
            afterFlip = newlist;
        }
        Piece piece = new Piece(afterFlip, this.color, this.getID(), orientation, flip, this.isOnBoard(), this.getAmountOfSquares());
        piece.moveLeft();
        piece.moveUp();

        return piece;

    }

    public List<Piece> getAllOrientations () {
        List<Piece> pieces = new ArrayList<>();

        for (PieceID.OrientationAndFlip orientationAndFlip : id.getAllOrientations()) {
            pieces.add(rotate(orientationAndFlip.getOrientation(), orientationAndFlip.isFlip()));
        }

        return pieces;
    }


    public static List<PieceID> getAllPieces (int pieceColor) {
        return Arrays.asList(
                PieceID.PIECE_1,
                PieceID.PIECE_2,
                PieceID.PIECE_3,
                PieceID.PIECE_4,
                PieceID.PIECE_5,
                PieceID.PIECE_6,
                PieceID.PIECE_7,
                PieceID.PIECE_8,
                PieceID.PIECE_9,
                PieceID.PIECE_10,
                PieceID.PIECE_11,
                PieceID.PIECE_12,
                PieceID.PIECE_13,
                PieceID.PIECE_14,
                PieceID.PIECE_15,
                PieceID.PIECE_16,
                PieceID.PIECE_17,
                PieceID.PIECE_18,
                PieceID.PIECE_19,
                PieceID.PIECE_20,
                PieceID.PIECE_21
        );
    }

    public int getColor() {
        return this.color;
    }

    public String toString () {
        StringBuilder builder = new StringBuilder("Color: " + this.color + "\n");
        for (int y = 0; y < this.mesh.length; y++) {
            for (int x = 0; x < this.mesh[y].length; x++) {
                builder.append(this.mesh[x][y] == Piece.TRANSPARENT ? " " : this.mesh[x][y]);
            }
            builder.append("\n");
        }

        return builder.toString();
    }

    public String bareToString () {
        StringBuilder builder = new StringBuilder();
        for (int y = 0; y < this.mesh.length; y++) {
            for (int x = 0; x < this.mesh[y].length; x++) {
                builder.append(this.mesh[x][y] == Piece.TRANSPARENT ? " " : this.mesh[x][y]);
            }
            builder.append("\n");
        }

        return builder.toString();
    }

    public boolean isOnBoard() {
        return onBoard;
    }

    public void setOnBoard(boolean set) {
//        this.onBoard = set;
    }

    public static int amountOfUniquePieces () {
        return 21;
    }
}