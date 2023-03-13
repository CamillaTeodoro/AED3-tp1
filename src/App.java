import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class App {
    private static int batchSize = 4;

    /**
     * 
     * @throws IOException
     */
    // Cria o arquivo .db através da leitura do aquivo csv
    static void loadDatabase(DatabaseAccess db) throws IOException {
        try {
            db.clearDb();
            Scanner fileReaderScanner = new Scanner(new File("../test.csv"));
            String line = fileReaderScanner.nextLine(); // Ignora primeira linha do csv

            while (fileReaderScanner.hasNextLine()) {
                line = fileReaderScanner.nextLine();
                Film film = new Film();
                film.ReadText(line);
                db.create(film);
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
                // Grava os 5 proximos filmes no path1 ordenados
                while (currentFilm != null && count < batchSize) {
                    films.add(currentFilm);
                    currentFilm = db.next();
                    count++;
                }
                InsertionSort(films);
                // escreve no arquivo path1
                for (Film film : films) {
                    path1.create(film);
                }
                films.clear();
                count = 0;

                // Grava os 5 proximos filmes no path2 ordenados
                while (currentFilm != null && count < batchSize) {
                    films.add(currentFilm);
                    currentFilm = db.next();
                    count++;
                }
                InsertionSort(films);
                // escreve no arquivo path1
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

    public static void simpleInterpolation() {
        sortBatchOfRegisters();

        // Abrir os dois arquivos
        try {
            DatabaseAccess path1 = new DatabaseAccess("../db/path1.db");
            DatabaseAccess path2 = new DatabaseAccess("../db/path2.db");

            DatabaseAccess path3 = new DatabaseAccess("../db/path3.db");
            DatabaseAccess path4 = new DatabaseAccess("../db/path4.db");

            path3.clearDb();
            path4.clearDb();

            while (!path1.isEndOfFile() && !path2.isEndOfFile()) {
                merge(path1, path2, path3);
                merge(path1, path2, path4);
            }

            path3.print();
            path4.print();

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("");
        System.out.println("Base de dados ordenada com sucesso!");
        System.out.println("");

    }

    public static void merge(DatabaseAccess source1, DatabaseAccess source2, DatabaseAccess destination)
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
