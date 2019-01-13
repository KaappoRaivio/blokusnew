package blokus;

import java.io.*;
import java.util.*;


public class Board implements Serializable {

    private static final int NO_PIECE = -1;
    private static final int EDGE = -2;


    private int[][] board;
    private int[][] errorBoard;

    private int dimX;
    private int dimY;
    private int amountOfPlayers;


    private PieceManager pieceManager;
    private boolean parallel;
    private int amountOfThreads;

    private List<int[][]> moveHistory = new ArrayList<>();

    public Board(int dimX, int dimY, PieceManager pieceManager) {
        this(dimX, dimY, pieceManager, false, 0);
    }

    public Board(int dimX, int dimY, PieceManager pieceManager, boolean parallel, int amountOfThreads) {
        this.dimX = dimX;
        this.dimY = dimY;
        this.amountOfPlayers = pieceManager.getAmountOfPlayers();

        this.pieceManager = pieceManager;


        board = new int[dimY][dimX];
        errorBoard = new int[dimY][dimX];

        this.parallel = parallel;
        this.amountOfThreads = amountOfThreads;
        
        initializeBoards();

    }

    private void saveUndoState () {
        int[][] oldBoard = new int[dimY][dimX];

        for (int y = 0; y < dimY; y++) {
            if (dimX >= 0) System.arraycopy(board[y], 0, oldBoard[y], 0, dimX);
        }

        moveHistory.add(oldBoard);

    }

    public void undo (int depth) {
        if (moveHistory.size() - 1 - depth >= 0) {
            int[][] oldBoard = moveHistory.get(moveHistory.size() - depth - 1);
            moveHistory.remove(moveHistory.size() - 1 - depth);
            this.board = oldBoard;
            pieceManager.undo(depth + 1);

        } else {
            throw new RuntimeException("Can't undo this far! " + depth + " " + moveHistory.size());
        }

    }

    private void initializeBoards () {
        for (int y = 0; y < dimY; y++) {
            for (int x = 0; x < dimX; x++) {
                errorBoard[y][x] = NO_PIECE;
                board[y][x] = NO_PIECE;
            }
        }
    }

    public boolean putOnBoard(int baseX, int baseY, PieceID pieceID, int color, Orientation orientation, boolean flip) {
        if (pieceManager.isOnBoard(pieceID, color)) {
            throw new RuntimeException("blokus.Piece " + pieceID + "already on board");
        }

        Piece piece = pieceManager.getCachedPiece(pieceID, color).rotate(orientation, flip);

        if (fits(baseX, baseY, pieceID, color, orientation, flip)) {
            dummyPut(baseX, baseY, piece);
            addToPiecesOnBoard(piece);
            piece.placeOnBoard(baseX, baseY);
            return true;
        } else {
            errorPut(baseX, baseY, piece);
            return false;
        }
    }

    public boolean putOnBoard (Move move) {
        return putOnBoard(move.getX(), move.getY(), move.getPieceID(), move.getColor(), move.getOrientation(), move.isFlip());
    }

    private int safeOffset(int baseX, int baseY, int offsetX, int offsetY) {
        try {
            return board[baseY + offsetY][baseX + offsetX];
        } catch (ArrayIndexOutOfBoundsException e) {
            return EDGE;
        }
    }

    public boolean fits(int baseX, int baseY, PieceID pieceID, int color, Orientation orientation, boolean flip) {
        return fits(baseX, baseY, pieceID, color, orientation, flip, true);
    }

