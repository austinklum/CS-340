import java.io.*; 
import java.util.*; 
public class HuffmanEncode { 
    
    private PriorityQueue<Item> queue; 
    private int totalChars;
    
    private class Item implements Comparable<Item> {
        private int freq;
        private HuffmanTree data;
        
        private Item(int f,HuffmanTree d) {
            freq = f;
            data = d;
        }
        
        @Override
        public int compareTo(Item item) {
            return this.freq - item.freq;
        }
        @Override
        public String toString() {
            String str = "Freq: ";
            str += this.freq;
            str += " Data: ";
            str += this.data.toString();
            return str;
        }
        
    }
    
    public static void main(String args[]) { 
        new HuffmanEncode(args[0], args[1]); 
     } 
    
    public HuffmanEncode(String in, String out) { 
    //Implements the huffman encoding algorithm 
    //Add  private methods as needed
        queue = new PriorityQueue<>(128);
        totalChars = 0;
        //Reads the file and gets freq
        
        readFile(in);
        buildTree();
        writeFile(in, out);
    } 
    /**
     * Reads the file and puts Items into the queue as single node huffman trees with a freq
     * @param in File to encode
     */
    private void readFile(String in) {
        //Must be initialized if I want to close it
        BufferedReader reader = null;
        int[] arr = new int[128];
        
        try {
            reader = new BufferedReader(new FileReader(in));
            
            int c = 0;
            //Create the freq array with a key of the ASCII encoding and value of frequency count
            while((c = reader.read()) != -1) {
                totalChars++;
                arr[c]++;
            }
            
            /* Prints out my array freqs.*/
           /*  for(int i = 0; i < arr.length; i++) {
                System.out.printf( "%3d Index: %c : %4d\n",i,(char)i, arr[i]);
            }*/
            
            for(int i = 0; i < arr.length; i++) {
                //Create priority queue of single huffman trees.
                if(arr[i] != 0) {
                    //Add to Queue: Create Item: Freq, a new Huffman tree with char i
                    queue.add(new Item(arr[i], new HuffmanTree((char)i)));
                }
            }
        } catch (Exception e) {
            //Do Nothing
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                //Do Nothing
            }
        }
    }
    /**
     * PRE Queue of single node huffman Trees
     * POST Queue will contain a single huffman tree with all the single nodes merged with lowest freq chars at the bottom of the tree
     */
    private void buildTree() {
    /* HOW TO BUILD A TREE
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * 1.) Pop off 2 highest priority Items
     * 2.) Merge the two together
     * 3.) Sum the two freq as the new freq
     * 4.) Add back into queue
     * 5.) Repeat until queue.size() == 1
     */
        while(queue.size() > 1) {
            //Step 1.) Pop off 2 highest items
            Item leftItem = queue.poll();
            Item rightItem = queue.poll();
            
            //Step 2.) Merge the trees
            //Step 3.) Create new Item and add freq
            Item mergedItem = new Item(leftItem.freq + rightItem.freq, new HuffmanTree(leftItem.data, rightItem.data, (char)128));
            
            //Step 4.) Add back to queue
            queue.add(mergedItem);
        }
        //Step 5.) Repeat
    }
   /**
    * Writes out the string representation of the tree and the string encodings. 
    * Encodes the in file to the out file 
    * 
    * @param in File to be encoded
    * @param out File to write encodings to
    */
   private void writeFile(String in, String out) {
       //arr will be my "hash map" for the encodings 
       //KEY: ASCII value char VALUE: Encoding path from iterator
       String[] arr = new String[128];
       
       HuffmanTree tree = queue.poll().data;
       Iterator<String> iter = tree.iterator();
       
       //Iterate through all the encodings and add to my "hash"
       while(iter.hasNext()) {
           String str = iter.next();
           arr[str.charAt(0)] = str.substring(1);
       }
       
       try {
           //Create the reader and writer
           HuffmanOutputStream writer = new HuffmanOutputStream(out,tree.toString(), totalChars);
           BufferedReader reader = new BufferedReader(new FileReader(in));
           
           int c = 0;
           while((c = reader.read()) != -1) {
               //Write the encodings out 1 bit at a time
               for(int i = 0; i < arr[c].length(); i++) {
                   writer.writeBit(Integer.parseInt(arr[c].substring(i,i+1)));
               }
           }
           writer.close();
           reader.close();
       } catch (IOException e) {
           //Do Nothing
       }
       
    } //End writeFile
   
}
