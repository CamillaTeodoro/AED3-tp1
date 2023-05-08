import java.io.RandomAccessFile;
import java.util.*;

public class LZW {

    // Atributtes

    // Constructors

    LZW() {

    }

    public void compress(String fileAsString, String fileName) {
        long tempoInicial = System.currentTimeMillis();

        // ler a string e verificar se existe no dicionário o char

        long tempoFinal = System.currentTimeMillis();
        long total = tempoFinal - tempoInicial;

        System.out.println("Tempo total para compactação pelo algoritmo LZW foi de " + total + " milessegundos");
    }

    public void unpacking() {

    }
}
