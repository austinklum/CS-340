import java.util.*; 
public class HuffmanTree { 
    private class Node { 
        
        private Node left; 
        private char data; 
        private Node right; 
        
        private Node(Node L, char d, Node R) { 
            left = L; 
            data = d; 
            right = R; 
        }
        public String toString() {
            return "Data: " + data;
        }
    } 
    
    private Node root; 
    private Node current; 
    
    public HuffmanTree() { 
        root = null; 
        current = null; 
    } 
    public HuffmanTree(char d) { 
    //Make a one node tree 
        root = current = new Node(null,d,null);
    } 
    //nonLeaf = 128 in our program. This will be the constructor that will build up a tree from a string
    public HuffmanTree(String t, char nonLeaf) { 
    //Assumes t represents a post order representation of the tree  
    //where a node is either a leaf or has two children. nonLeaf  
    //is the char value of the data in the non-leaf nodes 
        
    } 
    public HuffmanTree(HuffmanTree b1, HuffmanTree b2, char d) { 
    //merge b1 and b2 
        //Create a new single node tree to connect to
       // HuffmanTree parent = new HuffmanTree(d);
       root = current = new Node(b1.root, d, b2.root);
        //Point the two other trees to the root of the new tree
        //parent.root.left = b1.root;
        //parent.root.right = b2.root;
    } 
    
    //The following methods allow a user object to follow a path in the tree. 
    //Each method except atLeaf and current changes the value of current 
    //atLeaf returns true if the current position is a leaf, otherwise it returns false 
    //current returns the data value in the current Node 
    public void moveRoot() { 
        current = root;
    } 
    public void moveLeft() { 
        current = current.left;
    } 
    public void moveRight() { 
        current = current.right;
    } 
    public boolean atLeaf() {
        return current.left == null && current.right == null;
        
    } 
    public char current() {
        return current.data; 
        
    } 
    
    //Inner class to create an iterator. The iterator allows the user class to find all paths from 
    //the root to a leaf. The paths are sequences of 0s and 1s. 0 means left and 1 means right 
    //You will find it easier to find all the paths when the iterator is created. 
    public class PathIterator implements Iterator<String> { 
        private LinkedList<String> paths;
        public PathIterator() { 
            paths = new LinkedList<>();
            makePath(root,"");
        } 
        public boolean hasNext() {
            return atLeaf(); 
            
        } 
        public String next() {
            return null; 
            
        } 
        public void remove() { 
            //optional method not implemented
        }
        
        private void makePath(Node r, String path) {
            if(atLeaf()) {
                paths.add(r.data + path);
            }
            makePath(r.left, path + "0");
            makePath(r.right, path + "1");
        }
    } 
    
    public Iterator<String> iterator() {
        return null; 
      //return a PathIterator object 
        
    } 
    public String toString() {
    //return a post order representation of the tree 
    //in the format we discussed in class 
        return toString(this.root);
    } 
    
    private String toString(Node r) {
        if (r == null) {
            return "";
        }
            return toString(r.left) + toString(r.right) + r.data;
    }
} 


