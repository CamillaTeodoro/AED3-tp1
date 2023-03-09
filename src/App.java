import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class App {

    /**
     * 
     * @throws IOException
     */
    // Cria o arquivo .db através da leitura do aquivo csv
    static void loadDatabase() throws IOException {
        try {
            RandomAccessFile arquivo = new RandomAccessFile("../db/banco.db", "rw");
            Scanner fileReaderScanner = new Scanner(new File("../netflix_titles.csv"));
            String line = fileReaderScanner.nextLine(); // Ignora primeira linha do csv
            line = fileReaderScanner.nextLine();
            long pointerPosition;
            byte[] b;
            arquivo.writeInt(0);
            arquivo.seek(4);
            while (fileReaderScanner.hasNextLine()) {
                Film film = new Film();
                film.ReadText(line);
                b = film.toByteArray();
                arquivo.writeChar('$'); // sinal de registro ativo

                arquivo.writeInt(b.length);
                arquivo.write(b);
                pointerPosition = arquivo.getFilePointer();
                // Volta ponteiro para cabeçalho e atualiza id
                arquivo.seek(0);
                arquivo.writeInt(film.show_id);
                arquivo.seek(pointerPosition);

                line = fileReaderScanner.nextLine();

            }
            arquivo.close();
            fileReaderScanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Base de dados criada com sucesso.");
    }

    /**
     * @throws IOException
     */
    static void create() throws IOException {

        try {
            RandomAccessFile arquivo = new RandomAccessFile("../db/banco.db", "rw");
            Scanner sc = new Scanner(System.in);
            Film film = new Film();
            byte[] b;
            Long idExists;

            System.out.println("Digite o id: ");
            int id = Integer.parseInt(sc.nextLine());
            idExists = read(id);
            while (idExists != (long) -1) {
                System.out.println("ID existente na base de dados. Digite outro valor: ");
                id = Integer.parseInt(sc.nextLine());
                idExists = read(id);
            }
            System.out.println("Cadastro do ID informado aceito com sucesso!!");
            film.setShow_id((id));
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

            arquivo.seek(arquivo.length());
            b = film.toByteArray();
            arquivo.writeChar('$'); // sinal de registro ativo

            arquivo.writeInt(b.length);
            arquivo.write(b);

            // Volta ponteiro para cabeçalho e atualiza id
            arquivo.seek(0);
            arquivo.writeInt(film.show_id);

            arquivo.close();
            sc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Registro criado com sucesso.");
    }

    /**
     * 
     * @param id
     * @throws IOException
     */
    // Lê um id do teclado e mostra na tela o filme/show
    static Long read(int id) throws IOException {
        try {
            RandomAccessFile arquivo = new RandomAccessFile("../db/banco.db", "rw");

            arquivo.seek(4);
            long fileSize = arquivo.length();
            long pointerPosition;
            long beginOfRegister = arquivo.getFilePointer();
            pointerPosition = arquivo.getFilePointer();

            while (pointerPosition < fileSize) {

                Film film = new Film();

                beginOfRegister = arquivo.getFilePointer();

                char lapide = arquivo.readChar();
                int size = arquivo.readInt();
                int filmID = arquivo.readInt();
                if (filmID == id && lapide == '$') {

                    // System.out.println("cheguei aqui.");
                    arquivo.seek(pointerPosition + 2);
                    int sizeFilm = arquivo.readInt();
                    // System.out.println(sizeFilm);
                    byte[] b = new byte[sizeFilm];
                    arquivo.read(b);
                    film.fromByteArray(b);

                    film.print();

                    System.out.println();
                    return beginOfRegister;
                }

                arquivo.seek(pointerPosition + 6 + size);
                pointerPosition = arquivo.getFilePointer();

            }

            System.out.println("Filme/Show não existe na base de dados.");
            System.out.println();

            arquivo.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return (long) -1;

    }

    /**
     * 
     * @param id
     * @throws IOException
     * 
     *                     1 - move pointer para inicio do tipo;
     *                     2 - le o tipo
     *                     3 - verifica o tamanho do tipo
     *                     4 - compara o tamanho do tipo antigo com o novo
     *                     5 - se o novo for menor, so substitui
     *                     6 - se for maior, colocar lápide no velho e copiar o novo
     *                     registro pro fim do
     *                     arquivo
     *                     7 - alterar o id do cabeçalho do arquivo
     */

    // Faz o update do filme/show
    static void update(int id) throws IOException {
        try {
            RandomAccessFile arquivo = new RandomAccessFile("../db/banco.db", "rw");
            Scanner sc = new Scanner(System.in);
            int option = -1;
            Long beginOfRegister;

            beginOfRegister = read(id);
            // (long) -1 retorno do read() quando registro não existe
            if (beginOfRegister != (long) -1) {
                Long whereToUpdate = beginOfRegister;
                // System.out.println(beginOfRegister);
                // System.out.println(whereToUpdate);
                Film film = new Film();
                arquivo.seek(beginOfRegister + 2);
                int sizeFilm = arquivo.readInt();
                byte[] b = new byte[sizeFilm];
                arquivo.read(b);
                film.fromByteArray(b);
                // System.out.println(film.director);

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
                    option = sc.nextInt();
                } catch (Exception e) {
                    System.out.println("O valor digitado deve ser um número!!");
                    option = 0;
                }

                // Limpa o \n no ultimo nextInt()
                sc.nextLine();

                switch (option) {
                    case 1:

                        System.out.println("Digite o novo Tipo do Filme/Show: ");
                        String type = sc.nextLine();
                        // coloca ponteiro na posição do type
                        arquivo.seek(whereToUpdate + 10);
                        // salva posição do type numa variável
                        Long pointerPosiLong = arquivo.getFilePointer();
                        String oldType = arquivo.readUTF();
                        int sizeOldType = oldType.length();
                        if (sizeOldType >= type.length()) {
                            String typeResult = type;
                            arquivo.seek(pointerPosiLong);
                            while (typeResult.length() < sizeOldType) {
                                typeResult += " ";
                            }
                            arquivo.writeUTF(typeResult);
                            film.type = typeResult;
                            System.out.println("Registro editado com sucesso.");
                            System.out.println("");
                            System.out.println("Novos dados: ");
                            film.print();

                        } else {
                            arquivo.seek(whereToUpdate);
                            arquivo.writeChar('*');
                            film.type = type;
                            byte[] c = film.toByteArray();
                            arquivo.seek(arquivo.length());
                            arquivo.writeChar('$');
                            arquivo.writeInt(c.length);
                            arquivo.write(c);
                            // Volta ponteiro para cabeçalho e atualiza id
                            arquivo.seek(0);
                            arquivo.writeInt(film.show_id);
                            System.out.println("Registro editado com sucesso.");
                            System.out.println("");
                            System.out.println("Novos dados: ");
                            film.print();

                        }

                        System.out.println();

                        break;
                    case 2:
                        System.out.println("Digite o novo Título do Filme/Show: ");
                        String title = sc.nextLine();
                        // coloca ponteiro na posição do type
                        arquivo.seek(whereToUpdate + 10);
                        type = arquivo.readUTF();
                        // salva posição do title numa variável
                        pointerPosiLong = arquivo.getFilePointer();
                        String oldtitle = arquivo.readUTF();
                        int sizeOldtitle = oldtitle.length();
                        if (sizeOldtitle >= title.length()) {
                            String titleResult = title;
                            arquivo.seek(pointerPosiLong);
                            while (titleResult.length() < sizeOldtitle) {
                                titleResult += " ";
                            }
                            arquivo.writeUTF(titleResult);
                            film.title = titleResult;
                            System.out.println("Registro editado com sucesso.");
                            System.out.println("");
                            System.out.println("Novos dados: ");
                            film.print();

                        } else {
                            arquivo.seek(whereToUpdate);
                            // System.out.println(whereToUpdate);
                            arquivo.writeChar('*');
                            film.title = title;
                            byte[] c = film.toByteArray();
                            arquivo.seek(arquivo.length());
                            arquivo.writeChar('$');
                            arquivo.writeInt(c.length);
                            arquivo.write(c);
                            // Volta ponteiro para cabeçalho e atualiza id
                            arquivo.seek(0);
                            arquivo.writeInt(film.show_id);
                            System.out.println("Registro editado com sucesso.");
                            System.out.println("");
                            System.out.println("Novos dados: ");
                            film.print();

                        }

                        System.out.println();

                        break;
                    case 3:
                        System.out.println("Digite o novo Diretor do Filme/Show: ");
                        String director = sc.nextLine();
                        // coloca ponteiro na posição do type
                        arquivo.seek(whereToUpdate + 10);
                        type = arquivo.readUTF();
                        title = arquivo.readUTF();
                        // salva posição do director numa variável
                        pointerPosiLong = arquivo.getFilePointer();
                        String oldDirector = arquivo.readUTF();
                        int sizeOldDirector = oldDirector.length();
                        if (sizeOldDirector >= director.length()) {
                            String directorResult = director;
                            arquivo.seek(pointerPosiLong);
                            while (directorResult.length() < sizeOldDirector) {
                                directorResult += " ";
                            }
                            arquivo.writeUTF(directorResult);
                            film.director = directorResult;
                            System.out.println("Registro editado com sucesso.");
                            System.out.println("");
                            System.out.println("Novos dados: ");
                            film.print();

                        } else {
                            arquivo.seek(whereToUpdate);
                            arquivo.writeChar('*');
                            film.director = director;
                            byte[] c = film.toByteArray();
                            arquivo.seek(arquivo.length());
                            arquivo.writeChar('$');
                            arquivo.writeInt(c.length);
                            arquivo.write(c);
                            // Volta ponteiro para cabeçalho e atualiza id
                            arquivo.seek(0);
                            arquivo.writeInt(film.show_id);
                            System.out.println("Registro editado com sucesso.");
                            System.out.println("");
                            System.out.println("Novos dados: ");
                            film.print();

                        }

                        System.out.println();

                        break;

                    case 4:
                        System.out.println("Digite a nova Data do Filme/Show: ");

                        String dateAdded = sc.nextLine();
                        // coloca ponteiro na posição do type
                        arquivo.seek(whereToUpdate + 10);
                        type = arquivo.readUTF();
                        title = arquivo.readUTF();
                        director = arquivo.readUTF();
                        // salva posição do date_added numa variável
                        pointerPosiLong = arquivo.getFilePointer();

                        String oldDate_added = arquivo.readUTF();
                        int sizeOldDate_added = oldDate_added.length();
                        Date newDate_added;
                        int sizeFormatNewDate_added;
                        String formatNewDate_added;

                        try {
                            newDate_added = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                                    .parse(dateAdded);
                            formatNewDate_added = new SimpleDateFormat("MMMMM dd, yyyy", Locale.ENGLISH)
                                    .format(newDate_added);
                            sizeFormatNewDate_added = formatNewDate_added.length();
                            if (sizeOldDate_added >= sizeFormatNewDate_added) {
                                String date_addedResult = formatNewDate_added;
                                arquivo.seek(pointerPosiLong);
                                while (date_addedResult.length() < sizeOldDate_added) {
                                    date_addedResult += " ";
                                }
                                arquivo.writeUTF(date_addedResult);
                                film.date_added = newDate_added;

                                System.out.println("Registro editado com sucesso.");
                                System.out.println("");
                                System.out.println("Novos dados: ");
                                film.print();

                            } else {
                                arquivo.seek(whereToUpdate);
                                arquivo.writeChar('*');
                                film.date_added = new SimpleDateFormat("MMMMM dd, yyyy", Locale.ENGLISH)
                                        .parse(formatNewDate_added);
                                byte[] c = film.toByteArray();
                                arquivo.seek(arquivo.length());
                                arquivo.writeChar('$');
                                arquivo.writeInt(c.length);
                                arquivo.write(c);
                                // Volta ponteiro para cabeçalho e atualiza id
                                arquivo.seek(0);
                                arquivo.writeInt(film.show_id);
                                System.out.println("Registro editado com sucesso.");
                                System.out.println("");
                                System.out.println("Novos dados: ");
                                film.print();

                            }
                        } catch (Exception e) {

                            System.out.println("A data deve ser no formato dd/mm/yyyy.");
                        }

                        System.out.println();

                        break;

                    case 5:
                        System.out.println("Digite o novo Ano de Estréia do Filme/Show: ");
                        int release_year = sc.nextInt();
                        // Limpa o \n no ultimo nextInt()
                        sc.nextLine();
                        // coloca ponteiro na posição do type
                        arquivo.seek(whereToUpdate + 10);
                        type = arquivo.readUTF();
                        title = arquivo.readUTF();
                        director = arquivo.readUTF();
                        dateAdded = arquivo.readUTF();

                        arquivo.writeInt(release_year);
                        film.release_year = release_year;

                        System.out.println("Registro editado com sucesso.");
                        System.out.println("");
                        System.out.println("Novos dados: ");
                        film.print();

                        System.out.println();

                        break;

                    case 6:
                        System.out.println("Digite a nova Duração do Filme/Show: ");

                        String duration = sc.nextLine();
                        // coloca ponteiro na posição do type
                        arquivo.seek(whereToUpdate + 10);
                        type = arquivo.readUTF();
                        title = arquivo.readUTF();
                        director = arquivo.readUTF();
                        dateAdded = arquivo.readUTF();
                        release_year = arquivo.readInt();

                        // salva posição do duration numa variável
                        pointerPosiLong = arquivo.getFilePointer();
                        String oldDuration = arquivo.readUTF();
                        int sizeOldDuration = oldDuration.length();
                        if (sizeOldDuration >= duration.length()) {
                            String durationResult = duration;
                            while (durationResult.length() < sizeOldDuration) {
                                durationResult += " ";
                            }
                            arquivo.seek(pointerPosiLong);
                            arquivo.writeUTF(durationResult);
                            film.duration = durationResult;
                            System.out.println("Registro editado com sucesso.");
                            System.out.println("");
                            System.out.println("Novos dados: ");
                            film.print();

                        } else {
                            arquivo.seek(whereToUpdate);
                            arquivo.writeChar('*');
                            film.duration = duration;
                            byte[] c = film.toByteArray();
                            arquivo.seek(arquivo.length());
                            arquivo.writeChar('$');
                            arquivo.writeInt(c.length);
                            arquivo.write(c);
                            // Volta ponteiro para cabeçalho e atualiza id
                            arquivo.seek(0);
                            arquivo.writeInt(film.show_id);
                            System.out.println("Registro editado com sucesso.");
                            System.out.println("");
                            System.out.println("Novos dados: ");
                            film.print();

                        }
                        System.out.println();

                        break;
                    case 7:
                        System.out.println("Digite o novo Gênero do Filme/Show: ");

                        String listed_in = sc.nextLine();
                        // coloca ponteiro na posição do type
                        arquivo.seek(whereToUpdate + 10);
                        type = arquivo.readUTF();
                        title = arquivo.readUTF();
                        director = arquivo.readUTF();
                        dateAdded = arquivo.readUTF();
                        release_year = arquivo.readInt();
                        duration = arquivo.readUTF();

                        // salva posição do listed_in numa variável
                        pointerPosiLong = arquivo.getFilePointer();
                        String oldListed_in = arquivo.readUTF();
                        int sizeOldListed_in = oldListed_in.length();
                        if (sizeOldListed_in >= listed_in.length()) {
                            String listed_inResult = listed_in;
                            while (listed_inResult.length() < sizeOldListed_in) {
                                listed_inResult += " ";
                            }
                            arquivo.seek(pointerPosiLong);
                            arquivo.writeUTF(listed_inResult);
                            film.listed_in = listed_inResult;
                            System.out.println("Registro editado com sucesso.");
                            System.out.println("");
                            System.out.println("Novos dados: ");
                            film.print();

                        } else {
                            arquivo.seek(whereToUpdate);
                            arquivo.writeChar('*');
                            film.listed_in = listed_in;
                            byte[] c = film.toByteArray();
                            arquivo.seek(arquivo.length());
                            arquivo.writeChar('$');
                            arquivo.writeInt(c.length);
                            arquivo.write(c);
                            // Volta ponteiro para cabeçalho e atualiza id
                            arquivo.seek(0);
                            arquivo.writeInt(film.show_id);
                            System.out.println("Registro editado com sucesso.");
                            System.out.println("");
                            System.out.println("Novos dados: ");
                            film.print();

                        }
                        System.out.println();

                        break;
                    case 0:
                        System.out.println("Voltando ao menu inicial");
                        break;

                    default:
                        System.out.println("ERRO");
                        break;
                }
            }

            arquivo.close();
            sc.close();

        } catch (

        Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 
     * @param id
     * @throws IOException
     */

    static void delete(int id) throws IOException {
        try {
            RandomAccessFile arquivo = new RandomAccessFile("../db/banco.db", "rw");
            arquivo.seek(4);
            long pointerPosition;
            pointerPosition = arquivo.getFilePointer();
            while (pointerPosition < arquivo.length()) {
                char lapide = arquivo.readChar();
                int size = arquivo.readInt();
                int filmID = arquivo.readInt();
                if (filmID == id && lapide == '$') {
                    arquivo.seek(pointerPosition);
                    arquivo.writeChar('*'); // informa que o arquivo está deletado
                    System.out.println("Filme/Show deletado com sucesso");
                    System.out.println();
                    return;
                } else if (filmID == id && lapide == '*') {
                    System.out.println("Filme/Show não existe na base de dados.");
                    System.out.println();
                    return;
                }
                arquivo.seek(pointerPosition + 6 + size);
                pointerPosition = arquivo.getFilePointer();

            }
            System.out.println("Filme/Show não existe na base de dados.");
            System.out.println();

            arquivo.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void SortingDatabase() {
        Scanner sc1 = new Scanner(System.in);
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
            SortingDatabase();
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

        sc1.close();

    }

    public static void simpleInterpolation() {
        try {
            RandomAccessFile arquivo = new RandomAccessFile("../db/banco.db", "rw");
            RandomAccessFile path1 = new RandomAccessFile("../db/path1.db", "rw");
            RandomAccessFile path2 = new RandomAccessFile("../db/path2.db", "rw");
            arquivo.seek(4);
            int count = 0;
            Long pointerPosition = arquivo.getFilePointer();

            ArrayList<Film> films = new ArrayList<Film>();
            while (pointerPosition < arquivo.length()) {

                while (pointerPosition < arquivo.length() && count < 5) {
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

                }
                InsertionSort(films);
                // escreve no arquivo path1
                for (Film film : films) {
                    // escreve no arquivo novo
                    byte[] c = film.toByteArray();
                    path1.writeChar('$'); // sinal de registro ativo
                    path1.writeInt(c.length);
                    path1.write(c);

                }

                films.clear();
                count = 0;
                while (pointerPosition < arquivo.length() && count < 5) {
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
        System.out.println("Base de dados ordenada com sucesso!");
        System.out.println("");

    }

    public static void variableSizeInterpolation() {
        try {
            RandomAccessFile arquivo = new RandomAccessFile("../db/banco.db", "rw");

            arquivo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        for (Film film : films) {
            film.print();
        }

    }

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);
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

                option = sc.nextInt();

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("O valor digitado deve ser um número!! - 3");
                option = 0;
            }

            switch (option) {
                case 1:
                    create();

                    System.out.println();
                    break;
                case 2:
                    System.out.println("Digite o id do Show que você deseja ver: ");
                    try {
                        id = sc.nextInt();
                        read(id);
                    } catch (Exception e) {
                        System.out.println("O valor digitado deve ser um número!!");
                        option = 0;
                    }
                    break;
                case 3:
                    System.out.println("Digite o id do Show que você deseja atualizar: ");
                    try {
                        id = sc.nextInt();
                        update(id);

                    } catch (Exception e) {
                        System.out.println("O valor digitado deve ser um número!!");
                        option = 0;
                    }
                    break;

                case 4:
                    System.out.println("Digite o id do Show que você deseja deletar: ");
                    try {
                        id = sc.nextInt();
                        delete(id);
                    } catch (Exception e) {
                        System.out.println("O valor digitado deve ser um número!!");
                        option = 0;
                    }
                    break;
                case 5:
                    SortingDatabase();
                    break;
                case 6:
                    loadDatabase();
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
    }
}
