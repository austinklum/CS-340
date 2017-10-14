import java.io.*; 

public class HuffmanOutputStream extends BitOutputStream { 
    
    public HuffmanOutputStream(String filename, String tree, int totalChars) { 
        super(filename); 
        try { 
            d.writeUTF(tree); 
            d.writeInt(totalChars); 
        } catch (IOException e) { 
            
        } 
    } 
    public void writeBit(int bit) { 
    //PRE bit == 0 || bit == 1 
        
    } 
    public void close() { 
        
    }

} 
