import java.io.*; 
import java.util.*; 

public class BinarySearchTree { 
/* 
Implements a binary search tree of ints stored in a random access file. 
Duplicates are recorded by a count field associated with the int
*/
    public static void main(String[] args) throws IOException {
        BinarySearchTree myTree = new BinarySearchTree("tree.bin", 0);
        myTree.insert(100);
        myTree.insert(100);
        myTree.insert(25);
        System.out.println("Pre Print");
        myTree.print();
        System.out.println("REMOVING 25");
        myTree.removeOne(25);
        System.out.println("REMOVED 25");
        //myTree.insert(150);
        myTree.print();
        System.out.println(myTree.free);
    }
    
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
            
           /* if(addr == root) {
                f.seek(f.readLong());
            }*/
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
}

    public BinarySearchTree(String fname, int mode) throws IOException{ 
    //if mode is CREATE a new empty file is created 
    //if mode is CREATE and a file with file name fname exists the file with fname must be
    //deleted before the new empty file is created 
    //if mode is REUSE an existing file is used if it exists otherwise a new empty file
    //is created 
        File path = new File(fname);
        if (path.exists() && mode == CREATE) {
            path.delete();
        } 
        
        f = new RandomAccessFile(path,"rw");
        f.seek(0);
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
        root = insert(root, d);
        //new Node(root).writeNode();
    } 
    
    private Long insert(Long nodeAddr, int d) throws IOException {
/*        if(nodeAddr == root) {
            Long newAddr = getFree();
            new Node(0,d,0,newAddr).writeNode();
            return newAddr;
        }*/
        if (nodeAddr == 0) {
            Long newAddr = getFree();
            new Node(0,d,0,newAddr).writeNode();
            return newAddr; //Case when we walked off tree. getFree();?
        }
        Node temp = new Node(nodeAddr);
        if (temp.data > d) {
            temp.left = insert(temp.left, d); //Case when we need to look left
            temp.writeNode();
        }
        else if (temp.data < d) {
            temp.right = insert(temp.right, d); //Case when we need to look right
            temp.writeNode();
        }
        else {
            temp.count++; //Case when a duplicate
            temp.writeNode();
        }
        return nodeAddr; //Base case. Return self.
    }
    
    public int find(int d) throws IOException { 
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
    
    public void removeOne(int d) throws IOException {  
    //remove one copy of d from the tree
    //if the copy is the last copy removed from the tree
    //if d is not in the tree the method has no effect
        remove(root,d);
    } 
    public void removeAll(int d) throws IOException { 
    //removed from the tree
    //if d is not in the tree the method has no effect
    } 
    
    private long remove(long addr, int d) throws IOException {
        if(addr == 0) {
            return 0;
        }
        Node temp = new Node(addr);
        Node retVal = temp;
        if(temp.data == d){
            temp.count--;
            if(temp.count == 0){
                free = temp.addr;
                if(temp.left == 0) {
                    retVal = new Node(temp.right);
                }
                else if(temp.right == 0) {
                    retVal = new Node(temp.left);
                } else {
                    temp.left = replace(new Node(temp.left),temp).addr;
                }
            }
        }
        else if(d > 0) {
            temp.left = remove(temp.left, d);
        } else {
            temp.right = remove(temp.right, d);
        }
        return retVal.addr;

    }
    
    private Node replace(Node r, Node repHere) throws IOException{
        if(r.right != 0){
            r.right = replace(new Node(r.right), repHere).addr;
            return r;
        } else {
            repHere.data = r.data;
            repHere.count = r.count;
            return new Node(r.left);
       }
    }

    
    public void close() throws IOException { 
    //close the randomaccessfile
    //before closing update the values of root and free if necessary 
        f.seek(0);
        f.writeLong(root);
        f.writeLong(free);
        f.close();
    } 
    
    private long getFree() throws IOException {
        f.seek(free);
        long addr = f.readLong();
        
        free = addr;
        if(addr == 0) {
            addr = f.length();
        }
        return addr;
        
    }
    
    public void print() throws IOException {
        f.seek(root);
        while(f.getFilePointer() < f.length()) {
            System.out.println("ADDRESS : " + f.getFilePointer());
            System.out.println("   DATA : " + f.readInt());
            System.out.println("   COUNT: " + f.readInt());
            System.out.println("   LEFT : " + f.readLong());
            System.out.println("   RIGHT: " + f.readLong());
            System.out.println();
        }
    }
    
    public void printSingular(long addr) throws IOException {
        f.seek(addr);
        
        System.out.println("ADDRESS : " + addr);
        System.out.println("   DATA : " + f.readInt());
        System.out.println("   COUNT: " + f.readInt());
        System.out.println("   LEFT : " + f.readLong());
        System.out.println("   RIGHT: " + f.readLong());
    }
    
}