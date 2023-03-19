import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class DatabaseAccess {

    // Atributtes
    private RandomAccessFile databaseFile;
    private Long position = (long) 4; // defaults the start of the record
    private String filepath; // sets the file path
    private boolean isEndOfFile = false;

    // Constructor
    DatabaseAccess(String filePath) throws FileNotFoundException {
        databaseFile = new RandomAccessFile(filePath, "rw");
        this.filepath = filePath;
    }

    public void close() throws IOException {
        databaseFile.close();
    }

    /**
     * Takes an id as paramater and returns the starting position of the record or
     * -1
     * 
     * @param id
     * @throws IOException
     */

    public Film read(int id) throws IOException {
        try {
            Long pointerPosition = find(id);
            if (pointerPosition == (long) -1) {
                return null;
            }

            Film film = new Film();

            // System.out.println("cheguei aqui.");
            databaseFile.seek(pointerPosition + 2);
            int sizeFilm = databaseFile.readInt();
            // System.out.println(sizeFilm);
            byte[] b = new byte[sizeFilm];
            databaseFile.read(b);
            film.fromByteArray(b);

            return film;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Takes an id as paramater and returns the starting position of the record or
     * -1
     * 
     * @param id
     * @throws IOException
     */
    public Long find(int id) throws IOException {
        try {

            databaseFile.seek(4);
            long fileSize = databaseFile.length();
            long pointerPosition = 4;

            while (pointerPosition < fileSize) {
                char lapide = databaseFile.readChar();
                int size = databaseFile.readInt();
                int filmID = databaseFile.readInt();
                if (filmID == id && lapide == '$') {
                    return pointerPosition;
                }

                databaseFile.seek(pointerPosition + 6 + size);
                pointerPosition = databaseFile.getFilePointer();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return (long) -1;

    }

    /**
     * @throws IOException
     */
    public Boolean create(Film film) throws IOException {

        try {

            if (film == null) {
                return false;
            }
            byte[] b;

            databaseFile.seek(databaseFile.length());
            b = film.toByteArray();
            databaseFile.writeChar('$'); // sinal de registro ativo

            databaseFile.writeInt(b.length);
            databaseFile.write(b);

            // Volta ponteiro para cabeçalho e atualiza id
            databaseFile.seek(0);
            databaseFile.writeInt(film.show_id);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @throws IOException
     */
    public Boolean update(Film film, Film editedFilm) throws IOException {

        try {
            byte[] filmByteArray = film.toByteArray();
            byte[] editedFilmByteArray = editedFilm.toByteArray();

            if (editedFilmByteArray.length <= filmByteArray.length) {
                databaseFile.seek(find(film.getShow_id()));
                databaseFile.writeChar('$'); // sinal de registro ativo

                databaseFile.writeInt(filmByteArray.length); // mantem o size antigo
                databaseFile.write(editedFilmByteArray);

            } else {
                databaseFile.seek(find(film.getShow_id()));
                databaseFile.writeChar('*'); // sinal de registro desativado

                databaseFile.seek(databaseFile.length());
                databaseFile.writeChar('$'); // sinal de registro ativo
                databaseFile.writeInt(editedFilmByteArray.length);
                databaseFile.write(editedFilmByteArray);
                // Volta ponteiro para cabeçalho e atualiza id
                databaseFile.seek(0);
                databaseFile.writeInt(film.show_id);

            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 
     * @param id
     * @throws IOException
     */

    public boolean delete(int id) throws IOException {
        try {

            Long pointerPosition = find(id);

            databaseFile.seek(pointerPosition);
            databaseFile.writeChar('*'); // informa que o arquivo está deletado
            return true;

        } catch (Exception e) {

        }
        return false;
    }

    public void clearDb() throws IOException {
        databaseFile.setLength(0);
        databaseFile.writeInt(0);
        resetPosition();
    }

    public Long length() throws IOException {
        return databaseFile.length();
    }

    public void resetPosition() {
        position = (long) 4;
        isEndOfFile = false;
    }

    public boolean isEndOfFile() {
        return isEndOfFile;
    }

    // Get next record and return the film object
    public Film next() {
        try {
            databaseFile.seek(position);
            long fileSize = databaseFile.length();
            long pointerPosition = position;

            while (pointerPosition < fileSize) {
                char lapide = databaseFile.readChar();
                int size = databaseFile.readInt();
                if (lapide == '$') {
                    break;
                }

                databaseFile.seek(pointerPosition + 6 + size);
                pointerPosition = databaseFile.getFilePointer();
            }

            if (pointerPosition >= fileSize) {
                isEndOfFile = true;
                return null;
            }

            Film film = new Film();

            // System.out.println("cheguei aqui.");
            databaseFile.seek(pointerPosition + 2);
            int sizeFilm = databaseFile.readInt();
            // System.out.println(sizeFilm);
            byte[] b = new byte[sizeFilm];
            databaseFile.read(b);
            film.fromByteArray(b);

            position = databaseFile.getFilePointer();
            return film;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void print() {
        long oldPos = position;
        position = (long) 4;
        // System.out.println("Print path: " + filepath);
        Film film = next();
        while (film != null) {
            System.out.println(film.getShow_id());
            film = next();
        }
        // System.out.println("Print path end: " + filepath);
        position = oldPos;
    }

    public Long getPosition() throws IOException {
        return position;
    }

    public void setPosition(Long pointerPosition) throws IOException {
        this.position = pointerPosition;
    }
}
