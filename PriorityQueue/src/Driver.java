
public class Driver {

	public static void main(String[] args) {
		PriorityQueue myQ = new PriorityQueue(7);
		for(int i = 7; i >= 1; i--) {
			myQ.insert(i, "Data" + i);
		}
		System.out.println(myQ);
	}

}
