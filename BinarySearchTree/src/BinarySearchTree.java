import java.io.*; 
import java.util.*; 

public class BinarySearchTree { 
/* 
Implements a binary search tree of ints stored in a random access file. 
Duplicates are recorded by a count field associated with the int
*/ 
    final int CREATE = 0;
    final int REUSE = 1; 
    final String fileName = "ppt-Tree";
    private RandomAccessFile f; 
    long root; //the address of the root node in the file 
    long free; //the address in the file of the first node in the free list

private class Node { 
    private long left; 
    private int data; 
    private int count; 
    private long right;
    
    private Node(long L,int d, long r) { 
    //constructor for a new node 
        data  = d;
        count = 1;
        left  = L;
        right = r;
           
    } 
    private Node(long addr) throws IOException{ 
    //constructor for a node that exists and is stored in the file
        f = new RandomAccessFile(fileName,"r");
        f.seek(addr);
        
        data  = f.readInt();
        count = f.readInt();
        left  = f.readLong();
        right = f.readLong();
        
    } 
    private void writeNode(long addr) throws IOException { 
    //writes the node to the file at location addr
    }
}

    public BinarySearchTree(String fname,int mode) throws IOException{ 
    //if mode is CREATE a new empty file is created 
    //if mode is CREATE and a file with file name fname exists the file with fname must be
    //deleted before the new empty file is created 
    //if mode is REUSE an existing file is used if it exists otherwise a new empty file
    //is created 
    
    }
    public void insert(int d) throws IOException{ 
    //insert d into the tree
    //if d is in the tree increment the count field associated with d
        root = insert(new Node(root), d);
    } 
    
    private Long insert(Node node, int d) {
        if (node == null) return free; //Case when we walked off tree
        if (node.data > d)  node.left = insert(new Node(node.left), d); //Case when we need to look left
        else if (node.data < d) node.right = insert(new Node(node.right), d); //Case when we need to look right
        else    node.count++; //Case when a duplicate
        return node;
    }
    
    public int find(int d) throws IOException{ 
    //if d is in the tree return the value of count associated with d 
    //otherwise return0 
        return 0;
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