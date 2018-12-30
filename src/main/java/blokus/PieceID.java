package blokus;

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
        switch (notation) {
            case "I1":
                return PIECE_1;
            case "I2":
                return PIECE_2;
            case "V3":
                return PIECE_3;
            case "I3":
                return PIECE_4;
            case "O":
                return PIECE_5;
            case "T4":
                return PIECE_6;
            case "I4":
                return PIECE_7;
            case "L4":
                return PIECE_8;
            case "Z4":
                return PIECE_9;
            case "L5":
                return PIECE_10;
            case "T5":
                return PIECE_11;
            case "V5":
                return PIECE_12;
            case "N":
                return PIECE_13;
            case "Z5":
                return PIECE_14;
            case "I5":
                return PIECE_15;
            case "P":
                return PIECE_16;
            case "W":
                return PIECE_17;
            case "U":
                return PIECE_18;
            case "F":
                return PIECE_19;
            case "X":
                return PIECE_20;
            case "Y":
                return PIECE_21;
            default:
                throw new RuntimeException("Unknown string " + notation);
        }
    }
}
