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
        readFile(in);
        buildTree();
        writeTree(in, out);
    } 

    private void readFile(String in) {
        BufferedReader reader = null;
        int[] arr = new int[128];
        
        try {
            reader = new BufferedReader(new FileReader(new File(in)));
            
            int c = 0;
            while((c = reader.read()) != -1) {
                totalChars++;
                arr[c]++;
            }
            
            /* Prints out my array freqs.*/
             for(int i = 0; i < arr.length; i++) {
                System.out.printf( "%3d Index: %c : %4d\n",i,(char)i, arr[i]);
            }
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
            
            System.out.println("LeftItem = " + leftItem + " | RightItem = " + rightItem);
            
            //Step 2.) Merge the trees
            HuffmanTree mergedTree = new HuffmanTree(leftItem.data, rightItem.data, (char)128);
            
            //Step 3.) Create new Item and add freq
            Item mergedItem = new Item(leftItem.freq + rightItem.freq, mergedTree);
            
            //Step 4.) Add back to queue
            queue.add(mergedItem);
        }
        //Step 5.) Repeat
        System.out.println(queue);
    }
    
   private void writeTree(String in, String out){
       String[] arr = new String[128];
       HuffmanTree tree = queue.poll().data;
       Iterator<String> iter = tree.iterator();
       while(iter.hasNext()) {
           String str = iter.next();
           arr[str.charAt(0)] = str.substring(1, str.length());
       }
       
       try {
           HuffmanOutputStream hout = new HuffmanOutputStream(out,tree.toString(), totalChars);
           BufferedReader reader = new BufferedReader(new FileReader(in));
           int c = 0;
           while((c = reader.read()) != -1) {
               for(int i = 0; i < arr[c].length(); i++) {
                   hout.writeBit(Integer.parseInt(arr[c]));
               }
           }
           hout.close();
           reader.close();
       } catch (IOException e) {
           
       }
       
       
    }
    
}
