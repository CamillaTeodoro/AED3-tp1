import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class BTree {

    // Atributtes

    private Node root;

    // Getters and Setters

    public Node getRoot() {
        return root;

    }

    public void setRoot(Node root) {
        this.root = root;
    }

    // Constructors
    public BTree() {

        this.root = new Node();
        

    }

    public BTree(String path) throws IOException {

        // checcks if the file exists
        File bTree = new File("bTree");
        if (!bTree.exists())
            bTree.mkdir();

        RandomAccessFile file = new RandomAccessFile("../db/" + bTree + ".db", "rw");
        if (file.length() < 8)
            file.writeLong(-1);

        this.root = new Node();
    }

    // Methods

    public void insert(int id, long address) {
        if (root == null) {
            root = new Node();
        }
        if (root.isFull()) {
            Node newRoot = new Node();
            newRoot.setPointer(null);
            newRoot.split(id, address, root);

        } else {
            root.insert(id, address);
        }

    }

    public Node search(int key, Node node) {
        return node;

    }

    public void delete(int id, long address) {

    }

    public void regroup(Node node1, Node node2) {

    }

    public void print(Node node) {
        if (node != null) {
        }
    }

}
