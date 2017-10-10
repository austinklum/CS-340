import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class PriorityQueue {
	//implements a d-heap based priority queue
	//the priority is an int (low value is high priority)
	//associated with each priority is an object
	//public static void main(String[] args) throws FileNotFoundException {
		/*
		int size;
		int ord;
		 size = 20;
		 ord = 1;
		PriorityQueue qq = new PriorityQueue(ord, size);
		for(int i = 20; i >= 1; i--) {
			qq.insert(i, "Data" + i);
		}
		System.out.println(qq);
		qq.remove();
		System.out.println(qq);
		//Scanner scan = new Scanner(System.in);
		Scanner scan = new Scanner(new File("30Desc.txt"));
		System.out.println("Enter size then number of children");
		size = scan.nextInt();
		ord = scan.nextInt();
		PriorityQueue myQ = new PriorityQueue(ord, size);
		System.out.println("Enter # of values to add");
		int iter = scan.nextInt();
		System.out.println("Enter input now...");
		for(int i = 0; i < iter; i++) {
			myQ.insert(scan.nextInt(), "Data " + i);
		}
		myQ.toArrayString();
		//myQ.remove();
		System.out.println();
		for(int i = 0; i < size; i++) {
			System.out.println(myQ.getPriority());
			myQ.remove();
		}
		
		System.out.println();
		myQ.toArrayString();*/
	//}
    public static void main(String args[]) {
        PriorityQueue p1 = new PriorityQueue(5, 100);
        PriorityQueue p2 = new PriorityQueue(6, 100);
        PriorityQueue p3 = new PriorityQueue(7, 100);

        int p = -1; //pointless initialization to keep the compiler happy
        p1.insert(0, new Integer(0));
        System.out.println("First insert");

        for (int i = 1; i < 100; i++)
            p1.insert(i, new Integer(i));

        for (int i = 0; i < 100; i++)
            p2.insert(i, new Integer(i));

        for (int i = 0; i < 100; i++)
            p3.insert(i, new Integer(i));

        System.out.println("First insert tests");

        System.out.print(p1.getPriority()+",");
        while (!p1.empty()) {
            p = p1.getPriority();
            Object d = p1.getData();
            p1.remove();
        }
        System.out.println(p);

        System.out.print(p2.getPriority()+",");

        while (!p2.empty()) {
            p = p2.getPriority();
            Object d = p2.getData();
            p2.remove();
        }
        System.out.println(p);

        System.out.print(p3.getPriority()+",");

        while (!p3.empty()) {
            p = p3.getPriority();
            Object d = p3.getData();
          
            p3.remove(); 
        }
        System.out.println(p);
        System.out.println("First Remove Test");

        for (int i = 100; i > 0 ; i--)
            p1.insert(i, new Integer(i));

        for (int i = 100; i > 0 ; i--)
            p2.insert(i, new Integer(i));

        for (int i = 100; i > 0 ; i--)
            p3.insert(i, new Integer(i));

        System.out.println("Second insert tests");

        System.out.print(p1.getPriority()+",");
        while (!p1.empty()) {
            p = p1.getPriority();
            Object d = p1.getData();
            p1.remove();
        }
        System.out.println(p);

        System.out.print(p2.getPriority()+",");

        while (!p2.empty()) {
            p = p2.getPriority();
            Object d = p2.getData();
            p2.remove();
        }
        System.out.println(p);

        System.out.print(p3.getPriority()+",");

        while (!p3.empty()) {
            p = p3.getPriority();
            Object d = p3.getData();
            p3.remove();
        }
        System.out.println(p);
        System.out.println("Second Remove Test");

        Random r1 = new Random(1000);
        while (!p3.full()) {
            p = r1.nextInt(200);
            System.out.print(p+",");
            p3.insert(p, new Integer(p));
        }
        System.out.println();

        while (!p3.empty()) {
           System.out.print(p3.getPriority()+",");
            Object d = p3.getData();
            p3.remove();
        }
        System.out.println();
        System.out.println("Third Remove Test");


    }
    
	private class Item{
		private int priority;
		private Object data;
		
		private Item(int p, Object d) {
			priority = p;
			data = d;
		}
	}
	
	private Item queue[];
	private int order;
	private int size;
	public PriorityQueue(int ord, int s) {
		queue = new Item[s];
		order = ord;
		size = 0;
	}
	
	public int getPriority() {
		//PRE !empty()
		//Return the highest priority value in the queue
		return queue[0].priority;
	}
	
	public Object getData() {
		//PRE !empty()
		//Return the data object associated with the highest priority
		return queue[0].data;
	}
	
	public void remove() {
		//PRE !empty()
		//Remove the item with the highest priority in the queue
		Item temp = queue[size - 1];
		size--;
		//Start at root
		int child = 0;
		
		//While not at the end of the tree
		while(child <= size) {
	          //Find the smallest child given a parent
		    child = findMinChild(child);
		    
			//If the smallest child is outside of the array.
		    if(child > size) {
		        break;
		    }
			//Shift child up if less than temp
			if(temp.priority > queue[child].priority) {
				queue[(child - 1)/order] = queue[child];
			}
			else{
				break;
			}
		}
		//We found where to place our new data
		queue[(child - 1)/order] = temp;
	}
	
	public int getSize() {
		//Return the number of items in the queue
		return size;
	}
	public boolean full() {
		return size == queue.length;
	}
	
	public boolean empty() {
		return size == 0;
	}
	
	public void insert(int p, Object d) {
		//PRE: !full()
		//Insert a new item into queue with priority p and associated data d
		
		//Start at the last leaf in the tree
		int child = size;
		size++;
		
		queue[child] = new Item(p,d); 
		Item temp = queue[child];    
		
		//While not the root and priority is less than the parent
        while (child > 0 && temp.priority < queue[(child - 1)/order].priority){
            
        	//Shift parent into child
        	queue[child] = queue[(child - 1)/order];
        	
        	//Child becomes the parent
            child = (child - 1)/order;
        }
        //We found where to place our new node.
        queue[child] = temp;
	}
	/**
	 * Finds the smallest child from a parent
	*/
	private int findMinChild(int i) {
		//Look at the first element
		int minChild = (order * i) + 1;
		int childToCheck = (order * i) + 1;
		
		//Loop through all possibilities and check if there are children with lesser value.
		int k = 1;
		while(k <= order && childToCheck < size) {
			if(queue[childToCheck].priority < queue[minChild].priority) {
				minChild = childToCheck;
			}
			//Increment k and the child position.
			k++;
			childToCheck = (order * i) + (k);
		}
		return minChild;
	}
	/** Prints out the heap as a list */
	@Override
	public String toString() {
		String textBuilder = "";
		for(int i = 0; i < size; i++) {
			textBuilder +=  "Array Pos: " + i + " | P: " + queue[i].priority + " | D: " + queue[i].data + "\n";
		}
		return textBuilder;
		
	}
	/**Prints out the heap as an array with indices on the top row and priorities on bottom row */
	private void toArrayString() {
		for(int i = 0; i < size; i++) {
			System.out.printf("%2d | ", i);
		}
		System.out.println();
		for(int i = 0; i < size; i++) {
			System.out.printf("%2d | ", queue[i].priority);
		}
	}
}

