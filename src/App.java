import java.io.RandomAccessFile;

public class App {
    public static void main(String[] args) throws Exception {
        Film l1 = new Film(1, "O nome do vento", 600);
        System.out.println(l1);
        Film l2 = new Film(2, "A Origem", 300);
        System.out.println(l2);

        RandomAccessFile arq = new RandomAccessFile("../db/banco.db", "rw");
        Film l3 = new Film();
        Film l4 = new Film();

        byte[] b;
        int len;
        try {
            b = l1.toByteArray();
            arq.writeInt(b.length);
            arq.write(b);
            b = l2.toByteArray();
            arq.writeInt(b.length);
            arq.write(b);

            arq.seek(0);
            len = arq.readInt();
            b = new byte[len];
            arq.read(b);
            l3.fromByteArray(b);
            System.out.println(l3);

            len = arq.readInt();
            b = new byte[len];
            arq.read(b);
            l4.fromByteArray(b);
            System.out.println(l4);

            arq.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
