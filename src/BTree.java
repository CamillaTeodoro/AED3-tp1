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

        this.root.setData(0, 29);
        this.root.setQuantity(1);
        this.root.setPointer(0, new Node());
        this.root.setLeaf(false);
        this.root.getPointer(0).setData(0, 8);
        this.root.getPointer(0).setData(1, 15);
        this.root.getPointer(0).setQuantity(2);
        this.root.getPointer(0).setPointer(0, new Node());
        this.root.getPointer(0).setLeaf(false);
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
        this.root.getPointer(1).setLeaf(false);

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

                return;
            }

            // Case 3: the Id is in a leaf node that will have more than 50% occupancy rate
            // after the deletion
            if (nodeWithElement.willBeBalanced()) {

                for (int i = position; i < nodeWithElement.getQuantity() - 1; i++) {
                    nodeWithElement.setData(i, nodeWithElement.getData(i + 1));
                    nodeWithElement.setAddress(i, nodeWithElement.getAddress(i + 1));
                    nodeWithElement.setPointer(i + 1, nodeWithElement.getPointer(i + 2));

                }
                nodeWithElement.setQuantity(nodeWithElement.getQuantity() - 1);

            } else {
                // Case 4: the Id is in a leaf node that will have less than 50% occupancy
                // rate after the deletion
                balanceOrMerge(nodeWithElement, id);
            }

        } else {
            System.out.println("Id não existe na arvore");
        }

    }

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

    private void balanceOrMerge(Node node, int id) {
        Node parent = getParent(node);
        int nodeIndex = parent.findPointer(node);

        // Balance nodes

        // the id is in the first pointer node
        if (nodeIndex == 0) {
            // Check right sibling
            Node rightSibling = parent.getPointer(nodeIndex + 1);
            if (rightSibling != null && rightSibling.getQuantity() > node.getOrder() / 2) {
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
                parent.setData(nodeIndex, key);
                parent.setAddress(nodeIndex, address);

                // delete the first id from right sibling
                for (int i = 0; i < rightSibling.getQuantity() - 1; i++) {
                    rightSibling.setData(i, rightSibling.getData(i + 1));
                    rightSibling.setAddress(i, rightSibling.getAddress(i + 1));
                    rightSibling.setPointer(i + 1, rightSibling.getPointer(i + 2));
                }
                rightSibling.setQuantity(rightSibling.getQuantity() - 1);
                return;
            }
            // the id is in the last pointer node
        } else if (nodeIndex == parent.getQuantity()) {
            // Check left sibling
            Node leftSibling = parent.getPointer(nodeIndex - 1);
            if (leftSibling != null && leftSibling.getQuantity() > node.getOrder() / 2) {
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

                // receive id from parent
                node.setData(0, parent.getData(nodeIndex - 1));
                node.setAddress(0, parent.getAddress(nodeIndex - 1));
                node.setPointer(1, node.getPointer(0));
                node.setPointer(0, child);

                // set in parent the id lended for leftsibling
                parent.setData(nodeIndex - 1, key);
                parent.setAddress(nodeIndex - 1, address);
                // delete the first id from right sibling
                leftSibling.setQuantity(leftSibling.getQuantity() - 1);
                return;
            }
            // the id is not in the first or last pointer node
        } else {
            // Check both siblings
            Node leftSibling = parent.getPointer(nodeIndex - 1);
            Node rightSibling = parent.getPointer(nodeIndex + 1);
            if (leftSibling != null && leftSibling.getQuantity() > node.getOrder() / 2) {
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
                return;
            } else if (rightSibling != null && rightSibling.getQuantity() > node.getOrder() / 2) {
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
                parent.setData(nodeIndex, key);
                parent.setAddress(nodeIndex, address);

                // delete the first id from right sibling
                for (int i = 0; i < rightSibling.getQuantity() - 1; i++) {
                    rightSibling.setData(i, rightSibling.getData(i + 1));
                    rightSibling.setAddress(i, rightSibling.getAddress(i + 1));
                    rightSibling.setPointer(i + 1, rightSibling.getPointer(i + 2));
                }
                rightSibling.setQuantity(rightSibling.getQuantity() - 1);
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
            leftSibling.setData(leftSibling.getQuantity(), parent.getData(nodeIndex - 1));
            leftSibling.setAddress(leftSibling.getQuantity(), parent.getAddress(nodeIndex - 1));
            leftSibling.setPointer(leftSibling.getQuantity(), node.getPointer(0));
            if (!leftSibling.getLeaf()) {
                leftSibling.setPointer(leftSibling.getQuantity() + 1, node.getPointer(1));
            }
            leftSibling.setQuantity(leftSibling.getQuantity() + 1);

            parent.setPointer(nodeIndex - 1, leftSibling);
            parent.setQuantity(parent.getQuantity() - 1);
        }
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