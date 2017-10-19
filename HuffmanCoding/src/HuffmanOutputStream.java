import java.io.*; 

public class HuffmanOutputStream extends BitOutputStream { 
    
    private int currentByte;
    private int bitCount;
    
    public HuffmanOutputStream(String filename, String tree, int totalChars) { 
        super(filename); 
        try { 
            d.writeUTF(tree); 
            d.writeInt(totalChars); 
        } catch (IOException e) { 
            
        }
        currentByte = 0;
        bitCount = 0;
    } 
    public void writeBit(int bit) { 
    //PRE bit == 0 || bit == 1 
        //Add to the current byte the bit shifted with the count
        currentByte += bit << bitCount;
        bitCount++;
        
        //We have 8 bits flush the output
        if(bitCount == 8) {
            flush();
        }
    } 
    
    private void flush() {
        //Write out the Byte
        //Should pad with 0's even if not full.
        try {
            d.write(currentByte);
        } catch (IOException e) {

        }
        //Reset the count
        currentByte = 0;
        bitCount = 0;
    }
    
    public void close() { 
        //If there is anything left to write out, flush();
        if(bitCount > 0) {
            flush();
        }
        //When we close it should flush out rest
        try {
            d.close();
        } catch (IOException e) {
            
        }
    }

} 
