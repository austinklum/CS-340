import java.io.*;
import java.util.*;

public class H5C {

		DBTable t1;
		DBTable t2;
		DBTable t3;

		int t1Fields[] = {10, 20};
		int t2Fields[] = {5, 10, 30};

	private void search(int val)  throws IOException {

		LinkedList<String> fields1;
		LinkedList<String> fields2;
		LinkedList<String> fields3;

		fields1 = t1.search(val);
		print(fields1, val);
		fields2 = t2.search(val);
		print(fields2, val);

	}

	private void print(LinkedList<String> f, int k) {
		if (f == null || f.size() == 0) { 
			System.out.println("Not Found "+k);
			return;
		}
		System.out.print(""+k+" ");
		for (int i = 0; i < f.size(); i++)
			System.out.print(f.get(i)+" ");
		System.out.println();
	}

	private char[][] makeFields(int fields[], int k) {
		char f[][] = new char[fields.length][];
		for (int i = 0; i < f.length; i++) {
			f[i] = Arrays.copyOf((new Integer(k)).toString().toCharArray(), fields[i]);
		}
		return f;
	}
			

	

	public H5C() throws IOException {
		int i;

		System.out.println("Inserts into t1");
		t1 = new DBTable("f1", t1Fields, 60);
		for (i = 0; i < 500; i++) {
			t1.insert(i, makeFields(t1Fields, i));

		}

		System.out.println("Inserts into t2");
		t2 = new DBTable("f2", t2Fields, 72);
		for (i = 0; i < 1080; i++) {
			t2.insert(i, makeFields(t2Fields, i));

		}
		t1.print();
		System.out.println("Removes from t1");
		for (i = 0; i < 500; i = i + 4) {
			t1.remove(i);
			//if(i==0) {t1.print();}
		}

		System.out.println("Removes from t2");
		for (i = 0; i < 1080; i = i + 5) {
			t2.remove(i);

		}

		System.out.println("Second set of Removes from t1");
		for (i = 3; i < 500; i = i + 4) {
			t1.remove(i);

		}

		System.out.println("Second set of Removes from t2");
		for (i = 4; i < 1080; i = i + 5) {
			t2.remove(i);

		}

		Scanner scan = new Scanner(System.in);
		int val;
		
		System.out.print("Enter a search value or -1 to quit: ");
		val = scan.nextInt();
		while (val != -1) {
			search(val);
			System.out.print("\nEnter a search value or -1 to quit: ");
			val = scan.nextInt();
		}


		t1.close();
		t2.close();

		t1 = new DBTable("f1");
		t2 = new DBTable("f2");

		System.out.println("Second set of inserts into t1");
		t1 = new DBTable("f1", t1Fields, 60);
		for (i = 490; i < 510; i++) {
			t1.insert(i, makeFields(t1Fields, i));

		}

		System.out.println("Second set Inserts into t2");
		t2 = new DBTable("f2", t2Fields, 72);
		for (i = 1070; i < 1090; i++) {
			t2.insert(i, makeFields(t2Fields, i));

		}

		System.out.println("Third set of removes from t1");
		for (i = 40; i > 0; i--) {
			t1.remove(i);

		}

		System.out.println("Third set of removes from t2");
		for (i = 40; i > 0; i--) {
			t2.remove(i);

		}

		System.out.print("Enter a search value or -1 to quit: ");
		val = scan.nextInt();
		while (val != -1) {
			search(val);
			System.out.print("\nEnter a search value or -1 to quit: ");
			val = scan.nextInt();
		}

		t1.close();
		t2.close();
	

	}


		

	public static void main(String args[])  throws IOException  {
		new H5C();
	}
}