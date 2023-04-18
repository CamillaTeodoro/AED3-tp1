import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class App {
    private static int batchSize = 5;
    private static final String CSV_PATH = "../netflix_titles.csv";
    private static final String DB_PATH = "../db/banco.db";
    private static final String BTREE_PATH = "../db/bTree.db";
    private static final String HASH_DIR_PATH = "../db/HashDir.db";
    private static final String HASH_IND_PATH = "../db/HashInd.db";

    /**
     * Read the csv file and load it into the database
     * 
     * @param db database access object
     * @param hh hash object
     * @throws IOException
     */
    static void loadDatabase(DatabaseAccess db, BTree bTree, Hash hh) throws IOException {
        try {
            db.clearDb();
            hh.reset();
            bTree.reset();
            Scanner fileReaderScanner = new Scanner(new File(CSV_PATH));
            String line = fileReaderScanner.nextLine(); // Ignora primeira linha do csv

            while (fileReaderScanner.hasNextLine()) {
                line = fileReaderScanner.nextLine();
                if (line.trim().length() == 0) {
                    continue;
                }

                Film film = new Film();
                film.ReadText(line);

                // System.out.println("Inserting: " + film.getShow_id());

                Long address = db.create(film);
                hh.inserir(film.getShow_id(), address);
                bTree.insert(film.getShow_id(), address);
            }

            // bTree.printStructure();

            fileReaderScanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Base de dados criada com sucesso.");
    }

    /**
     * Receive from user the data
     * 
     * @param sc
     * @param db
     * @return
     * @throws IOException
     */
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
            return null;
        }
        System.out.println("Digite o Ano de estréia do Filme/Show: ");
        film.setRelease_year(Integer.parseInt(sc.nextLine()));
        System.out.println("Digite a Duração do Filme/Show: ");
        film.setDuration(sc.nextLine());
        System.out.println("Digite o Gênero do Filme/Show: ");
        film.setListed_in(sc.nextLine().split(","));

        return film;
    }

    /**
     * Receive from user the data that he wants to update
     * 
     * @param film
     * @param sc
     * @return
     */
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

                try {
                    String newDate = sc.nextLine();
                    newFilm.setDate_added(new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(newDate));
                } catch (Exception e) {
                    System.out.println("A data deve ser no formato dd/mm/yyyy.");
                    return null;
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
                newFilm.setListed_in(listed_in.split(","));
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

    /**
     * Create the menu from the sorting methods
     * 
     * @param sc1
     */
    public static void SortingDatabase(Scanner sc1) {

        int option = -1;

        System.out.println("Por qual algoritmo de ordenação você deseja ordenar?");
        System.out.println("1 - Intercalação balanceada comum");
        System.out.println("2 - Intercalação balanceada com blocos de tamanho variável");
        System.out.println("3 - Intercalação balanceada com seleção por substituição");
        System.out.println("0 - Voltar ao menu inicial");

        try {
            option = Integer.parseInt(sc1.nextLine());
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

    /**
     * Read the banco file
     * Sort small batches of records from db and save it into two paths
     */
    public static void sortBatchOfRecords() {
        try {
            DatabaseAccess db = new DatabaseAccess("../db/banco.db");

            DatabaseAccess path1 = new DatabaseAccess("../db/path1.db");
            DatabaseAccess path2 = new DatabaseAccess("../db/path2.db");

            path1.clearDb();
            path2.clearDb();

            int count = 0;
            db.resetPosition();
            Film currentFilm = db.next();

            ArrayList<Film> films = new ArrayList<Film>();
            while (currentFilm != null) {
                // Save the next sorted 5 films into path1
                while (currentFilm != null && count < batchSize) {
                    films.add(currentFilm);
                    currentFilm = db.next();
                    count++;
                }
                InsertionSort(films);
                // write into path1
                for (Film film : films) {
                    path1.create(film);
                }
                films.clear();
                count = 0;

                // Save the next sorted 5 films into path2
                while (currentFilm != null && count < batchSize) {
                    films.add(currentFilm);
                    currentFilm = db.next();
                    count++;
                }
                InsertionSort(films);
                // write into path2
                for (Film film : films) {
                    path2.create(film);
                }
                films.clear();
                count = 0;
            }
            db.close();

            path1.close();
            path2.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * executes a simple interpolation to sorte the file
     */
    public static void simpleInterpolation() {

        sortBatchOfRecords();

        // Abrir os dois arquivos
        try {
            DatabaseAccess path1 = new DatabaseAccess("../db/path1.db");
            DatabaseAccess path2 = new DatabaseAccess("../db/path2.db");

            DatabaseAccess path3 = new DatabaseAccess("../db/path3.db");
            DatabaseAccess path4 = new DatabaseAccess("../db/path4.db");

            int internalBatchSize = batchSize;

            do {
                path1.resetPosition();
                path2.resetPosition();
                path3.clearDb();
                path4.clearDb();

                while (!path1.isEndOfFile() && !path2.isEndOfFile()) {
                    merge(path1, path2, path3, internalBatchSize);
                    merge(path1, path2, path4, internalBatchSize);
                }

                // Double the batch size
                internalBatchSize = internalBatchSize * 2;

                // Swap files 1 and 3
                DatabaseAccess temp = path1;
                path1 = path3;
                path3 = temp;

                // Swap files 2 and 4
                DatabaseAccess temp2 = path2;
                path2 = path4;
                path4 = temp2;

            } while (path2.length() > 4);

            DatabaseAccess db = new DatabaseAccess("../db/banco.db");
            db.clearDb();
            path1.resetPosition();
            while (!path1.isEndOfFile()) {
                db.create(path1.next());
            }

            // It should be sorted now
            System.out.println("");
            db.print();

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("");
        System.out.println("Base de dados ordenada com sucesso!");
        System.out.println("");

    }

    /**
     * merge two files into a new one file using constant batch sizes
     * 
     * @param source1
     * @param source2
     * @param destination
     * @param batchSize
     * @throws IOException
     */
    public static void merge(
            DatabaseAccess source1,
            DatabaseAccess source2,
            DatabaseAccess destination,
            int batchSize)
            throws IOException {
        Film film1 = source1.next();
        Film film2 = source2.next();

        int count1 = batchSize;
        int count2 = batchSize;

        while (count1 > 0 && count2 > 0 && film1 != null && film2 != null) {
            if (film1.getShow_id() < film2.getShow_id()) {
                destination.create(film1);
                if (--count1 != 0) {
                    film1 = source1.next();
                }
            } else {
                destination.create(film2);
                if (--count2 != 0) {
                    film2 = source2.next();
                }
            }

        }

        while (count1 != 0 && film1 != null) {
            destination.create(film1);
            if (--count1 != 0) {
                film1 = source1.next();
            }

        }

        while (count2 != 0 && film2 != null) {
            destination.create(film2);
            if (--count2 != 0) {
                film2 = source2.next();
            }
        }

    }

    /**
     * merge two files into a new one file using variable batch sizes
     * 
     * @param source1
     * @param source2
     * @param destination
     * @param batchSize
     * @throws IOException
     */
    public static void mergeVariableSize(
            DatabaseAccess source1,
            DatabaseAccess source2,
            DatabaseAccess destination,
            int batchSize)
            throws IOException {
        Film film1 = source1.next();
        Film film2 = source2.next();

        int count1 = batchSize;
        int count2 = batchSize;

        while (count1 > 0 && count2 > 0 && film1 != null && film2 != null) {
            if (film1.getShow_id() < film2.getShow_id()) {
                destination.create(film1);
                if (--count1 != 0) {

                    film1 = source1.next();

                } else {
                    int id = film1.getShow_id();
                    Long pointerPosition = source1.getPosition();
                    film1 = source1.next();
                    if (film1 == null) {
                        break;
                    }

                    if (id <= film1.getShow_id()) {
                        count1 += batchSize;
                    } else {
                        source1.setPosition(pointerPosition);

                    }

                }
            } else {
                destination.create(film2);
                if (--count2 != 0) {
                    film2 = source2.next();

                } else {
                    int id = film2.getShow_id();
                    Long pointerPosition = source2.getPosition();
                    film2 = source2.next();
                    if (film2 == null) {
                        break;
                    }

                    if (id <= film2.getShow_id()) {
                        count2 += batchSize;
                    } else {
                        source2.setPosition(pointerPosition);

                    }

                }
            }
        }

        while (count1 != 0 && film1 != null) {
            destination.create(film1);
            if (--count1 != 0) {
                film1 = source1.next();
            }

        }

        while (count2 != 0 && film2 != null) {
            destination.create(film2);
            if (--count2 != 0) {
                film2 = source2.next();
            }
        }

    }

    public static void variableSizeInterpolation() {
        sortBatchOfRecords();

        // open 4 files
        try {
            DatabaseAccess path1 = new DatabaseAccess("../db/path1.db");
            DatabaseAccess path2 = new DatabaseAccess("../db/path2.db");

            DatabaseAccess path3 = new DatabaseAccess("../db/path3.db");
            DatabaseAccess path4 = new DatabaseAccess("../db/path4.db");

            int internalBatchSize = batchSize;

            do {
                path1.resetPosition();
                path2.resetPosition();
                path3.clearDb();
                path4.clearDb();

                while (!path1.isEndOfFile() && !path2.isEndOfFile()) {
                    mergeVariableSize(path1, path2, path3, internalBatchSize);
                    mergeVariableSize(path1, path2, path4, internalBatchSize);
                }

                // Double the batch size
                internalBatchSize = internalBatchSize * 2;

                // Swap files 1 and 3
                DatabaseAccess temp = path1;
                path1 = path3;
                path3 = temp;

                // Swap files 2 and 4
                DatabaseAccess temp2 = path2;
                path2 = path4;
                path4 = temp2;

            } while (path2.length() > 4);

            DatabaseAccess db = new DatabaseAccess("../db/banco.db");
            db.clearDb();
            path1.resetPosition();
            while (!path1.isEndOfFile()) {
                db.create(path1.next());
            }

            // It should be sorted now
            System.out.println("");
            db.print();

            System.out.println("");
            System.out.println("Base de dados ordenada com sucesso!");
            System.out.println("");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void substituteInterpolation() {

    }

    /**
     * method to sort the batch of records
     * 
     * @param films
     * @throws Exception
     */
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
        // print sorted records
        // for (Film film : films) {
        // film.print();
        // }

    }

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);
        DatabaseAccess db = new DatabaseAccess(DB_PATH);
        Hash hh = new Hash(HASH_DIR_PATH, HASH_IND_PATH);
        int option = -1;
        int id = 0;

        // Create the tree
        BTree bTree = new BTree(BTREE_PATH);

        // bTree.printStructure();

        do {
            System.out.println("");
            System.out.println("Entre com uma opção:");
            System.out.println("");
            System.out.println("1 - Criar um novo registro");
            System.out.println("2 - Ler um registro da base de dados");
            System.out.println("3 - Atualizar um registro da base de dados");
            System.out.println("4 - Deletar um registro da base de dados");
            System.out.println("5 - Ordenar a base de dados");
            System.out.println("6 - Criar a base de dados");
            System.out.println("7 - Ler um registro usando o arquivo de index em árvore B");
            System.out.println("8 - Atualizar um registro usando o arquivo de index em árvore B");
            System.out.println("9 - Deletar um registro usando o arquivo de index em árvore B");
            System.out.println("10 - Busca via hash");
            System.out.println("11 - Atualização via hash");
            System.out.println("12 - Deletar via hash");
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
                    if (film == null) {
                        System.out.println("Erro ao cadastrar!");
                    } else {
                        Long address = db.create(film);
                        bTree.insert(film.getShow_id(), address);
                        hh.inserir(film.getShow_id(), address);
                        if (address != -1) {
                            System.out.println("Registro criado com sucesso!");
                        } else {
                            System.out.println("Erro ao cadastrar!");
                        }
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
                        if (film != null) {
                            film.print();
                            Film editedFilm = readEditDataFromUser(film, sc);
                            if (editedFilm == null) {
                                System.out.println("Erro ao editar!");
                                break;
                            }
                            long addr = db.update(film, editedFilm);
                            boolean result, result2;
                            if (addr == -1) {
                                result = false;
                                result2 = false;
                            } else if (addr == -2) {
                                result = true;
                                result2 = true;
                            } else {
                                result = true;
                                result2 = hh.atualizar(film.getShow_id(), addr);
                            }
                            if (result) {
                                System.out.println("Registro editado com sucesso!");
                            } else {
                                System.out.println("Erro ao editar!");
                            }

                            System.out.println();
                        } else {
                            System.out.println("Filme/Show não existe na base de dados");
                        }

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

                        // TODO: Consider duplicating this case
                        boolean result2 = hh.deletar(id);
                        if (result && result2) {
                            System.out.println("Registro deletado com sucesso!");
                        } else {
                            System.out.println("Erro ao deletar!");
                        }

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
                    loadDatabase(db, bTree, hh);

                    break;
                case 7:
                    System.out.println("Digite o id do Show que você deseja ver: ");
                    try {
                        id = Integer.parseInt(sc.nextLine());
                        Long idAddress = bTree.findIndexInBTreeFile(id);
                        if (idAddress == -1) {
                            System.out.println("Filme/Show não existe na base de dados!");
                        } else {
                            Film film = db.read(idAddress);
                            film.print();
                        }
                    } catch (Exception e) {
                        System.out.println("O valor digitado deve ser um número!!");
                        option = 0;
                    }
                    break;

                case 8: {
                    System.out.println("Digite o id do Show que você deseja atualizar: ");
                    try {
                        id = Integer.parseInt(sc.nextLine());
                        Long oldAddress = bTree.findIndexInBTreeFile(id);
                        Film film = db.read(oldAddress);
                        if (film != null) {
                            film.print();
                            Film editedFilm = readEditDataFromUser(film, sc);
                            if (editedFilm == null) {
                                System.out.println("Erro ao editar!");
                                break;
                            }
                            Long newAddress = db.updateWithAddress(film, editedFilm, oldAddress);
                            if (newAddress == -1) {
                                System.out.println("Erro ao editar!");
                            } else {
                                if (newAddress != oldAddress) {
                                    boolean result = bTree.updateDBAddress(id, newAddress);
                                    boolean result2 = hh.atualizar(id, newAddress);
                                    if (!result && result2) {
                                        System.out.println("Erro ao atualizar a arvore btree!");
                                    }
                                }

                                System.out.println("Registro editado com sucesso!");
                            }

                            System.out.println();
                        } else {
                            System.out.println("Filme/Show não existe na base de dados");
                        }

                    } catch (Exception e) {
                        System.out.println("O valor digitado deve ser um número!!");
                        option = 0;
                    }
                    break;
                }

                case 9: {
                    System.out.println("Digite o id do Show que você deseja deletar: ");
                    try {
                        id = Integer.parseInt(sc.nextLine());
                        Long idAddress = bTree.findIndexInBTreeFile(id);
                        Boolean result = db.delete(idAddress);
                        if (result) {
                            bTree.delete(id);
                            hh.deletar(id);
                            System.out.println("Registro deletado com sucesso!");
                        } else {
                            System.out.println("Erro ao deletar!");
                        }

                    } catch (Exception e) {
                        System.out.println("O valor digitado deve ser um número!!");
                        option = 0;
                    }
                    break;
                }

                case 10: {
                    System.out.println("Digite o id do Show que você deseja ver: ");
                    try {
                        int movieID = Integer.parseInt(sc.nextLine());
                        if (movieID > 0) {
                            long addr = hh.ler(movieID);
                            if (addr == -1) {
                                System.out.println("Filme não encontrado!");
                            } else {
                                db.readFromAddr(addr).print();
                            }
                        } else {
                            System.out.println("Por favor,insira um id válido!(maior que 0)");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                    break;
                case 11: {
                    System.out.println("Digite o id do Show que você deseja atualizar: ");
                    try {
                        id = Integer.parseInt(sc.nextLine());
                        Long oldAddress = hh.ler(id);
                        if (oldAddress == -1) {
                            System.out.println("Filme não encontrado!");
                        } else {
                            Film film = db.readFromAddr(oldAddress);
                            if (film != null) {
                                film.print();
                                Film editedFilm = readEditDataFromUser(film, sc);
                                if (editedFilm == null) {
                                    System.out.println("Erro ao editar!");
                                    break;
                                }
                                Long newAddress = db.updateWithAddress(film, editedFilm, oldAddress);
                                if (newAddress == -1) {
                                    System.out.println("Erro ao editar!");
                                } else {
                                    if (newAddress != oldAddress) {
                                        boolean result = hh.atualizar(id, newAddress);
                                        boolean result2 = bTree.updateDBAddress(id, newAddress);
                                        if (result && !result2) {
                                            System.out.println("Erro ao atualizar o hash!");
                                        }
                                    }

                                    System.out.println("Registro editado com sucesso!");
                                }

                                System.out.println();
                            } else {
                                System.out.println("Filme/Show não existe na base de dados");
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("O valor digitado deve ser um número!!");
                    }
                }
                    break;
                case 12: {
                    System.out.println("Digite o id do Show que você deseja deletar: ");
                    try {
                        id = Integer.parseInt(sc.nextLine());
                        Long idAddress = hh.ler(id);
                        Boolean result = db.delete(idAddress);
                        if (result) {
                            hh.deletar(id);
                            bTree.delete(id);
                            System.out.println("Registro deletado com sucesso!");
                        } else {
                            System.out.println("Erro ao deletar!");
                        }

                    } catch (Exception e) {
                        System.out.println("O valor digitado deve ser um número!!");
                        option = 0;
                    }
                }
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
        hh.fechar();
    }
}
