import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.Stack;

public class BTree { 
RandomAccessFile f; 
int order; 
int blockSize; 
long root; 
long free;
Stack<Long> paths;
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
   /**
    * Reads a single node from the file
    * @param addr address of node
    */
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
           f.seek(getFirstChild(addr)); 
           int i = 0;
           for (i = 0; i < Math.abs(count); i++ ) {
               children[i] = f.readLong();
           }
           
           //Its a leaf
           if (count < 0) {
               //Then read the last addr
               f.seek(getLastChild(addr));
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
   
   private void writeNode(long addr) {
       //Writes the node to the file at location addr
       try {
           f.seek(addr);
           f.writeInt(count);
           int i = 0;
           //Write out the keys
           for(i = 0; i < Math.abs(count); i++) {
               f.writeInt(keys[i]);
           }
           //Go to start of children and write them out
           f.seek(getFirstChild(addr));
           for(i = 0; i < Math.abs(count); i++) {
               f.writeLong(children[i]);
           }
           
           //If a leaf, write out at the end
           if(count < 0) {
               f.seek(getLastChild(addr));
               f.writeLong(children[i]);
          //Else its not a leaf, write it out in the next position.
           } else {
               f.writeLong(children[i]);
           }
       } catch (IOException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
   }
   /**
    * Returns the last child address
    * @param addr start of node address
    * @return last child address
    */
   private long getLastChild(long addr) {
       //Start at the node, Go past the keys, Go past all but 1 child
       return addr + (order * 4) + (order * 7);
   }
   
   /**
    * Returns the start of children addresses
    * @param addr start of node address
    * @return first child address
    */
   private long getFirstChild(long addr) {
       //Start at the node, Go past the keys
       return addr + (order * 4);
   }
   
   private boolean isLeaf() {
       return count < 0;
   }
}  
/* ************************************************
 * End of Node class
 * ************************************************
 */

public BTree(String filename, int bsize) { 
//bsize  is the block size. This value is used to calculate the order of the B+Tree
//all B+Tree nodes will use bsize bytes 
//make a new B+tree
    try {
        f = new RandomAccessFile(filename,"rw");
        //Set everything up
        root = 0;
        free = 0;
        blockSize = bsize; 
        order = bsize / 12;
        paths = new Stack<>();
        //Write everything out
        f.seek(0);
        f.writeLong(root);
        f.writeLong(free);
        f.writeInt(blockSize);
        
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
            paths = new Stack<>();
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
        int i = 0;
        paths.push(root);
        BTreeNode r = new BTreeNode(root);
        
        while(k != r.keys[i] && !r.isLeaf()) {
            //Look at in index leaves to guide where to go
            for(i = 0; i < Math.abs(r.count); i++) {
                if(k < r.keys[i]) {
                   break;
                   //We have found where to look next in the path
                }
                //We looked at all the keys and are at a leaf, k is not there.
                if (i > Math.abs(r.count) && r.isLeaf()) {
                    return 0;
                }
            }
            //Add to the paths the address we are going to look at
            paths.push(r.children[i]);
            r = new BTreeNode(r.children[i]);
        }
        //We stopped looping that must mean we found the value
        return r.children[i];
    } 
    public LinkedList<Long> rangeSearch(int low, int high) {
    //PRE: low <= high 
    /* 
       
       return a list of row addresses for all keys in the range low to high inclusive 
       return an empty list when no keys are in the range 
    */ 
    }
    public void print() { 
    //print the B+Tree to standard output 
    //print one node per line 
    } 
    public void close() { 
    //close the B+tree. The tree should not be accessed after close is called 
    } 
} 
