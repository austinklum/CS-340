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
        table = new Node[s];
    }
    private int hash(String k) {
    //return the hash function value for k 
        int h = 0;
        for(int i = 0; i < k.length(); i++) {
            h = h*31+k.charAt(i);
        }
        return h % table.length;
    } 
    public boolean insert(String k) { 
    //if k is not in the table create a new entry (with a null data value) and return true 
    //if k is in the table return false 
    boolean inTable = find(k);
        if (!inTable) {
            // head = table[hashValue];
            //Add to Head of linked list.
           table[hash(k)].next = new Node(k, null, table[hash(k)].next);
        }
        return inTable;
    }
    public boolean find(String k) {
    //return true if k is in the table otherwise return false 
        return table[hash(k)] != null;
    }
    
    /**
     * PRE: k is in the table
     * 
     * Find the node in the table at hash k and position k
     * 
     * @param k Key
     * @return Node at key
     */
    private Node findAux(String k) {
        //Set temp to head of linked list
        Node temp = table[hash(k)];
        
       //Loop through list until null or the key
        while (temp != null || temp.key.equals(k)) {
            temp = temp.next;
        }
        
        return temp;
    }
    public Object getData(String k) { 
    //if k is in the table return the data (which could be null) associated with k 
    //if k is not in the table return null
        
        //Check if k is in the table
        if (find(k)) {
           //Find the data associated 
           return findAux(k).data;
        }
        //Else case. k is not in the table
        return null;
        
    }
    public void setValue(String k, Object d) {
    //PRE: k is in the table 
    //make d the data value associated with k 
    
        //Find the node to modify
        Node temp = findAux(k);
        
        //Set data to d
        if(temp != null) {
            temp.data = d;
        }
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
         //optional method not implemented 
        } 
    }
    
    public boolean remove(String k) { 
    //if k is in the table, return the entry for k and return true 
    //if k is not in the table, return false
        boolean inTable = find(k);
        if(inTable) {
            //Set temp to head of linked list
            Node temp = table[hash(k)];
            if(temp.data.equals(k)) {
                temp.next = temp.next.next;
            }
           //Loop through list until null or the key
            while (temp.next != null || temp.next.key.equals(k)) {
                temp = temp.next;
            }
            //We are one before the key value
            //Skip over the key value to the next value.
            temp.next = temp.next.next;
        }
        return inTable;
    } 
    public Iterator<String> iterator() { 
    //return a new STIterator object 
        
    } 
    public static void main(String args[]) { 
    //code to test SymbolTable 
    }
}