    public boolean fits (int baseX, int baseY, PieceID pieceID, int color, Orientation orientation, boolean flip, boolean noDupe) {
        if (pieceManager.isOnBoard(pieceID, color) && noDupe) {
            return false;
        }


        Piece piece = pieceManager.getCachedPiece(pieceID, color).rotate(orientation, flip);
        char[][] mesh = piece.getMesh();

        boolean isConnected = false;
        boolean fits = true;
        boolean touchesCorner = false;

        if (!(mesh.length == 5 && mesh[0].length == 5)) {
            System.out.println("Length: " + mesh.length + " " + mesh[0].length);
        }

        for (int y = 0; y < mesh.length; y++) {
            for (int x = 0; x < mesh[y].length; x++) {
                char current = mesh[y][x];
                if (current == Piece.TRANSPARENT) {
                    continue;
                }

                int absX = baseX + x;
                int absY = baseY + y;

                if (safeOffset(absX, absY, 0, 0) != NO_PIECE) {
//                    fits = false;
//                    break;
                    return false;
                }

                if (!touchesCorner &&
                        (absX == 0 && absY == 0 ||
                        absX == 0 && absY == dimY - 1 ||
                        absX == dimX - 1 && absY == 0 ||
                        absX == dimX - 1 && absY == dimY - 1)) {

                    touchesCorner = true;
                }

                int topRight = safeOffset(absX, absY, +1, -1);
                int topLeft = safeOffset(absX, absY, -1, -1);
                int bottomRight = safeOffset(absX, absY, +1, +1);
                int bottomLeft = safeOffset(absX, absY, -1, +1);


                if (!isConnected &&
                       (topRight == piece.getColor() ||
                        topLeft == piece.getColor() ||
                        bottomRight == piece.getColor() ||
                        bottomLeft == piece.getColor())
                    ) {
                    isConnected = true;
                }

                int top = safeOffset(absX, absY, 0, -1);
                int bottom = safeOffset(absX, absY, 0, +1);
                int left = safeOffset(absX, absY, -1, 0);
                int right = safeOffset(absX, absY, +1, 0);

                if (top == piece.getColor() ||
                    bottom == piece.getColor() ||
                    left == piece.getColor() ||
                    right == piece.getColor()) {
//                        fits = false;
//                        break;
                        return false;
                }
            }
        }

        if (fits && !isColorOnBoard(piece.getColor()) && touchesCorner) {
            isConnected = true;
        }


        return fits && isConnected;

    }

    public boolean fits (Move move) {
        return fits(move.getX(), move.getY(), move.getPieceID(), move.getColor(), move.getOrientation(), move.isFlip());
    }

    private void addToPiecesOnBoard (Piece piece) {
        pieceManager.placeOnBoard(piece.getID(), piece.getColor());
    }

    private boolean isColorOnBoard (int color) {
        return pieceManager.isColorOnBoard(color);
    }

    private void dummyPut (int baseX, int baseY, Piece piece) {
        char[][] mesh = piece.getMesh();
        saveUndoState();

        for (int y = 0; y < mesh.length; y++) {
            for (int x = 0; x < mesh[y].length; x++) {
                char current = mesh[y][x];

                switch (current) {
                    case Piece.TRANSPARENT:
                        break;
                    case Piece.OPAQUE:
                        board[baseY + y][baseX + x] = piece.getColor();
                        break;
                    default:
                        throw new RuntimeException("Invalid piece " + piece.toString() + ", " + current);
                }

            }
        }
    }

    private void errorPut (int baseX, int baseY, Piece piece) {
        char[][] mesh = piece.getMesh();

        for (int y = 0; y < mesh.length; y++) {
            for (int x = 0; x < mesh[y].length; x++) {
                char current = mesh[y][x];

                switch (current) {
                    case Piece.OPAQUE:
                        errorBoard[baseY + y][baseX + x] = piece.getColor();
                        break;
                    case Piece.TRANSPARENT:
                        break;
                    default:
                        throw new RuntimeException("Invalid piece " + piece.toString());
                }

            }
        }
    }

    public boolean hasMoves (int color) {
        return getAllFittingMoves(color).size() != 0;
    }

    public boolean canPlay () {
        for (int i = 0; i < getAmountOfPlayers(); i++) {
            if (!hasMoves(i)) {
                return false;
            }
        }

        return true;
    }

    private static char getMatchingChar (int color) {
        if (color == -1) {
            return Piece.TRANSPARENT;
        }
        return (char) (color + 48);
    }

    private static int getPieceColorFromChar (char color) {
        return ((int) color) - 48;
    }

    public int[][] getBoard() {
        return board;
    }

