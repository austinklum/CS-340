import java.io.*; 

public class HuffmanInputStream extends BitInputStream { 
    
    private String tree; 
    private int totalChars;
    
    public HuffmanInputStream(String filename) { 
        super(filename); 
        try { 
            tree = d.readUTF(); 
            totalChars = d.readInt(); 
        }catch (IOException e){
           
        } 
     }
     @Override
     public int readBit() {
         //TODO
        return 0;
     }
     
     public String getTree() {
         //TODO
         return "TODO";
     }
     
     public int totalChars() { 
         //TODO
         return 0;
     }
     
     @Override
     public void close() {
        //TODO
     } 
}
