import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Film {
    protected int id;
    protected String name;
    protected int pages;

    public Film() {

    }

    public Film(int id, String name, int pages) {
        this.id = id;
        this.name = name;
        this.pages = pages;
    }

    public String toString() {
        return "id: " + this.id + "| name: " + this.name + " | pages: " + this.pages;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.name);
        dos.writeInt(this.pages);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.name = dis.readUTF();
        this.pages = dis.readInt();

    }
}
