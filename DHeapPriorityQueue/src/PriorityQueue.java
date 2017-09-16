import java.util.*;
import java.util.AbstractQueue;

public class PriorityQueue {
	//implements a d-heap based priority queue
	//the priority is an int (low value is high priority)
	//associated with each priority is an object
	public static void main(String[] args) {
		//TODO: Testing
		int size = 7;
		int ord = 3;
		PriorityQueue myQ = new PriorityQueue(ord, size);
		for(int i = 7; i >= 1; i--) {
			myQ.insert(i, "Data" + i);
		}
		System.out.println(myQ);
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
		//TODO Implement me!
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
	private int findMinChild() {
		int minChild = 0;
		return minChild;
	}
	@Override
	public String toString() {
		String textBuilder = "";
		int i = 0;
		for(Item node : queue) {
			textBuilder +=  "Array Pos: " + i + " | P: " + node.priority + " | D: " + node.data + "\n";
			i++;
		}
		return textBuilder;
		
	}
}

