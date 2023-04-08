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
        // this.root = new Node();

        // this.root.setData(0, 10);
        // this.root.setPointer(0, new Node());
        // this.root.setLeaf(false);
        // this.root.getPointer(0).setData(0, 5);
        // this.root.getPointer(0).setPointer(0, new Node());
        // this.root.getPointer(0).setLeaf(false);
        // this.root.getPointer(0).getPointer(0).setData(0, 1);
        // this.root.getPointer(0).getPointer(0).setData(1, 2);
        // this.root.getPointer(0).setPointer(1, new Node());
        // this.root.getPointer(0).getPointer(1).setData(0, 7);
        // this.root.setPointer(1, new Node());
        // this.root.getPointer(1).setData(0, 20);
        // this.root.getPointer(1).setData(1, 40);
        // this.root.getPointer(1).setPointer(0, new Node());
        // this.root.getPointer(1).setLeaf(false);
        // this.root.getPointer(1).getPointer(0).setData(0, 15);
        // this.root.getPointer(1).setPointer(1, new Node());
        // this.root.getPointer(1).getPointer(1).setData(0, 30);
        // this.root.getPointer(1).setPointer(2, new Node());
        // this.root.getPointer(1).getPointer(2).setData(0, 50);

        // this.root.setQuantity(1);
        // this.root.getPointer(0).setQuantity(1);
        // this.root.getPointer(0).getPointer(0).setQuantity(2);
        // this.root.getPointer(0).getPointer(1).setQuantity(1);
        // this.root.getPointer(1).setQuantity(2);
        // this.root.getPointer(1).getPointer(0).setQuantity(1);
        // this.root.getPointer(1).getPointer(1).setQuantity(1);
        // this.root.getPointer(1).getPointer(2).setQuantity(1);

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
        if (!node.isFull()) {
            node.insert(id, address);
            return;
        }

        // Case 3: There is no space in the node
        splitNode(node, parent, id, address);
    }

    private void splitNode(Node node, Node parent, int id, long address) {
        Node newNode = new Node();

        // onde more space for the new value
        int[] tempData = new int[node.getNumberOfChildrens() + 1];
        long[] tempAddress = new long[node.getNumberOfChildrens() + 1];
        Node[] tempPointer = new Node[node.getNumberOfChildrens() + 2];

        // Copy the values of node children for the temporary array
        for (int i = 0; i < node.getNumberOfChildrens(); i++) {
            tempData[i] = node.getData(i);
            tempAddress[i] = node.getAddress(i);
            tempPointer[i] = node.getPointer(i);
        }
        tempPointer[node.getNumberOfChildrens()] = node.getPointer(node.getNumberOfChildrens());

        // Find the position to insert the new id
        int pos = node.findPosition(id);

        // Insert the value in the temporary array
        for (int i = node.getNumberOfChildrens(); i > pos; i--) {
            tempData[i] = tempData[i - 1];
            tempAddress[i] = tempAddress[i - 1];
            tempPointer[i + 1] = tempPointer[i];
        }
        tempData[pos] = id;
        tempAddress[pos] = address;
        tempPointer[pos + 1] = null;

        // Split the node in two
        int medianIndex = (node.getNumberOfChildrens() / 2) + 1;

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

        newNode.setLeaf(node.getLeaf());

        if (parent != null) {

            // Insert the value from media in parent
            int pointerIndex = parent.findPosition(id);

            // Case 4: parent is not full
            if (!parent.isFull()) {

                for (int i = parent.getQuantity(); i > pointerIndex; i--) {

                    parent.setPointer(i + 1, parent.getPointer(i));
                }
                parent.setPointer(pointerIndex + 1, newNode);
                parent.insert(tempData[medianIndex], tempAddress[medianIndex]);
                return;
            }

            // Case 5: recursive split from parent

            splitNode(parent, null, tempData[medianIndex], tempAddress[medianIndex]);
            int index = parent.findPosition(tempData[medianIndex]);
            parent.setPointer(index + 1, newNode);

        } else {

            // Case 6: split the root
            Node newRoot = new Node();
            newRoot.setLeaf(false);
            setRoot(newRoot);

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

    public void delete(int id) {

    }

    public void printRoot() {
        printBTree(this.getRoot());
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