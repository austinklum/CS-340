import java.io.*; 
import java.util.*; 

public class BinarySearchTree { 
/* 
Implements a binary search tree of ints stored in a random access file. 
Duplicates are recorded by a count field associated with the int
*/
    public static void main(String[] args) throws IOException {
        BinarySearchTree myTree = new BinarySearchTree("tree2.bin", 0);
        /*myTree.insert(100);
        myTree.insert(100);
        myTree.insert(25);
        myTree.printPretty();
        myTree.removeOne(25);
        System.out.println(myTree.free);
        myTree.insert(150);
        myTree.printPretty();
        System.out.println(myTree.free);
        myTree.removeAll(100);
        myTree.printPretty();
        System.out.println(myTree.root);
        myTree.insert(150);
        myTree.insert(175);
        myTree.printPretty();*/
        /*myTree.removeOne(150);
        myTree.insert(75);
        myTree.printPretty();
        myTree.removeOne(175);
        myTree.printPretty();*/
      /*  System.out.println(myTree.free);
       myTree.printSingular(myTree.free);*/
        
        /*Test on deleting root with 2 children*/
     /* myTree.insert(100);
        myTree.insert(150);
        myTree.insert(50);
        myTree.insert(175);
        myTree.insert(125);
        myTree.printPretty();
        System.out.println("---Removing a node with 2 children!---\n");
        myTree.removeAll(100);
        myTree.printPretty();
        System.out.println(myTree.free);*/
        
        /*Test on deleting a node with 2 children*/
      /* myTree.insert(100);
        myTree.insert(150);
        myTree.insert(50);
        myTree.insert(175);
        myTree.insert(125);
        myTree.printPretty();
        System.out.println("---Removing a node with 2 children!---\n");
        myTree.removeOne(150);
        myTree.printPretty();
        System.out.println(myTree.free);*/
        
        /*Test on Deleting left leave*/
        myTree.insert(100);
        myTree.insert(150);
        myTree.insert(50);
        myTree.insert(175);
        myTree.insert(125);
        myTree.printPretty();
        myTree.removeOne(125);
        myTree.printPretty();
        System.out.println(myTree.free);
        
       myTree.close();
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
    } 
    
    private long insert(Long nodeAddr, int d) throws IOException {
        if (nodeAddr == 0) {
            Long newAddr = getFree();
            new Node(0, d, 0, newAddr).writeNode();
            return newAddr; //Case when we walked off tree. 
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
       root = remove(root,d,false);
    } 
    public void removeAll(int d) throws IOException { 
    //removed from the tree
    //if d is not in the tree the method has no effect
        root = remove(root,d,true);
    } 
    
    private long remove(long addr, int d, boolean removeAll) throws IOException {
        if(addr == 0) {
            //addToFree(0);
            return 0;
        }
        Node temp = new Node(addr);
        Node retVal = temp;
        System.out.println("Temp.Data = " + temp.data  + "  d = " + d);
        if(temp.data == d){
            if(removeAll) {
                temp.count = 0;
            } else {
                temp.count--;
            }
            temp.writeNode();
            if(temp.count == 0){
                System.out.println("Count = 0; Removing node at : " + temp.addr);
                if (temp.left == 0) {
                    retVal = new Node(temp.right);
                    addToFree(temp.addr);
                } else if(temp.right == 0) {
                    retVal = new Node(temp.left);
                    addToFree(temp.addr);
                } else {
                    temp.left = replace(new Node(temp.left),temp).addr;
                    temp.writeNode();
                }
            }
        } else if(d < 0) {
            temp.left = remove(temp.left, d, removeAll);
            temp.writeNode();
        } else {
            temp.right = remove(temp.right, d, removeAll);
            temp.writeNode();
        }
        retVal.writeNode();
        return retVal.addr;

    }
    
    
    private Node replace(Node r, Node repHere) throws IOException{
        if(r.right != 0){
            r.right = replace(new Node(r.right), repHere).addr;
            r.writeNode();
            return r;
        } else {
            repHere.data = r.data;
            repHere.count = r.count;
            addToFree(r.addr);
            repHere.writeNode();
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
        long addr = 0;
        //When at the end of free, write to the end of file.
        if (free == 0) {
            addr = f.length();
        } else {
            //New address is where free is pointing
            addr = free;
            //Move free value to next in list.
            f.seek(free);
            free = f.readLong();
        }
        return addr;
    }
    
    private void addToFree(long addr) throws IOException {
        System.out.println("Adding to FREE");
        //Seek to position to write to
        System.out.println("Address inputed = " + addr);
        f.seek(addr);
        System.out.println("F = " + f.getFilePointer());
        System.out.println("Free = " + free);
        //Write out old value of free
        f.writeLong(free);
        System.out.println("F Now equals = "  + f.getFilePointer());
        //Set free to new value
        free = addr;
        System.out.println("Free now equals = " + free);
        System.out.println();
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
        long next = f.readLong();
        System.out.println("Next Address = " + next);
        f.seek(next);
        long nextNext = f.readLong();
        System.out.println("Next-Next Address = " + nextNext);
        /*System.out.println("ADDRESS : " + addr);
        System.out.println("   DATA : " + f.readInt());
        System.out.println("   COUNT: " + f.readInt());
        System.out.println("   LEFT : " + f.readLong());
        System.out.println("   RIGHT: " + f.readLong());*/
    }
    
    public void printPretty() throws IOException {
        f.seek(16);
        System.out.printf("%7s %7s %7s %7s %7s\n", "Address", "Data", "Count", "Left", "Right");
        while(f.getFilePointer() < f.length()) {
             Node n = new Node(f.getFilePointer());
            if (f.getFilePointer() - 24 == root) {
                System.out.printf("%7s %7s %7s %7s %7s %7s\n", n.addr, n.data, n.count, n.left, n.right, "[ROOT]");
            } else if (f.getFilePointer() - 24 == free) {
                System.out.printf("%7s %7s %7s %7s %7s %7s\n", n.addr, n.data, n.count, n.left, n.right, "[FREE]");
            } else {
                System.out.printf("%7s %7s %7s %7s %7s\n", n.addr, n.data, n.count, n.left, n.right);
            }
        }
        System.out.println();
    }
}