import java.io.IOException;
import java.io.RandomAccessFile;

public class BTree {

    // Atributtes

    private Node root;
    private RandomAccessFile file;

    // There is a long in the beginning of the file that
    // points to the position of the root node in the file
    // A long takes 8 bytes (0 - 7) so the first position
    // starts on byte 8
    private static final long INITIAL_POSITION = 8;

    private static final String INDENTATION = "---";

    private long nextPosition = INITIAL_POSITION;

    // Getters and Setters

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    // Constructors
    public BTree() {

    }

    public BTree(String filePath) throws IOException {
        file = new RandomAccessFile(filePath, "rw");
        if (file.length() > 0) {
            mountBTree();
        }
    }

    /**
     * creates a node based on the data read from the file
     * 
     * @param node
     * @throws IOException
     */
    private void loadNode(Node node) throws IOException {
        file.seek(node.getInitialAddress());
        byte[] record = new byte[Node.RECORD_SIZE];
        file.read(record);
        node.fromByteArray(record);
    }

    private void mountNode(Node node) throws IOException {
        loadNode(node);
        for (int i = 0; i < Node.ORDER; i++) {
            if (node.getPointer(i) == null)
                continue;
            mountNode(node.getPointer(i));
        }
    }

    /**
     * create in memory the B-Tree using the data in file
     * 
     * @param file
     * @throws IOException
     */
    private void mountBTree() throws IOException {
        file.seek(0);
        long rootPosition = file.readLong();
        Node node = new Node(rootPosition);
        setRoot(node);
        mountNode(node);
    }

    public long getNextPosition() {
        long position = nextPosition;
        nextPosition += Node.RECORD_SIZE;
        return position;
    }

    public void reset() throws IOException {
        setRoot(null);
        file.setLength(0);
    }

