import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class BTree { 
RandomAccessFile f; 
int order; 
int blockSize; 
long root; 
long free;
Stack<BTreeNode> paths;
int stopper = 0;
int mid;
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
   
   private BTreeNode copy() {
     return new BTreeNode(count,Arrays.copyOf(keys, keys.length),Arrays.copyOf(children, children.length),addr);
   }
   
   private boolean isLeaf() {
       return count < 0;
   }
   
   private boolean hasRoom() {
       return Math.abs(count) < order - 1;
   }
   
   private boolean isTooSmall() {
       int min = minKeys();
       return Math.abs(count) < minKeys();
   }
   
   private int minKeys() {
       return (int)(Math.ceil(order/2));
   }
   
   /**
    * <Pre>PRE: key is in tree</pre>
    * Parent of the key should call this method<br>
    * Returns a positive or negative number n:<br>
    * -n : Left Neighbor <br>
    *  0 : Can't Borrow <br>
    *  n : Right Neighbor <br>
    * 
    * @param key key to look for
    * @return -n, 0, n
    */
private long borrowFrom(BTreeNode unchanged, int key) {
       //Look for key. Look at k-1 and k+1. If count > min children then return true
       int i = 0;
       for(i = 0; i < Math.abs(count); i++) {
           if (key < keys[i]) {
               break;
           }
       }
       
       /* Status Meaning
        * -n : Left Neighbor
        *  0 : Can't Borrow
        *  n : Right Neighbor
        */
       long status = 0;
       
       //We borrow from the right
       if(Math.abs(new BTreeNode(children[i + 1]).count) > minKeys()) {
           status = children[i + 1];
       // When looking at the first key, there is no left neighbor
       } 
       if(i != 0 && Math.abs(new BTreeNode(children[i-1]).count) > minKeys()) {
           status = children[i - 1]*-1;
       }
       
//       if(r.isLeaf()) {
//           count++;
//       }else {
//           count--;
//       }
       
     //NOTE: When left and right can both be borrowed from, we'll choose the left neighbor
       return status;
   }
   
   private boolean canBorrow(BTreeNode child, int key) {
       return borrowFrom(child,key) != 0;
   }
   
   private int findKeyIndex(int k) {
       for(int i = 0; i < Math.abs(count); i++) {
           if (k == keys[i]) {
               return i;
           }
       }
       return -1;
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
   
   private void removeEntry(int key) {
       for(int j = findKeyIndex(key); j < Math.abs(count)-1; j++) {
           keys[j] = keys[j+1];
           children[j] = children[j+1];
       }
       
       if(isLeaf()) {
           count++;
       } else {
           count--;
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
       
       if(key != -1 && addr != -1) {
           //Add the new entry to it
           splitNode.insertEntry(key,addr);
       }
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
           mid = keyArr[0];
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
   
//   private BTreeNode share() {
//       //Determine the count of the new node.
//       int newCount = (int) Math.ceil((double)keys.length/2);
//       
//        //split   the values  (including  k)  between node  and the newnode
//        //Update the caller //splitnode.count - newCount
//        int callerNewCount = Math.abs(count) + 1 - newCount;
//        if(isLeaf()) {
//            count = callerNewCount*-1;
//        } else {
//            count = callerNewCount;  
//        }
//        
//        //Need to make a node that contain the other half.
//   }
   
    private void borrow(BTreeNode r, BTreeNode unchanged, int key) {
        long status = r.borrowFrom(unchanged,key);
        BTreeNode neighbor = null;
        int splitCount = 0;
        //Borrow from left
        if(status < 0) {
            System.out.println("Borrowing from the left at " + status*-1);
            neighbor = new BTreeNode(status*-1);
            splitCount = (int) Math.ceil((double)(Math.abs(neighbor.count)-minKeys())/2);
            int i = 0;
            int count = Math.abs(neighbor.count);
            for(i = count - 1; i > count - splitCount - 1; i--) {
                insertEntry(neighbor.keys[i],neighbor.children[i]);
            }
            for(i = count - 1; i > count - splitCount - 1; i--) {
                neighbor.removeEntry(neighbor.keys[i]);
            }
            for(i = 0; i < Math.abs(r.count); i++) {
                if(unchanged.keys[0] < r.keys[i]) {
                    System.out.println("Welp, " + r.keys[i]);
                    break;
                }
            }
            r.keys[i-1] = keys[0];
            
//            BTreeNode cupOfSugar = new BTreeNode(neighbor.count, 
//                    Arrays.copyOfRange(neighbor.keys, minKeys(), Math.abs(neighbor.count)), 
//                    Arrays.copyOfRange(neighbor.children, minKeys()+1, order), 
//                    -1);
//            
//           BTreeNode cupOfFlour = cupOfSugar.share();
//           
//           //Add new keys into child
//           keys = IntStream.concat(
//                   Arrays.stream(Arrays.copyOfRange(neighbor.keys,minKeys(),Math.abs(neighbor.count))),
//                   Arrays.stream(cupOfFlour.keys))
//                   .toArray();
//           
//           //Add new children into child
//           children =  LongStream.concat(
//                   Arrays.stream( Arrays.copyOfRange(neighbor.children,minKeys()+1,Math.abs(neighbor.count)+1)),
//                   Arrays.stream(cupOfFlour.children))
//                   .toArray();
//                   
//            //Add min keys and cupOfSugar.keys
//            neighbor.keys = IntStream.concat(
//                    Arrays.stream( Arrays.copyOfRange(neighbor.keys,0,minKeys())),
//                    Arrays.stream(cupOfSugar.keys))
//                    .toArray();
//            
//            //add min children and cupOfSugar.children
//            neighbor.children = LongStream.concat(
//                    Arrays.stream( Arrays.copyOfRange(neighbor.children,0,minKeys()+1)),
//                    Arrays.stream(cupOfSugar.children))
//                    .toArray();
            
        //Borrow from right
        } else {
            System.out.println("Borrowing from the right at " + status);
           neighbor = new BTreeNode(status);
           splitCount = (int) Math.ceil((double)(Math.abs(neighbor.count)-minKeys())/2);
           //If its a leaf take the immediant child
           //Its its a nonleaf take the next child
           for(int i = 0; i < splitCount; i++) {
               insertEntry(neighbor.keys[i],neighbor.children[i]);
           }
           for(int i = 0; i < splitCount; i++) {
               neighbor.removeEntry(neighbor.keys[i]);
           }
           
           //Maintain Parent
           int i = 0;
           for(i = 0; i < Math.abs(r.count); i++) {
               if(neighbor.keys[0] < r.keys[i]) {
                   System.out.println("Welp, " + r.keys[i]);
                   break;
               }
           }
           r.keys[i-1] = neighbor.keys[0];
           
        }
        //create a node that doesn't use the first min keys
        //split that node, give the smaller half back to node
        //take the larger half and give it to the child
//        r.insertEntry(neighbor.keys[0],neighbor.children[0]);

//        r.removeEntry(r.keys[0]);
        
        
        /*
         * [X] Maintain Parent
         * [ ] Non-Leaf Delete
         * [X] Leaf Delete 
         */
        r.writeNode();
        neighbor.writeNode();
        writeNode();
        
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
        //System.out.println("Inserting " + key + " at DB: " + addr);
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
                    val = mid;
                    loc = newnode.addr;
//                    if(newnode.isLeaf()) {
//                        for(int i = 0; i < order-1; i++) {
//                            newnode.keys[i+1] = newnode.keys[i];
//                        }
//                        for(int i = 0; i < order; i++) {
//                            newnode.children[i+1] = newnode.children[i];
//                        }
//                        newnode.count--;
//                    }
                    node.writeNode();
                    newnode.writeNode();
                   
                    split = true;
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
//                BTreeNode rightNode = new BTreeNode(loc);
//                if(!rightNode.isLeaf()) {
//                    for(int i = 0; i < rightNode.count; i++) {
//                        rightNode.keys[i] = rightNode.keys[i + 1];
//                    }
//                    rightNode.count--;
//                }
                
                newnode.writeNode();
                //rightNode.writeNode();
            }
      }
        //print();
        //System.out.println("   ***   ");
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
        boolean tooSmall = false;
        if(0 == search(key)) {
            return 0;
        } 
        
        BTreeNode r = paths.pop();
        BTreeNode unchanged = r.copy();
//        if  k   is  in  node   
        //key will always be in the node at this point, If it wasn't I would've return earlier.
//        remove  it  
        r.removeEntry(key);
        r.writeNode();
//        if  the node    has too few keys    set tooSmall    =   true   
        tooSmall = r.isTooSmall();
//        while   (!path.empty()  &&  tooSmall)   {   
        while (!paths.empty() && tooSmall) {
            BTreeNode child = r; //the   node    that    is  too small   
            r = paths.pop(); //the   parent  of  child 
        
//            check   the neighbors   of  child; the immediate left and right
            if(r.canBorrow(unchanged,key)) {
//                shift    values  between the children    and adjust  the key 
//                in  node    that    is  between the nodes   involved    in  the borrowing  
                child.borrow(r,unchanged,key);
                tooSmall = false;
            } else {
//                combine child   with    a   neighbor    and remove  the key in  
 //               node    between the nodes involved    in  the combining   
               // r.merge(child);
                
                //Check if r is still too small
                tooSmall = r.isTooSmall();
            }
        }   
        if(tooSmall) { //this mean the root is now empty
           // set the root to the leftmost child of the empty root and free the space used by the old root
        }
        
        return 0;
    } 
    
    private BTreeNode searchToLeaf(int k) {
        int i = 0;
        paths = new Stack<>();
        BTreeNode r = new BTreeNode(root);
        paths.push(r);
        if(root == 0) {
            return r;
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
        return r;
    }
    
    //Calling search will reset the paths stack. I don't forsee this being an issue
    public long search(int k) { 
    /* 
       This is an equality search 
       If the key is found return the address of the row with the key 
       otherwise return 0  
    */ 
        BTreeNode r = searchToLeaf(k);
        if(root == 0) {
            return 0;
        }
        long addr = 0;
        
        //I am now at a leaf. Look at all the contents of the node.
        int i = r.findKeyIndex(k);
        
        if(i != -1) {
            addr = r.children[i];
        }
                
        return addr;
    } 
    public LinkedList<Long> rangeSearch(int low, int high) {
    //PRE: low <= high 
    /* 
       
       return a list of row addresses for all keys in the range low to high inclusive 
       return an empty list when no keys are in the range 
    */ 
        
        LinkedList<Long> list = new LinkedList<>();
        BTreeNode r = searchToLeaf(low);
        int i = 0;
       while (r.keys[i] < high) {
           list.add(r.children[i]);
           i++;
           if (i == r.count*-1) {
               if(r.children[order-1] == 0) {
                   //We are at the end of the leaves
                   break;
               }
               //Look to the next leaf
               r = new BTreeNode(r.children[order-1]);
               i = 0;
           }
       }
        return list;
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
    
    public static void main(String[] args) {
        BTree tree = new BTree("tree", 60);
//        System.out.println("I got this far!");
//        //long addr = tree.search(75);
//       // System.out.println(addr);
        tree.insert(110,32);
        tree.insert(50, 24);
        tree.insert(75, 64);
        tree.insert(130, 48);
        tree.insert(150, 128);
        tree.insert(120, 420);
        tree.insert(30, 255);
        //Borrow from right
//        tree.remove(75);
//        tree.print();
//        tree.remove(50);
       
        //Borrow from left
//        tree.remove(150);
//        tree.remove(120);
//        tree.print();
//        tree.remove(130);
//        
        tree.insert(20, 272);
        tree.insert(100, 316);
        tree.insert(160, 353);
        tree.insert(5, 420);
        tree.insert(112, 456);
        tree.insert(123, 495);
        tree.insert(125, 535);
        //At this point we have M-1 keys in the root and M children
        tree.insert(180, 582);
        //This will split our root. If this works, we should be set.
        tree.insert(170, 612);
        
        //I need 18 values
        tree.insert(60, 655);
        tree.insert(82, 676);
        tree.insert(105, 707);
        
        tree.remove(20);
        tree.remove(125);
        tree.print();
        
//        //Borrow from right
        //tree.remove(30);
        
        //Borrow from left
        //tree.remove(110);
        
        tree.insert(95, 737);
        tree.insert(99, 777);
        tree.insert(109, 801);
        tree.insert(200, 833);
        tree.insert(250, 888);
        tree.print();
        tree.close();
//       // System.out.println(tree.search(100));
    }
//    
} 
