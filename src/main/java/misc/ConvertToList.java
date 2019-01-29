package misc;

import blokus.Board;
import blokus.MyPieceManager;

import java.util.*;

public class ConvertToList {
    public static char[][] convertToList (String original, String verticalDelimiter, String horizontalDelimiter, char transparentChar) {
        int dimX, dimY;

        String[] split = original.split(verticalDelimiter);

        dimY = split.length;

        List<List<Character>> notNormalized = new ArrayList<>();
        for (String row : split) {
            notNormalized.add(new ArrayList<>());

            List<Character> current = notNormalized.get(notNormalized.size() - 1);
            for (String character : row.split(horizontalDelimiter)) {
                if (character.length() > 0) {
                    current.add(character.charAt(0));
                } else {
                    current.add(transparentChar);
                }
            }
        }

        return normalizeArray(notNormalized, transparentChar);
    }

    private static char[][] normalizeArray (List<List<Character>> notNormalized, char transparentChar) {
        char[][] normalizedArray = new char[notNormalized.size()][getLongestRowLen(notNormalized)];

        for (int y = 0; y < normalizedArray.length; y++) {
            for (int x = 0; x < normalizedArray[y].length; x++) {
                normalizedArray[y][x] = transparentChar;
            }
        }

        for (int y = 0; y < notNormalized.size(); y++) {
            for (int x = 0; x < notNormalized.get(y).size(); x++) {
                normalizedArray[y][x] = notNormalized.get(y).get(x);
            }
        }

        return normalizedArray;
    }

    private static int getLongestRowLen (List<List<Character>> notNormalized) {
        return Collections.max(notNormalized, new Comparator<List<Character>> () {
            @Override
            public int compare(List<Character> characters, List<Character> t1) {
                return characters.size() - t1.size();
            }
        }).size();
    }

    public static void main (String[] arghs) {
        Arrays.stream(convertToList(new Board(14, 14, new MyPieceManager(2)).toString(), "\n", "", '$')).forEach((row) -> System.out.println(Arrays.toString(row)));
        
    }
}
