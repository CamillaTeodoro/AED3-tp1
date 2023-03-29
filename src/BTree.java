import java.io.FileNotFoundException;
//import java.io.RandomAccessFile;

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
    public BTree() throws FileNotFoundException {
        // RandomAccessFile bTree = new RandomAccessFile("../db/bTree.db", "rw");
        this.root = new Node();
        this.root.setData1(29);
        this.root.setPointer1(new Node());
        this.root.getPointer1().setData1(8);
        this.root.getPointer1().setData2(15);
        this.root.getPointer1().setPointer1(new Node());
        this.root.getPointer1().getPointer1().setData1(1);
        this.root.getPointer1().getPointer1().setData2(3);
        this.root.getPointer1().getPointer1().setData3(4);
        this.root.getPointer1().getPointer1().setData4(7);
        this.root.getPointer1().setPointer2(new Node());
        this.root.getPointer1().getPointer2().setData1(10);
        this.root.getPointer1().getPointer2().setData2(12);
        this.root.getPointer1().getPointer2().setData3(13);
        this.root.getPointer1().getPointer2().setData4(14);
        this.root.getPointer1().setPointer3(new Node());
        this.root.getPointer1().getPointer3().setData1(18);
        this.root.getPointer1().getPointer3().setData2(20);
        this.root.getPointer1().getPointer3().setData3(25);

        this.root.setPointer2(new Node());
        this.root.getPointer2().setData1(37);
        this.root.getPointer2().setData2(45);
        this.root.getPointer2().setData3(60);
        this.root.getPointer2().setPointer1(new Node());
        this.root.getPointer2().getPointer1().setData1(30);
        this.root.getPointer2().getPointer1().setData2(35);
        this.root.getPointer2().setPointer2(new Node());
        this.root.getPointer2().getPointer2().setData1(40);
        this.root.getPointer2().getPointer2().setData2(41);
        this.root.getPointer2().getPointer2().setData3(42);
        this.root.getPointer2().getPointer2().setData4(43);
        this.root.getPointer2().setPointer3(new Node());
        this.root.getPointer2().getPointer3().setData1(51);
        this.root.getPointer2().getPointer3().setData2(52);
        this.root.getPointer2().setPointer4(new Node());
        this.root.getPointer2().getPointer4().setData1(70);
        this.root.getPointer2().getPointer4().setData2(77);
        this.root.getPointer2().getPointer4().setData3(83);

    }

    public BTree(Node root) {
        this.root = root;
    }

    // Methods

    public void insert(int id, long address) {

    }

    public void search() {

    }

    public void delete(int id, long address) {

    }

    public void split(Node node) {

    }

    public void regroup(Node node1, Node node2) {

    }

    public void print() {

    }

    public void traverse() {

    }

}
