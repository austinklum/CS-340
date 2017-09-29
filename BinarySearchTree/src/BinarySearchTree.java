import java.io.*; 
import java.util.*; 

public class BinarySearchTree { 
/* 
Implements a binary search tree of ints stored in a random access file. 
Duplicates are recorded by a count field associated with the int
*/ 
    final int CREATE = 0;
    final int REUSE = 1; 
    private RandomAccessFile f; 
    long root; //the address of the root node in the file 
    long free; //the address in the file of the first node in the free list

private class Node { 
    private long left; 
    private int data; 
    private int count; 
    private long right;
    private long addr;
    
    private Node(long L,int d, long r, long a) { 
    //constructor for a new node 
        data  = d;
        count = 1;
        left  = L;
        right = r;
        addr  = a;
           
    } 
    private Node(long addr) throws IOException{ 
    //constructor for a node that exists and is stored in the file
       // f = new RandomAccessFile(fileName,"r");
        f.seek(addr);
        
        data  = f.readInt();
        count = f.readInt();
        left  = f.readLong();
        right = f.readLong();
        this.addr = addr;
        
    } 
    private void writeNode() throws IOException { 
    //writes the node to the file at location addr
        f.seek(addr);
        f.writeInt(data);
        f.writeInt(count);
        f.writeLong(left);
        f.writeLong(right);
    }
    private void writeNode(Long addr) throws IOException {
       new Node(addr).writeNode();
    }
}

    public BinarySearchTree(String fname, int mode) throws IOException{ 
    //if mode is CREATE a new empty file is created 
    //if mode is CREATE and a file with file name fname exists the file with fname must be
    //deleted before the new empty file is created 
    //if mode is REUSE an existing file is used if it exists otherwise a new empty file
    //is created 
        File path = new File("tree.txt");
        if (path.exists() && mode == CREATE) {
            path.delete();
        } 
        
        f = new RandomAccessFile(path,"rw");
        
        if(mode == CREATE) {
            root = 0;
            free = 0;
            f.writeLong(root);
            f.writeLong(free);
        } else {
            f.seek(0);
            root = f.readLong();
            free = f.readLong();
        }
        
    }
    public void insert(int d) throws IOException { 
    //insert d into the tree
    //if d is in the tree increment the count field associated with d
        root = insert(new Node(root), d);
        new Node(root).writeNode();
    } 
    
    private Long insert(Node node, int d) throws IOException {
        if (node == null) {
            return free; //Case when we walked off tree. getFree();?
        }
        if (node.data > d) {
            node.left = insert(new Node(node.left), d); //Case when we need to look left
            node.writeNode();
        }
        else if (node.data < d) {
            node.right = insert(new Node(node.right), d); //Case when we need to look right
            node.writeNode();
        }
        else {
            node.count++; //Case when a duplicate
            node.writeNode();
        }
        
        return node.addr; //Base case. Return self.
    }
    
    public int find(int d) throws IOException{ 
    //if d is in the tree return the value of count associated with d 
    //otherwise return0
        return find(new Node(root), d);
    }
    
    private int find(Node r, int d) throws IOException {
        if(r == null) return 0;
        if (r.data > d) return find(new Node(r.left), d);
        if (r.data < d) return find(new Node(r.right), d);
        return r.count;
    }
    
    public void removeOne(int d) throws IOException{ 
    //remove one copy of d from the tree
    //if the copy is the last copy removed from the tree
    //if d is not in the tree the method has no effect
    } 
    public void removeAll(int d) throws IOException{ 
    //removed from the tree
    //if d is not in the tree the method has no effect
    } 
    public void close() { 
    //close the randomaccessfile
    //before closing update the values of root and free if necessary 
    } 
}