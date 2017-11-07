import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;

public class DBTable { 
    RandomAccessFile rows; //the file that stores the rows in the table 
    long free; //head of the free list space for rows 
    int numOtherFields; 
    int otherFieldLengths[]; 
    //add other instance variables as needed 
    
    private class Row { 
        private int keyField; 
        private char otherFields[][]; 
       /* 
       Each row consists of unique key and one or more character array fields. 
       Each character array field is a fixed length field (for example 10 characters). 
       Each field can have a different length. 
       Fields are padded with null characters so a field with a length of  
       of x characters always uses space for x characters. 
       */ 
       //Constructors and other Row methods 
       
    } 
    
    public DBTable(String filename, int fL[], int bsize ) { 
    /* 
       Use this constructor to create a new DBTable. 
       filename is the name of the file used to store the table 
       fL is the lengths of the otherFields
       fL.length indicates how many other fields are part of the row 
       bsize is the block size. It is used to calculate the order of the B+Tree
       A B+Tree must be created for the key field in the table 
       
       If a file with name filename exists, the file should be deleted before the  
       new file is created. 
    */ 
        try {
            rows = new RandomAccessFile("filename","rw");
            rows.seek(0);
            for()
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    public DBTable(String filename) { 
    //Use this constructor to open an existing DBTable
    } 
    public boolean insert(int key, char fields[][]) { 
    //PRE: the length of each row is fields matches the expected length 
    /* 
       If a row with the key is not in the table, the row is added and the method 
       returns true otherwise the row is not added and the method returns false. 
       The method must use the B+tree to determine if a row with the key exists. 
       If the row is added the key is also added into the B+tree. 
    */ 
    } 
    
    public boolean remove(int key) { 
    /* 
       If a row with the key is in the table it is removed and true is returned 
       otherwise false is returned. 
       The method must use the B+Tree to determine if a row with the key exists. 
       
       If the row is deleted the key must be deleted from the B+Tree
    */ 
    } 
     
    public LinkedList<String> search(int key) { 
    /* 
       If a row with the key is found in the table return a list of the other fields in the row.  
       The string values in the list should not include the null characters. 
       If a row with the key is not found return an empty list 
       The method must use the equality search in B+Tree
    */ 
    
    }
    public LinkedList<LinkedList<String>> rangeSearch(int low, int high) {  
    //PRE: low <= high 
    /* 
       For each row with a key that is in the range low to high inclusive a list 
       of the fields in the row is added to the list returned by the call. 
       If there are no rows with a key in the range return an empty list 
       The method must use the range search in B+Tree
    */
        
    } 
    public void print() { 
    //Print the rows to standard output is ascending order (based on the keys) 
    //One row per line 
    } 
    public void close() { 
    //close the DBTable. The table should not be used aler it is closed 
    } 
} 
