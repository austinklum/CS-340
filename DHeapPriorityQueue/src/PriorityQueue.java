import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class PriorityQueue {
	//implements a d-heap based priority queue
	//the priority is an int (low value is high priority)
	//associated with each priority is an object
	public static void main(String[] args) throws FileNotFoundException {
		//TODO: Testing
		int size;
		int ord;
		/* size = 7;
	 		ord = 3;
		PriorityQueue myQ = new PriorityQueue(ord, size);
		for(int i = 7; i >= 1; i--) {
			myQ.insert(i, "Data" + i);
		}
		System.out.println(myQ);
		myQ.remove();
		System.out.println(myQ);*/
		//Scanner scan = new Scanner(System.in);
		Scanner scan = new Scanner(new File("7Desc.txt"));
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
		myQ.remove();
		System.out.println();
		myQ.toArrayString();
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
		int child = 0;
		while((order * child) + 1 < size) {
			child = findMinChild(child);
			if(temp.priority > queue[child].priority) {
				queue[(child - 1)/order] = queue[child];
			}
			else{
				break;
			}
			//Shift child up to parent
			
			//move child index ahead
			//child = (order * child) + 1;
		}
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
		int child;
		size++;
		child = size - 1;
		queue[child] = new Item(p,d); 
		
		Item temp = queue[child];    
        while (child > 0 && temp.priority < queue[(child - 1)/order].priority){
            queue[child] = queue[(child - 1)/order];
            child = (child - 1)/order;
        }                   
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
			System.out.println(childToCheck);
			//Increment k and the child position.
			k++;
			childToCheck = (order * i) + (k);
		}
		System.out.println("\nSmallest Child at " + i + " is " + queue[minChild].priority);
		return minChild;
	}
	@Override
	public String toString() {
		String textBuilder = "";
		for(int i = 0; i < size; i++) {
			textBuilder +=  "Array Pos: " + i + " | P: " + queue[i].priority + " | D: " + queue[i].data + "\n";
		}
		return textBuilder;
		
	}
	
	private void toArrayString() {
		for(int i = 0; i < size; i++) {
			System.out.printf("%2d | ", queue[i].priority);
		}
		System.out.println();
		for(int i = 0; i < size; i++) {
			System.out.printf("%2d | ", i);
		}
	}
}

