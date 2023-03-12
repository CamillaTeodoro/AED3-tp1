import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class App {

    /**
     * 
     * @throws IOException
     */
    // Cria o arquivo .db através da leitura do aquivo csv
    static void loadDatabase(DatabaseAccess db) throws IOException {
        try {
            Scanner fileReaderScanner = new Scanner(new File("../netflix_titles.csv"));
            String line = fileReaderScanner.nextLine(); // Ignora primeira linha do csv
            line = fileReaderScanner.nextLine();

            while (fileReaderScanner.hasNextLine()) {
                Film film = new Film();
                film.ReadText(line);
                db.create(film);

                line = fileReaderScanner.nextLine();

            }

            fileReaderScanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Base de dados criada com sucesso.");
    }

    public static Film readFilmDataFromUser(Scanner sc, DatabaseAccess db) throws IOException {
        Film film = new Film();

        System.out.println("Digite o id: ");
        int id = Integer.parseInt(sc.nextLine());

        Long idExists = db.find(id);
        while (idExists != (long) -1) {
            System.out.println("ID existente na base de dados. Digite outro valor: ");
            id = Integer.parseInt(sc.nextLine());
            idExists = db.find(id);
        }
        System.out.println("Cadastro do ID informado aceito com sucesso!!");
        film.setShow_id((id));

        // =====================================================
        System.out.println("Digite o Tipo de Filme/Show: ");
        film.setType(sc.nextLine());
        System.out.println("Digite o Título do Filme/Show: ");
        film.setTitle(sc.nextLine());
        System.out.println("Digite o Diretor do Filme/Show: ");
        film.setDirector(sc.nextLine());
        System.out.println("Digite a data de lançamento do Filme/Show: ");
        try {
            String newDate = sc.nextLine();
            film.setDate_added(new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(newDate));
        } catch (Exception e) {
            System.out.println("A data deve ser no formato dd/mm/yyyy.");
        }
        System.out.println("Digite o Ano de estréia do Filme/Show: ");
        film.setRelease_year(Integer.parseInt(sc.nextLine()));
        System.out.println("Digite a Duração do Filme/Show: ");
        film.setDuration(sc.nextLine());
        System.out.println("Digite o Gênero do Filme/Show: ");
        film.setListed_in(sc.nextLine());

        return film;
    }

    static Film readEditDataFromUser(Film film, Scanner sc) {
        int option = -1;
        Film newFilm = new Film(film);

        System.out.println("Digite o número correspondente ao campo que deseja editar:");
        System.out.println("1 - Tipo ");
        System.out.println("2 - Título ");
        System.out.println("3 - Diretor ");
        System.out.println("4 - Data ");
        System.out.println("5 - Ano de estréia ");
        System.out.println("6 - Duração ");
        System.out.println("7 - Gênero ");
        System.out.println("0 - Voltar ao menu inicial ");
        System.out.println("");

        try {
            option = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("O valor digitado deve ser um número!!");
            option = 0;
        }

        switch (option) {
            case 1:

                System.out.println("Digite o novo Tipo do Filme/Show: ");
                String type = sc.nextLine();
                newFilm.setType(type);
                break;

            case 2:
                System.out.println("Digite o novo Título do Filme/Show: ");
                String title = sc.nextLine();
                newFilm.setTitle(title);
                break;
            case 3:
                System.out.println("Digite o novo Diretor do Filme/Show: ");
                String director = sc.nextLine();
                newFilm.setDirector(director);
                break;

            case 4:
                System.out.println("Digite a nova Data do Filme/Show: ");
                String dateAdded = sc.nextLine();
                try {
                    String newDate = sc.nextLine();
                    newFilm.setDate_added(new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(newDate));
                } catch (Exception e) {
                    System.out.println("A data deve ser no formato dd/mm/yyyy.");
                }

                break;

            case 5:
                System.out.println("Digite o novo Ano de Estréia do Filme/Show: ");
                int release_year = Integer.parseInt(sc.nextLine());
                newFilm.setRelease_year(release_year);
                break;

            case 6:
                System.out.println("Digite a nova Duração do Filme/Show: ");
                String duration = sc.nextLine();
                newFilm.setDuration(duration);
                break;

            case 7:
                System.out.println("Digite o novo Gênero do Filme/Show: ");

                String listed_in = sc.nextLine();
                newFilm.setListed_in(listed_in);
                break;

            case 0:
                System.out.println("Voltando ao menu inicial");
                return null;

            default:
                System.out.println("ERRO");
                break;
        }

        return newFilm;
    }

    public static void SortingDatabase(Scanner sc1) {

        int option = -1;

        System.out.println("Por qual algoritmo de ordenação você deseja ordenar?");
        System.out.println("1 - Intercalação balanceada comum");
        System.out.println("2 - Intercalação balanceada com blocos de tamanho variável");
        System.out.println("3 - Intercalação balanceada com seleção por substituição");
        System.out.println("0 - Voltar ao menu inicial");

        try {
            option = sc1.nextInt();
        } catch (Exception e) {
            System.out.println("O valor digitado deve ser um número!!");
            System.out.println("");
            SortingDatabase(sc1);
        }
        switch (option) {
            case 1:
                simpleInterpolation();
                break;
            case 2:
                variableSizeInterpolation();
                break;
            case 3:
                substituteInterpolation();
                break;
            case 0:
                System.out.println("Voltando ao menu inicial");
                break;

            default:
                System.out.println("Opção inválida.");
                break;
        }

    }

    public static void sortBatchOfRegisters() {

        try {
            RandomAccessFile arquivo = new RandomAccessFile("../db/banco.db", "rw");
            RandomAccessFile path1 = new RandomAccessFile("../db/path1.db", "rw");
            RandomAccessFile path2 = new RandomAccessFile("../db/path2.db", "rw");
            arquivo.seek(4);
            int count = 0;
            Long pointerPosition = arquivo.getFilePointer();

            ArrayList<Film> films = new ArrayList<Film>();
            while (pointerPosition != arquivo.length()) {

                while (pointerPosition != arquivo.length() && count < 5) {
                    Film film = new Film();
                    char lapide = arquivo.readChar();
                    if (lapide == '$') {

                        int sizeFilm = arquivo.readInt();
                        // System.out.println(sizeFilm);
                        byte[] b = new byte[sizeFilm];
                        arquivo.read(b);
                        film.fromByteArray(b);
                        films.add(film);
                        // System.out.println("Adicionado filme: " + film.show_id);
                        count++;

                    } else {
                        int sizeRegister = arquivo.readInt();
                        arquivo.seek(arquivo.getFilePointer() + sizeRegister);
                    }
                    pointerPosition = arquivo.getFilePointer();
                }
                InsertionSort(films);
                // escreve no arquivo path1
                for (Film film : films) {

                    byte[] c = film.toByteArray();
                    path1.writeChar('$'); // sinal de registro ativo
                    path1.writeInt(c.length);
                    path1.write(c);

                }
                films.clear();
                count = 0;

                while (pointerPosition != arquivo.length() && count < 5) {
                    Film film = new Film();

                    char lapide = arquivo.readChar();
                    if (lapide == '$') {

                        int sizeFilm = arquivo.readInt();
                        // System.out.println(sizeFilm);
                        byte[] b = new byte[sizeFilm];
                        arquivo.read(b);
                        film.fromByteArray(b);
                        films.add(film);
                        // System.out.println("Adicionado filme: " + film.show_id);
                        count++;

                    } else {
                        int sizeRegister = arquivo.readInt();
                        arquivo.seek(arquivo.getFilePointer() + sizeRegister);
                    }
                    pointerPosition = arquivo.getFilePointer();

                }
                InsertionSort(films);
                // escreve no arquivo path1
                for (Film film : films) {
                    // escreve no arquivo novo
                    byte[] c = film.toByteArray();
                    path2.writeChar('$'); // sinal de registro ativo
                    path2.writeInt(c.length);
                    path2.write(c);

                }
                films.clear();
                count = 0;
                pointerPosition = arquivo.getFilePointer();
            }
            path1.close();
            path2.close();
            arquivo.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void simpleInterpolation() {
        sortBatchOfRegisters();

        // Abrir os dois arquivos
        try {
            RandomAccessFile path1 = new RandomAccessFile("../db/path1.db", "rw");
            RandomAccessFile path2 = new RandomAccessFile("../db/path2.db", "rw");
            RandomAccessFile sortedFile = new RandomAccessFile("../db/sortedFile.db", "rw");

            Long pointerPositionPath1 = path1.getFilePointer();
            Long pointerPositionPath2 = path2.getFilePointer();
            Long pointerPositionSortedFile = sortedFile.getFilePointer();

            path1.seek(0);
            path2.seek(0);
            int countPath1 = 0;
            int countPath2 = 0;
            sortedFile.seek(0);
            sortedFile.writeInt(0);

            Film film1 = new Film();
            char lapide1 = path1.readChar();
            int size1 = path1.readInt();
            byte[] b1 = new byte[size1];
            path1.read(b1);
            film1.fromByteArray(b1);

            Film film2 = new Film();
            char lapide2 = path2.readChar();
            int size2 = path2.readInt();
            byte[] b2 = new byte[size2];
            path2.read(b2);
            film2.fromByteArray(b2);
            while (pointerPositionPath1 != path1.length() && pointerPositionPath2 != path2.length()) {
                // System.out.println("entrando no while:" + pointerPositionPath1 + " " +
                // pointerPositionPath2);

                while (countPath1 < 5 && countPath2 < 5) {

                    if (film1.getShow_id() < film2.getShow_id()) {
                        sortedFile.writeChar(lapide1);
                        sortedFile.writeInt(size1);
                        sortedFile.write(b1);
                        pointerPositionSortedFile = sortedFile.getFilePointer();
                        pointerPositionPath1 = path1.getFilePointer();
                        // System.out.println(film1.getShow_id());
                        sortedFile.seek(0);
                        sortedFile.writeInt(film1.getShow_id());
                        sortedFile.seek(pointerPositionSortedFile);
                        countPath1++;
                        lapide1 = path1.readChar();
                        size1 = path1.readInt();
                        b1 = new byte[size1];
                        path1.read(b1);
                        film1.fromByteArray(b1);

                    } else {
                        sortedFile.writeChar(lapide2);
                        sortedFile.writeInt(size2);
                        sortedFile.write(b2);
                        pointerPositionSortedFile = sortedFile.getFilePointer();
                        pointerPositionPath2 = path2.getFilePointer();
                        // System.out.println(film2.getShow_id());
                        sortedFile.seek(0);
                        sortedFile.writeInt(film2.getShow_id());
                        sortedFile.seek(pointerPositionSortedFile);
                        countPath2++;
                        lapide2 = path2.readChar();
                        size2 = path2.readInt();
                        b2 = new byte[size2];
                        path2.read(b2);
                        film2.fromByteArray(b2);

                    }

                }
                if (countPath1 < countPath2) {
                    for (int i = countPath1; i < 5; i++) {

                        sortedFile.writeChar(lapide1);
                        sortedFile.writeInt(size1);
                        sortedFile.write(b1);
                        pointerPositionSortedFile = sortedFile.getFilePointer();
                        pointerPositionPath1 = path1.getFilePointer();
                        // System.out.println(pointerPositionPath1 + "path1");
                        sortedFile.seek(0);
                        sortedFile.writeInt(film1.getShow_id());
                        sortedFile.seek(pointerPositionSortedFile);
                        lapide1 = path1.readChar();
                        size1 = path1.readInt();
                        b1 = new byte[size1];
                        path1.read(b1);
                        film1.fromByteArray(b1);
                    }
                } else {
                    for (int i = countPath2; i < 5; i++) {
                        // System.out.println("coutpath2: " + i);
                        // System.out.println(film2.getShow_id());
                        sortedFile.writeChar(lapide2);
                        sortedFile.writeInt(size2);
                        sortedFile.write(b2);
                        pointerPositionSortedFile = sortedFile.getFilePointer();
                        pointerPositionPath2 = path2.getFilePointer();
                        // System.out.println(pointerPositionPath2 + "path2");
                        sortedFile.seek(0);
                        sortedFile.writeInt(film2.getShow_id());
                        sortedFile.seek(pointerPositionSortedFile);
                        lapide2 = path2.readChar();
                        size2 = path2.readInt();
                        b2 = new byte[size2];
                        path2.read(b2);
                        film2.fromByteArray(b2);
                    }

                }
                countPath1 = 0;
                countPath2 = 0;
                pointerPositionPath1 = path1.getFilePointer();
                pointerPositionPath2 = path2.getFilePointer();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("");
        System.out.println("Base de dados ordenada com sucesso!");
        System.out.println("");

    }

    public static void variableSizeInterpolation() {
        sortBatchOfRegisters();

        System.out.println("");
        System.out.println("Base de dados ordenada com sucesso!");
        System.out.println("");
    }

    public static void substituteInterpolation() {
        try {
            RandomAccessFile arquivo = new RandomAccessFile("../db/banco.db", "rw");

            arquivo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void InsertionSort(ArrayList<Film> films) throws Exception {
        Film temp = null;

        for (int i = 1; i < films.size(); i++) {

            temp = films.get(i);
            // System.out.println("Temp: " + temp);
            int j = i - 1; // inicia com 0

            while (j >= 0 && temp.getShow_id() < films.get(j).getShow_id()) {

                films.set(j + 1, films.get(j));
                j--;
                // System.out.println(j);
            }
            films.set(j + 1, temp);

        }
        // imprime registros ordenados
        // for (Film film : films) {
        // film.print();
        // }

    }

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);
        DatabaseAccess db = new DatabaseAccess("../db/banco.db");
        int option = -1;
        int id = 0;

        do {
            System.out.println("Entre com uma opção:");
            System.out.println("");
            System.out.println("1 - Criar um novo registro");
            System.out.println("2 - Ler um registro da base de dados");
            System.out.println("3 - Atualizar um registro da base de dados");
            System.out.println("4 - Deletar um registro da base de dados");
            System.out.println("5 - Ordenar a base de dados");
            System.out.println("6 - Criar a base de dados");
            System.out.println("0 - Sair");
            System.out.println();

            try {

                option = Integer.parseInt(sc.nextLine());

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("O valor digitado deve ser um número!!");
                option = 0;
            }

            switch (option) {
                case 1: {
                    Film film = readFilmDataFromUser(sc, db);

                    boolean result = db.create(film);
                    if (result) {
                        System.out.println("Registro criado com sucesso!");
                    } else {
                        System.out.println("Erro ao cadastrar!");
                    }

                    System.out.println();
                    break;
                }
                case 2: {
                    System.out.println("Digite o id do Show que você deseja ver: ");
                    try {
                        id = Integer.parseInt(sc.nextLine());
                        Film film = db.read(id);
                        if (film == null) {
                            System.out.println("Filme/Show não existe na base de dados!");
                        } else {
                            film.print();
                        }
                    } catch (Exception e) {
                        System.out.println("O valor digitado deve ser um número!!");
                        option = 0;
                    }
                    break;
                }
                case 3: {
                    System.out.println("Digite o id do Show que você deseja atualizar: ");
                    try {
                        id = Integer.parseInt(sc.nextLine());
                        Film film = db.read(id);
                        film.print();
                        Film editedFilm = readEditDataFromUser(film, sc);
                        if (editedFilm == null) {
                            break;
                        }
                        boolean result = db.update(film, editedFilm);
                        if (result) {
                            System.out.println("Registro editado com sucesso!");
                        } else {
                            System.out.println("Erro ao editar!");
                        }

                        System.out.println();

                    } catch (Exception e) {
                        System.out.println("O valor digitado deve ser um número!!");
                        option = 0;
                    }
                    break;
                }

                case 4: {
                    System.out.println("Digite o id do Show que você deseja deletar: ");
                    try {
                        id = Integer.parseInt(sc.nextLine());
                        Boolean result = db.delete(id);
                        if (result) {
                            System.out.println("Registro deletado com sucesso!");
                        } else {
                            System.out.println("Erro ao deletar!");
                        }

                        System.out.println();
                    } catch (Exception e) {
                        System.out.println("O valor digitado deve ser um número!!");
                        option = 0;
                    }
                    break;
                }
                case 5: {
                    SortingDatabase(sc);
                    break;
                }
                case 6:
                    loadDatabase(db);
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;

                default:
                    System.out.println("Opção inválida.");
                    break;
            }

        } while (option != 0);
        sc.close();
        db.close();
    }
}
