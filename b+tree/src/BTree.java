import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
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
int stopper = 0;
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
       if(addr != 0) {
           keys = new int[order-1];
           children = new long[order];
           try {
               f.seek(addr);
               this.addr = addr;
               count = f.readInt();
               for (int i = 0; i < order-1; i++) {
                   keys[i] = f.readInt();
               }
               
               for (int i = 0; i < order; i++) {
                   children[i] = f.readLong();
               }
               
           } catch(EOFException e){
               System.out.println("Got him!");
           } catch (IOException e) {
               System.out.println("No I did!");
               e.printStackTrace();
           }
       }
   }
   
   /**
    *  Writes the node to the file at location addr
    * @param addr address of node in file
    */
   private void writeNode() {
       try {
           f.seek(addr);
           
           f.writeInt(count);
           //Write out the keys
           for(int i = 0; i < order-1; i++) {
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
   
   private boolean hasRoom() {
       return Math.abs(count) < order - 1;
   }
   
   @Override
   public String toString() {
       String str = Long.toString(addr);
       str += " : " + count + " |";
       for (int key : keys) {
           str += " " + key;
       }
       str += " |";
       for (long children : children) {
           str += " " + children;
       }
       
       return str;
   }
   
   private void insertEntry(int k, long addr) {
//       int i = Math.abs(count)-1;
//       int j = Math.abs(count)-1;
//       
//     //  The guide leaves need to be offset by 1.
//       if(!isLeaf()) {
//           j++;
//       }
//       //Case that k is greater than everything
//       if (k > keys[i]) {
//           i++;
//           j++;
//       } else {
//           while(i > 0 && i < Math.abs(count) && k < keys[i]) {
//               //We know there is room for the node. So I cant index out of bounds
//               //start at end of count
//               //While k > r.keys
//               //shift k[i] to k[i+1]
//               
//           /* --VISUAL-- 
//            * key = 5, Order = 5
//            * [1][6][10][?] 
//            * Shift -> [1][6][10][10] -> [1][6][6][10] -> [1][5][6][10]
//            */
//               keys[i+1] = keys[i];
//               children[j+1] = children[j];
//               i--;
//               j--;
//           }
//           //Case that k is either that smallest or second smallest
//           if (i == 0) { 
//               //K must be the smallest 
//               if(k < keys[i]) {
//                   keys[i+1] = keys[i];
//                   children[j+1] = children[j];
//                   
//               //Second smallest
//               } else {
//                   i++;
//                   j++;
//               }
//           }
//       }
//       //Put the new key in place.
//       keys[i] = k;
//       children[j] = addr;
//       int i;
//      
//       for (i=Math.abs(count)-1; (keys[i] > k && i > 0); i--) {
//          keys[i+1] = keys[i];
//          children[i+1] = children[i];
//       }
//       //Case that k is either that smallest or second smallest
//     if (i == 0) { 
//         //K must be the smallest 
//         if(k < keys[i]) {
//             keys[i+1] = keys[i];
//             children[i+1] = children[i];
//             keys[i] = k;
//             children[i] = addr;
//             
//         //Second smallest
//         } else {
//             i++;
//         }
//     } else {
//       keys[i+1] = k;
//       children[i+1] = addr;
//     }
       
       //Add to the count
       if(isLeaf()) {
           count--;
       } else {
           count++;
       }
       //n will be our count
       int n = Math.abs(count);
       
       //Put our new key at the end of keys
       keys[n-1] = k;
       
       //If its not a leaf, put children at the last place in children.
       //We may have overwrote the last value otherwise
       if(!isLeaf()) {
           children[n] = addr;
       } else { //Then put it at the second to last place in children
           children[n-1] = addr;
       }
       
       int max = keys[n-2] > k ? keys[n-2] : k;
       long[] hash = new long[++max];
       
       //Save a 'hash' table of all the associated children addrs
       if(!isLeaf()) {
           //When its not a leaf, we know that children[0] should not be touched.
           for(int i = 0; i < n; i++) {
               hash[keys[i]] = children[i + 1];
           }
       } else {
           //Its a leaf, we might have a new smallest key/ child
           for(int i = 0; i < n; i++) {
               hash[keys[i]] = children[i];
           }
       }
       
       for (int i = 1; i < n; ++i){
           int key = keys[i];
           int j = i-1;
//           if(!isLeaf()) {
//               m = j ;
//           }
           
           //Shift values in array
           while (j>=0 && keys[j] > key) {
               keys[j+1] = keys[j];
               j--;
           }
           
           keys[j+1] = key;
       }
       if(!isLeaf()) {
       //re do the children array to match the key array
           //System.out.println("Adding to non-leaf node : " + toString());
           for (int i = 0; i < n; i++) {
               children[i + 1] = hash[keys[i]];
           }
       } else {
           for (int i = 0; i < n; i++) {
               children[i] = hash[keys[i]];
           }
       }
   }
   
   /**
    * This method will split a node into 2 parts. <br>
    * Returns the new part and modifies the caller
    * @param key New key value to insert
    * @param addr Adress in DBTable of key
    * @return The new node from split
    */
   private BTreeNode split(int key, long addr) {
       //Make a new temp node that is 1 bigger than normal nodes
       
         BTreeNode splitNode = new BTreeNode(count, Arrays.copyOf(keys, order),Arrays.copyOf(children, order+1), this.addr);
       
       //Add the new entry to it
       splitNode.insertEntry(key,addr);
      
       //Determine the count of the new node. ??? Maybe use count field instead?
      int newCount = (int) Math.ceil((double)splitNode.keys.length/2);
      
       //split   the values  (including  k)  between node  and the newnode
       //Update the caller //splitnode.count - newCount
       int callerNewCount = Math.abs(count) + 1 - newCount;
       if(isLeaf()) {
           count = callerNewCount*-1;
       } else {
           count = callerNewCount;  
           //When splitting a non-leaf, we dont care about the smallest value
       }
       //System.out.println("Order " + order + " tree. The callerCount is " + callerNewCount + " and newCount is " + newCount);
       //Link will be copied into the last child addr for leaves so we can keep the linked list behavior
       long link = children[order-1];
      
       //Get the new values for the caller. arr[0] to arr[count]
       keys = Arrays.copyOf(Arrays.copyOfRange(splitNode.keys, 0, Math.abs(count)),order-1);
       children = Arrays.copyOf(Arrays.copyOfRange(splitNode.children, 0, Math.abs(count)+1),order);
       
       //Get the new values for the new node. Starting at the middle until the end.
       int[] keyArr = Arrays.copyOf(Arrays.copyOfRange(splitNode.keys, newCount-1, splitNode.keys.length),order-1);
       long[] childrenArr = Arrays.copyOf(Arrays.copyOfRange(splitNode.children, newCount-1, splitNode.children.length),order);
       
//       //When we have even orders, there will be overlap since the two nodes are of equal size after the split
//       //OR When splitting a nonleaf we need to start 1 place over when copying address.
//       if(order % 2 == 0 || !splitNode.isLeaf()) {
//         //This will copy the array starting 1 place over to the right
//           keyArr = Arrays.copyOf(Arrays.copyOfRange(splitNode.keys, newCount, splitNode.keys.length),order-1);
//           childrenArr = Arrays.copyOf(Arrays.copyOfRange(splitNode.children, newCount, splitNode.children.length),order);
//           
//           //Since sometimes even orders have non-leaves, we need to shift yet another time over to the right
//           if(order % 2 == 0 && !splitNode.isLeaf()) {
//               keyArr = Arrays.copyOf(Arrays.copyOfRange(splitNode.keys, 1 + newCount, splitNode.keys.length),order-1);
//               childrenArr = Arrays.copyOf(Arrays.copyOfRange(splitNode.children, 1 + newCount, splitNode.children.length),order);
//               //We'll end up with a count too large, so decrement it.
//               newCount--;
//           //We'll end up with a count too large, so decrement it.
//           //Case when its an odd order but is a non-leaf
//           }  else if (order % 2 == 1 && !splitNode.isLeaf()) {
//               newCount--;
//           }
//           
//       }
//       
       
       
       if(order % 2 == 0 || !splitNode.isLeaf()) {
           //This will copy the array starting 1 place over to the right
            keyArr = Arrays.copyOf(Arrays.copyOfRange(splitNode.keys, newCount, splitNode.keys.length),order-1);
            childrenArr = Arrays.copyOf(Arrays.copyOfRange(splitNode.children, newCount, splitNode.children.length),order);
            if(order % 2 == 1) {
                newCount--;
            }
            
       }
       
       if(splitNode.isLeaf()) {
           newCount*=-1;
       } 
       
       //Maintain LinkedList and add a new node
       childrenArr[order-1] = link;
       BTreeNode newnode = new BTreeNode(newCount,keyArr,childrenArr,getFree());
       children[order-1] = newnode.addr;
       
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
        File path = new File(filename);
        if (path.exists()) {
            path.delete();
        } 
        f = new RandomAccessFile(path,"rw");
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
            order = blockSize/12;
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
        if (root == 0) {
            System.out.println("Inserted a new root!");
            insertRoot(key,addr);
            return true;
        }
        boolean inTable = 0 != search(key);
        boolean split = false;
        if(!inTable) {
            long loc = 0;
            int val = 0;
            //Do Insert
            BTreeNode r = paths.pop();
            
            //If there is room in node for new value: M-1 children Go BACK
            if (r.hasRoom()) {
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
            
            while (!paths.empty() && split) {
                BTreeNode node = paths.pop();
                if(node.hasRoom()) {
                  //  insert  val and loc into    node   
                    node.insertEntry(val, loc);
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
                    if(val == 740) {
                        System.out.println("Waat!");
                    }
                }
            }//End while
            if (split) { //Then root was split
                //BTreeNode rootNode = new BTreeNode(root);
                int[] keys = new int[order-1];
                long[] children = new long[order];
                keys[0] = val;
                children[0] = root;
                children[1] = loc;
                
                //Create a new root 
                BTreeNode newnode = new BTreeNode(1,keys,children,getFree());
                root = newnode.addr;
                
                //Our right most child gets out of whack
                //This code block helps maintain it.
                BTreeNode rightNode = new BTreeNode(loc);
                if(!rightNode.isLeaf()) {
                    for(int i = 0; i < rightNode.count; i++) {
                        rightNode.keys[i] = rightNode.keys[i + 1];
                    }
                    rightNode.count--;
                }
                
                newnode.writeNode();
                rightNode.writeNode();
            }
      }
        
        return !inTable;
    } 
    private void insertRoot(int key, long addr) {
        int[] keys = new int[order-1];
        long[] children = new long[order];
        keys[0] = key;
        children[0] = addr;
        BTreeNode r = new BTreeNode(-1,keys,children,getFree());
        r.writeNode();
        root = r.addr;
    }
    public long remove(int key) { 
    /* 
       If  the key is in the Btree, remove the key and return the address of the row 
       return 0 if the key is not found in the B+tree
    */
        return 0;
    } 
    
    //Calling search will reset the paths stack. I don't forsee this being an issue
    public long search(int k) { 
    /* 
       This is an equality search 
       If the key is found return the address of the row with the key 
       otherwise return 0  
    */ 
        paths = new Stack<>();
        int i = 0;
        BTreeNode r = new BTreeNode(root);
        paths.push(r);
        
        if(root == 0) {
            System.out.println("Root is zero!");
            return 0;
        }
       
        //Logic to follow search path and bring me to a leaf
        while(!r.isLeaf()) {
            for(i = 0; i <= Math.abs(r.count); i++) {
              //k is larger than everything else, look at last node
                if (i == Math.abs(r.count)) {
                    r = new BTreeNode(r.children[i]);
                    paths.push(r);
                    break;
                //Will look at the first node that is greater than k
                } else if (k < r.keys[i]) {
                    r = new BTreeNode(r.children[i]);
                    paths.push(r);
                    break;
                }  
            }
        }
        long addr = 0;
        //I am now at a leaf. Look at all the contents of the node.
        for(i = 0; i < Math.abs(r.count); i++) {
            if (k == r.keys[i]) {
                addr = r.children[i];
                break;
            }
        }
        
        return addr;
    } 
    public LinkedList<Long> rangeSearch(int low, int high) {
    //PRE: low <= high 
    /* 
       
       return a list of row addresses for all keys in the range low to high inclusive 
       return an empty list when no keys are in the range 
    */ 
        
        return null;
    }
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
        try {
            f.seek(20);
            System.out.println("ROOT: " + root);
            System.out.println("FREE: " + free);
            while(f.getFilePointer() < f.length()) {
                BTreeNode n = new BTreeNode(f.getFilePointer());
                System.out.println(n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 
    public void close() { 
    //close the randomaccessfile
    //before closing update the values of root and free if necessary 
        try {
            f.seek(0);
            f.writeLong(root);
            f.writeLong(free);
            f.writeInt(blockSize);
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 
    
//    public static void main(String[] args) {
//        BTree tree = new BTree("tree", 60);
//        System.out.println("I got this far!");
//        //long addr = tree.search(75);
//       // System.out.println(addr);
//        tree.insert(110,32);
//        tree.insert(50, 24);
//        tree.insert(75, 64);
//        tree.insert(130, 48);
//        tree.insert(150, 128);
//        tree.insert(120, 255);
//       
//        tree.insert(20, 272);
//        tree.insert(100, 316);
//        tree.insert(160, 353);
//        tree.insert(5, 420);
//        tree.insert(112, 456);
//        tree.insert(123, 495);
//        tree.insert(125, 535);
//        //At this point we have M-1 keys in the root and M children
//        tree.insert(180, 582);
//        //This will split our root. If this works, we should be set.
//        tree.insert(170, 612);
//        //I need 18 values
//        tree.insert(60, 655);
//        tree.insert(82, 676);
//        tree.insert(105, 707);
//        tree.insert(95, 737);
//        tree.insert(99, 777);
//        tree.insert(109, 801);
//
//        tree.print();
//        tree.close();
////       // System.out.println(tree.search(100));
//    }
//    
} 
