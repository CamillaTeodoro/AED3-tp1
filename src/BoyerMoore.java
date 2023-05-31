import java.util.*;

public class BoyerMoore {
    long tempoInicial = System.currentTimeMillis();

    private static int NO_OF_CHARS = 256; // Assuming an extended ASCII character set

    /**
     * Create the bad character table
     * 
     * @param pattern
     * @param badChar
     */
    private static void badCharHeuristic(char[] pattern, int[] badChar) {
        int patternLength = pattern.length;

        // Initialize the bad character array with -1
        Arrays.fill(badChar, -1);

        // Store the rightmost occurrence of each character in the pattern, except for
        // the last character
        for (int i = 0; i < patternLength - 1; i++) {
            badChar[pattern[i]] = i;
        }
    }

    /**
     * Create the Good Suffix table
     */
    private static void goodSuffixHeuristic(char[] pattern, int[] goodSuffix) {
        int patternLength = pattern.length;
        //
        int[] suffixes = new int[patternLength];
        int lastPrefixPosition = patternLength;

        for (int i = patternLength - 1; i >= 0; i--) {
            if (isPrefix(pattern, i + 1)) {
                lastPrefixPosition = i + 1;
            }

            // Calculate the lengths of the suffixes
            suffixes[i] = lastPrefixPosition - i + patternLength - 1;
        }

        for (int i = 0; i < patternLength - 1; i++) {
            int suffixLength = suffixLength(pattern, i);
            // Update the table with the lengths of the suffixes
            suffixes[suffixLength] = patternLength - 1 - i + suffixLength;
        }

        for (int i = 0; i < patternLength; i++) {
            // Initialize the goodsuffix array with the default shift value
            goodSuffix[i] = patternLength;
        }

        for (int i = patternLength - 1; i >= 0; i--) {
            if (suffixes[i] == i + 1) {
                for (int j = 0; j < patternLength - 1 - i; j++) {
                    if (goodSuffix[j] == patternLength) {
                        goodSuffix[j] = patternLength - 1 - i;
                    }
                }
            }
        }

        for (int i = 0; i < patternLength - 1; i++) {
            int suffixLength = suffixLength(pattern, i);
            goodSuffix[patternLength - 1 - suffixLength] = patternLength - 1 - i;
        }
    }

    /**
     * Check if a given pattern is a prefix of another pattern
     * 
     * @param pattern
     * @param p
     * @return
     */
    private static boolean isPrefix(char[] pattern, int p) {
        int patternLength = pattern.length;
        for (int i = p, j = 0; i < patternLength; i++, j++) {
            if (pattern[i] != pattern[j]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calculate the length of a suffix
     * 
     * @param pattern
     * @param p
     * @return
     */
    private static int suffixLength(char[] pattern, int p) {
        int patternLength = pattern.length;
        int length = 0;
        for (int i = p, j = patternLength - 1; i >= 0 && pattern[i] == pattern[j]; i--, j--) {
            length += 1;
        }
        return length;
    }

    /**
     * Search for the given pattern in the text
     * 
     * @param text
     * @param pattern
     * @return
     */
    public List<Integer> boyerMooreSearch(String fileAsString, String pattern) {
        List<Integer> occurrences = new ArrayList<>();

        // Convert the fileAsString and pattern strings into character arrays
        char[] textChars = fileAsString.toCharArray();
        char[] patternChars = pattern.toCharArray();

        int textLength = textChars.length;
        int patternLength = patternChars.length;

        // count the pattern shifts inside the text
        int patternShifts = 0;

        int[] badChar = new int[NO_OF_CHARS];
        int[] goodSuffix = new int[patternLength + 1];

        // Preprocess the bad Char and the good suffix
        badCharHeuristic(patternChars, badChar);
        goodSuffixHeuristic(patternChars, goodSuffix);

        int shift = 0;
        // Loop that continues as long as the shift is less than or equal to the
        // difference between the lengths of the text and the pattern
        while (shift <= textLength - patternLength) {
            int j = patternLength - 1;

            // Compare the pattern with the text character by character from right to left
            while (j >= 0 && patternChars[j] == textChars[shift + j]) {
                j--;
            }

            if (j < 0) {
                // add the position to the list of occurrences
                occurrences.add(shift);
                // Apply the shift based on the good suffix array
                shift += goodSuffix[0];
            } else {
                int badCharShift = j - badChar[textChars[shift + j]];
                int goodSuffixShift = goodSuffix[j + 1];
                // shift the pattern according to the max jump
                shift += Math.max(badCharShift, goodSuffixShift);
                patternShifts++;
            }
        }

        long tempoFinal = System.currentTimeMillis();
        long total = tempoFinal - tempoInicial;

        System.out.println("Tempo total para busca pelo algoritmo Boyer-Moore foi de " + total + " milessegundos");
        System.out.println("Numero de movimentações do padrão pelo algoritmo Boyer-Moore foi de " + patternShifts
                + " movimentações");
        return occurrences;
    }
}
