import java.io.*; 
import java.util.*; 

public class HuffmanDecode { 
    public HuffmanDecode(String in, String out) { 
    //Implements the huffman decoding algorithm 
    //Add  private methods as needed
       try {
           convertFile(in,out);
       } catch (FileNotFoundException e) {
           System.out.println("File not found!");
       }
    } 
    /**
     *  Reads from in file and creates a tree from the string representation.
     *  Uses the tree to read bits from encoded in file and find the proper value to decode to. 
     *  Writes out the decoding to the out file. 
     * 
     * @param in Encoded file to decode
     * @param out File to store the decoded file
     * @throws FileNotFoundException
     */
    private void convertFile(String in, String out) throws FileNotFoundException{
        HuffmanInputStream reader = new HuffmanInputStream(in);
        PrintWriter writer = new PrintWriter(out);
        
        //Tree built from string encoding
        HuffmanTree tree = new HuffmanTree(reader.getTree(),(char)128);
        int charsRead = 0;
        int bit = -1;
        //While not at the end of the file
        while(charsRead != reader.totalChars()){
            //If not a leaf continue looking left or right
            if(!tree.atLeaf()) {
                bit = reader.readBit();
                if (bit == 0) {
                    tree.moveLeft();
                } else {
                    tree.moveRight();
                }
            } else {
                //Write the char out and return to root
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
