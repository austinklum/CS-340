import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

//Tests insert and search
//values inserted are ints 0, 1, 5, 6, ... up to the limit
//the character fields are character representation of the inserted ints
public class H5A {

    DBTable t1;
    DBTable t2;
    DBTable t3;

    int t1Fields[] = { 10, 20 };
    int t2Fields[] = { 5, 10, 30 };
    int t3Fields[] = { 35 };
    ArrayList<Integer> inTree = new ArrayList<>(50);

    private void insert(int limit) throws IOException {
        int i;
        for (i = 0; i < limit; i = i + 5) {
            inTree.add(i);
            t1.insert(i, makeFields(t1Fields, i));
            t2.insert(i, makeFields(t2Fields, i));
            t3.insert(i, makeFields(t3Fields, i));
        }
        for (i = limit + 1; i >= 1; i = i - 5) {
            inTree.add(i);
            t1.insert(i, makeFields(t1Fields, i));
            t2.insert(i, makeFields(t2Fields, i));
            t3.insert(i, makeFields(t3Fields, i));
        }
    }

    private void search(int val) throws IOException {

        LinkedList<String> fields1;
        LinkedList<String> fields2;
        LinkedList<String> fields3;

        fields1 = t1.search(val);
        print(fields1, val);
        fields2 = t2.search(val);
        print(fields2, val);
        fields3 = t3.search(val);
        print(fields3, val);

    }

    private void print(LinkedList<String> f, int k) {
        if (f == null || f.size() == 0) {
            System.out.println("Not Found " + k);
            return;
        }
        System.out.print("" + k + " ");
        for (int i = 0; i < f.size(); i++)
            System.out.print(f.get(i) + " ");
        System.out.println();
    }

    private char[][] makeFields(int fields[], int k) {
        char f[][] = new char[fields.length][];
        for (int i = 0; i < f.length; i++) {
            f[i] = Arrays.copyOf((new Integer(k)).toString().toCharArray(), fields[i]);
        }
        return f;
    }

    public H5A() throws IOException {
        int limit;

        t1 = new DBTable("f1", t1Fields, 60);
        t2 = new DBTable("f2", t2Fields, 72);
        t3 = new DBTable("f3", t3Fields, 132);

        // Scanner scan = new Scanner(System.in);
        RandomAccessFile f = new RandomAccessFile("input","rw");
        f.seek(0);
        System.out.print("Enter the input limit: ");
        limit = f.readInt();

        insert(limit);

        /*
         * I added the printing of the trees
         */
        t1.print();
        System.out.println("\n");
        t2.print();
        System.out.println("\n");
        t3.print();
        System.out.println("\n");
        System.out.println(inTree.size() + " keys inserted!");
        for (Integer integer : inTree) {
            //System.out.println("Searching for : " + integer);
            search(integer);
        }

        System.out.print("Enter a search value or -1 to quit: ");
        int val = f.readInt();
        while (val != -1) {
            search(val);
            System.out.print("\nEnter a search value or -1 to quit: ");
            val = f.readInt();
        }

        t1.close();
        t2.close();
        t3.close();

        t1 = new DBTable("f1");
        t2 = new DBTable("f2");
        t3 = new DBTable("f3");
        
        t1.insert(limit, makeFields(t1Fields, limit));
        t2.insert(limit, makeFields(t2Fields, limit));
        t3.insert(limit, makeFields(t3Fields, limit));

        System.out.print("Enter a search value or -1 to quit: ");
        val = f.readInt();
        while (val != -1) {
            search(val);
            System.out.print("\nEnter a search value or -1 to quit: ");
            val = f.readInt();
        }
        t1.rangeSearch(0, 50);
        t1.close();
        t2.close();
        t3.close();
    }

    public static void main(String args[]) throws IOException {
        RandomAccessFile f = new RandomAccessFile("input", "rw");
        f.seek(0);
        f.writeInt(100);
        f.writeInt(-1);
        f.writeInt(-1);
        f.seek(0);
        f.close();
        new H5A();
    }
}