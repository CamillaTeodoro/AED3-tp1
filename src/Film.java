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
    protected int typeSize;
    protected int titleSize;
    protected int directorSize;
    protected int durationSize;
    protected int listed_inSize;

    // Construtores
    public Film() {

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
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Date getDate_added() {
        return date_added;
    }

    public void setDate_added(Date date_added) {
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
        this.duration = duration;
    }

    public String getListed_in() {
        return listed_in;
    }

    public void setListed_in(String listed_in) {
        this.listed_in = listed_in;
    }

    public int getTypeSize() {
        return typeSize;
    }

    public void setTypeSize(int typeSize) {
        this.typeSize = typeSize;
    }

    public int getTitleSize() {
        return titleSize;
    }

    public void setTitleSize(int titleSize) {
        this.titleSize = titleSize;
    }

    public int getDirectorSize() {
        return directorSize;
    }

    public void setDirectorSize(int directorSize) {
        this.directorSize = directorSize;
    }

    public int getDurationSize() {
        return durationSize;
    }

    public void setDurationSize(int durationSize) {
        this.durationSize = durationSize;
    }

    public int getListed_inSize() {
        return listed_inSize;
    }

    public void setListed_inSize(int listed_inSize) {
        this.listed_inSize = listed_inSize;
    }

    // Função para imprimir dados do filme/show
    public String print() {
        return "Id: " + Integer.toString(this.show_id) + " | Tipo: " + this.type + " | Título: " + this.title
                + " | Diretor(s): "
                + this.director
                + " | Data: "
                + new SimpleDateFormat("MMMMM dd, yyyy", Locale.ENGLISH).format(this.date_added)
                + " | Ano de estréia: " + Integer.toString(this.release_year)
                + " | Duração: " + this.duration + " | Listado como: " + this.listed_in;
    }

    // Função que cria o byte array com os dados do filme/show
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        SimpleDateFormat date = new SimpleDateFormat("MMMMM dd, yyyy");
        dos.writeInt(this.show_id);
        dos.writeInt(this.typeSize);
        dos.writeUTF(this.type);
        dos.writeInt(this.titleSize);
        dos.writeUTF(this.title);
        dos.writeInt(this.directorSize);
        dos.writeUTF(this.director);
        dos.writeUTF(date.format(this.date_added));
        dos.writeInt(this.release_year);
        dos.writeInt(this.durationSize);
        dos.writeUTF(this.duration);
        dos.writeInt(this.listed_inSize);
        dos.writeUTF(this.listed_in);

        return baos.toByteArray();
    }

    // Função que lê o byte array e cria o objeto
    public void fromByteArray(byte[] b) throws IOException, ParseException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        SimpleDateFormat date = new SimpleDateFormat("MMMMM dd, yyyy");
        this.show_id = dis.readInt();
        this.typeSize = dis.readInt();
        this.type = dis.readUTF();
        this.titleSize = dis.readInt();
        this.title = dis.readUTF();
        this.directorSize = dis.readInt();
        this.director = dis.readUTF();
        this.date_added = date.parse(dis.readUTF());
        this.release_year = dis.readInt();
        this.durationSize = dis.readInt();
        this.duration = dis.readUTF();
        this.listed_inSize = dis.readInt();
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

        setTypeSize(vetor[1].length());
        setTitleSize(vetor[2].length());
        setDirectorSize(vetor[3].length());
        setDurationSize(vetor[6].length());
        setListed_inSize(vetor[7].length());

    }

}
