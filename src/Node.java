import java.io.*;

public class Node {
    public static final int ORDER = 8;
    public static final int NUMBER_OF_CHILDREN = ORDER - 1;
    // 4 int quantity
    // 8 long last pointer
    // 20 = long pointer (8) + int id (4) + long address (8)
    public static final int RECORD_SIZE = 4 + 8 + (NUMBER_OF_CHILDREN * 20);

    // Atributtes
    private long initialAddress = 0xAAAAAAAAAAAAAAAAL;
    private int quantity = 0;
    private int[] data;
    private long[] address;
    private Node[] pointer;
    private boolean isLeaf = true;

    // Constructor

    Node(long initialAddress) {
        this.initialAddress = initialAddress;
        data = new int[NUMBER_OF_CHILDREN];
        address = new long[NUMBER_OF_CHILDREN];
        pointer = new Node[ORDER];
        for (int i = 0; i < NUMBER_OF_CHILDREN; i++) {
            setAddress(i, -1);
        }
        for (int i = 0; i < ORDER; i++) {
            setPointer(i, null);
        }
    }

    public Node(long initialAddress, int quantity, int[] data, long[] address,
            Node[] pointer) {
        this.initialAddress = initialAddress;
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
            dos.writeLong(this.pointer[i] == null ? -1 : this.pointer[i].initialAddress);
            dos.writeInt(data[i]);
            dos.writeLong(address[i]);
            i++;
        }
        dos.writeLong(this.pointer[i] == null ? -1 : this.pointer[i].initialAddress);

        while (i < NUMBER_OF_CHILDREN) {
            dos.writeInt(0); // Empty record id
            dos.writeLong(-1); // Empty sequential file address
            dos.writeLong(-1); // Empty btree file pointer
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
        while (i < NUMBER_OF_CHILDREN) {
            long pointerPosition = dis.readLong();
            if (pointerPosition != -1) {
                this.setLeaf(false);
                pointer[i] = new Node(pointerPosition);
            }

            data[i] = dis.readInt();
            address[i] = dis.readLong();
            i++;
        }
        long pointerPosition = dis.readLong();
        if (pointerPosition != -1) {
            this.setLeaf(false);
            pointer[i] = new Node(pointerPosition);
        }
    }

    /**
     * Method to check if the node is full
     * 
     * @return
     */
    public boolean isFull() {
        return quantity == NUMBER_OF_CHILDREN;
    }

    /**
     * Method to check if the node is balanced
     * 
     * @return
     */
    public boolean isBalanced() {
        return quantity >= NUMBER_OF_CHILDREN / 2;
    }

    /**
     * Method to check if the node will continue balanced after a deletion
     * 
     * @return
     */
    public boolean willBeBalanced() {
        return quantity - 1 >= NUMBER_OF_CHILDREN / 2;
    }

    /**
     * Method to find the id position in the node
     */
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
     * Method to insert the id and the address in the Node
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

    public int findPointer(Node node) {
        for (int i = 0; i < quantity + 1; i++) {
            if (pointer[i] == node) {
                return i;
            }
        }
        return -1; // Node not found in pointers
    }
}
