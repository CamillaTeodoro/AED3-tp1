import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class DatabaseAccess {

    private RandomAccessFile databaseFile;

    DatabaseAccess(String filePath) throws FileNotFoundException {
        databaseFile = new RandomAccessFile(filePath, "rw");
    }

    public void close() throws IOException {
        databaseFile.close();
    }

    /**
     * 
     * @param id
     * @throws IOException
     */
    // Lê um id do teclado e mostra na tela o filme/show
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
     * 
     * @param id
     * @throws IOException
     */
    // Lê um id do teclado e mostra na tela o filme/show
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

                databaseFile.writeInt(editedFilmByteArray.length);
                databaseFile.write(editedFilmByteArray);

            } else {
                databaseFile.seek(find(film.getShow_id()));
                databaseFile.writeChar('*'); // sinal de registro ativo

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
            e.printStackTrace();
        }
        return false;
    }
}
