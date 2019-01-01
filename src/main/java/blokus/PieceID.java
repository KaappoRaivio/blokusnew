package blokus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum PieceID {
    PIECE_1 ("I1", 0),
    PIECE_2 ("I2", 1),
    PIECE_3 ("V3", 2),
    PIECE_4 ("I3", 3),
    PIECE_5 ("O", 4),
    PIECE_6 ("T4", 5),
    PIECE_7 ("I4", 6),
    PIECE_8 ("L4", 7),
    PIECE_9 ("Z4", 8),
    PIECE_10("L5", 9),
    PIECE_11("T5", 10),
    PIECE_12("V5", 11),
    PIECE_13("N", 12),
    PIECE_14("Z5", 13),
    PIECE_15("I5", 14),
    PIECE_16("P", 15),
    PIECE_17("W", 16),
    PIECE_18("U", 17),
    PIECE_19("F", 18),
    PIECE_20("X", 19),
    PIECE_21("Y", 20);

    private static HashMap<String, PieceID> dict = new HashMap<String, PieceID>();

    static {
        Arrays.stream(PieceID.values()).forEach((pieceID) -> dict.put(pieceID.term, pieceID));
    }

    private String term;
    private int ordinal;
    PieceID (String term, int ordinal) {
        this.term = term;
        this.ordinal = ordinal;
    }


    public int getOrdinal () {
        return ordinal;
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
