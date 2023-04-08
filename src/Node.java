import java.io.*;

public class Node {

    // Atributtes
    private long initialAddress;
    private int order = 4;
    private int numberOfChildrens = order - 1;
    private int quantity = 0;
    private int[] data;
    private long[] address;
    private Node[] pointer;
    private boolean isLeaf = true;

    // Constructor

    Node() {
        data = new int[numberOfChildrens];
        address = new long[numberOfChildrens];
        pointer = new Node[order];
        for (int i = 0; i < numberOfChildrens; i++) {
            setAddress(i, -1);

        }
        for (int i = 0; i < order; i++) {
            setPointer(i, null);

        }

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

    public int getNumberOfChildrens() {
        return numberOfChildrens;
    }

    public void setNumberOfChildrens(int children) {
        this.numberOfChildrens = children;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getData(int i) {
        return data[i];
    }

    public void setData(int i, int data) {
        this.data[i] = data;
    }

    public long getAddress(int i) {
        return address[i];
    }

    public void setAddress(int i, long address) {
        this.address[i] = address;
    }

    public Node getPointer(int i) {
        return pointer[i];
    }

    public void setPointer(int i, Node pointer) {
        this.pointer[i] = pointer;
    }

    public boolean getLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
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

    public int findPosition(int id) {
        int position = 0;
        while (position < this.quantity && this.data[position] < id) {
            position++;
        }
        return position;
    }

    /**
     * Method to find a value in the Node. Used to check if an id can be added to
     * the tree
     * If the id exists, return true
     */

    public boolean findID(int id) {
        for (int i = 0; i < this.quantity; i++) {
            if (this.data[i] == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to insert the id and the adrees in the Node
     * 
     * @param id
     * @param address
     */
    public void insert(int id, long address) {
        int position = findPosition(id);
        if (quantity != 0) {

            for (int i = this.quantity - 1; i >= position; i--) {
                this.data[i + 1] = this.data[i];
                this.address[i + 1] = this.address[i];
            }
        }

        this.data[position] = id;
        this.address[position] = address;
        this.quantity++;
    }

}
