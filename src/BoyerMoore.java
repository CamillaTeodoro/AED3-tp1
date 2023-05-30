import java.util.*;

public class BoyerMoore {
    private static final int ALPHABET_SIZE = 256;

    public List<Integer> search(String fileAString, String pattern) {
        List<Integer> matches = new ArrayList<>();

        int n = fileAString.length();
        int m = pattern.length();

        int[] last = preprocessLastOccurrence(pattern);
        Map<Character, Integer> badCharShift = preprocessBadCharacterShift(pattern);

        int shift = 0;
        while (shift <= n - m) {
            int j = m - 1;

            while (j >= 0 && pattern.charAt(j) == fileAString.charAt(shift + j))
                j--;

            if (j < 0) {
                // Match found at current shift
                matches.add(shift);
                shift += (shift + m < n) ? m - last[fileAString.charAt(shift + m)] : 1;
            } else {
                int charShift = badCharShift.getOrDefault(fileAString.charAt(shift + j), m);
                shift += Math.max(1, j - charShift);
            }
        }

        return matches;
    }

    private static Map<Character, Integer> preprocessBadCharacterShift(String pattern) {
        Map<Character, Integer> badCharShift = new HashMap<>();
        int m = pattern.length();

        for (int i = 0; i < m - 1; i++) {
            char c = pattern.charAt(i);
            badCharShift.put(c, m - 1 - i);
        }

        return badCharShift;
    }

    private static int[] preprocessLastOccurrence(String pattern) {
        int[] last = new int[ALPHABET_SIZE];
        int m = pattern.length();

        for (int i = 0; i < ALPHABET_SIZE; i++) {
            last[i] = -1;
        }

        for (int i = 0; i < m - 1; i++) {
            last[pattern.charAt(i)] = i;
        }

        return last;
    }

}
