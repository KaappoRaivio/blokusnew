package blokus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum PieceID {
    PIECE_1 ("I1", 0, 1),
    PIECE_2 ("I2", 1, 2),
    PIECE_3 ("V3", 2, 3),
    PIECE_4 ("I3", 3, 3),
    PIECE_5 ("O", 4, 4),
    PIECE_6 ("T4", 5, 4),
    PIECE_7 ("I4", 6, 4),
    PIECE_8 ("L4", 7, 4),
    PIECE_9 ("Z4", 8, 4),
    PIECE_10("L5", 9, 5),
    PIECE_11("T5", 10, 5),
    PIECE_12("V5", 11, 5),
    PIECE_13("N", 12, 5),
    PIECE_14("Z5", 13, 5),
    PIECE_15("I5", 14, 5),
    PIECE_16("P", 15, 5),
    PIECE_17("W", 16, 5),
    PIECE_18("U", 17, 5),
    PIECE_19("F", 18, 5),
    PIECE_20("X", 19, 5),
    PIECE_21("Y", 20, 5);

    private static HashMap<String, PieceID> dict = new HashMap<String, PieceID>();

    static {
        Arrays.stream(PieceID.values()).forEach((pieceID) -> dict.put(pieceID.term, pieceID));
    }

    private String term;
    private int ordinal;
    private int amountOfSquares;

    PieceID (String term, int ordinal, int amountOfSquares) {
        this.term = term;
        this.ordinal = ordinal;
        this.amountOfSquares = amountOfSquares;
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
