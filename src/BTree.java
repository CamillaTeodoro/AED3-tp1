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
    public void insert(int id, long address) {

        // Case 1: empty tree
        if (root == null) {
            Node newNode = new Node();
            newNode.insert(id, address);
            setRoot(newNode);
            return;
        }

        // search for the node to insert

        Node node = root;
        Node parent = null;
        boolean idExists;
        int index = -1;

        while (!node.getLeaf()) {
            parent = node;
            idExists = node.findID(id);

            if (idExists) {
                // The value already exists in the tree and can't be added
                System.out.println("ID já existe. Não foi possivel inserir!");
                return;
            }
            index = node.findPosition(id);

            node = node.getPointer(index);
        }

        // Case 2: the node has space for a new insertion
        if (node.getQuantity() < node.getNumberOfChildrens()) {
            node.insert(id, address);
            return;
        }

        // Case 3: There is no space in the node
        splitNode(node, parent, index, id, address);
    }

    private void splitNode(Node node, Node parent, int index, int data, long address) {
        Node newNode = new Node();

        int[] tempData = new int[node.getNumberOfChildrens() + 1];
        long[] tempAddress = new long[node.getNumberOfChildrens() + 1];
        Node[] tempPointer = new Node[node.getNumberOfChildrens() + 2];

        // Copy the values of node children for an temporary array
        for (int i = 0; i < node.getNumberOfChildrens(); i++) {
            tempData[i] = node.getData(i);
            tempAddress[i] = node.getAddress(i);
            tempPointer[i] = node.getPointer(i);
        }
        tempPointer[node.getNumberOfChildrens()] = node.getPointer(node.getNumberOfChildrens());

        // Find the position to insert the new id
        int pos = node.findPosition(data);

        // Insert the value in the temporary array
        for (int i = node.getNumberOfChildrens(); i > pos; i--) {
            tempData[i] = tempData[i - 1];
            tempAddress[i] = tempAddress[i - 1];
            tempPointer[i + 1] = tempPointer[i];
        }
        tempData[pos] = data;
        tempAddress[pos] = address;
        tempPointer[pos + 1] = newNode;

        // Split the node in two
        int medianIndex = node.getNumberOfChildrens() / 2;
        node.setQuantity(medianIndex);
        for (int i = 0; i < medianIndex; i++) {
            node.setData(i, tempData[i]);
            node.setAddress(i, tempAddress[i]);
            node.setPointer(i, tempPointer[i]);
        }
        node.setPointer(medianIndex, tempPointer[medianIndex]);

        newNode.setQuantity(node.getNumberOfChildrens() - medianIndex);
        for (int i = medianIndex + 1; i <= node.getNumberOfChildrens(); i++) {
            newNode.setData(i - medianIndex - 1, tempData[i]);
            newNode.setAddress(i - medianIndex - 1, tempAddress[i]);
            newNode.setPointer(i - medianIndex - 1, tempPointer[i]);
        }
        newNode.setPointer(newNode.getQuantity(), tempPointer[node.getNumberOfChildrens() + 1]);

        // Case 4: recursive split from parent
        if (parent != null) {
            newNode.setLeaf(node.getLeaf());

            // Insert the value from media in parent
            int pointerIndex = parent.findPosition(data);
            for (int i = parent.getQuantity(); i > pointerIndex; i--) {
                parent.setPointer(i + 1, parent.getPointer(i));
            }
            parent.setPointer(pointerIndex + 1, newNode);
            parent.insert(tempData[medianIndex], tempAddress[medianIndex]);

            if (parent.getQuantity() == parent.getNumberOfChildrens()) {
                splitNode(parent, null, -1, tempData[medianIndex], tempAddress[medianIndex]);
            }
        } else {

            // Case 5: split the root
            Node newRoot = new Node();
            newRoot.setLeaf(false);
            setRoot(newRoot);

            node.setLeaf(false);
            newNode.setLeaf(node.getLeaf());

            newRoot.setPointer(0, node);
            newRoot.setPointer(1, newNode);
            newRoot.insert(tempData[medianIndex], tempAddress[medianIndex]);
            newRoot.setQuantity(1);
        }
    }

    public Node searchId(int id) {
        Node currentNode = root;
        while (currentNode != null) {
            int position = currentNode.findPosition(id);
            for (int i = position; i < currentNode.getQuantity(); i++) {
                if (currentNode.getData(i) == id) {
                    return currentNode;
                }
            }
            currentNode = currentNode.getPointer(position);
        }
        return null; // Node not found in tree
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