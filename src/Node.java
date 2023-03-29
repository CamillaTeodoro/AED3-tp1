import java.io.*;

public class Node {

    private long address;

    // Atributtes
    private int quantity = 0;

    private Node pointer1 = null;
    private int data1;
    private long address1 = -1;

    private Node pointer2 = null;
    private int data2;
    private long address2 = -1;

    private Node pointer3 = null;
    private int data3;
    private long address3 = -1;

    private Node pointer4 = null;
    private int data4;
    private long address4 = -1;

    private Node pointer5 = null;
    private int data5;
    private long address5 = -1;

    private Node pointer6 = null;
    private int data6;
    private long address6 = -1;

    private Node pointer7 = null;
    private int data7;
    private long address7 = -1;

    private Node pointer8 = null;

    // Constructor

    Node() {

    }

    public Node(long address, int quantity, Node pointer1, int data1, long address1, Node pointer2, int data2,
            long address2, Node pointer3, int data3, long address3, Node pointer4, int data4, long address4,
            Node pointer5, int data5, long address5, Node pointer6, int data6, long address6, Node pointer7, int data7,
            long address7, Node pointer8) {
        this.address = address;
        this.quantity = quantity;
        this.pointer1 = pointer1;
        this.data1 = data1;
        this.address1 = address1;
        this.pointer2 = pointer2;
        this.data2 = data2;
        this.address2 = address2;
        this.pointer3 = pointer3;
        this.data3 = data3;
        this.address3 = address3;
        this.pointer4 = pointer4;
        this.data4 = data4;
        this.address4 = address4;
        this.pointer5 = pointer5;
        this.data5 = data5;
        this.address5 = address5;
        this.pointer6 = pointer6;
        this.data6 = data6;
        this.address6 = address6;
        this.pointer7 = pointer7;
        this.data7 = data7;
        this.address7 = address7;
        this.pointer8 = pointer8;
    }

    // Getters and Setters

    public long getAddress() {
        return address;
    }

    public void setAddress(long address) {
        this.address = address;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Node getPointer1() {
        return pointer1;
    }

    public void setPointer1(Node pointer1) {
        this.pointer1 = pointer1;
    }

    public int getData1() {
        return data1;
    }

    public void setData1(int data1) {
        this.data1 = data1;
    }

    public long getAddress1() {
        return address1;
    }

    public void setAddress1(long address1) {
        this.address1 = address1;
    }

    public Node getPointer2() {
        return pointer2;
    }

    public void setPointer2(Node pointer2) {
        this.pointer2 = pointer2;
    }

    public int getData2() {
        return data2;
    }

    public void setData2(int data2) {
        this.data2 = data2;
    }

    public long getAddress2() {
        return address2;
    }

    public void setAddress2(long address2) {
        this.address2 = address2;
    }

    public Node getPointer3() {
        return pointer3;
    }

    public void setPointer3(Node pointer3) {
        this.pointer3 = pointer3;
    }

    public int getData3() {
        return data3;
    }

    public void setData3(int data3) {
        this.data3 = data3;
    }

    public long getAddress3() {
        return address3;
    }

    public void setAddress3(long address3) {
        this.address3 = address3;
    }

    public Node getPointer4() {
        return pointer4;
    }

    public void setPointer4(Node pointer4) {
        this.pointer4 = pointer4;
    }

    public int getData4() {
        return data4;
    }

    public void setData4(int data4) {
        this.data4 = data4;
    }

    public long getAddress4() {
        return address4;
    }

    public void setAddress4(long address4) {
        this.address4 = address4;
    }

    public Node getPointer5() {
        return pointer5;
    }

    public void setPointer5(Node pointer5) {
        this.pointer5 = pointer5;
    }

    public int getData5() {
        return data5;
    }

    public void setData5(int data5) {
        this.data5 = data5;
    }

    public long getAddress5() {
        return address5;
    }

    public void setAddress5(long address5) {
        this.address5 = address5;
    }

    public Node getPointer6() {
        return pointer6;
    }

    public void setPointer6(Node pointer6) {
        this.pointer6 = pointer6;
    }

    public int getData6() {
        return data6;
    }

    public void setData6(int data6) {
        this.data6 = data6;
    }

    public long getAddress6() {
        return address6;
    }

    public void setAddress6(long address6) {
        this.address6 = address6;
    }

    public Node getPointer7() {
        return pointer7;
    }

    public void setPointer7(Node pointer7) {
        this.pointer7 = pointer7;
    }

    public int getData7() {
        return data7;
    }

    public void setData7(int data7) {
        this.data7 = data7;
    }

    public long getAddress7() {
        return address7;
    }

    public void setAddress7(long address7) {
        this.address7 = address7;
    }

    public Node getPointer8() {
        return pointer8;
    }

    public void setPointer8(Node pointer8) {
        this.pointer8 = pointer8;
    }

    // Turns an object into a byte array
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.quantity);
        dos.writeLong(this.pointer1 == null ? -1 : this.pointer1.address);
        dos.writeInt(this.data1);
        dos.writeLong(this.address1);
        dos.writeLong(this.pointer2 == null ? -1 : this.pointer2.address);
        dos.writeInt(this.data2);
        dos.writeLong(this.address2);
        dos.writeLong(this.pointer3 == null ? -1 : this.pointer3.address);
        dos.writeInt(this.data3);
        dos.writeLong(this.address3);
        dos.writeLong(this.pointer4 == null ? -1 : this.pointer4.address);
        dos.writeInt(this.data4);
        dos.writeLong(this.address4);
        dos.writeLong(this.pointer5 == null ? -1 : this.pointer5.address);
        dos.writeInt(this.data5);
        dos.writeLong(this.address5);
        dos.writeLong(this.pointer6 == null ? -1 : this.pointer6.address);
        dos.writeInt(this.data6);
        dos.writeLong(this.address6);
        dos.writeLong(this.pointer7 == null ? -1 : this.pointer7.address);
        dos.writeInt(this.data7);
        dos.writeLong(this.address7);
        dos.writeLong(this.pointer8 == null ? -1 : this.pointer8.address);

        return baos.toByteArray();
    }

    // Turns a byte array into an object
    public void fromByteArray(byte[] b) throws IOException {

        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        this.quantity = dis.readInt();
        this.pointer1.address = dis.readLong();
        this.data1 = dis.readInt();
        this.address1 = dis.readLong();
        this.pointer2.address = dis.readLong();
        this.data2 = dis.readInt();
        this.address2 = dis.readLong();
        this.pointer3.address = dis.readLong();
        this.data3 = dis.readInt();
        this.address3 = dis.readLong();
        this.pointer4.address = dis.readLong();
        this.data4 = dis.readInt();
        this.address4 = dis.readLong();
        this.pointer5.address = dis.readLong();
        this.data5 = dis.readInt();
        this.address5 = dis.readLong();
        this.pointer6.address = dis.readLong();
        this.data6 = dis.readInt();
        this.address6 = dis.readLong();
        this.pointer7.address = dis.readLong();
        this.data7 = dis.readInt();
        this.address7 = dis.readLong();
        this.pointer8.address = dis.readLong();

    }
}
