//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//
//public class Assignments {
//
//    public static void main(String[] args) throws IOException {
//        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
//        String[] str = reader.readLine().split(" ");
//        SymbolTable hash = new SymbolTable(10);
//        String assignment = str[0];
//        for(int i = 0; i < str.length; i++) {
//            if(str[i].equals("=")) {
//                hash.insert(assignment);
//                hash.setValue(assignment, evaluate());
//            }
//        }
//    }
//}