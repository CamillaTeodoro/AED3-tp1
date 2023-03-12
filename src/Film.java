import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Film {

    // Atributos
    protected int show_id;
    protected String type;
    protected String title;
    protected String director;
    protected Date date_added;
    protected int release_year;
    protected String duration;
    protected String listed_in;

    // Construtores
    public Film() {

    }

    public Film(Film film) {
        this.show_id = film.getShow_id();
        this.type = film.getType();
        this.title = film.getTitle();
        this.director = film.getDirector();
        this.date_added = film.getDate_added();
        this.release_year = film.getRelease_year();
        this.duration = film.getDuration();
        this.listed_in = film.getListed_in();
    }

    public Film(int show_id, String type, String title, String director, Date date_added,
            int release_year, String duration, String listed_in) {

        this.show_id = show_id;
        this.type = type;
        this.title = title;
        this.director = director;
        this.date_added = date_added;
        this.release_year = release_year;
        this.duration = duration;
        this.listed_in = listed_in;
    }

    // Getters e Setters

    public int getShow_id() {
        return show_id;
    }

    public void setShow_id(int show_id) {
        this.show_id = show_id;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title.trim();
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director.trim();
    }

    public Date getDate_added() {
        return date_added;
    }

    public void setDate_added(Date date_added) {
        // System.out.println(date_added);
        this.date_added = date_added;
    }

    public int getRelease_year() {
        return release_year;
    }

    public void setRelease_year(int release_year) {
        this.release_year = release_year;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration.trim();
    }

    public String getListed_in() {
        return listed_in;
    }

    public void setListed_in(String listed_in) {
        this.listed_in = listed_in.trim();
    }

    // Função para imprimir dados do filme/show
    public void print() {
        System.out.println();
        System.out.println("Id: " + Integer.toString(show_id) + " | Tipo: " + type.trim() + " | Título: " + title.trim()
                + " | Diretor(es): "
                + director.trim()
                + " | Data: "
                + new SimpleDateFormat("MMMMM dd, yyyy", Locale.ENGLISH).format(date_added)
                + " | Ano de estréia: " + Integer.toString(release_year)
                + " | Duração: " + duration.trim() + " | Gênero(s): " + listed_in.trim());

    }

    // Função que cria o byte array com os dados do filme/show
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        SimpleDateFormat date = new SimpleDateFormat("MMMMM dd, yyyy");
        dos.writeInt(this.show_id);
        dos.writeUTF(this.type);
        dos.writeUTF(this.title);
        dos.writeUTF(this.director);
        dos.writeUTF(date.format(this.date_added));
        dos.writeInt(this.release_year);
        dos.writeUTF(this.duration);
        dos.writeUTF(this.listed_in);
        return baos.toByteArray();
    }

    // Função que lê o byte array e cria o objeto
    public void fromByteArray(byte[] b) throws IOException, ParseException {

        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        SimpleDateFormat date = new SimpleDateFormat("MMMMM dd, yyyy");
        this.show_id = dis.readInt();
        System.out.println(this.show_id);
        this.type = dis.readUTF();
        this.title = dis.readUTF();
        this.director = dis.readUTF();
        this.date_added = date.parse(dis.readUTF());
        this.release_year = dis.readInt();
        // System.out.println(this.release_year);
        this.duration = dis.readUTF();
        this.listed_in = dis.readUTF();

    }

    // Lê a linha recebida do csv e cria o objeto.
    public void ReadText(String line) throws ParseException {

        String[] vetor = new String[8];
        int j = 0;
        String result = "";
        boolean isIn = false;

        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ',' && !isIn) {
                vetor[j] = result;
                j++;
                result = "";
            } else if (line.charAt(i) == '"') {
                isIn = !isIn;
            } else if (line.charAt(i) == '[' || line.charAt(i) == ']') {
                continue;
            } else {
                result += line.charAt(i);
            }
        }
        vetor[j] = result; // last column does not have a comma

        setShow_id(Integer.parseInt(vetor[0]));
        setType(vetor[1]);
        setTitle(vetor[2]);
        setDirector(vetor[3]);
        try {
            setDate_added(new SimpleDateFormat("MMMMM dd, yyyy", Locale.ENGLISH).parse(vetor[4]));
        } catch (Exception e) {

        }
        setRelease_year(Integer.parseInt(vetor[5]));
        setDuration(vetor[6]);
        setListed_in(vetor[7]);

    }

}
