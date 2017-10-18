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
        PrintWriter log = new PrintWriter("log.txt");
        //System.out.println(out);
        int charsRead = 0;
        
        while(charsRead != reader.totalChars()){
            if(!tree.atLeaf()) {
                int bit = reader.readBit();
                log.print("Read a bit called: " + bit + "\n");
                log.println("Not at leaf!");
                if (bit == 0) {
                   log.println("Going left!");
                    tree.moveLeft();
                }else if (bit == 1) {
                    log.println("Going right");
                    tree.moveRight();
                }
            } else {
                log.print("Chars Read = " + charsRead + "\n");
                log.print("  Im at a leaf! Write and move root. : " + tree.current() + "\n");
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