    // Methods
    /**
     * Insert the id and the address in the bTree
     * 
     * @throws IOException
     */
    public void insert(int id, long address) throws IOException {

        // Case 1: empty tree
        if (root == null) {
            Node newNode = new Node(getNextPosition());
            newNode.insert(id, address);
            setRoot(newNode);
            printBTreeFile();
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

        idExists = node.findID(id);

        if (idExists) {
            // The value already exists in the tree and can't be added
            System.out.println("ID já existe. Não foi possivel inserir!");
            return;
        }

        // Case 2: the node has space for a new insertion
        if (!node.isFull()) {
            node.insert(id, address);
            printBTreeFile();
            return;
        }

        // Case 3: There is no space in the node
        splitNode(node, parent, id, address);

        printBTreeFile();
    }

    private void splitNode(Node node, Node parent, int id, long address) throws IOException {
        Node newNode = new Node(getNextPosition());
        newNode.setLeaf(node.getLeaf());

        // one more space for the new value
        int[] tempData = new int[Node.NUMBER_OF_CHILDREN + 1];
        long[] tempAddress = new long[Node.NUMBER_OF_CHILDREN + 1];
        Node[] tempPointer = new Node[Node.NUMBER_OF_CHILDREN + 2];

        // Copy the values of node children for the temporary array
        for (int i = 0; i < Node.NUMBER_OF_CHILDREN; i++) {
            tempData[i] = node.getData(i);
            tempAddress[i] = node.getAddress(i);
            tempPointer[i] = node.getPointer(i);
        }
        tempPointer[Node.NUMBER_OF_CHILDREN] = node.getPointer(Node.NUMBER_OF_CHILDREN);

        // Find the position to insert the new id
        int pos = node.findPosition(id);

        // Insert the value in the temporary array
        for (int i = Node.NUMBER_OF_CHILDREN; i > pos; i--) {
            tempData[i] = tempData[i - 1];
            tempAddress[i] = tempAddress[i - 1];
            tempPointer[i + 1] = tempPointer[i];
        }
        tempData[pos] = id;
        tempAddress[pos] = address;
        tempPointer[pos + 1] = null;

        // Split the node in two
        int medianIndex = (Node.NUMBER_OF_CHILDREN / 2);

        node.setQuantity(medianIndex);
        for (int i = 0; i < medianIndex; i++) {
            node.setData(i, tempData[i]);
            node.setAddress(i, tempAddress[i]);
            node.setPointer(i, tempPointer[i]);
        }
        node.setPointer(medianIndex, tempPointer[medianIndex]);

        newNode.setQuantity(Node.NUMBER_OF_CHILDREN - medianIndex);
        for (int i = medianIndex + 1; i <= Node.NUMBER_OF_CHILDREN; i++) {
            newNode.setData(i - medianIndex - 1, tempData[i]);
            newNode.setAddress(i - medianIndex - 1, tempAddress[i]);
            newNode.setPointer(i - medianIndex - 1, tempPointer[i]);
        }
        newNode.setPointer(newNode.getQuantity(), tempPointer[Node.NUMBER_OF_CHILDREN + 1]);

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
                printBTreeFile();
                return;
            }

            // Case 5: recursive split from parent

            splitNode(parent, getParent(parent), tempData[medianIndex], tempAddress[medianIndex]);

            parent = getParent(node);
            int index = parent.findPosition(tempData[medianIndex]);
            parent.setPointer(index + 1, newNode);
            printBTreeFile();
        } else {

            // Case 6: split the root
            Node newRoot = new Node(getNextPosition());
            newRoot.setLeaf(false);
            setRoot(newRoot);

            newRoot.setPointer(0, node);
            newRoot.setPointer(1, newNode);
            newRoot.insert(tempData[medianIndex], tempAddress[medianIndex]);
            newRoot.setQuantity(1);
            printBTreeFile();
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

    public void delete(int id) throws IOException {
        mountBTree();
        // Case 1: empty tree
        if (root == null) {
            System.out.println("Arquivo vazio.");
            return;
        }

        // search for the node with the id
        Node nodeWithElement = searchId(id);
        if (nodeWithElement != null) {
            int position = nodeWithElement.findPosition(id);

            // Case 2: the Id is not in a leaf node
            if (!nodeWithElement.getLeaf()) {

                // Node to be deleted is not a leaf node
                Node predecessorNode = nodeWithElement.getPointer(position);
                while (!predecessorNode.getLeaf()) {
                    predecessorNode = predecessorNode.getPointer(predecessorNode.getQuantity());
                }
                int predecessorId = predecessorNode.getData(predecessorNode.getQuantity() - 1);
                long predecessorAddress = predecessorNode.getAddress(predecessorNode.getQuantity() - 1);
                nodeWithElement.setData(position, predecessorId);
                nodeWithElement.setAddress(position, predecessorAddress);

                predecessorNode.setQuantity(predecessorNode.getQuantity() - 1);

                if (!predecessorNode.isBalanced()) {
                    balanceOrMerge(predecessorNode, id);
                }
                printBTreeFile();
                return;
            }

            // Case 3: the Id is in a leaf node that will have more than 50% occupancy rate
            // after the deletion
            if (nodeWithElement.willBeBalanced() || root.getLeaf()) {

                for (int i = position; i < nodeWithElement.getQuantity() - 1; i++) {
                    nodeWithElement.setData(i, nodeWithElement.getData(i + 1));
                    nodeWithElement.setAddress(i, nodeWithElement.getAddress(i + 1));
                    nodeWithElement.setPointer(i + 1, nodeWithElement.getPointer(i + 2));

                }
                nodeWithElement.setQuantity(nodeWithElement.getQuantity() - 1);
                printBTreeFile();

            } else {
                // Case 4: the Id is in a leaf node that will have less than 50% occupancy
                // rate after the deletion
                balanceOrMerge(nodeWithElement, id);
                printBTreeFile();
            }

        } else {
            System.out.println("Id não existe na arvore");
        }

    }

    /**
     * Method to find the node`s parent
     * 
     * @param node
     * @return
     */
    public Node getParent(Node node) {
        Node currentNode = root;
        Node parent = null;

        while (currentNode != null) {
            int position = currentNode.findPosition(node.getData(0));
            for (int i = position; i < currentNode.getQuantity(); i++) {
                if (currentNode.getData(i) == node.getData(0)) {
                    return parent;
                }
            }
            parent = currentNode;
            currentNode = currentNode.getPointer(position);
        }
        return null;
    }

    /**
     * Recursive method to balance a node after a deletion
     * 
     * @param node
     * @param id
     * @throws IOException
     */
    private void balanceOrMerge(Node node, int id) throws IOException {

        Node parent = getParent(node);
        int nodeIndex = parent.findPointer(node);

        // Balance nodes

        // the id is in the first pointer node
        if (nodeIndex == 0) {
            // Check right sibling
            Node rightSibling = parent.getPointer(nodeIndex + 1);
            if (rightSibling != null && rightSibling.getQuantity() > Node.ORDER / 2) {

                borrowFromRightSibling(node, parent, nodeIndex, rightSibling, id);

                return;
            }
            // the id is in the last pointer node
        } else if (nodeIndex == parent.getQuantity()) {
            // Check left sibling
            Node leftSibling = parent.getPointer(nodeIndex - 1);
            if (leftSibling != null && leftSibling.getQuantity() > Node.ORDER / 2) {
                borrowFromLeftSibling(node, parent, nodeIndex,
                        leftSibling, id);

                return;
            }

        } else {
            // Check both siblings
            Node leftSibling = parent.getPointer(nodeIndex - 1);
            Node rightSibling = parent.getPointer(nodeIndex + 1);
            if (leftSibling != null && leftSibling.getQuantity() > Node.ORDER / 2) {
                borrowFromLeftSibling(node, parent, nodeIndex,
                        leftSibling, id);
                return;
            } else if (rightSibling != null && rightSibling.getQuantity() > Node.ORDER / 2) {

                borrowFromRightSibling(node, parent, nodeIndex, rightSibling, id);

                return;

            }
        }

        // Merge with left or right sibling
        if (nodeIndex == 0) {
            Node rightSibling = parent.getPointer(1);
            node.setData(node.getQuantity(), parent.getData(0));
            node.setAddress(node.getQuantity(), parent.getAddress(0));
            node.setPointer(node.getQuantity() - 1, rightSibling.getPointer(0));
            if (!node.getLeaf()) {
                node.setPointer(node.getQuantity(), rightSibling.getPointer(1));
            }
            node.setQuantity(node.getQuantity() + 1);

            parent.setPointer(0, node);
            parent.setQuantity(parent.getQuantity() - 1);

        } else {
            Node leftSibling = parent.getPointer(nodeIndex - 1);

            // transfer the parent id for leftsibling
            leftSibling.setData(leftSibling.getQuantity(), parent.getData(nodeIndex - 1));
            leftSibling.setAddress(leftSibling.getQuantity(), parent.getAddress(nodeIndex - 1));
            leftSibling.setPointer(leftSibling.getQuantity(), node.getPointer(0));

            leftSibling.setQuantity(leftSibling.getQuantity() + 1);

            // transfer the node id for leftsibling
            leftSibling.setData(leftSibling.getQuantity(), node.getData(nodeIndex - 1));
            leftSibling.setAddress(leftSibling.getQuantity(), node.getAddress(nodeIndex - 1));
            leftSibling.setPointer(leftSibling.getQuantity(), node.getPointer(0));

            if (!leftSibling.getLeaf()) {
                leftSibling.setPointer(leftSibling.getQuantity() + 1, node.getPointer(1));
            }
            leftSibling.setQuantity(leftSibling.getQuantity() + 1);

            parent.setPointer(nodeIndex - 1, leftSibling);
            for (int i = nodeIndex - 1; i < parent.getQuantity(); i++) {
                parent.setData(i, parent.getData(i + 1));
                parent.setAddress(i, parent.getAddress(i + 1));
                parent.setPointer(i + 1, parent.getPointer(i + 2));

            }
            parent.setQuantity(parent.getQuantity() - 1);

        }
    }

    /**
     * Method to borrow from left sibling when right node is unbalanced
     * 
     * @param node
     * @param parent
     * @param nodeIndex
     * @param leftSibling
     * @param id
     */

    public void borrowFromLeftSibling(Node node, Node parent, int nodeIndex,
            Node leftSibling, int id) {
        // Move a key and pointer from left sibling to node
        int key = leftSibling.getData(leftSibling.getQuantity() - 1);
        long address = leftSibling.getAddress(leftSibling.getQuantity() - 1);
        Node child = leftSibling.getPointer(leftSibling.getQuantity() - 1);
        // delete the id from the node and move id's to receive parents donation
        int pos = node.findPosition(id);
        for (int i = 0; i < pos; i++) {
            node.setData(i + 1, node.getData(i));
            node.setAddress(i + 1, node.getAddress(i));
            node.setPointer(i + 2, node.getPointer(i + 1));

        }
        node.setData(0, parent.getData(nodeIndex - 1));
        node.setAddress(0, parent.getAddress(nodeIndex - 1));
        node.setPointer(1, node.getPointer(0));
        node.setPointer(0, child);

        // set in parent the id lended for leftsibling
        parent.setData(nodeIndex - 1, key);
        parent.setAddress(nodeIndex - 1, address);

        // delete the first id from right sibling
        leftSibling.setQuantity(leftSibling.getQuantity() - 1);
    }

    /**
     * Method to borrow from right sibling when left node is unbalanced
     * 
     * @param node
     * @param parent
     * @param nodeIndex
     * @param rightSibling
     * @param id
     */
    public void borrowFromRightSibling(Node node, Node parent, int nodeIndex,
            Node rightSibling, int id) {

        // Move a key and pointer from right sibling to node
        int key = rightSibling.getData(0);
        long address = rightSibling.getAddress(0);
        Node child = rightSibling.getPointer(0);
        // remove id from the node
        int pos = node.findPosition(id);
        for (int i = pos; i < node.getQuantity() - 1; i++) {
            node.setData(i, node.getData(i + 1));
            node.setAddress(i, node.getAddress(i + 1));
            node.setPointer(i + 1, node.getPointer(i + 2));

        }
        node.setQuantity(node.getQuantity() - 1);

        // receive the new id from parent
        node.setData(node.getQuantity(), parent.getData(nodeIndex));
        node.setAddress(node.getQuantity(), parent.getAddress(nodeIndex));
        node.setPointer(node.getQuantity() - 1, child);
        node.setQuantity(node.getQuantity() + 1);

        // set in parent the id lended for righsibling
        parent.setData(nodeIndex, key);
        parent.setAddress(nodeIndex, address);

        // delete the first id from right sibling
        for (int i = 0; i < rightSibling.getQuantity() - 1; i++) {
            rightSibling.setData(i, rightSibling.getData(i + 1));
            rightSibling.setAddress(i, rightSibling.getAddress(i + 1));
            rightSibling.setPointer(i + 1, rightSibling.getPointer(i + 2));
        }
        rightSibling.setQuantity(rightSibling.getQuantity() - 1);

    }

    /**
     * Recursive method for print the ID and the address
     */
    public void printRoot() {
        printBTree(this.getRoot());
    }

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

    /**
     * Recursive method to debug the tree structure
     */
    public void printStructure() {
        System.out.println("===============================");
        printStructure(root, "");
        System.out.println("===============================");
    }

    private void printStructure(Node node, String indent) {
        if (node == null)
            return;

        String text = indent + "(";
        for (int i = 0; i < node.getQuantity(); i++) {
            text += node.getData(i) + " ";
        }
        System.out.println(text.trim() + ")");

        for (int i = 0; i <= node.getQuantity(); i++) {
            if (!node.getLeaf()) {
                printStructure(node.getPointer(i), indent + INDENTATION);
            }
        }
    }

    /**
     * Method to write just the node in the file
     * 
     * @param node
     * @return
     */
    public boolean saveNode(Node node) {
        if (node == null)
            return false;
        try {
            file.seek(node.getInitialAddress());
            file.write(node.toByteArray());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Recursive method to write the entire b-tree in the file.
     * 
     * @throws IOException
     */

    public void printBTreeFile() throws IOException {
        this.file.setLength(0);
        file.writeLong(this.getRoot().getInitialAddress());
        printBTreeFile(this.getRoot());
    }

    public void printBTreeFile(Node node) {
        if (node == null)
            return;
        saveNode(node);

        for (int i = 0; i <= node.getQuantity(); i++) {
            if (!node.getLeaf()) {
                printBTreeFile(node.getPointer(i));
            }
        }
    }

    /**
     * Takes an id as paramater and returns the starting position of the record
     * in the sequencial file or -1
     * Read the file searching for the id
     * 
     * @param id
     * @return
     */
    public long findIndexInBTreeFile(int id) throws IOException {
        file.seek(0);
        long rootPosition = file.readLong();
        Node currentNode = new Node(rootPosition);
        loadNode(currentNode);

        while (currentNode != null) {
            int position = currentNode.findPosition(id);
            for (int i = position; i < currentNode.getQuantity(); i++) {
                if (currentNode.getData(i) == id) {
                    return currentNode.getAddress(i);
                }
            }
            currentNode = currentNode.getPointer(position);
            if (currentNode == null)
                return -1;
            loadNode(currentNode);
        }
        return -1;
    }

    /**
     * Search for the id in the file and updates it's address
     * 
     * @param id
     * @return
     */
    public boolean updateDBAddress(int id, long newAddress) throws IOException {
        file.seek(0);
        long rootPosition = file.readLong();
        Node currentNode = new Node(rootPosition);
        loadNode(currentNode);

        while (currentNode != null) {
            int position = currentNode.findPosition(id);
            for (int i = position; i < currentNode.getQuantity(); i++) {
                if (currentNode.getData(i) == id) {
                    currentNode.setAddress(i, newAddress);
                    saveNode(currentNode);
                    return true;
                }
            }
            currentNode = currentNode.getPointer(position);
            if (currentNode == null)
                return false;
            loadNode(currentNode);
        }
        return false;
    }

}