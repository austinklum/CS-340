import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;

public class BTree { 
RandomAccessFile f; 
int order; 
int blockSize; 
long root; 
long free;
Stack<BTreeNode> paths;
//add instance variables as needed. 
private class BTreeNode { 
   private int count;
   private int keys[]; 
   private long children[];
   private long addr;
   //constructors and other method 
   private BTreeNode(int c, int k[], long childs[], long addr) {
       count     = c;
       keys      = k;
       children  = childs;
       this.addr = addr;
   }
   /**
    * Reads a single node from the file
    * @param addr address of node
    */
   private BTreeNode (long addr) {
       try {
           f.seek(addr);
           this.addr = addr;
           count = f.readInt();
           for (int i = 0; i < order; i++) {
               keys[i] = f.readInt();
           }
           
           for (int i = 0; i < order; i++) {
               children[i] = f.readLong();
           }
           
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
   
   /**
    *  //Writes the node to the file at location addr
    * @param addr address of node in file
    */
   private void writeNode() {
       try {
           f.seek(addr);
           
           f.writeInt(count);
           //Write out the keys
           for(int i = 0; i < order; i++) {
               f.writeInt(keys[i]);
           }
           //Write out the children
           for(int i = 0; i < order; i++) {
               f.writeLong(children[i]);
           }
       } catch (IOException e) {
           e.printStackTrace();
       }
   } 
   private boolean isLeaf() {
       return count < 0;
   }
   
   private boolean isRoom() {
       return Math.abs(count) < order - 1;
   }
   
   private void insertEntry(int k, long addr) {
       int i = Math.abs(count);
       while(i >= 0 && k < keys[i]) {
           //We know there is room for the node. So I cant index out of bounds
           //start at end of count
           //While k > r.keys
           //shift k[i] to k[i+1]
           
       /* --VISUAL-- 
        * key = 5, Order = 5
        * [1][6][10][?] 
        * Shift -> [1][6][10][10] -> [1][6][6][10] -> [1][5][6][10]
        */
           keys[i+1] = keys[i];
           children[i+1] = children[i];
           i--;
       }
       keys[i] = k;
       children[i] = addr;
       if(isLeaf()) {
           count--;
       } else {
           count++;
       }
   }
   
   /**
    * This method will split a node into 2 parts. <br>
    * Returns the new part and modifies the caller
    * @return The new node from split
    */
   private BTreeNode split(int key, long addr) {
       //Make a new temp node that is 1 bigger than normal nodes
       if(isLeaf()) {
           count--;
       } else {
           count++;
       }
       BTreeNode splitNode = new BTreeNode(count, Arrays.copyOf(keys, keys.length+1),Arrays.copyOf(children, children.length+1), addr);
       
       //Add the new entry to it
       splitNode.insertEntry(key,addr);
      
       //Determine the count of the new node. ??? Maybe use count field instead?
      int newCount = (int)Math.ceil(splitNode.keys.length/2);
      
       //split   the values  (including  k)  between node  and the newnode
       //Update the caller
       count = (newCount - Math.abs(count-1)); 
       keys = Arrays.copyOfRange(splitNode.keys, 0, newCount-1);
       children = Arrays.copyOfRange(splitNode.children, 0, newCount-1);
      
       if(splitNode.isLeaf()) {
           newCount*=-1;
           count*=-1;
       }
       
       //Create the return node
       BTreeNode newnode = new BTreeNode(newCount,Arrays.copyOfRange(splitNode.keys, newCount-1, splitNode.keys.length-1),Arrays.copyOfRange(splitNode.children, newCount -1, splitNode.children.length-1),getFree());
       
       return newnode;
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
        boolean inTable = -1 != search(key);
        boolean split = false;
        if(!inTable) {
            long loc = 0;
            int val = 0;
            //Do Insert
            BTreeNode r = paths.pop();
            
            //If there is room in node for new value: M-1 children
            if (r.isRoom()) {
                //Insert k into the node
                r.insertEntry(key,addr);
                //Write node to the file
                r.writeNode();
            } else {
                BTreeNode newnode = r.split(key,addr);

                //let val be  the smallest    value   in  the newnode
                val = newnode.keys[0];
                //write   node    to  the file    (into   the same    location where   is  was previously  located)  
                //write   newnode into    the file   
                r.writeNode();
                newnode.writeNode();
                //let loc be  the address in  the file    of  newnode
                loc = newnode.addr;
                //set split   to  true 
                split = true;
            }
            
//            while  (!path.empty()  &&  split)      {   
            while (!paths.empty() && split) {
                BTreeNode node = paths.pop();
                if(node.isRoom()) {
                  //  insert  val and loc into    node   
                    r.insertEntry(val, loc);
//                  write   node    to  the file    (into   the same    location where   is  was previously  located)    
                    node.writeNode();
//                  set split   to  false 
                    split = false;
                } else {
                    BTreeNode newnode = node.split(val, loc);
                    node.writeNode();
                    newnode.writeNode();
                    val = newnode.keys[0];
                    loc = newnode.addr;
                    split = true;
                }
            }//End while
            if (split) { //Then root was split
                BTreeNode rootNode = new BTreeNode(root);
                BTreeNode newnode = rootNode.split(val, loc);
                root = newnode.addr;
                newnode.writeNode();
            }
            
        }
        
        return inTable;
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
        
        BTreeNode r = new BTreeNode(root);
        paths.push(r);
        while(k != r.keys[i] && !r.isLeaf()) {
            //Look at in index leaves to guide where to go
            for(i = 0; i < Math.abs(r.count); i++) {
                if(k < r.keys[i]) {
                   break;
                   //We have found where to look next in the path
                }
                //We looked at all the keys and are at a leaf, k is not there.
                if (i > Math.abs(r.count) && r.isLeaf()) {
                    return -1;
                }
            }
            //Add to the paths the address we are going to look at
            r = new BTreeNode(r.children[i]);
            paths.push(r);
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
    /**
     * <pre>PRE: There is room for the new key and child.
     * POST: New key and child are inserted and ordering is maintained.</pre>
     * Inserts and maintains a new key and child into asc order
     * 
     * @param r Node to add to
     * @param k Key to add
     * @param addr Address of child
     */
  
    
    private long getFree() {
        long addr = 0;
        //When at the end of free, write to the end of file.
        try {
            if (free == 0) {
                addr = f.length();
            } else {
                //New address is where free is pointing
                addr = free;
                //Move free value to next in list.
                f.seek(free);
                free = f.readLong();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addr;
    }
    public void print() { 
    //print the B+Tree to standard output 
    //print one node per line 
    } 
    public void close() { 
    //close the B+tree. The tree should not be accessed after close is called 
    } 
} 
