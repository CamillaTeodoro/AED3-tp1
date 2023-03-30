import java.io.*;

public class Node {

    // Atributtes
    private long initialAddress;
    private int order = 8;
    private int numberOfChildrens = order - 1;
    private int quantity = 0;
    private int[] data;
    private long[] address;
    private Node[] pointer;

    // Constructor

    Node() {

    }

    public Node(long initialAddress, int order, int children, int quantity, int[] data, long[] address,
            Node[] pointer) {
        this.initialAddress = initialAddress;
        this.order = order;
        this.numberOfChildrens = children;
        this.quantity = quantity;
        this.data = data;
        this.address = address;
        this.pointer = pointer;
    }

    // Getters and Setters

    public long getInitialAddress() {
        return initialAddress;
    }

    public void setInitialAddress(long initialAddress) {
        this.initialAddress = initialAddress;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getChildren() {
        return numberOfChildrens;
    }

    public void setChildren(int children) {
        this.numberOfChildrens = children;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int[] getData() {
        return data;
    }

    public void setData(int[] data) {
        this.data = data;
    }

    public long[] getAddress() {
        return address;
    }

    public void setAddress(long[] address) {
        this.address = address;
    }

    public Node[] getPointer() {
        return pointer;
    }

    public void setPointer(Node[] pointer) {
        this.pointer = pointer;
    }

    // Turns an object into a byte array
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // Number of elements
        dos.writeInt(this.quantity);

        // write the pointer, data and address
        int i = 0;
        while (i < quantity) {

            dos.writeLong(this.pointer[i] == null ? -1 : this.pointer[i].address[i]);
            dos.writeInt(data[i]);
            dos.writeLong(address[i]);
            i++;
        }
        dos.writeLong(this.pointer[i + 1] == null ? -1 : this.pointer[i].address[i]);

        // As we have a fixed-length record, we need to
        // fill in the other fields
        int recordSize = 20;
        byte[] empty = new byte[recordSize];
        while (i < numberOfChildrens) {
            dos.write(empty);
            dos.writeLong(this.pointer[i + 1] == null ? -1 : this.pointer[i].address[i]);
            i++;
        }

        return baos.toByteArray();
    }

    // Turns a byte array into an object
    public void fromByteArray(byte[] b) throws IOException {

        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        // read the number of elements
        quantity = dis.readInt();

        int i = 0;
        while (i < numberOfChildrens) {
            pointer[i].address[i] = dis.readLong();
            data[i] = dis.readInt();
            address[i] = dis.readLong();
            i++;
        }
        pointer[i].address[i] = dis.readLong();

    }

    public boolean isFull() {
        return quantity == numberOfChildrens;
    }

    public void split(int id, long address, Node node) {

    }

    public void insert(int id, long address) {

    }
}
