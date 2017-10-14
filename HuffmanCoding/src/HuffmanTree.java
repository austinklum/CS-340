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
    } 
    
    private Node root; 
    private Node current; 
    
    public HuffmanTree() { 
        root = null; 
        current = null; 
    } 
    public HuffmanTree(char d) { 
    //Make a one node tree 
        
    } 
    public HuffmanTree(String t, char nonLeaf) { 
    //Assumes t represents a post order representaLon of the tree  
    //where a node is either a leaf or has two children. nonLeaf  
    //is the char value of the data in the non-leaf nodes 
        
    } 
    public HuffmanTree(HuffmanTree b1, HuffmanTree b2, char d) { 
    //merge b1 and b2 
        
    } 
    
    //The following methods allow a user object to follow a path in the tree. 
    //Each method except atLeaf and current changes the value of current 
    //atLeaf returns true if the current posiLon is a leaf, otherwise it returns false 
    //current returns the data value in the current Node 
    public void moveRoot() { 
        
    } 
    public void moveLeft() { 
        
    } 
    public void moveRight() { 
        
    } 
    public boolean atLeaf() {
        return false; 
        
    } 
    public char current() {
        return 0; 
        
    } 
    
    //Inner class to create an iterator. The iterator allows the user class to find all paths from 
    //the root to a leaf. The paths are sequences of 0s and 1s. 0 means left and 1 means right 
    //You will find it easier to find all the paths when the iterator is created. 
    public class PathIterator implements Iterator<String> { 
        public PathIterator() { 
            
        } 
        public boolean hasNext() {
            return false; 
            
        } 
        public String next() {
            return null; 
            
        } 
        public void remove() { 
            //optional method not implemented
            
        } 
    } 
    
    public Iterator<String> iterator() {
        return null; 
      //return a PathIterator object 
        
    } 
    public String toString() {
        return null; 
    //return a post order representaLon of the tree 
    //in the format we discussed in class 
        
    } 
} 


