import java.util.*;
public class SymbolTable {
    private class Node {
    //a node used to build linked lists
    //do not use the Java LinkedList class
        private String key;
        private Object data; 
        private Node next; 
        private Node(String k, Object d, Node x) {
            key = k; 
            data = d;
            next = x;
        }
    } 

    private Node table[]; 
    public SymbolTable(int s) { 
    //s is the size of the table. You do not have to handle resizing the table 
    }
    private int hash(String k) {
    //return the hash funcTIon value for k 
    } 
    public boolean insert(String k) { 
    //if k is not in the table create a new entry (with a null data value) and return true 
    //if k is in the table return false 
    }
    public boolean find(String k) {
    //return true if k is in the table otherwise return false 
    }
    public Object getData(String k) { 
    //if k is in the table return the data (which could be null) associated with k 
    //if k is not in the table return null 
    }
    public void setValue(String k, Object d) {
    //PRE: k is in the table 
    //make d the data value associated with k 
    }
    public class STIterator implements Iterator<String> { 
        //An iterator that iterates through the keys in the table 
        public STIterator() {
        } 
        public boolean hasNext() {
        }
        public String next() { 
        //PRE: hasNext() 
        //The format of the String should be key:data where key is a key in the 
        //symbol table and data is the String representatIon of the data associated 
        //with the key 
        } 
        public void remove() {
         //optionalmethod not implemented 
        } 
    }
    
    public boolean remove(String k) { 
    //if k is in the table, return the entry for k and return true 
    //if k is not in the table, return false
        
    } 
    public Iterator<String> iterator() { 
    //return a new STIterator object 
        
    } 
    public static void main(String args[]) { 
    //code to test SymbolTable 
    }
}