    public String toString () {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < dimY; i++) {
            int[] row = board[i];
            builder.append("\n");
            for (int index = 0; index < row.length - 1; index++) {
                if (errorBoard[i][index] != NO_PIECE) {
                    builder.append('E');
                } else {
                    builder.append(getMatchingChar(row[index]));
                }
                builder.append(" ");
            }

            if (errorBoard[i][row.length - 1] != NO_PIECE) {
                builder.append('E');
            } else {
                builder.append(getMatchingChar(row[row.length - 1]));
            }

        }

        return builder.toString();
    }

    public String save (String name) {
        String path = System.getProperty("user.dir") + "/src/main/resources/boards/" + name + ".ser";


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

    public String save () {
        return save(String.valueOf(System.currentTimeMillis() * new Random().nextFloat()));
    }

    public static Board fromFile (String path, boolean relative) {
        String absolutePath;

        if (relative) {
            absolutePath = System.getProperty("user.dir") + "/src/main/resources/boards/" + new Date().toString() + ".ser";
        } else {
            absolutePath = path;
        }

        Board board;
        try {
            FileInputStream fileIn = new FileInputStream(absolutePath);

            ObjectInputStream in = new ObjectInputStream(fileIn);
            board = (Board) in.readObject();
            in.close();

            fileIn.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return board;
    }

    public static Board fromHumanReadableFile (String filePath, boolean relative) {
        throw new RuntimeException(new NotImplementedError());
    }

    private List<PieceID> getPiecesNotOnBoard (int color) {
        return pieceManager.getPiecesNotOnBoard(color);
    }

    private List<Span> splitBoardInto (int amountOfChunks) {
        int surfaceArea = dimX * dimY;


        int[] lengths = new int[amountOfChunks];
        int remainder = surfaceArea % amountOfChunks;

        for (int i = 0; i < amountOfChunks; i++) {
            lengths[i] = surfaceArea / amountOfChunks;
        }

        for (int i = 0; i < remainder; i++) {
            lengths[i] += 1;
        }

        int startX = 0;
        int startY = 0;

        int endX = 0;
        int endY = 0;

        List<Span> spans = new ArrayList<>();

        for (int length : lengths) {
            endX = (endX + length) % 20;
            endY += (endX + length) / 20;

            spans.add(new Span(new Position(startX, startY), new Position(endX, endY)));

            startX = endX;
            startY = endY;
        }

        return spans;
    }


    private List<Move> getAllFittingMovesParallel(int color, int numberOfCores) {

        List<Move> result = new ArrayList<>();
        List<Span> spans = splitBoardInto(numberOfCores);
        List<WorkerThread> threads = new ArrayList<>();

        for (Span span : spans) {
            WorkerThread thread = new WorkerThread(this.deepCopy(), span, color);
            threads.add(thread);
            thread.run();

        }

        for (WorkerThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ignored) {}

            result.addAll(thread.getResult());
        }


        return result;
    }

    public List<Move> getAllFittingMoves (int color) {
            List<Move> moves = new ArrayList<>();
            List<PieceID> pieces = getPiecesNotOnBoard(color);



            for (Position boardPosition : this.getEligibleCorners(color)) {
                moves.addAll(getAllFittingMoves(color, boardPosition.x, boardPosition.y));
            }


            return moves;
    }

    private List<Move> getAllFittingMoves (int color, int x, int y) {
        List<Move> moves = new ArrayList<>();
        List<PieceID> pieces = getPiecesNotOnBoard(color);

        for (PieceID pieceID : pieces) {
            moves.addAll(getAllFittingMoves(color, x, y, pieceID));
        }

        return moves;
    }

    public List<Move> getAllFittingMovesBad (int color) {
        var moves = new ArrayList<Move>();
        var pieces = getPiecesNotOnBoard(color);

        for (var pieceID : pieces) {
            for (PieceID.OrientationAndFlip orientationAndFlip : pieceID.getAllOrientations()) {
                for (int x = 0; x < dimX; x++) {
                    for (int y = 0; y < dimY; y++) {
                        if (fits(new Move(x, y, pieceID, color, orientationAndFlip.getOrientation(), orientationAndFlip.isFlip()))) {
                            moves.add(new Move(x, y, pieceID, color, orientationAndFlip.getOrientation(), orientationAndFlip.isFlip()));
                        }
                    }
                }
            }
        }

        return moves;
    }


    public List<Move> getAllFittingMoves (int color, int x, int y, PieceID pieceID) {
        List<Move> moves = new ArrayList<>();
        Piece piece = pieceManager.getCachedPiece(pieceID, color);

        for (PieceID.OrientationAndFlip orientationAndFlip: pieceID.getAllOrientations()) {
            for (Position position : piece.getSquares()) {
                int baseX = x - position.x;
                int baseY = y - position.y;

                Move move = new Move(baseX, baseY, pieceID, color, orientationAndFlip.getOrientation(), orientationAndFlip.isFlip());

                if (fits(move)) {
                    moves.add(move);
                }
            }
        }

        return moves;
    }

    private boolean isCorner (int x, int y) {
        return (x == 0 || x == dimX - 1) && (y == 0 || y == dimY - 1);
    }

    private boolean isEligibleCorner (int color, int x, int y) {
        if (!isColorOnBoard(color) && isCorner(x, y)) {
            return true;
        }

        int topRight = safeOffset(x, y, 1, -1);
        int bottomRight = safeOffset(x, y, 1, 1);
        int topLeft = safeOffset(x, y, -1, -1);
        int bottomLeft = safeOffset(x, y, -1, 1);

        int left = safeOffset(x, y, -1 ,0);
        int right = safeOffset(x, y, 1 ,0);
        int top = safeOffset(x, y, 0 ,-1);
        int bottom = safeOffset(x, y, 0 ,1);

        boolean edges = left != color && right != color && top != color && bottom != color;
        boolean corners = topRight == color || bottomRight == color || topLeft == color || bottomLeft == color;

        return edges && corners;
    }

    private List<Position> getEligibleCorners (int color) {
        List<Position> corners = new ArrayList<>();

        for (int y = 0; y < dimY; y++) {
            for (int x = 0; x < dimX; x++) {
                if (isEligibleCorner(color, x, y)) {
                    corners.add(new Position(x, y));
                }
            }
        }

        return corners;
    }


    public Board deepCopy () {
        Board newBoard;

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();

            InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            newBoard = (Board) objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return newBoard;
    }

    public int getDimX() {
        return dimX;
    }

    public int getDimY() {
        return dimY;
    }

    public int getAmountOfPlayers() {
        return amountOfPlayers;
    }

    public PieceManager getPieceManager() {
        return pieceManager;
    }

    public void setPieceManager(PieceManager pieceManager) {
        this.pieceManager = pieceManager;
    }

    public class WorkerThread extends Thread {

        private final int color;
        private Board board;
        private Span span;
        private List<Move> moves = new ArrayList<>();

        public WorkerThread (Board board, Span span, int color) {
            super();

            this.board = board;
            this.span = span;
            this.color = color;
        }

        @Override
        public void run() {
            List<PieceID> pieces = board.getPiecesNotOnBoard(color);

            for (Position position : span) {
                for (PieceID pieceID : pieces) {
//                    Piece notRotated = pieceManager.getCachedPiece(pieceID, color);

                    for (Orientation orientation : Orientation.values()) {
                        if (board.fits(position.x, position.y, pieceID, color, orientation, false)) {
                            moves.add(new Move(position.x, position.y, pieceID, color, orientation, false));
                        }

                        if (board.fits(position.x, position.y, pieceID, color, orientation, true)) {
                            moves.add(new Move(position.x, position.y, pieceID, color, orientation, true));
                        }
                    }
                }
            }
        }

        public List<Move> getResult () {
            return moves;
        }


    }

    public List<Move> getFirstNFittingMoves (int n, int color) {
        List<Move> moves = getAllFittingMoves(color);
        moves.sort(new Comparator<Move>() {
            @Override
            public int compare(Move move, Move t1) {
                return t1.getPieceID().getAmountOfSquares() - move.getPieceID().getAmountOfSquares();
            }
        });

        try {
            return moves.subList(0, n);
        } catch (IndexOutOfBoundsException e) {
            return moves;
        }


    }

}

class NotImplementedError extends Exception {}