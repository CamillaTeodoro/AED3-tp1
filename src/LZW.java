import java.io.*;
import java.text.ParseException;
import java.util.*;

public class LZW {

    // Atributtes

    // Constructors

    LZW() {

    }

    /**
     * Method to save a compacted string in a file
     * 
     * @param fileAsString db file as a string
     * @param fileName     name of the destination file
     * @throws IOException
     */
    public void compress(String fileAsString, String fileName) throws IOException {
        long tempoInicial = System.currentTimeMillis();

        // create the initial dictionary
        HashMap<String, Integer> dictionary = new HashMap<>();

        for (int i = 0; i < 256; i++) {
            dictionary.put("" + (char) i, i);

        }
        // System.out.println("Tamanho do dicionário inicial: " + dictionary.size());

        String current = "";
        // List where the codes will be saved before writing it in file
        List<Integer> result = new ArrayList<>();
        DataOutputStream compressedFile = new DataOutputStream(new FileOutputStream(fileName));

        // loop through the string to compress
        for (char c : fileAsString.toCharArray()) {
            String combined = current + c;
            if (dictionary.containsKey(combined)) {
                current = combined;
            } else {
                result.add(dictionary.get(current));
                dictionary.put(combined, dictionary.size());

                current = "" + c;

            }
        }

        // saves the code of the last char/string in result
        if (!current.equals("")) {
            result.add(dictionary.get(current));
        }

        // loop through result and write the codes in the file
        for (Integer code : result) {
            compressedFile.writeInt(code);

        }

        compressedFile.close();

        long tempoFinal = System.currentTimeMillis();
        long total = tempoFinal - tempoInicial;

        System.out.println("Tempo total para compactação pelo algoritmo LZW foi de " + total + " milessegundos");
    }

    /**
     * Method to unpack a file and overwrite the banco.db file
     * 
     * @param CompressedFileName file to unpack
     * @param newDBFile          original db file to be overwritten
     */
    public void unpack(String CompressedFileName, String newDBFile)
            throws IOException, NumberFormatException, ParseException {

        long tempoInicial = System.currentTimeMillis();

        // create the initial dictionary
        HashMap<Integer, String> dictionary = new HashMap<>();

        for (int i = 0; i < 256; i++) {
            dictionary.put(i, "" + (char) i);

        }

        DataInputStream compressedFile = new DataInputStream(new FileInputStream(CompressedFileName));

        List<Integer> compressed = new ArrayList<>();

        // read the compressed file and populate the list with the codes
        while (compressedFile.available() > 0) {
            compressed.add(compressedFile.readInt());
        }
        compressedFile.close();

        int code = compressed.remove(0);
        String current = dictionary.get(code);
        String unpackedFileString = current;

        // read the list and search/populate the dictionary to create the unpacked
        // string
        for (int nextCode : compressed) {
            String entry;
            if (dictionary.containsKey(nextCode)) {
                entry = dictionary.get(nextCode);
            } else if (nextCode == dictionary.size()) {
                entry = current + current.charAt(0);
            } else {
                throw new IllegalArgumentException("Bad compressed code: " + nextCode);
            }
            unpackedFileString += entry;
            dictionary.put(dictionary.size(), current + entry.charAt(0));
            current = entry;
        }

        // write the unpacked file
        DatabaseAccess db = new DatabaseAccess(newDBFile);
        db.dbFromString(unpackedFileString);

        long tempoFinal = System.currentTimeMillis();
        long total = tempoFinal - tempoInicial;

        System.out.println("Tempo total para descompactação pelo algoritmo LZW foi de " + total + " milessegundos");
    }
}
