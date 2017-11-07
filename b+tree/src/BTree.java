import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;

public class BTree { 
RandomAccessFile f; 
int order; 
int blockSize; 
long root; 
long free; 
//add instance variables as needed. 
private class BTreeNode { 
   private int count;
   private int keys[]; 
   private long children[]; 
   //constructors and other method 
   private BTreeNode(int c, int k[], long childs[]) {
       count = c;
       keys = k;
       children = childs;
   }
   
   private BTreeNode (Long addr) {
       //If its not a child it has 1 more than the count
       //
       try {
           f.seek(addr);
           count = f.readInt();
           for(int i = 0; i < Math.abs(count); i++) {
               keys[i] = f.readInt();
           }
           
           //Start reading the children
           f.seek(addr + 4 * order); 
           int i = 0;
           for (i = 0; i < Math.abs(count); i++ ) {
               children[i] = f.readLong();
           }
           
           //Its a leaf
           if (count < 0) {
               //Then read the last addr
               f.seek(addr + 4 * order + 7 * order);
               children[i] = f.readLong();
           } else {
           //Its not a leaf
               //Then read the next addr
               children[i] = f.readLong();
           }
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
   
} 

public BTree(String filename, int bsize) { 
//bsize  is the block size. This value is used to calculate the order of the B+Tree
//all B+Tree nodes will use bsize bytes 
//make a new B+tree
    try {
        f = new RandomAccessFile(filename,"rw");
        root = 0;
        free = 0;
        blockSize = bsize; 
        order = bsize / 12;
    } catch (IOException e) {
        e.printStackTrace();
    }
} 
public BTree(String filename) { 
//open an existing B+Tree
    try {
        f = new RandomAccessFile(filename,"rw");
        f.seek(0);
        root = f.readLong();
        free = f.readLong();
        blockSize = f.readInt();
    } catch (IOException e) {
        e.printStackTrace();
    }
} 

public boolean insert(int key, long addr) { 
/* 
   If  key is not a duplicate add, key to the B+tree
   addr (in DBTable) is the address of the row that contains the key 
   return true if the key is added 
   return false if the key is a duplicate 
*/ 
} 
public long remove(int key) { 
/* 
   If  the key is in the Btree, remove the key and return the address of the row 
   return 0 if the key is not found in the B+tree
*/ 
} 

public long search(int k) { 
/* 
   This is an equality search 
   If the key is found return the address of the row with the key 
   otherwise return 0  
*/ 
} 
public LinkedList<Long> rangeSearch(int low, int high) 
//PRE: low <= high 
/* 
   
   return a list of row addresses for all keys in the range low to high inclusive 
   return an empty list when no keys are in the range 
*/ 

public void print() { 
//print the B+Tree to standard output 
//print one node per line 
} 
public void close() { 
//close the B+tree. The tree should not be accessed aler close is called 
} 
} 
