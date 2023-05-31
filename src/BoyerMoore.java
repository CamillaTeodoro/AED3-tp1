import java.util.*;

public class BoyerMoore {
    public List<Integer> boyerMooreSearch(String text, String pattern) {
        List<Integer> matches = new ArrayList<>();

        int n = text.length();
        int m = pattern.length();

        if (m == 0 || m > n) {
            return matches;
        }

        Map<Character, Integer> badCharTable = generateBadCharTable(pattern);
        int[] goodSuffixTable = generateGoodSuffixTable(pattern);

        int i = m - 1;
        int j = m - 1;

        while (i < n) {
            if (text.charAt(i) == pattern.charAt(j)) {
                if (j == 0) {
                    matches.add(i);
                    i += m;
                } else {
                    i--;
                    j--;
                }
            } else {
                int shift1 = badCharTable.getOrDefault(text.charAt(i), m);
                int shift2 = goodSuffixTable[j];
                int shift = Math.max(shift1, shift2);
                i += shift;
                j = m - 1;
            }
        }

        return matches;
    }

    /**
     * Create the table for the bad character
     * 
     * @param pattern
     * @return
     */
    private static Map<Character, Integer> generateBadCharTable(String pattern) {
        Map<Character, Integer> table = new HashMap<>();

        for (int i = 0; i < pattern.length() - 1; i++) {
            char c = pattern.charAt(i);
            table.put(c, i);
        }
        table.forEach((key, value) -> System.out.println(key + " " + value));
        return table;
    }

    private static int[] generateGoodSuffixTable(String pattern) {
        int m = pattern.length();
        int[] table = new int[m];
        int[] suffixes = generateSuffixes(pattern);

        for (int i = 0; i < m; i++) {
            table[i] = m;
        }

        int j = 0;
        for (int i = m - 1; i >= 0; i--) {
            if (suffixes[i] == i + 1) {
                for (; j < m - 1 - i; j++) {
                    if (table[j] == m) {
                        table[j] = m - 1 - i;
                    }
                }
            }
        }

        for (int i = 0; i < m - 1; i++) {
            table[m - 1 - suffixes[i]] = m - 1 - i;
        }

        return table;
    }

    private static int[] generateSuffixes(String pattern) {
        int m = pattern.length();
        int[] suffixes = new int[m];
        int f = 0;
        int g;

        suffixes[m - 1] = m;
        g = m - 1;
        for (int i = m - 2; i >= 0; i--) {
            if (i > g && suffixes[i + m - 1 - f] < i - g) {
                suffixes[i] = suffixes[i + m - 1 - f];
            } else {
                if (i < g) {
                    g = i;
                }
                f = i;
                while (g >= 0 && pattern.charAt(g) == pattern.charAt(g + m - 1 - f)) {
                    g--;
                }
                suffixes[i] = f - g;
            }
        }

        return suffixes;
    }
}
