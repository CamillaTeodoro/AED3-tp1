import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class App {

    /**
     * 
     * @throws IOException
     */
    // Cria o arquivo .db através da leitura do aquivo csv
    static void create() throws IOException {
        try {
            RandomAccessFile arq = new RandomAccessFile("../db/banco.db", "rw");
            Scanner fileReaderScanner = new Scanner(new File("../netflix_titles.csv"));
            String line = fileReaderScanner.nextLine(); // Ignora primeira linha do csv
            line = fileReaderScanner.nextLine();
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

    /**
     * 
     * @param id
     * @throws IOException
     */
    // Lê um id do teclado e mostra na tela o filme/show
    static Long read(int id) throws IOException {
        try {
            RandomAccessFile arq = new RandomAccessFile("../db/banco.db", "rw");

            arq.seek(4);
            long fileSize = arq.length();
            long pointerPosition;
            long beginOfRegister = arq.getFilePointer();
            pointerPosition = arq.getFilePointer();

            while (pointerPosition < fileSize) {

                Film film = new Film();

                beginOfRegister = arq.getFilePointer();

                char lapide = arq.readChar();
                int size = arq.readInt();
                int filmID = arq.readInt();
                if (filmID == id && lapide == '$') {

                    // System.out.println("cheguei aqui.");
                    arq.seek(pointerPosition + 2);
                    int sizeFilm = arq.readInt();
                    // System.out.println(sizeFilm);
                    byte[] b = new byte[sizeFilm];
                    arq.read(b);
                    film.fromByteArray(b);

                    film.print();

                    System.out.println();
                    return beginOfRegister;
                }

                arq.seek(pointerPosition + 6 + size);
                pointerPosition = arq.getFilePointer();

            }

            System.out.println("Filme/Show não existe na base de dados.");
            System.out.println();
            arq.close();

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
            RandomAccessFile arq = new RandomAccessFile("../db/banco.db", "rw");
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
                arq.seek(beginOfRegister + 2);
                int sizeFilm = arq.readInt();
                byte[] b = new byte[sizeFilm];
                arq.read(b);
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
                System.out.println("0 - Sair ");
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
                        arq.seek(whereToUpdate + 10);
                        // salva posição do type numa variável
                        Long pointerPosiLong = arq.getFilePointer();
                        String oldType = arq.readUTF();
                        int sizeOldType = oldType.length();
                        if (sizeOldType >= type.length()) {
                            String typeResult = type;
                            arq.seek(pointerPosiLong);
                            while (typeResult.length() < sizeOldType) {
                                typeResult += " ";
                            }
                            arq.writeUTF(typeResult);
                            film.type = typeResult;
                            System.out.println("Registro editado com sucesso.");
                            film.print();

                        } else {
                            arq.seek(whereToUpdate);
                            arq.writeChar('*');
                            film.type = type;
                            byte[] c = film.toByteArray();
                            arq.seek(arq.length());
                            arq.writeChar('$');
                            arq.writeInt(c.length);
                            arq.write(c);
                            // Volta ponteiro para cabeçalho e atualiza id
                            arq.seek(0);
                            arq.writeInt(film.show_id);
                            System.out.println("Registro editado com sucesso.");
                            film.print();

                        }

                        System.out.println();

                        break;
                    case 2:
                        System.out.println("Digite o novo Título do Filme/Show: ");
                        String title = sc.nextLine();
                        // coloca ponteiro na posição do type
                        arq.seek(whereToUpdate + 10);
                        type = arq.readUTF();
                        // salva posição do title numa variável
                        pointerPosiLong = arq.getFilePointer();
                        String oldtitle = arq.readUTF();
                        int sizeOldtitle = oldtitle.length();
                        if (sizeOldtitle >= title.length()) {
                            String titleResult = title;
                            arq.seek(pointerPosiLong);
                            while (titleResult.length() < sizeOldtitle) {
                                titleResult += " ";
                            }
                            arq.writeUTF(titleResult);
                            film.title = titleResult;
                            System.out.println("Registro editado com sucesso.");
                            film.print();

                        } else {
                            arq.seek(whereToUpdate);
                            // System.out.println(whereToUpdate);
                            arq.writeChar('*');
                            film.title = title;
                            byte[] c = film.toByteArray();
                            arq.seek(arq.length());
                            arq.writeChar('$');
                            arq.writeInt(c.length);
                            arq.write(c);
                            // Volta ponteiro para cabeçalho e atualiza id
                            arq.seek(0);
                            arq.writeInt(film.show_id);
                            System.out.println("Registro editado com sucesso.");
                            film.print();

                        }

                        System.out.println();

                        break;
                    case 3:
                        System.out.println("Digite o novo Diretor do Filme/Show: ");
                        String director = sc.nextLine();
                        // coloca ponteiro na posição do type
                        arq.seek(whereToUpdate + 10);
                        type = arq.readUTF();
                        title = arq.readUTF();
                        // salva posição do director numa variável
                        pointerPosiLong = arq.getFilePointer();
                        String oldDirector = arq.readUTF();
                        int sizeOldDirector = oldDirector.length();
                        if (sizeOldDirector >= director.length()) {
                            String directorResult = director;
                            arq.seek(pointerPosiLong);
                            while (directorResult.length() < sizeOldDirector) {
                                directorResult += " ";
                            }
                            arq.writeUTF(directorResult);
                            film.director = directorResult;
                            System.out.println("Registro editado com sucesso.");
                            film.print();

                        } else {
                            arq.seek(whereToUpdate);
                            arq.writeChar('*');
                            film.director = director;
                            byte[] c = film.toByteArray();
                            arq.seek(arq.length());
                            arq.writeChar('$');
                            arq.writeInt(c.length);
                            arq.write(c);
                            // Volta ponteiro para cabeçalho e atualiza id
                            arq.seek(0);
                            arq.writeInt(film.show_id);
                            System.out.println("Registro editado com sucesso.");
                            film.print();

                        }

                        System.out.println();

                        break;

                    case 4:
                        System.out.println("Digite a nova Data do Filme/Show: ");

                        String dateAdded = sc.nextLine();
                        // coloca ponteiro na posição do type
                        arq.seek(whereToUpdate + 10);
                        type = arq.readUTF();
                        title = arq.readUTF();
                        director = arq.readUTF();
                        // salva posição do date_added numa variável
                        pointerPosiLong = arq.getFilePointer();

                        String oldDate_added = arq.readUTF();
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
                                arq.seek(pointerPosiLong);
                                while (date_addedResult.length() < sizeOldDate_added) {
                                    date_addedResult += " ";
                                }
                                arq.writeUTF(date_addedResult);
                                film.date_added = newDate_added;

                                System.out.println("Registro editado com sucesso.");
                                film.print();

                            } else {
                                arq.seek(whereToUpdate);
                                arq.writeChar('*');
                                film.date_added = new SimpleDateFormat("MMMMM dd, yyyy", Locale.ENGLISH)
                                        .parse(formatNewDate_added);
                                byte[] c = film.toByteArray();
                                arq.seek(arq.length());
                                arq.writeChar('$');
                                arq.writeInt(c.length);
                                arq.write(c);
                                // Volta ponteiro para cabeçalho e atualiza id
                                arq.seek(0);
                                arq.writeInt(film.show_id);
                                System.out.println("Registro editado com sucesso.");
                                film.print();

                            }
                        } catch (Exception e) {
                            try {
                                newDate_added = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
                                        .parse(dateAdded);
                                formatNewDate_added = new SimpleDateFormat("MMMMM dd, yyyy", Locale.ENGLISH)
                                        .format(newDate_added);
                                sizeFormatNewDate_added = formatNewDate_added.length();
                                if (sizeOldDate_added >= sizeFormatNewDate_added) {
                                    String date_addedResult = formatNewDate_added;
                                    arq.seek(pointerPosiLong);
                                    while (date_addedResult.length() < sizeOldDate_added) {
                                        date_addedResult += " ";
                                    }
                                    arq.writeUTF(date_addedResult);
                                    film.date_added = newDate_added;

                                    System.out.println("Registro editado com sucesso.");
                                    film.print();

                                } else {
                                    arq.seek(whereToUpdate);
                                    arq.writeChar('*');
                                    film.date_added = new SimpleDateFormat("MMMMM dd, yyyy", Locale.ENGLISH)
                                            .parse(formatNewDate_added);
                                    byte[] c = film.toByteArray();
                                    arq.seek(arq.length());
                                    arq.writeChar('$');
                                    arq.writeInt(c.length);
                                    arq.write(c);
                                    // Volta ponteiro para cabeçalho e atualiza id
                                    arq.seek(0);
                                    arq.writeInt(film.show_id);
                                    System.out.println("Registro editado com sucesso.");
                                    film.print();

                                }
                            } catch (Exception e1) {
                                System.out.println("A data deve ser no formato dd/mm/yyyy ou dd-mm-yyyy.");
                            }
                        }

                        System.out.println();

                        break;

                    case 5:
                        System.out.println("Digite o novo Ano de Estréia do Filme/Show: ");
                        int release_year = sc.nextInt();
                        // Limpa o \n no ultimo nextInt()
                        sc.nextLine();
                        // coloca ponteiro na posição do type
                        arq.seek(whereToUpdate + 10);
                        type = arq.readUTF();
                        title = arq.readUTF();
                        director = arq.readUTF();
                        dateAdded = arq.readUTF();

                        arq.writeInt(release_year);
                        film.release_year = release_year;

                        System.out.println("Registro editado com sucesso.");
                        film.print();

                        System.out.println();

                        break;

                    case 6:
                        System.out.println("Digite a nova Duração do Filme/Show: ");

                        String duration = sc.nextLine();
                        // coloca ponteiro na posição do type
                        arq.seek(whereToUpdate + 10);
                        type = arq.readUTF();
                        title = arq.readUTF();
                        director = arq.readUTF();
                        dateAdded = arq.readUTF();
                        release_year = arq.readInt();

                        // salva posição do duration numa variável
                        pointerPosiLong = arq.getFilePointer();
                        String oldDuration = arq.readUTF();
                        int sizeOldDuration = oldDuration.length();
                        if (sizeOldDuration >= duration.length()) {
                            String durationResult = duration;
                            while (durationResult.length() < sizeOldDuration) {
                                durationResult += " ";
                            }
                            arq.seek(pointerPosiLong);
                            arq.writeUTF(durationResult);
                            film.duration = durationResult;
                            System.out.println("Registro editado com sucesso.");
                            film.print();

                        } else {
                            arq.seek(whereToUpdate);
                            arq.writeChar('*');
                            film.duration = duration;
                            byte[] c = film.toByteArray();
                            arq.seek(arq.length());
                            arq.writeChar('$');
                            arq.writeInt(c.length);
                            arq.write(c);
                            // Volta ponteiro para cabeçalho e atualiza id
                            arq.seek(0);
                            arq.writeInt(film.show_id);
                            System.out.println("Registro editado com sucesso.");
                            film.print();

                        }
                        System.out.println();

                        break;
                    case 7:
                        System.out.println("Digite o novo Gênero do Filme/Show: ");

                        String listed_in = sc.nextLine();
                        // coloca ponteiro na posição do type
                        arq.seek(whereToUpdate + 10);
                        type = arq.readUTF();
                        title = arq.readUTF();
                        director = arq.readUTF();
                        dateAdded = arq.readUTF();
                        release_year = arq.readInt();
                        duration = arq.readUTF();

                        // salva posição do listed_in numa variável
                        pointerPosiLong = arq.getFilePointer();
                        String oldListed_in = arq.readUTF();
                        int sizeOldListed_in = oldListed_in.length();
                        if (sizeOldListed_in >= listed_in.length()) {
                            String listed_inResult = listed_in;
                            while (listed_inResult.length() < sizeOldListed_in) {
                                listed_inResult += " ";
                            }
                            arq.seek(pointerPosiLong);
                            arq.writeUTF(listed_inResult);
                            film.listed_in = listed_inResult;
                            System.out.println("Registro editado com sucesso.");
                            film.print();

                        } else {
                            arq.seek(whereToUpdate);
                            arq.writeChar('*');
                            film.listed_in = listed_in;
                            byte[] c = film.toByteArray();
                            arq.seek(arq.length());
                            arq.writeChar('$');
                            arq.writeInt(c.length);
                            arq.write(c);
                            // Volta ponteiro para cabeçalho e atualiza id
                            arq.seek(0);
                            arq.writeInt(film.show_id);
                            System.out.println("Registro editado com sucesso.");
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

            arq.close();

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
            RandomAccessFile arq = new RandomAccessFile("../db/banco.db", "rw");
            arq.seek(4);
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
                    return;
                }
                arq.seek(pointerPosition + 6 + size);
                pointerPosition = arq.getFilePointer();

            }
            arq.close();
            System.out.println("Filme/Show não existe na base de dados.");
            System.out.println();

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

            try {
                option = sc.nextInt();
            } catch (Exception e) {
                System.out.println("O valor digitado deve ser um número!!");
                option = 0;
            }

            switch (option) {
                case 1:
                    create();
                    System.out.println("Arquivo criado com sucesso.");
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
