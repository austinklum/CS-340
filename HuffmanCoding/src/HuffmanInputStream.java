import java.io.*; 

public class HuffmanInputStream extends BitInputStream { 
    
    private String tree; 
    private int totalChars;
    private int currentByte;
    private int bitCount;
    private int totalCharsUsed;
    
    
    public HuffmanInputStream(String filename) { 
        super(filename); 
        try { 
            tree = d.readUTF(); 
            totalChars = d.readInt(); 
        }catch (IOException e){
           
        }
        currentByte = 0;
        bitCount = 0;
        nextByte();
     }
     @Override
     public int readBit() {
         if(currentByte == -1) {
             try {
                d.close();
            } catch (IOException e) {
            }
             return -1;
             //I'm Done!
         }
         
         int result = currentByte % 2;
         System.out.print("result = " +  result);
         System.out.printf(" : CurrentByte = %d : %d \n",currentByte,currentByte/2 );
         currentByte /= 2;
         bitCount++;
         if(bitCount == 8) {
             //Get the next byte to process
             nextByte();
         }
         return result;
     }
     
     private void nextByte() {
         System.out.println("reading the nextByte");
         try {
            currentByte = d.readUnsignedByte();
        } catch (IOException e) {
            //Do Nothing
        }
         System.out.println("New byte = " + currentByte);
        bitCount = 0;
     }
     
     public String getTree() {
         return tree;
     }
     
     public int totalChars() { 
         return totalChars;
     }
     
     @Override
     public void close() {
        try {
            d.close();
        } catch (IOException e) {

        }
     } 
}
