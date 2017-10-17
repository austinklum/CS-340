import java.io.*; 
import java.util.*; 

public class HuffmanDecode { 
    public HuffmanDecode(String in, String out) { 
    //Implements the huffman decoding algorithm 
    //Add  private methods as needed
       try {
           System.out.println("Converting file!");
           convertFile(in,out);
       } catch (FileNotFoundException e) {
           System.out.println("File not found!");
           e.printStackTrace();
       }
       //buildTree();
       //writeTree(in, out);
    } 
    private void convertFile(String in, String out) throws FileNotFoundException{
        HuffmanInputStream reader = new HuffmanInputStream(in);
        HuffmanTree tree = new HuffmanTree(reader.getTree(),(char)128);
        PrintWriter writer = new PrintWriter(out);
        System.out.println(out);
        int charsRead = 0;
        
        while(charsRead != reader.totalChars()){
            int c = reader.readBit();
            System.out.println("Read a bit called: " + c);
            if(!tree.atLeaf()) {
                //System.out.println("Not at leaf!");
                if (c == 0) {
                   // System.out.println("Going left!");
                    tree.moveLeft();
                }else if (c == 1) {
                    //System.out.println("Going right");
                    tree.moveRight();
                }
            } else {
                System.out.println("Chars Read = " + charsRead);
                System.out.println("Im at a leaf! Write and move root. : " + tree.current());
                //Write the char out
                charsRead++;
                writer.write(tree.current());
                tree.moveRoot();  
            }
        }
        writer.close();
        reader.close();
    }
    
    public static void main(String args[]) { 
        new HuffmanDecode(args[0], args[1]); 
    } 
} 
