import java.util.*;

import javax.security.auth.kerberos.KerberosKey;
public class SymbolTable implements Iterable<String> {
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
        if (h < 0) {
            h *= -1;
        }
        return h % table.length;
    } 
    public boolean insert(String k) { 
    //if k is not in the table create a new entry (with a null data value) and return true 
    //if k is in the table return false
        System.out.println("Inserting...");
        boolean inTable = find(k);
        if (!inTable) {
            // head = table[hashValue];
            //Add to Head of linked list.
          Node n = new Node(k, null, table[hash(k)]);
           table[hash(k)] = n;
        }
        return inTable;
    }
    public boolean find(String k) {
    //return true if k is in the table otherwise return false 
        System.out.println("Finding...");
        if(table[hash(k)] == null) {
            System.out.println("Head is null: Retrun false");
            return false;
        }
        System.out.println("Head is something find if it's not null");
        Node thing = findAux(k);
        System.out.println("auxVal in find : " + thing.key);
        System.out.println("Did findAux find something in find()? : " + thing != null);
        return findAux(k) != null;
    }
    
    /**
     * <pre> k is in the table
     * 
     * Find the node in the table at hash k and position k
     * 
     * @param k Key
     * @return Node at key
     */
    private Node findAux(String k) {
        System.out.println("Finding Aux...");
        //Set temp to head of linked list
        Node temp = table[hash(k)];
        System.out.println("Temp = " + temp.key);
       //Loop through list until null or the key
        while (temp != null) {
            if(temp.key.equals(k)) {
                break;
            }
            temp = temp.next;
        }
        System.out.println("Returning from aux : " + temp.key);
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
    
    public boolean remove(String k) { 
        //if k is in the table, return the entry for k and return true 
        //if k is not in the table, return false
        boolean inTable = find(k);
        System.out.println("inTable = " + inTable);
        if(inTable) {
            //Set temp to head of linked list
            Node temp = table[hash(k)];
            System.out.println("temp = " + temp.key);
            if(temp.key.equals(k)) {
                System.out.println("That happened");
                //This can only happen if we were at the head and the next thing didnt exist.
                if(temp.next == null) {
                    table[hash(k)] = null;
                }else {
                    temp.next = temp.next.next;
                }
                //We might want to return here?
                return inTable;
            }
            System.out.println("This is a test");
            //Loop through list until null or the key
            System.out.println("Temp.next != Null is " + temp.next != null);
            System.out.println("Temp.next.key is " + temp.next.key);
            while (temp.next != null || temp.next.key.equals(k)) {
                temp = temp.next;
                System.out.println("In while?");
            }
            System.out.println("Skipping it!");
            //We are one before the key value
            //Skip over the key value to the next value.
            temp.next = temp.next.next;
        }
        return inTable;
    } 
    

    
    public class STIterator implements Iterator<String> { 
        //An iterator that iterates through the keys in the table
        private Node indexNode;
        private int nextIndex;
        
        public STIterator() {
            //Needs to loop through each entry in the table.
            //Every entry may have more than 1 value, so loop through them 1 at a time
            nextIndex = 0;
            indexNode = table[nextIndex];
            nextIndex++;
           // System.out.println("I was made!");
        } 
        public boolean hasNext() {
            //System.out.println("HasNext() = " + nextIndex);
            return nextIndex < table.length;

        }
        public boolean hasNextNode() {
            return indexNode != null;
        }
        public String next() { 
        //PRE: hasNext() 
        //The format of the String should be key:data where key is a key in the 
        //symbol table and data is the String representation of the data associated 
        //with the key 
            /*If I am at the end of a list of a specific row in the table.
              Go to the next row in the table */
            //While I dont have a next node look at the next table index
            while ((!hasNextNode() && nextIndex <= table.length)/* || indexNode.data != null*/) {
                indexNode = table[nextIndex];
                nextIndex++;
                if(indexNode != null) {
                    break;
                }
               // System.out.printf("indexNode = %b : nextIndex = %d\n", indexNode == null, nextIndex);
//                if(nextIndex <= table.length) {
//                    nextIndex = table.length + 1;
//                }
            }
            
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
        SymbolTable myTable = new SymbolTable(5);
        myTable.insert("1");
        myTable.setValue("1", "The first thing");
        myTable.insert("achoo");
        myTable.setValue("achoo", "The second thing");
        myTable.insert("I really need to pee");
        myTable.setValue("I really need to pee", "Well too bad!");
        System.out.println("Printing out table");
        for (String string : myTable) {
            System.out.println(string);
        }
        myTable.remove("I really need to pee");
        System.out.println("Removed well too bad!");
        for (String string : myTable) {
            System.out.println(string);
        }
        
//        Iterator<String> iter = myTable.iterator();
        
//        System.out.println(iter.hasNext());
//        //Iterate through all the encodings and add to my "hash"
//        int i = 0;
//        while(iter.hasNext()) {
//            i++;
//            System.out.println("hasNext() = " + iter.hasNext());
//            System.out.println("Looping: " + i);
//            String str = iter.next();
//            System.out.println(str);
//        }
    }
}
