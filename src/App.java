import java.io.*;
import java.util.Scanner;

public class App {

    static void create() throws IOException {
        try {
            RandomAccessFile arq = new RandomAccessFile("../db/banco.db", "rw");
            Scanner fileReaderScanner = new Scanner(new File("../netflix_titles.csv"));
            String line = fileReaderScanner.nextLine();
            line = fileReaderScanner.nextLine(); // Ignora primeira linha do csv
            long pointerPosition;
            byte[] b;
            arq.writeInt(0);
            arq.seek(4);
            while (fileReaderScanner.hasNextLine()) {
                Film film = new Film();
                film.ReadText(line);
                b = film.toByteArray();
                arq.writeChar('$'); // sinal de registro ativo

                arq.writeInt(b.length);
                arq.write(b);
                pointerPosition = arq.getFilePointer();
                // Volta ponteiro para cabeçalho e atualiza id
                arq.seek(0);
                arq.writeInt(film.show_id);
                arq.seek(pointerPosition);

                line = fileReaderScanner.nextLine();

            }
            arq.close();
            fileReaderScanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void read(int id) throws IOException {
        try {
            RandomAccessFile arq = new RandomAccessFile("../db/banco.db", "rw");
            Scanner fileReaderScanner = new Scanner(new File("../netflix_titles.csv"));

            arq.close();
            fileReaderScanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void update(int id) throws IOException {
        try {
            RandomAccessFile arq = new RandomAccessFile("../db/banco.db", "rw");
            Scanner fileReaderScanner = new Scanner(new File("../netflix_titles.csv"));

            arq.close();
            fileReaderScanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void delete(int id) throws IOException {
        try {
            RandomAccessFile arq = new RandomAccessFile("../db/banco.db", "rw");
            arq.seek(0);
            int finalId = arq.readInt();
            long pointerPosition;
            pointerPosition = arq.getFilePointer();
            while (pointerPosition < arq.length()) {
                char lapide = arq.readChar();
                int size = arq.readInt();
                int filmID = arq.readInt();
                if (filmID == id && lapide == '$') {
                    arq.seek(pointerPosition);
                    arq.writeChar('*'); // informa que o arquivo está deletado
                    System.out.println("Filme/Show deletado com sucesso");
                    System.out.println();
                    return;
                } else if (filmID == id && lapide == '*') {
                    System.out.println("Filme/Show não existe na base de dados.");
                    System.out.println();
                }
                arq.seek(pointerPosition + 6 + size);
                pointerPosition = arq.getFilePointer();

            }

            arq.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);
        int option = -1;
        int id = 0;

        do {
            System.out.println("Entre com uma opção:");
            System.out.println("1 - Criar a base de dados");
            System.out.println("2 - Ler um registro da base de dados");
            System.out.println("3 - Atualizar um registro da base de dados");
            System.out.println("4 - Deletar um registro da base de dados");
            System.out.println("5 - Ordenar a base de dados");
            System.out.println("0 - Sair");
            System.out.println();

            option = sc.nextInt();

            switch (option) {
                case 1:
                    create();
                    System.out.println("Arquivo criado com sucesso.");
                    System.out.println();
                    break;
                case 2:
                    System.out.println("Digite o id do Show que você deseja ver: ");
                    id = sc.nextInt();
                    read(id);
                    break;
                case 3:
                    System.out.println("Digite o id do Show que você deseja atualizar: ");
                    id = sc.nextInt();
                    update(id);
                    break;
                case 4:
                    System.out.println("Digite o id do Show que você deseja deletar: ");
                    id = sc.nextInt();
                    delete(id);
                    break;
                case 5:
                    System.out.println("Opção 5");
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;

                default:
                    break;
            }

        } while (option != 0);
        sc.close();
    }
}
