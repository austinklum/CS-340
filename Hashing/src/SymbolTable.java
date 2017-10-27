import java.util.*;
public class SymbolTable implements Iterable{
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
    //return the hash funcTIon value for k 
        int h = 0;
        for(int i = 0; i < k.length(); i++) {
            h = h*31+k.charAt(i);
        }
        if (h < 0) {
            h *= -1;
        }
        return h % table.length;
    } 
    public boolean insert(String k) { 
    //if k is not in the table create a new entry (with a null data value) and return true 
    //if k is in the table return false 
        boolean inTable = find(k);
        if(!inTable) {
            //Then insert it
            if (table[hash(k)] == null) {
                table[hash(k)] = new Node(k,null,null);
            } else {
               //Make a new head to put in front
                table[hash(k)].next = new Node(k,null,table[hash(k)].next);
            }
        }
        
        return inTable;
    }
    public boolean find(String k) {
    //return true if k is in the table otherwise return false 
        //Check if it entry is null. If null then return false
        if(table[hash(k)] == null)
            return false;
        //If the entry is something, look at everything in the list
        return findAux(k) != null;
    }
    
    private Node findAux(String k) {
        Node temp = table[hash(k)];
        //Loop until we hit the end or find the key
        while(temp != null && !temp.key.equals(k)) {
            temp = temp.next;
        }
        return temp;
    }
    
    public Object getData(String k) { 
    //if k is in the table return the data (which could be null) associated with k 
    //if k is not in the table return null 
        if(find(k))
            return findAux(k).data;
        else
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
    
    public boolean remove(String k) { 
        //if k is in the table, return the entry for k and return true 
        //if k is not in the table, return false
            boolean inTable = find(k);
            if(inTable) {
                //Remove it
                //Find where it is
                Node temp = table[hash(k)];
                //Loop until we hit the end or find the key
                if(temp.key.equals(k)) {
                    table[hash(k)] = temp.next;
                    return inTable;
                }
                // We know if its in the table we cannot hit null
                while(!temp.next.key.equals(k)) {
                    temp = temp.next;
                }
                /*  
                 * [ ]->[ ]->NULL
                 *  ^.next^
                 */
                temp.next = temp.next.next;
            }
                return inTable;
        } 
    
    public class STIterator implements Iterator<String> { 
        private Node indexNode;
        private int index;
        //An iterator that iterates through the keys in the table 
        public STIterator() {
        } 
        public boolean hasNext() {
            return index < table.length || indexNode != null;
        }
        public String next() { 
        //PRE: hasNext() 
        //The format of the String should be key:data where key is a key in the 
        //symbol table and data is the String representatIon of the data associated 
        //with the key 
            while(indexNode == null && hasNext()) {
                indexNode = table[index];
                index++;
            }
            if(indexNode == null)
                return "";
            
            Node retVal = indexNode;
            indexNode = indexNode.next;
            
            return retVal.key + ":" + retVal.data.toString();
        } 
        public void remove() {
         //optional method not implemented 
        } 
    }
    
   
    public Iterator<String> iterator() { 
    //return a new STIterator object 
        return (Iterator<String>)new STIterator();
    } 
    public static void main(String args[]) { 
    //code to test SymbolTable 
        SymbolTable myTable = new SymbolTable(2);
        myTable.insert("a");
        myTable.setValue("a", "apples");
        
        myTable.insert("b");
        myTable.setValue("b", "Bummer");
        
        myTable.insert("c");
        myTable.setValue("c", "cats");
        
//        for (String string : myTable) {
//            System.out.println(string);
//        }
        Iterator<String> iter = myTable.iterator();
        System.out.println("Printing table...");
        while(iter.hasNext()) {
            String str = iter.next();
            System.out.println(str);
        }
    }
}
