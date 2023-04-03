import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class BTree {

    // Atributtes

    private Node root;
    private RandomAccessFile file;

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
        this.root.setData(0, 29);
        this.root.setQuantity(1);
        this.root.setPointer(0, new Node());
        this.root.getPointer(0).setData(0, 8);
        this.root.getPointer(0).setData(1, 15);
        this.root.getPointer(0).setQuantity(2);
        this.root.getPointer(0).setPointer(0, new Node());
        this.root.getPointer(0).getPointer(0).setData(0, 1);
        this.root.getPointer(0).getPointer(0).setData(1, 3);
        this.root.getPointer(0).getPointer(0).setData(2, 4);
        this.root.getPointer(0).getPointer(0).setData(3, 7);
        this.root.getPointer(0).getPointer(0).setQuantity(4);
        this.root.getPointer(0).getPointer(0).setLeaf(true);

        this.root.getPointer(0).setPointer(1, new Node());
        this.root.getPointer(0).getPointer(1).setData(0, 10);
        this.root.getPointer(0).getPointer(1).setData(1, 12);
        this.root.getPointer(0).getPointer(1).setData(2, 13);
        this.root.getPointer(0).getPointer(1).setData(3, 14);
        this.root.getPointer(0).getPointer(1).setQuantity(4);
        this.root.getPointer(0).getPointer(1).setLeaf(true);

        this.root.getPointer(0).setPointer(2, new Node());
        this.root.getPointer(0).getPointer(2).setData(0, 18);
        this.root.getPointer(0).getPointer(2).setData(1, 20);
        this.root.getPointer(0).getPointer(2).setData(2, 25);
        this.root.getPointer(0).getPointer(2).setQuantity(3);
        this.root.getPointer(0).getPointer(2).setLeaf(true);

        this.root.setPointer(1, new Node());
        this.root.getPointer(1).setData(0, 37);
        this.root.getPointer(1).setData(1, 45);
        this.root.getPointer(1).setData(2, 60);
        this.root.getPointer(1).setQuantity(3);

        this.root.getPointer(1).setPointer(0, new Node());
        this.root.getPointer(1).getPointer(0).setData(0, 30);
        this.root.getPointer(1).getPointer(0).setData(1, 35);
        this.root.getPointer(1).getPointer(0).setQuantity(2);
        this.root.getPointer(1).getPointer(0).setLeaf(true);

        this.root.getPointer(1).setPointer(1, new Node());
        this.root.getPointer(1).getPointer(1).setData(0, 40);
        this.root.getPointer(1).getPointer(1).setData(1, 41);
        this.root.getPointer(1).getPointer(1).setData(2, 42);
        this.root.getPointer(1).getPointer(1).setData(3, 43);
        this.root.getPointer(1).getPointer(1).setQuantity(4);
        this.root.getPointer(1).getPointer(1).setLeaf(true);

        this.root.getPointer(1).setPointer(2, new Node());
        this.root.getPointer(1).getPointer(2).setData(0, 51);
        this.root.getPointer(1).getPointer(2).setData(1, 52);
        this.root.getPointer(1).getPointer(2).setQuantity(2);
        this.root.getPointer(1).getPointer(2).setLeaf(true);

        this.root.getPointer(1).setPointer(3, new Node());
        this.root.getPointer(1).getPointer(3).setData(0, 70);
        this.root.getPointer(1).getPointer(3).setData(1, 77);
        this.root.getPointer(1).getPointer(3).setData(2, 83);
        this.root.getPointer(1).getPointer(3).setQuantity(3);
        this.root.getPointer(1).getPointer(3).setLeaf(true);

    }

    public BTree(String filePath) throws IOException {

        // checcks if the file exists
        File bTree = new File(filePath);
        if (!bTree.exists()) {

            file = new RandomAccessFile(bTree + ".db", "rw");
        }

        if (file.length() <= 8) {
            file.seek(0);
            file.writeLong(-1);
        }
        this.root = new Node();
    }

    // Methods
    /**
     * Insert the id and the address in the bTree
     */
    public void insert(int data, long address) {

    }

    public void delete(int id, long address) {

    }

    /**
     * Recursive method for print the ID and the address
     */
    public void printBTree(Node node) {
        if (node != null) {
            int i;
            for (i = 0; i < node.getQuantity(); i++) {
                if (!node.getLeaf()) {
                    printBTree(node.getPointer(i));
                }
                System.out.println("ID: " + node.getData(i) + " / Endereço: " + node.getAddress(i) + " ");
            }
            if (!node.getLeaf()) {
                printBTree(node.getPointer(i));
            }
        }
    }

}