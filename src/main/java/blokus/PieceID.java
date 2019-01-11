package blokus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum PieceID {
    PIECE_1 ("I1", 0, 1, 4, new OrientationGroup(true, false, false, false, false, false, false, false)),
    PIECE_2 ("I2", 1, 2, 4, new OrientationGroup(true, false, true, false, false, false, false, false)),
    PIECE_3 ("V3", 2, 3, 5, new OrientationGroup(true, false, true, false, true, false, true, false)),
    PIECE_4 ("I3", 3, 3, 4, new OrientationGroup(true, false, true, false, false, false, false, false)),
    PIECE_5 ("O", 4, 4, 4, new OrientationGroup(true, false, false, false, false, false, false, false)),
    PIECE_6 ("T4", 5, 4, 6, new OrientationGroup(true, false, true, false, true, false, true, false)),
    PIECE_7 ("I4", 6, 4, 4, new OrientationGroup(true, false, true, false, false, false, false, false)),
    PIECE_8 ("L4", 7, 4, 5, new OrientationGroup(true, true, true, true, true, true, true, true)),
    PIECE_9 ("Z4", 8, 4, 6, new OrientationGroup(true, false, true, false, true, false, true, false)),
    PIECE_10("L5", 9, 5, 4, new OrientationGroup(true, true, true, true, true, true, true, true)),
    PIECE_11("T5", 10, 5, 6, new OrientationGroup(true, false, true, false, true, false, true, false)),
    PIECE_12("V5", 11, 5, 5, new OrientationGroup(true, false, true, false, true, false, true, false)),
    PIECE_13("N", 12, 5, 6, new OrientationGroup(true, true, true, true, true, true, true, true)),
    PIECE_14("Z5", 13, 5, 6, new OrientationGroup(true, false, true, false, true, false, true, false)),
    PIECE_15("I5", 14, 5, 4, new OrientationGroup(true, false, true, false, false, false, false, false)),
    PIECE_16("P", 15, 5, 5, new OrientationGroup(true, true, true, true, true, true, true, true)),
    PIECE_17("W", 16, 5, 7, new OrientationGroup(true, false, true, false, true, false, true, false)),
    PIECE_18("U", 17, 5, 6, new OrientationGroup(true, false, true, false, true, false, true, false)),
    PIECE_19("F", 18, 5, 7, new OrientationGroup(true, true, true, true, true, true, true, true)),
    PIECE_20("X", 19, 5, 8, new OrientationGroup(true, false, false, false, false, false, false, false)),
    PIECE_21("Y", 20, 5, 6, new OrientationGroup(true, true, true, true, true, true, true, true));

    private static HashMap<String, PieceID> dict = new HashMap<String, PieceID>();

    static {
        Arrays.stream(PieceID.values()).forEach((pieceID) -> dict.put(pieceID.term, pieceID));
    }

    private String term;
    private int ordinal;
    private int amountOfSquares;
    private OrientationGroup relevantOrientations;

    public int getAmountOfCorners() {
        return amountOfCorners;
    }

    private int amountOfCorners;

    PieceID (String term, int ordinal, int amountOfSquares, int amountOfCorners, OrientationGroup relevantOrientations) {
        this.term = term;
        this.ordinal = ordinal;
        this.amountOfSquares = amountOfSquares;
        this.amountOfCorners = amountOfCorners;
        this.relevantOrientations = relevantOrientations;
    }


    public int getOrdinal () {
        return ordinal;
    }

    public int getAmountOfSquares () {
        return amountOfSquares;
    }

    @Override
    public String toString() {
        return /*super.toString().substring(6) + " " +*/ term;
    }


    public static PieceID fromStandardNotation (String notation) {
        if (dict.containsKey(notation)) {
            return dict.get(notation);
        } else {
            throw new RuntimeException("Unknown string " + notation);
        }


    }
}
