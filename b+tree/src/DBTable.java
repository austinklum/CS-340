import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;

public class DBTable { 
    RandomAccessFile rows; //the file that stores the rows in the table 
    long free; //head of the free list space for rows 
    int numOtherFields; 
    int otherFieldLengths[]; 
    //add other instance variables as needed 
    BTree tree;
    private class Row { 
        private int keyField; 
        private char otherFields[][]; 
        private long addr;
       /* 
       Each row consists of unique key and one or more character array fields. 
       Each character array field is a fixed length field (for example 10 characters). 
       Each field can have a different length. 
       Fields are padded with null characters so a field with a length of  
       of x characters always uses space for x characters. 
       */ 
       //Constructors and other Row methods 
       private Row(int key, char field[][], long addr) {
           keyField    = key;
           otherFields = field;
           this.addr   = addr;
       }
        
       private Row(long addr) {
           try {
                rows.seek(addr);
                this.addr = addr;
                keyField = rows.readInt();
                otherFields = new char[numOtherFields][];
                for(int i = 0; i < numOtherFields; i++) {
                    otherFields[i] = new char[otherFieldLengths[i]];
                    for(int j = 0; j < otherFields[i].length;j++) {
                        otherFields[i][j] = rows.readChar();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
       }
       
        private void writeRow() {
            try {
                rows.seek(addr);
                rows.writeInt(keyField);
                for(int i = 0; i < numOtherFields;i++) {
                    for(int j = 0; j < otherFieldLengths[i]; j++) {
                        rows.writeChar(otherFields[i][j]);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
     
        public String toString() {
            String str = Integer.toString(keyField) + " ";
            for(int i = 0; i < numOtherFields; i++) {
                for(int j = 0; j < otherFieldLengths[i]; j++) {
                    try {
                        char temp = rows.readChar();
                        if(temp == '\0') {
                            //Do nothing
                        }
                        str += temp;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                str += " ";
            }
            
            return str;
        }
        
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
            File path = new File(filename);
            File treePath = new File(filename+"_tree");
            if(path.exists()) {
                path.delete();
                treePath.delete();
            }
            rows = new RandomAccessFile(path,"rw");
            rows.seek(0);
            numOtherFields = fL.length;
            
            rows.writeInt(numOtherFields);
            otherFieldLengths = new int[numOtherFields];
            for(int i = 0; i < numOtherFields;i++) {
                otherFieldLengths[i] = fL[i];
                rows.writeInt(fL[i]);
            }
            tree = new BTree(filename+"_tree",bsize);
            free = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public DBTable(String filename) { 
    //Use this constructor to open an existing DBTable
      
        try {
            rows = new RandomAccessFile(filename,"rw");
            rows.seek(0);
            
            numOtherFields = rows.readInt();
            otherFieldLengths = new int[numOtherFields];
            
            for(int i = 0; i < numOtherFields; i++) {
                otherFieldLengths[i] = rows.readInt();
            }
            free = rows.readLong();
            tree = new BTree(filename+"_tree");
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 
    public boolean insert(int key, char fields[][]) { 
    //PRE: the length of each row is fields matches the expected length 
    /* 
       If a row with the key is not in the table, the row is added and the method 
       returns true otherwise the row is not added and the method returns false. 
       The method must use the B+tree to determine if a row with the key exists. 
       If the row is added the key is also added into the B+tree. 
    */ 
        if(tree.search(key) != 0) {
            return false;
        }
        if(key == 30) {
            System.out.println("YUP!");
        }
        long newAddr = getFree();
        tree.insert(key, newAddr);
        
        Row r = new Row(key, fields,newAddr);
        r.writeRow();
        
        return true;
    } 
    
    public boolean remove(int key) { 
    /* 
       If a row with the key is in the table it is removed and true is returned 
       otherwise false is returned. 
       The method must use the B+Tree to determine if a row with the key exists. 
       
       If the row is deleted the key must be deleted from the B+Tree
    */ 
        return false;
    } 
     
    public LinkedList<String> search(int key) { 
    /* 
       If a row with the key is found in the table return a list of the other fields in the row.  
       The string values in the list should not include the null characters. 
       If a row with the key is not found return an empty list 
       The method must use the equality search in B+Tree
    */
        if(key == 30) {
            System.out.println("I see you! Stop lying to me!");
        }
        long dbAddr = tree.search(key);
        if(dbAddr == 0) {
            return null;
        }
        
        LinkedList<String> list = new LinkedList<String>();
        
        Row r = new Row(dbAddr);
        
        for(int i = 0; i < numOtherFields; i++) {
            String str = "";
            for (int j = 0; j < otherFieldLengths[i]; j++) {
                if(r.otherFields[i][j] == '\0') {
                    break;
                }
                str += r.otherFields[i][j];
            }
            list.add(str);
        }
        
        return list;
    }
    
    private long getFree() {
        long addr = 0;
        //When at the end of free, write to the end of file.
        try {
            if (free == 0) {
                addr = rows.length();
            } else {
                //New address is where free is pointing
                addr = free;
                //Move free value to next in list.
                rows.seek(free);
                free = rows.readLong();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addr;
    }
    
    public LinkedList<LinkedList<String>> rangeSearch(int low, int high) {  
    //PRE: low <= high 
    /* 
       For each row with a key that is in the range low to high inclusive a list 
       of the fields in the row is added to the list returned by the call. 
       If there are no rows with a key in the range return an empty list 
       The method must use the range search in B+Tree
    */
       return null; 
    } 
    public void print() { 
    //Print the rows to standard output is ascending order (based on the keys) 
    //One row per line 
//        try {
//            rows.seek(4 * (numOtherFields + 3));
//            System.out.println("I started look at " + (4*(numOtherFields + 3)));
//            while (rows.getFilePointer() < rows.length()) {
//             Row r = new Row(rows.getFilePointer());
//             System.out.println(r);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        tree.print();
        
    } 
    public void close() { 
    //close the DBTable. The table should not be used alter it is closed 
        try {
            //Get to the location of the free address
            rows.seek(4 * (numOtherFields + 1));
            System.out.println("I wrote out free at " + (4*(numOtherFields + 1)));
            rows.writeLong(free);
            tree.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 
        
} 
